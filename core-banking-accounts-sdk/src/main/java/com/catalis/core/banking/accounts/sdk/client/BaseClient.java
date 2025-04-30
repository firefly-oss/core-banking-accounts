package com.catalis.core.banking.accounts.sdk.client;

import com.catalis.core.banking.accounts.sdk.model.FilterRequest;
import com.catalis.core.banking.accounts.sdk.model.PaginationResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.function.Function;

/**
 * Base client class with common functionality for all API clients.
 */
public abstract class BaseClient {
    protected final WebClient webClient;

    /**
     * Creates a new BaseClient with the specified WebClient.
     *
     * @param webClient The WebClient to use
     */
    protected BaseClient(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * Performs a GET request to the specified path.
     *
     * @param path The path to request
     * @param responseType The type of the response
     * @param <T> The type of the response
     * @return A Mono of the response
     */
    protected <T> Mono<T> get(String path, Class<T> responseType) {
        return webClient.get()
                .uri(path)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * Performs a GET request to the specified path with query parameters.
     *
     * @param path The path to request
     * @param queryParams The query parameters
     * @param responseType The type of the response
     * @param <T> The type of the response
     * @return A Mono of the response
     */
    protected <T> Mono<T> get(String path, Map<String, Object> queryParams, Class<T> responseType) {
        return webClient.get()
                .uri(uriBuilder -> buildUri(uriBuilder, path, queryParams))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * Performs a GET request to the specified path with a parameterized type reference.
     *
     * @param path The path to request
     * @param typeReference The type reference for the response
     * @param <T> The type of the response
     * @return A Mono of the response
     */
    protected <T> Mono<T> get(String path, ParameterizedTypeReference<T> typeReference) {
        return webClient.get()
                .uri(path)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(typeReference);
    }

    /**
     * Performs a GET request to the specified path with query parameters and a parameterized type reference.
     *
     * @param path The path to request
     * @param queryParams The query parameters
     * @param typeReference The type reference for the response
     * @param <T> The type of the response
     * @return A Mono of the response
     */
    protected <T> Mono<T> get(String path, Map<String, Object> queryParams, ParameterizedTypeReference<T> typeReference) {
        return webClient.get()
                .uri(uriBuilder -> buildUri(uriBuilder, path, queryParams))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(typeReference);
    }

    /**
     * Performs a GET request to the specified path that returns a Flux.
     *
     * @param path The path to request
     * @param elementType The type of the elements in the Flux
     * @param <T> The type of the elements in the Flux
     * @return A Flux of the response elements
     */
    protected <T> Flux<T> getFlux(String path, Class<T> elementType) {
        return webClient.get()
                .uri(path)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(elementType);
    }

    /**
     * Performs a GET request to the specified path with query parameters that returns a Flux.
     *
     * @param path The path to request
     * @param queryParams The query parameters
     * @param elementType The type of the elements in the Flux
     * @param <T> The type of the elements in the Flux
     * @return A Flux of the response elements
     */
    protected <T> Flux<T> getFlux(String path, Map<String, Object> queryParams, Class<T> elementType) {
        return webClient.get()
                .uri(uriBuilder -> buildUri(uriBuilder, path, queryParams))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(elementType);
    }

    /**
     * Performs a POST request to the specified path.
     *
     * @param path The path to request
     * @param body The request body
     * @param responseType The type of the response
     * @param <T> The type of the response
     * @param <R> The type of the request body
     * @return A Mono of the response
     */
    protected <T, R> Mono<T> post(String path, R body, Class<T> responseType) {
        return webClient.post()
                .uri(path)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * Performs a POST request to the specified path with query parameters.
     *
     * @param path The path to request
     * @param queryParams The query parameters
     * @param body The request body
     * @param responseType The type of the response
     * @param <T> The type of the response
     * @param <R> The type of the request body
     * @return A Mono of the response
     */
    protected <T, R> Mono<T> post(String path, Map<String, Object> queryParams, R body, Class<T> responseType) {
        return webClient.post()
                .uri(uriBuilder -> buildUri(uriBuilder, path, queryParams))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * Performs a PUT request to the specified path.
     *
     * @param path The path to request
     * @param body The request body
     * @param responseType The type of the response
     * @param <T> The type of the response
     * @param <R> The type of the request body
     * @return A Mono of the response
     */
    protected <T, R> Mono<T> put(String path, R body, Class<T> responseType) {
        return webClient.put()
                .uri(path)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * Performs a DELETE request to the specified path.
     *
     * @param path The path to request
     * @return A Mono of Void
     */
    protected Mono<Void> delete(String path) {
        return webClient.delete()
                .uri(path)
                .retrieve()
                .bodyToMono(Void.class);
    }

    /**
     * Builds a URI with the specified path and query parameters.
     *
     * @param uriBuilder The URI builder
     * @param path The path
     * @param queryParams The query parameters
     * @return The built URI
     */
    private URI buildUri(UriBuilder uriBuilder, String path, Map<String, Object> queryParams) {
        UriBuilder builder = uriBuilder.path(path);
        if (queryParams != null) {
            queryParams.forEach((key, value) -> {
                if (value != null) {
                    builder.queryParam(key, value);
                }
            });
        }
        return builder.build();
    }

    /**
     * Creates a ParameterizedTypeReference for a PaginationResponse of the specified type.
     *
     * @param clazz The class of the elements in the PaginationResponse
     * @param <T> The type of the elements in the PaginationResponse
     * @return A ParameterizedTypeReference for a PaginationResponse of the specified type
     */
    protected <T> ParameterizedTypeReference<PaginationResponse<T>> createPaginationTypeReference(Class<T> clazz) {
        return new ParameterizedTypeReference<PaginationResponse<T>>() {};
    }
}
