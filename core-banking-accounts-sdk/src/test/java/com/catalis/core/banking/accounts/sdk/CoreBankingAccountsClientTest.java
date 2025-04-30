package com.catalis.core.banking.accounts.sdk;

import com.catalis.core.banking.accounts.interfaces.dtos.core.v1.AccountDTO;
import com.catalis.core.banking.accounts.interfaces.dtos.space.v1.AccountSpaceDTO;
import com.catalis.core.banking.accounts.sdk.config.CoreBankingAccountsClientConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Tests for the CoreBankingAccountsClient class.
 */
public class CoreBankingAccountsClientTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private CoreBankingAccountsClient client;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Create a client with the mocked WebClient
        client = new CoreBankingAccountsClient(webClient);
    }

    @Test
    public void testClientCreation() {
        // Test client creation with default configuration
        CoreBankingAccountsClient defaultClient = new CoreBankingAccountsClient();
        assertNotNull(defaultClient);
        assertNotNull(defaultClient.getWebClient());
        assertNotNull(defaultClient.getAccountClient());
        assertNotNull(defaultClient.getAccountSpaceClient());
        assertNotNull(defaultClient.getAccountParameterClient());
        assertNotNull(defaultClient.getAccountProviderClient());
        assertNotNull(defaultClient.getAccountStatusHistoryClient());

        // Test client creation with custom base URL
        CoreBankingAccountsClient customUrlClient = new CoreBankingAccountsClient("http://example.com");
        assertNotNull(customUrlClient);
        assertNotNull(customUrlClient.getWebClient());
        assertNotNull(customUrlClient.getAccountClient());
        assertNotNull(customUrlClient.getAccountSpaceClient());
        assertNotNull(customUrlClient.getAccountParameterClient());
        assertNotNull(customUrlClient.getAccountProviderClient());
        assertNotNull(customUrlClient.getAccountStatusHistoryClient());

        // Test client creation with custom configuration
        CoreBankingAccountsClientConfig config = new CoreBankingAccountsClientConfig();
        config.setBaseUrl("http://example.com");
        config.setConnectTimeoutMs(10000);
        config.setReadTimeoutMs(10000);
        config.setWriteTimeoutMs(10000);
        config.setEnableLogging(true);

        CoreBankingAccountsClient customConfigClient = new CoreBankingAccountsClient(config);
        assertNotNull(customConfigClient);
        assertNotNull(customConfigClient.getWebClient());
        assertNotNull(customConfigClient.getAccountClient());
        assertNotNull(customConfigClient.getAccountSpaceClient());
        assertNotNull(customConfigClient.getAccountParameterClient());
        assertNotNull(customConfigClient.getAccountProviderClient());
        assertNotNull(customConfigClient.getAccountStatusHistoryClient());

        // Test client creation with mocked WebClient
        assertNotNull(client);
        assertNotNull(client.getWebClient());
        assertNotNull(client.getAccountClient());
        assertNotNull(client.getAccountSpaceClient());
        assertNotNull(client.getAccountParameterClient());
        assertNotNull(client.getAccountProviderClient());
        assertNotNull(client.getAccountStatusHistoryClient());
    }
}
