
# Core Banking Accounts SDK - Quick Start Guide

## Overview

The Core Banking Accounts SDK provides a convenient way to interact with the Core Banking Accounts microservice API using reactive programming patterns. This SDK simplifies the process of making API calls to manage bank accounts, account spaces, parameters, providers, and status history.

## Installation

### Maven

Add the following dependency to your `pom.xml` file:

```xml
<dependency>
    <groupId>com.catalis</groupId>
    <artifactId>core-banking-accounts-sdk</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### Gradle

Add the following dependency to your `build.gradle` file:

```groovy
implementation 'com.catalis:core-banking-accounts-sdk:1.0.0-SNAPSHOT'
```

## Getting Started

### Creating a Client

The SDK provides several ways to create a client:

#### Default Configuration

```java
// Create a client with default configuration (localhost:8080)
CoreBankingAccountsClient client = new CoreBankingAccountsClient();
```

#### Custom Base URL

```java
// Create a client with a custom base URL
CoreBankingAccountsClient client = new CoreBankingAccountsClient("https://api.example.com");
```

#### Custom Configuration

```java
// Create a client with fully customized configuration
CoreBankingAccountsClientConfig config = new CoreBankingAccountsClientConfig();
config.setBaseUrl("https://api.example.com");
config.setConnectTimeoutMs(10000);
config.setReadTimeoutMs(10000);
config.setWriteTimeoutMs(10000);
config.setMaxInMemorySize(32 * 1024 * 1024); // 32MB
config.setEnableLogging(true);

CoreBankingAccountsClient client = new CoreBankingAccountsClient(config);
```

#### Custom WebClient (for testing)

```java
// Create a client with a custom WebClient instance
WebClient webClient = WebClient.builder()
    .baseUrl("https://api.example.com")
    .build();

CoreBankingAccountsClient client = new CoreBankingAccountsClient(webClient);
```

## Using the SDK

The SDK provides specialized clients for each resource type:

- `AccountClient`: For managing bank accounts
- `AccountSpaceClient`: For managing account spaces/buckets
- `AccountSpaceTransactionClient`: For managing account space transactions
- `AccountParameterClient`: For managing account parameters
- `AccountProviderClient`: For managing account providers
- `AccountStatusHistoryClient`: For managing account status history

### Account Operations

```java
// Get the account client
AccountClient accountClient = client.getAccountClient();

// Create a new account
AccountDTO newAccount = new AccountDTO();
newAccount.setContractId(500123L);
newAccount.setAccountNumber("2024-00001-001");
newAccount.setAccountType("CHECKING");
newAccount.setCurrency("EUR");
newAccount.setOpenDate(LocalDate.now());
newAccount.setAccountStatus("OPEN");
newAccount.setBranchId(1001L);
newAccount.setDescription("Primary checking account");

Mono<AccountDTO> createdAccount = accountClient.createAccount(newAccount);

// Subscribe to the Mono to execute the request
createdAccount.subscribe(
    account -> System.out.println("Created account with ID: " + account.getAccountId()),
    error -> System.err.println("Error creating account: " + error.getMessage())
);

// Get an account by ID
Mono<AccountDTO> account = accountClient.getAccount(100001L);

// Update an account
account.flatMap(existingAccount -> {
    existingAccount.setDescription("Updated description");
    return accountClient.updateAccount(existingAccount.getAccountId(), existingAccount);
}).subscribe(
    updatedAccount -> System.out.println("Updated account: " + updatedAccount.getAccountNumber()),
    error -> System.err.println("Error updating account: " + error.getMessage())
);

// Delete an account
Mono<Void> deleteResult = accountClient.deleteAccount(100001L);
deleteResult.subscribe(
    () -> System.out.println("Account deleted successfully"),
    error -> System.err.println("Error deleting account: " + error.getMessage())
);

// Get accounts by customer ID
Flux<AccountDTO> customerAccounts = accountClient.getAccountsByCustomerId(500123L);
customerAccounts.subscribe(
    account -> System.out.println("Found account: " + account.getAccountNumber()),
    error -> System.err.println("Error retrieving accounts: " + error.getMessage())
);

// Get paginated accounts by customer ID
Mono<PaginationResponse<AccountDTO>> paginatedAccounts =
    accountClient.getAccountsByCustomerIdPaginated(500123L, 0, 10);
