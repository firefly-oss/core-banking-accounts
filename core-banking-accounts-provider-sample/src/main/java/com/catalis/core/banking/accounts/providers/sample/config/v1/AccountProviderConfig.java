package com.catalis.core.banking.accounts.providers.sample.config.v1;

import com.catalis.core.banking.accounts.interfaces.interfaces.AccountProvider;
import com.catalis.core.banking.accounts.providers.sample.provider.v1.SampleAccountProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan(basePackages = {
        "com.catalis.core.banking.accounts.providers.sample.config"
})
public class AccountProviderConfig {

    @Autowired
    private SampleProperties sampleProperties;

    @Bean
    public AccountProvider accountProvider(){
        return new SampleAccountProvider();
    }

}
