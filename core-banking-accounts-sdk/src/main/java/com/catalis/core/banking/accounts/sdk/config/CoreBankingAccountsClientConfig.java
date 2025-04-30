package com.catalis.core.banking.accounts.sdk.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.Data;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Configuration class for Core Banking Accounts SDK WebClient.
 * Provides customization options for the WebClient used by the SDK.
 */
@Data
public class CoreBankingAccountsClientConfig {
    private String baseUrl = "http://localhost:8080";
    private int connectTimeoutMs = 5000;
    private int readTimeoutMs = 5000;
    private int writeTimeoutMs = 5000;
    private int maxInMemorySize = 16 * 1024 * 1024; // 16MB
    private Consumer<HttpClient> httpClientCustomizer;
    private Consumer<WebClient.Builder> webClientCustomizer;
    private boolean enableLogging = false;

    /**
     * Creates a WebClient instance with the configured settings.
     *
     * @return A configured WebClient instance
     */
    public WebClient createWebClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutMs)
                .responseTimeout(Duration.ofMillis(readTimeoutMs))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(readTimeoutMs, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(writeTimeoutMs, TimeUnit.MILLISECONDS)));

        if (httpClientCustomizer != null) {
            httpClientCustomizer.accept(httpClient);
        }

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(maxInMemorySize))
                .build();

        WebClient.Builder webClientBuilder = WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(exchangeStrategies);

        if (enableLogging) {
            webClientBuilder.filter(logRequest());
            webClientBuilder.filter(logResponse());
        }

        if (webClientCustomizer != null) {
            webClientCustomizer.accept(webClientBuilder);
        }

        return webClientBuilder.build();
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            System.out.println("Request: " + clientRequest.method() + " " + clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> System.out.println(name + ": " + value)));
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            System.out.println("Response status: " + clientResponse.statusCode());
            clientResponse.headers().asHttpHeaders().forEach((name, values) -> values.forEach(value -> System.out.println(name + ": " + value)));
            return Mono.just(clientResponse);
        });
    }
}