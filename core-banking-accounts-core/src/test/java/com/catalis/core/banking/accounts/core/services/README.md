# Core Banking Accounts Service - Test Documentation

## Overview

This directory contains unit tests for the service layer of the Core Banking Accounts microservice. The tests are designed to verify the correct behavior of all service implementations, ensuring that they handle both positive scenarios and edge cases appropriately.

## Test Coverage

The test suite covers the following service implementations:

1. **Account Service**
   - `AccountServiceImplTest`: Tests for account creation, retrieval, update, and deletion operations.

2. **Account Balance Service**
   - `AccountBalanceServiceImplTest`: Tests for balance management operations, including pagination, creation, retrieval, update, and deletion.

3. **Account Parameter Service**
   - `AccountParameterServiceImplTest`: Tests for account parameter operations, including pagination, creation, retrieval, update, and deletion.

4. **Account Provider Service**
   - `AccountProviderServiceImplTest`: Tests for account provider operations, including pagination, creation, retrieval, update, and deletion.

5. **Account Status History Service**
   - `AccountStatusHistoryServiceImplTest`: Tests for status history operations, including pagination, creation, retrieval, update, and deletion.

## Testing Approach

### Mocking Strategy

The tests use Mockito to mock dependencies:
- Repository interfaces are mocked to isolate the service layer from the data access layer
- Mapper interfaces are mocked to isolate the service layer from the mapping logic
- Static utility methods (like `PaginationUtils.paginateQuery`) are mocked using `MockedStatic`

### Reactive Testing

Since the services return reactive types (`Mono`), the tests use StepVerifier from the `reactor-test` library to verify the reactive streams:
- `StepVerifier.create(...)` is used to create a verifier for the reactive stream
- `.expectNext(...)` is used to verify the expected result
- `.verifyComplete()` is used to verify that the stream completes successfully

### Test Scenarios

For each service method, the tests cover:

1. **Positive Scenarios**
   - Successful operations when all preconditions are met

2. **Edge Cases**
   - When records don't exist
   - When records exist but belong to a different account
   - When operations fail due to other conditions

### Test Data

Test data is initialized in the `setUp` method of each test class:
- Entity objects are created with appropriate test values
- DTO objects are created with corresponding values
- Constants are defined for test IDs to ensure consistency

## Running the Tests

The tests can be run using Maven:

```bash
mvn test -pl core-banking-accounts-core
```

Or individually using your IDE's test runner.

## Test Results

All tests pass successfully, confirming that the service implementations behave as expected.

## Future Improvements

Potential improvements to the test suite:

1. Add integration tests that test the services with actual repositories
2. Add tests for error scenarios (e.g., when repositories throw exceptions)
3. Add performance tests for pagination with large datasets
4. Add tests for concurrent operations to verify thread safety