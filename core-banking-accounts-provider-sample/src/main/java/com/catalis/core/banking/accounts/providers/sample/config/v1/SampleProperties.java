package com.catalis.core.banking.accounts.providers.sample.config.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "account.sample")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SampleProperties {
    private String baseUrl;
    private String apiKey;
    private int timeout;
}