paginatedAccounts.subscribe(
    response -> {
        System.out.println("Total accounts: " + response.getTotalElements());
        response.getContent().forEach(acc ->
            System.out.println("Account: " + acc.getAccountNumber()));
    },
    error -> System.err.println("Error retrieving paginated accounts: " + error.getMessage())
);

// Get account balance
Mono<AccountBalanceDTO> balance = accountClient.getAccountBalance(100001L);
balance.subscribe(
    b -> System.out.println("Current balance: " + b.getBalanceAmount()),
    error -> System.err.println("Error retrieving balance: " + error.getMessage())
);
```

### Account Space Operations

```java
// Get the account space client
AccountSpaceClient spaceClient = client.getAccountSpaceClient();

// Create a new account space
AccountSpaceDTO newSpace = new AccountSpaceDTO();
newSpace.setAccountId(100001L);
newSpace.setSpaceName("Vacation Fund");
newSpace.setSpaceType(AccountSpaceTypeEnum.VACATION);
newSpace.setBalance(BigDecimal.ZERO);
newSpace.setTargetAmount(new BigDecimal("2000.00"));
newSpace.setTargetDate(LocalDateTime.now().plusMonths(6));
newSpace.setIconId("vacation_icon");
newSpace.setColorCode("#FF5733");
newSpace.setIsVisible(true);
newSpace.setDescription("Saving for summer vacation in Italy");

Mono<AccountSpaceDTO> createdSpace = spaceClient.createAccountSpace(newSpace);
createdSpace.subscribe(
    space -> System.out.println("Created space with ID: " + space.getAccountSpaceId()),
    error -> System.err.println("Error creating space: " + error.getMessage())
);

// Get an account space by ID
Mono<AccountSpaceDTO> space = spaceClient.getAccountSpace(1000002L);

// Update an account space
space.flatMap(existingSpace -> {
    existingSpace.setTargetAmount(new BigDecimal("2500.00"));
    return spaceClient.updateAccountSpace(existingSpace.getAccountSpaceId(), existingSpace);
}).subscribe(
    updatedSpace -> System.out.println("Updated space: " + updatedSpace.getSpaceName()),
    error -> System.err.println("Error updating space: " + error.getMessage())
);

// Get all spaces for an account
Flux<AccountSpaceDTO> spaces = spaceClient.getAccountSpacesByAccountId(100001L);
spaces.subscribe(
    space -> System.out.println("Found space: " + space.getSpaceName()),
    error -> System.err.println("Error retrieving spaces: " + error.getMessage())
);

// Transfer between spaces
Mono<Boolean> transferResult = spaceClient.transferBetweenSpaces(1000001L, 1000002L, new BigDecimal("200.00"));
transferResult.subscribe(
    success -> System.out.println("Transfer successful: " + success),
    error -> System.err.println("Error transferring between spaces: " + error.getMessage())
);

// Calculate goal progress
Mono<AccountSpaceDTO> goalProgress = spaceClient.calculateGoalProgress(1000002L);
goalProgress.subscribe(
    space -> {
        System.out.println("Space: " + space.getSpaceName());
        System.out.println("Progress: " + space.getGoalProgressPercentage() + "%");
        System.out.println("Remaining: " + space.getRemainingToTarget());
        System.out.println("Estimated completion: " + space.getEstimatedCompletionDate());
    },
    error -> System.err.println("Error calculating goal progress: " + error.getMessage())
);

// Configure automatic transfers
Mono<AccountSpaceDTO> autoTransferResult = spaceClient.configureAutomaticTransfers(
    1000002L, true, TransferFrequencyEnum.MONTHLY, new BigDecimal("100.00"), 1000001L);
autoTransferResult.subscribe(
    space -> System.out.println("Configured automatic transfers for: " + space.getSpaceName()),
    error -> System.err.println("Error configuring automatic transfers: " + error.getMessage())
);

// Get the account space transaction client
AccountSpaceTransactionClient spaceTransactionClient = client.getAccountSpaceTransactionClient();

// Record a transaction for an account space
Mono<SpaceTransactionDTO> transactionResult = spaceTransactionClient.recordTransaction(
    1000002L, new BigDecimal("50.00"), "Monthly deposit", "TXN-123456");
