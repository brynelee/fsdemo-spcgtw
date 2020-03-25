package com.xdorg1.fsdemospcgtw;

import com.xdorg1.fsdemospcgtw.filter.RequestTimeFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;


@SpringBootApplication
public class FsdemoSpcgtwApplication {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("path_route", r -> r.path("/httpbin")
                        .uri("http://httpbin.org"))
                .route("host_route", r -> r.host("*.myhost.org")
                        .uri("http://httpbin.org"))
                .route("rewrite_route", r -> r.host("*.rewrite.org")
                        .filters(f -> f.rewritePath("/foo/(?<segment>.*)", "/${segment}"))
                        .uri("http://httpbin.org"))
                .route("customer_filter_router", r -> r.path("/usercenter/**")
                        .filters(f -> f.filter(new RequestTimeFilter())
                                .addResponseHeader("X-Response-Default-Foo", "Default-Bar"))
                        .uri("http://fsdemo-usercenter:8081")
                        .order(0)
                )
                .route("authsrv_route", r -> r.path("/oauth/**", "/auth/**","/custom/**")
                        .filters(f -> f.filter(new RequestTimeFilter()))
                        .uri("http://fsdemo-authsrv:8084")
                        .order(0)
                )
                .route("customer_filter_router2", r -> r.path("/**")
                        .filters(f -> f.filter(new RequestTimeFilter())
                                .addResponseHeader("X-Response-Default-Foo", "Default-Bar"))
                        .uri("http://fsdemo-frontend:8080")
                        .order(Ordered.LOWEST_PRECEDENCE)
                )
/*                .route("hystrix_route", r -> r.host("*.hystrix.org")
                        .filters(f -> f.hystrix(c -> c.setName("slowcmd")))
                        .uri("http://httpbin.org"))
                .route("hystrix_fallback_route", r -> r.host("*.hystrixfallback.org")
                        .filters(f -> f.hystrix(c -> c.setName("slowcmd").setFallbackUri("forward:/hystrixfallback")))
                        .uri("http://httpbin.org"))
                .route("limit_route", r -> r
                        .host("*.limited.org").and().path("/anything/**")
                        .filters(f -> f.requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter())))
                        .uri("http://httpbin.org"))*/
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(FsdemoSpcgtwApplication.class, args);
    }

}
