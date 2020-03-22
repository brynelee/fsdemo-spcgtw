package com.xdorg1.fsdemospcgtw.filter;

import com.xdorg1.fsdemospcgtw.utils.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class TokenFilter implements GlobalFilter, Ordered {

    private Logger logger= LoggerFactory.getLogger( this.getClass() );

    @Value("${fsdemo.authPath}")
    private String authPath;

    @Value("${fsdemo.loginPath}")
    private String loginPath;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //if in the white name list, pass it through
        if(checkPath(exchange)){
            return chain.filter(exchange);
        }

        String token = exchange.getRequest().getHeaders().getFirst("AuthorizationToken");

        //String token = exchange.getRequest().getQueryParams().getFirst("token");

        logger.info("global filter got token: " + token);

        if (token == null || token.isEmpty()) {
            logger.info( "token 为空，无法进行访问." );
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();

        }else if(!JWTUtil.verifyToken(token)){
            logger.info( "token 无效，无法进行访问." );
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    /**
     * check if the path is in the whitelist allowing passing through
     * @param exchange
     * @return
     * reference:
     * https://www.cnblogs.com/cjsblog/p/12425912.html
     */
    private boolean checkPath(ServerWebExchange exchange){

        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        logger.info("TokenFilter got request path: " + path);

        //if access the root, that is for web client, pass it through
        boolean rootAccess = "/".equals(path) ||
                "/favicon.ico".equals(path) ||
                path.indexOf("/js") >= 0 ||
                path.indexOf("/img") >= 0 ||
                path.indexOf("/css") >= 0 ||
                path.indexOf("/sockjs-node") >= 0;

        boolean isLoginRequest = path.indexOf(authPath) >=0 || path.indexOf(loginPath) >=0;

        return rootAccess || isLoginRequest;
    }



    @Override
    public int getOrder() {
        return 0;
    }





}