transactionResult.subscribe(
    transaction -> System.out.println("Recorded transaction with ID: " + transaction.getSpaceTransactionId()),
    error -> System.err.println("Error recording transaction: " + error.getMessage())
);

// Get transactions for an account space
Mono<PaginationResponse<SpaceTransactionDTO>> transactions =
    spaceTransactionClient.getTransactions(1000002L, 0, 10);
transactions.subscribe(
    response -> {
        System.out.println("Total transactions: " + response.getTotalElements());
        response.getContent().forEach(tx ->
            System.out.println("Transaction: " + tx.getAmount() + " (" + tx.getTransactionType() + ")"));
    },
    error -> System.err.println("Error retrieving transactions: " + error.getMessage())
);

// Get transactions for a specific date range
Mono<PaginationResponse<SpaceTransactionDTO>> dateRangeTransactions =
    spaceTransactionClient.getTransactionsByDateRange(
        1000002L,
        LocalDateTime.now().minusMonths(1),
        LocalDateTime.now(),
        0, 10);
dateRangeTransactions.subscribe(
    response -> {
        System.out.println("Transactions in date range: " + response.getTotalElements());
        response.getContent().forEach(tx ->
            System.out.println("Transaction: " + tx.getAmount() + " on " + tx.getTransactionDateTime()));
    },
    error -> System.err.println("Error retrieving date range transactions: " + error.getMessage())
);

// Calculate total deposits for a date range
Mono<BigDecimal> totalDeposits = spaceTransactionClient.calculateTotalDeposits(
    1000002L, LocalDateTime.now().minusMonths(1), LocalDateTime.now());
totalDeposits.subscribe(
    amount -> System.out.println("Total deposits: " + amount),
    error -> System.err.println("Error calculating total deposits: " + error.getMessage())
);

// Calculate total withdrawals for a date range
Mono<BigDecimal> totalWithdrawals = spaceTransactionClient.calculateTotalWithdrawals(
    1000002L, LocalDateTime.now().minusMonths(1), LocalDateTime.now());
totalWithdrawals.subscribe(
    amount -> System.out.println("Total withdrawals: " + amount),
    error -> System.err.println("Error calculating total withdrawals: " + error.getMessage())
);

// Get balance at a specific point in time
Mono<BigDecimal> balanceAt = spaceTransactionClient.getBalanceAtDateTime(
    1000002L, LocalDateTime.now().minusDays(7));
balanceAt.subscribe(
    balance -> System.out.println("Balance 7 days ago: " + balance),
    error -> System.err.println("Error retrieving historical balance: " + error.getMessage())
);
```

### Account Parameter Operations

```java
// Get the account parameter client
AccountParameterClient parameterClient = client.getAccountParameterClient();

// Create a new account parameter
AccountParameterDTO newParameter = new AccountParameterDTO();
newParameter.setAccountId(100001L);
newParameter.setParamType(ParamTypeEnum.INTEREST_RATE);
newParameter.setParamValue(new BigDecimal("0.025"));
newParameter.setParamUnit("PERCENT");
newParameter.setEffectiveDate(LocalDateTime.now());
newParameter.setExpiryDate(LocalDateTime.now().plusYears(1));
newParameter.setDescription("Annual interest rate");

Mono<AccountParameterDTO> createdParameter = parameterClient.createParameter(100001L, newParameter);
createdParameter.subscribe(
    parameter -> System.out.println("Created parameter with ID: " + parameter.getAccountParameterId()),
    error -> System.err.println("Error creating parameter: " + error.getMessage())
);

// Get an account parameter by ID
Mono<AccountParameterDTO> parameter = parameterClient.getParameter(100001L, 10001L);

// Update an account parameter
parameter.flatMap(existingParameter -> {
    existingParameter.setParamValue(new BigDecimal("0.03"));
    return parameterClient.updateParameter(
        existingParameter.getAccountId(),
        existingParameter.getAccountParameterId(),
        existingParameter
    );
}).subscribe(
    updatedParameter -> System.out.println("Updated parameter: " + updatedParameter.getParamType()),
    error -> System.err.println("Error updating parameter: " + error.getMessage())
);

// List parameters for an account
Mono<PaginationResponse<AccountParameterDTO>> parameters =
    parameterClient.listParameters(100001L, 0, 10);
