package com.example.SaleService.exceptions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String s, Response response) {
        String message = extractMessageFromBody(response);

        log.error("Feign client error. Status: {}, Reason: {}, Message: {}",
                response.status(), response.reason(), message);

        return switch (response.status()) {
            case 400 -> new BadRequestException(message);
            case 404 -> new NotFoundException(message);
            case 403 -> new UnAuthorizeException(message);
            default -> {
                log.error("Server error in feign client: {}",message);
                yield null;
            }
        };
    }
    private String extractMessageFromBody(Response response) {
        if (response.body() == null) {
            return null;
        }

        try {
            String responseBody = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
            // Parse the JSON response body
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.has("message") ? jsonNode.get("message").asText() : responseBody;
        } catch (IOException e) {
            log.error("Error reading Feign response body", e);
            return "Error reading response body";
        }
    }
}
