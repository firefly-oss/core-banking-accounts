package com.catalis.core.banking.accounts.sdk;

import com.catalis.core.banking.accounts.sdk.client.AccountClient;
import com.catalis.core.banking.accounts.sdk.client.AccountParameterClient;
import com.catalis.core.banking.accounts.sdk.client.AccountProviderClient;
import com.catalis.core.banking.accounts.sdk.client.AccountSpaceClient;
import com.catalis.core.banking.accounts.sdk.client.AccountSpaceTransactionClient;
import com.catalis.core.banking.accounts.sdk.client.AccountStatusHistoryClient;
import com.catalis.core.banking.accounts.sdk.config.CoreBankingAccountsClientConfig;
import lombok.Getter;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Main client class for the Core Banking Accounts SDK.
 * This class serves as the entry point for interacting with the Core Banking Accounts API.
 */
public class CoreBankingAccountsClient {
    private final WebClient webClient;

    @Getter
    private final AccountClient accountClient;

    @Getter
    private final AccountSpaceClient accountSpaceClient;

    @Getter
    private final AccountParameterClient accountParameterClient;

    @Getter
    private final AccountProviderClient accountProviderClient;

    @Getter
    private final AccountStatusHistoryClient accountStatusHistoryClient;

    @Getter
    private final AccountSpaceTransactionClient accountSpaceTransactionClient;



    /**
     * Creates a new CoreBankingAccountsClient with default configuration.
     */
    public CoreBankingAccountsClient() {
        this(new CoreBankingAccountsClientConfig());
    }

    /**
     * Creates a new CoreBankingAccountsClient with the specified base URL.
     *
     * @param baseUrl The base URL of the Core Banking Accounts API
     */
    public CoreBankingAccountsClient(String baseUrl) {
        CoreBankingAccountsClientConfig config = new CoreBankingAccountsClientConfig();
        config.setBaseUrl(baseUrl);
        this.webClient = config.createWebClient();
        this.accountClient = new AccountClient(webClient);
        this.accountSpaceClient = new AccountSpaceClient(webClient);
        this.accountParameterClient = new AccountParameterClient(webClient);
        this.accountProviderClient = new AccountProviderClient(webClient);
        this.accountStatusHistoryClient = new AccountStatusHistoryClient(webClient);
        this.accountSpaceTransactionClient = new AccountSpaceTransactionClient(webClient);
    }

    /**
     * Creates a new CoreBankingAccountsClient with the specified configuration.
     *
     * @param config The configuration for the client
     */
    public CoreBankingAccountsClient(CoreBankingAccountsClientConfig config) {
        this.webClient = config.createWebClient();
        this.accountClient = new AccountClient(webClient);
        this.accountSpaceClient = new AccountSpaceClient(webClient);
        this.accountParameterClient = new AccountParameterClient(webClient);
        this.accountProviderClient = new AccountProviderClient(webClient);
        this.accountStatusHistoryClient = new AccountStatusHistoryClient(webClient);
        this.accountSpaceTransactionClient = new AccountSpaceTransactionClient(webClient);
    }

    /**
     * Creates a new CoreBankingAccountsClient with the specified WebClient.
     * This constructor is useful for testing or when you need to provide a custom WebClient.
     *
     * @param webClient The WebClient to use
     */
    public CoreBankingAccountsClient(WebClient webClient) {
        this.webClient = webClient;
        this.accountClient = new AccountClient(webClient);
        this.accountSpaceClient = new AccountSpaceClient(webClient);
        this.accountParameterClient = new AccountParameterClient(webClient);
        this.accountProviderClient = new AccountProviderClient(webClient);
        this.accountStatusHistoryClient = new AccountStatusHistoryClient(webClient);
        this.accountSpaceTransactionClient = new AccountSpaceTransactionClient(webClient);
    }

    /**
     * Returns the WebClient used by this client.
     *
     * @return The WebClient instance
     */
    public WebClient getWebClient() {
        return webClient;
    }
}