parameters.subscribe(
    response -> {
        System.out.println("Total parameters: " + response.getTotalElements());
        response.getContent().forEach(param ->
            System.out.println("Parameter: " + param.getParamType() + " = " + param.getParamValue()));
    },
    error -> System.err.println("Error retrieving parameters: " + error.getMessage())
);

// Delete a parameter
Mono<Void> deleteResult = parameterClient.deleteParameter(100001L, 10001L);
deleteResult.subscribe(
    () -> System.out.println("Parameter deleted successfully"),
    error -> System.err.println("Error deleting parameter: " + error.getMessage())
);
```

### Account Provider Operations

```java
// Get the account provider client
AccountProviderClient providerClient = client.getAccountProviderClient();

// Create a new account provider
AccountProviderDTO newProvider = new AccountProviderDTO();
newProvider.setAccountId(100001L);
newProvider.setProviderName("ClearBank");
newProvider.setExternalReference("CB-ACC-123456");
newProvider.setStatus(ProviderStatusEnum.ACTIVE);

Mono<AccountProviderDTO> createdProvider = providerClient.createProvider(100001L, newProvider);
createdProvider.subscribe(
    provider -> System.out.println("Created provider with ID: " + provider.getAccountProviderId()),
    error -> System.err.println("Error creating provider: " + error.getMessage())
);

// Get an account provider by ID
Mono<AccountProviderDTO> provider = providerClient.getProvider(100001L, 10001L);

// Update an account provider
provider.flatMap(existingProvider -> {
    existingProvider.setStatus(ProviderStatusEnum.SUSPENDED);
    return providerClient.updateProvider(
        existingProvider.getAccountId(),
        existingProvider.getAccountProviderId(),
        existingProvider
    );
}).subscribe(
    updatedProvider -> System.out.println("Updated provider: " + updatedProvider.getProviderName()),
    error -> System.err.println("Error updating provider: " + error.getMessage())
);

// List providers for an account
Mono<PaginationResponse<AccountProviderDTO>> providers =
    providerClient.listProviders(100001L, 0, 10);
providers.subscribe(
    response -> {
        System.out.println("Total providers: " + response.getTotalElements());
        response.getContent().forEach(prov ->
            System.out.println("Provider: " + prov.getProviderName() + " (" + prov.getStatus() + ")"));
    },
    error -> System.err.println("Error retrieving providers: " + error.getMessage())
);

// Create a provider for a specific account space
AccountProviderDTO spaceProvider = new AccountProviderDTO();
spaceProvider.setAccountId(100001L);
spaceProvider.setAccountSpaceId(1000002L);
spaceProvider.setProviderName("Modulr");
spaceProvider.setExternalReference("MOD-ACC-001");
spaceProvider.setStatus(ProviderStatusEnum.ACTIVE);

Mono<AccountProviderDTO> createdSpaceProvider =
    providerClient.createProviderForSpace(100001L, 1000002L, spaceProvider);
createdSpaceProvider.subscribe(
    provider -> System.out.println("Created space provider with ID: " + provider.getAccountProviderId()),
    error -> System.err.println("Error creating space provider: " + error.getMessage())
);
```



### Account Status History Operations

```java
// Get the account status history client
AccountStatusHistoryClient statusHistoryClient = client.getAccountStatusHistoryClient();

// Create a new status history record
AccountStatusHistoryDTO newStatusHistory = new AccountStatusHistoryDTO();
newStatusHistory.setAccountId(100001L);
newStatusHistory.setStatusCode(StatusCodeEnum.OPEN);
newStatusHistory.setStatusStartDatetime(LocalDateTime.now());
newStatusHistory.setReason("Account opened by customer request");

Mono<AccountStatusHistoryDTO> createdStatusHistory =
    statusHistoryClient.createStatusHistory(100001L, newStatusHistory);
createdStatusHistory.subscribe(
    history -> System.out.println("Created status history with ID: " + history.getAccountStatusHistoryId()),
    error -> System.err.println("Error creating status history: " + error.getMessage())
);

// Get a status history record by ID
Mono<AccountStatusHistoryDTO> statusHistory =
    statusHistoryClient.getStatusHistory(100001L, 10001L);

// Update a status history record
statusHistory.flatMap(existingHistory -> {
    existingHistory.setStatusEndDatetime(LocalDateTime.now());
    return statusHistoryClient.updateStatusHistory(
        existingHistory.getAccountId(),
        existingHistory.getAccountStatusHistoryId(),
        existingHistory
    );
}).subscribe(
    updatedHistory -> System.out.println("Updated status history: " + updatedHistory.getStatusCode()),
    error -> System.err.println("Error updating status history: " + error.getMessage())
);

