package com.xdorg1.fsdemospcgtw.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JWTUtil {

    private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);

    /**
     * Verify token and print username information
     * @param token
     * @return true if token is valid
     */

    public static boolean verifyToken(String token){

        Algorithm algorithm = Algorithm.HMAC256("test-secret");

        JWTVerifier jwtVerifier = JWT.require(algorithm).build();

        try {
            jwtVerifier.verify(token);
            String username = JWTUtil.getUserInfo(token);
            logger.info("The JWT token username is: {}", username);
            return true;
        }catch (JWTDecodeException jwtDecodeException) {
            logger.error("JWT decode exception got.");
        } catch (SignatureVerificationException signatureVerificationException) {
            //throw new TokenAuthenticationException(ResponseCodeEnum.TOKEN_SIGNATURE_INVALID.getCode(), ResponseCodeEnum.TOKEN_SIGNATURE_INVALID.getMessage());
            logger.error("JWT signature verification exception got.");
        } catch (TokenExpiredException tokenExpiredException) {
            //throw new TokenAuthenticationException(ResponseCodeEnum.TOKEN_EXPIRED.getCode(), ResponseCodeEnum.TOKEN_INVALID.getMessage());
            logger.error("JWT token expired exception got.");
        } catch (Exception ex) {
            //throw new TokenAuthenticationException(ResponseCodeEnum.UNKNOWN_ERROR.getCode(), ResponseCodeEnum.UNKNOWN_ERROR.getMessage());
            logger.error("Other exception got while verifying the JWT token.");
        }

        return false;
    }


    /**
     * 从Token中提取用户信息
     * @param token
     * @return
     */
    public static String getUserInfo(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getClaim("user_name").asString();
        return username;
    }
}
