package de.doubleslash.thingworx.mcp.server.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

/**
 * Low-level ThingWorx REST API helper built on WebClient.
 * Centralizes base URL, authentication, headers and error handling.
 */
@Component
public class ThingWorxApi {

    private final String baseUrl;
    private final String appKey;
    private volatile WebClient client;

    public ThingWorxApi(@Value("${thingworx.base-url}") String baseUrl,
                        @Value("${thingworx.app-key}") String appKey) {
        this.baseUrl = baseUrl;
        this.appKey = appKey;
    }

    private WebClient client() {
        if (client == null) {
            synchronized (this) {
                if (client == null) {
            client = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("AppKey", appKey)
                .clientConnector(new ReactorClientHttpConnector())
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024)) // 1 MB
                .build();
                }
            }
        }
        return client;
    }

    /**
     * GET using path segments for safe encoding (e.g., get("Thingworx","Things",thingName)).
     */
    public String get(String... segments) {
        return client()
                .get()
                .uri(b -> b.pathSegment(segments).build())
                .retrieve()
                .onStatus(s -> !s.is2xxSuccessful(), r ->
                        r.bodyToMono(String.class).defaultIfEmpty("")
                         .map(body -> new ThingWorxApiException(r.statusCode().value(), body)))
                .bodyToMono(String.class)
                .block(Duration.ofSeconds(30));
    }

    public boolean exists(String... segments) {
        try {
            return client()
                    .method(HttpMethod.GET)
                    .uri(b -> b.pathSegment(segments).build())
                    .exchangeToMono(resp -> Mono.just(resp.statusCode().is2xxSuccessful()))
                    .onErrorReturn(false)
                    .block(Duration.ofSeconds(15));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * POST JSON using path segments for safe encoding.
     */
    public String postJson(Map<String, Object> body, String... segments) {
        return client()
                .post()
                .uri(b -> b.pathSegment(segments).build())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(body == null ? Map.of() : body))
                .retrieve()
                .onStatus(s -> !s.is2xxSuccessful(), r ->
                        r.bodyToMono(String.class).defaultIfEmpty("")
                         .map(resp -> new ThingWorxApiException(r.statusCode().value(), resp)))
                .bodyToMono(String.class)
                .block(Duration.ofSeconds(60));
    }


    public static class ThingWorxApiException extends RuntimeException {
        private final int status;
        public ThingWorxApiException(int status, String body) {
            super("ThingWorx API error (status=" + status + "): " + body);
            this.status = status;
        }
        public int getStatus() { return status; }
    }
}
