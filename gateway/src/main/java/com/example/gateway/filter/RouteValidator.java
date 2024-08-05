package com.example.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.function.Predicate;

@Component
@Slf4j
public class RouteValidator {

    private static final List<String> openApiEndpoints = List.of(
            "/v1/auth/login",
            "/v1/auth/register"
    );

    public Predicate<ServerHttpRequest> isSecured = serverHttpRequest -> {
        String path = serverHttpRequest.getURI().getPath();
        boolean isOpen = openApiEndpoints.stream().anyMatch(uri -> path.contains(uri));
        log.info("Path: {}, isOpen: {}", path, isOpen);
        return !isOpen;
    };
}