// List status history records for an account
Mono<PaginationResponse<AccountStatusHistoryDTO>> statusHistoryRecords =
    statusHistoryClient.listStatusHistory(100001L, 0, 10);
statusHistoryRecords.subscribe(
    response -> {
        System.out.println("Total status history records: " + response.getTotalElements());
        response.getContent().forEach(history ->
            System.out.println("Status: " + history.getStatusCode() +
                " from " + history.getStatusStartDatetime() +
                (history.getStatusEndDatetime() != null ? " to " + history.getStatusEndDatetime() : "")));
    },
    error -> System.err.println("Error retrieving status history: " + error.getMessage())
);
```

## Advanced Usage

### Error Handling

The SDK uses Project Reactor's error handling mechanisms. Here's how to handle errors properly:

```java
client.getAccountClient().getAccount(999999L)
    .doOnSuccess(account -> System.out.println("Found account: " + account.getAccountNumber()))
    .doOnError(error -> System.err.println("Error occurred: " + error.getMessage()))
    .onErrorResume(error -> {
        // Provide a fallback or alternative action
        System.out.println("Using fallback account");
        return Mono.just(new AccountDTO());
    })
    .subscribe();
```

### Retrying Failed Requests

You can implement retry logic for transient failures:

```java
client.getAccountClient().getAccount(100001L)
    .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
        .filter(throwable -> throwable instanceof IOException))
    .subscribe(
        account -> System.out.println("Account: " + account.getAccountNumber()),
        error -> System.err.println("Failed after retries: " + error.getMessage())
    );
```

### Batch Operations

For batch operations, you can use Reactor's operators:

```java
// Create multiple accounts
List<AccountDTO> accountsToCreate = Arrays.asList(account1, account2, account3);

Flux.fromIterable(accountsToCreate)
    .flatMap(account -> client.getAccountClient().createAccount(account))
    .collectList()
    .subscribe(
        createdAccounts -> System.out.println("Created " + createdAccounts.size() + " accounts"),
        error -> System.err.println("Error in batch operation: " + error.getMessage())
    );
```

## Logging

The SDK supports logging of HTTP requests and responses. To enable logging:

```java
CoreBankingAccountsClientConfig config = new CoreBankingAccountsClientConfig();
config.setEnableLogging(true);
CoreBankingAccountsClient client = new CoreBankingAccountsClient(config);
```

## Troubleshooting

### Common Issues

1. **Connection Timeouts**

   If you're experiencing connection timeouts, try increasing the timeout values:

   ```java
   CoreBankingAccountsClientConfig config = new CoreBankingAccountsClientConfig();
   config.setConnectTimeoutMs(15000);
   config.setReadTimeoutMs(15000);
   config.setWriteTimeoutMs(15000);
   ```

2. **Memory Issues with Large Responses**

   If you're dealing with large responses, increase the maximum in-memory size:

   ```java
   CoreBankingAccountsClientConfig config = new CoreBankingAccountsClientConfig();
   config.setMaxInMemorySize(64 * 1024 * 1024); // 64MB
   ```

3. **SSL/TLS Issues**

   For SSL/TLS issues, you may need to customize the HttpClient:

   ```java
   CoreBankingAccountsClientConfig config = new CoreBankingAccountsClientConfig();
   config.setHttpClientCustomizer(httpClient ->
       httpClient.secure(sslContextSpec ->
           sslContextSpec.sslContext(SslContextBuilder.forClient().build())
       )
   );
   ```

### Debugging

For detailed debugging, enable logging and use the `doOnNext` and `doOnError` operators:

```java
client.getAccountClient().getAccount(100001L)
    .doOnSubscribe(s -> System.out.println("Subscribing to getAccount"))
    .doOnNext(account -> System.out.println("Received account: " + account))
    .doOnError(error -> System.err.println("Error: " + error.getMessage()))
    .doOnSuccess(account -> System.out.println("Successfully retrieved account"))
    .doOnTerminate(() -> System.out.println("Operation terminated"))
    .subscribe();
```

## Support

For issues, questions, or feature requests, please contact the Core Banking team or submit an issue in the project repository.