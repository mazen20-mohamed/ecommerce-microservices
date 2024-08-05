package com.example.gateway;

import com.example.gateway.exceptions.NotAuthenticateException;
import com.example.gateway.filter.RouteValidator;
import com.example.gateway.service.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    private final RouteValidator routeValidator;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthenticationFilter(RouteValidator routeValidator,JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
        this.routeValidator = routeValidator;
        log.info("AuthenticationFilter initialized");
    }

    @Override
    public GatewayFilter apply(Config config) {
        log.info("Applying AuthenticationFilter");
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            log.info("Here");
            if (routeValidator.isSecured.test(request)) {
                if (this.isAuthMissing(request) || this.isPrefixMissing(request))
                    throw new NotAuthenticateException("Header not found");

                final String token = this.getAuthHeader(request);

                if (!jwtUtil.isExpired(token))
                    throw new NotAuthenticateException("Invalid Token");

//                this.populateRequestWithHeaders(exchange, token);
            }
            return chain.filter(exchange);
        };
    }
    public static class Config {
        // Put the configuration properties here
    }

    private String getAuthHeader(ServerHttpRequest request) {
        var header = request.getHeaders().getOrEmpty("Authorization").get(0);
        return header.replace("Bearer ","").trim();
    }
    private boolean isPrefixMissing(ServerHttpRequest request) {
        var header = request.getHeaders().getFirst ("Authorization");
        assert header != null;
        return !header.startsWith("Bearer ");
    }
    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }
    private void populateRequestWithHeaders(ServerWebExchange exchange, String token) {
        Claims claims = jwtUtil.extractAllClaims(token);
        exchange.getRequest().mutate()
                .header("roles", String.valueOf(claims.get("roles")))
                .build();
    }

}
