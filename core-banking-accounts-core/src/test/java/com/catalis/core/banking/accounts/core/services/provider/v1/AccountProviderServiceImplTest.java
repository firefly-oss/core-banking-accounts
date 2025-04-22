package com.catalis.core.banking.accounts.core.services.provider.v1;

import com.catalis.common.core.queries.PaginationRequest;
import com.catalis.common.core.queries.PaginationResponse;
import com.catalis.common.core.queries.PaginationUtils;
import com.catalis.core.banking.accounts.core.mappers.models.provider.v1.AccountProviderMapper;
import com.catalis.core.banking.accounts.interfaces.dtos.provider.v1.AccountProviderDTO;
import com.catalis.core.banking.accounts.interfaces.enums.provider.v1.ProviderStatusEnum;
import com.catalis.core.banking.accounts.models.entities.provider.v1.AccountProvider;
import com.catalis.core.banking.accounts.models.repositories.provider.v1.AccountProviderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountProviderServiceImplTest {

    @Mock
    private AccountProviderRepository repository;

    @Mock
    private AccountProviderMapper mapper;

    @InjectMocks
    private AccountProviderServiceImpl accountProviderService;

    private AccountProvider testAccountProvider;
    private AccountProviderDTO testAccountProviderDTO;
    private final Long TEST_ACCOUNT_ID = 1L;
    private final Long TEST_PROVIDER_ID = 100L;

    @BeforeEach
    void setUp() {
        // Setup test account provider entity
        testAccountProvider = new AccountProvider();
        testAccountProvider.setAccountProviderId(TEST_PROVIDER_ID);
        testAccountProvider.setAccountId(TEST_ACCOUNT_ID);
        testAccountProvider.setProviderName("ClearBank");
        testAccountProvider.setExternalReference("CB-ACC-123456");
        testAccountProvider.setStatus(ProviderStatusEnum.ACTIVE);

        // Setup test account provider DTO
        testAccountProviderDTO = new AccountProviderDTO();
        testAccountProviderDTO.setAccountProviderId(TEST_PROVIDER_ID);
        testAccountProviderDTO.setAccountId(TEST_ACCOUNT_ID);
        testAccountProviderDTO.setProviderName("ClearBank");
        testAccountProviderDTO.setExternalReference("CB-ACC-123456");
        testAccountProviderDTO.setStatus(ProviderStatusEnum.ACTIVE);
        testAccountProviderDTO.setDateCreated(LocalDateTime.now());
        testAccountProviderDTO.setDateUpdated(LocalDateTime.now());
    }

    @Test
    void listProviders_ShouldReturnPaginatedProviders() {
        // Arrange
        PaginationRequest paginationRequest = new PaginationRequest();
        
        // Create a mock PaginationResponse
        @SuppressWarnings("unchecked")
        PaginationResponse<AccountProviderDTO> mockResponse = mock(PaginationResponse.class);
        
        // Mock the static PaginationUtils.paginateQuery method
        try (MockedStatic<PaginationUtils> mockedPaginationUtils = mockStatic(PaginationUtils.class)) {
            mockedPaginationUtils.when(() -> PaginationUtils.paginateQuery(
                    any(PaginationRequest.class),
                    any(Function.class),
                    any(Function.class),
                    any(Supplier.class)
            )).thenReturn(Mono.just(mockResponse));
            
            // Act & Assert
            StepVerifier.create(accountProviderService.listProviders(TEST_ACCOUNT_ID, paginationRequest))
                    .expectNext(mockResponse)
                    .verifyComplete();
                    
            // Verify the PaginationUtils.paginateQuery was called
            mockedPaginationUtils.verify(() -> PaginationUtils.paginateQuery(
                    eq(paginationRequest),
                    any(Function.class),
                    any(Function.class),
                    any(Supplier.class)
            ));
        }
    }

    @Test
    void createProvider_ShouldReturnCreatedProvider() {
        // Arrange
        when(mapper.toEntity(any(AccountProviderDTO.class))).thenReturn(testAccountProvider);
        when(repository.save(any(AccountProvider.class))).thenReturn(Mono.just(testAccountProvider));
        when(mapper.toDTO(any(AccountProvider.class))).thenReturn(testAccountProviderDTO);

        // Act & Assert
        StepVerifier.create(accountProviderService.createProvider(TEST_ACCOUNT_ID, testAccountProviderDTO))
                .expectNext(testAccountProviderDTO)
                .verifyComplete();

        verify(mapper).toEntity(testAccountProviderDTO);
        verify(repository).save(testAccountProvider);
        verify(mapper).toDTO(testAccountProvider);
    }

    @Test
    void getProvider_ShouldReturnProvider_WhenProviderExistsForAccount() {
        // Arrange
        when(repository.findById(TEST_PROVIDER_ID)).thenReturn(Mono.just(testAccountProvider));
        when(mapper.toDTO(testAccountProvider)).thenReturn(testAccountProviderDTO);

        // Act & Assert
        StepVerifier.create(accountProviderService.getProvider(TEST_ACCOUNT_ID, TEST_PROVIDER_ID))
                .expectNext(testAccountProviderDTO)
                .verifyComplete();

        verify(repository).findById(TEST_PROVIDER_ID);
        verify(mapper).toDTO(testAccountProvider);
    }

    @Test
    void getProvider_ShouldReturnEmptyMono_WhenProviderDoesNotExistForAccount() {
        // Arrange
        when(repository.findById(TEST_PROVIDER_ID)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(accountProviderService.getProvider(TEST_ACCOUNT_ID, TEST_PROVIDER_ID))
                .verifyComplete();

        verify(repository).findById(TEST_PROVIDER_ID);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void getProvider_ShouldReturnEmptyMono_WhenProviderExistsButNotForSpecifiedAccount() {
        // Arrange
        AccountProvider providerForDifferentAccount = new AccountProvider();
        providerForDifferentAccount.setAccountProviderId(TEST_PROVIDER_ID);
        providerForDifferentAccount.setAccountId(999L); // Different account ID
        
        when(repository.findById(TEST_PROVIDER_ID)).thenReturn(Mono.just(providerForDifferentAccount));

        // Act & Assert
        StepVerifier.create(accountProviderService.getProvider(TEST_ACCOUNT_ID, TEST_PROVIDER_ID))
                .verifyComplete();

        verify(repository).findById(TEST_PROVIDER_ID);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void updateProvider_ShouldReturnUpdatedProvider_WhenProviderExistsForAccount() {
        // Arrange
        when(repository.findById(TEST_PROVIDER_ID)).thenReturn(Mono.just(testAccountProvider));
        when(mapper.toEntity(testAccountProviderDTO)).thenReturn(testAccountProvider);
        when(repository.save(testAccountProvider)).thenReturn(Mono.just(testAccountProvider));
        when(mapper.toDTO(testAccountProvider)).thenReturn(testAccountProviderDTO);

        // Act & Assert
        StepVerifier.create(accountProviderService.updateProvider(TEST_ACCOUNT_ID, TEST_PROVIDER_ID, testAccountProviderDTO))
                .expectNext(testAccountProviderDTO)
                .verifyComplete();

        verify(repository).findById(TEST_PROVIDER_ID);
        verify(mapper).toEntity(testAccountProviderDTO);
        verify(repository).save(testAccountProvider);
        verify(mapper).toDTO(testAccountProvider);
    }

    @Test
    void updateProvider_ShouldReturnEmptyMono_WhenProviderDoesNotExistForAccount() {
        // Arrange
        when(repository.findById(TEST_PROVIDER_ID)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(accountProviderService.updateProvider(TEST_ACCOUNT_ID, TEST_PROVIDER_ID, testAccountProviderDTO))
                .verifyComplete();

        verify(repository).findById(TEST_PROVIDER_ID);
        verifyNoMoreInteractions(mapper, repository);
    }

    @Test
    void updateProvider_ShouldReturnEmptyMono_WhenProviderExistsButNotForSpecifiedAccount() {
        // Arrange
        AccountProvider providerForDifferentAccount = new AccountProvider();
        providerForDifferentAccount.setAccountProviderId(TEST_PROVIDER_ID);
        providerForDifferentAccount.setAccountId(999L); // Different account ID
        
        when(repository.findById(TEST_PROVIDER_ID)).thenReturn(Mono.just(providerForDifferentAccount));

        // Act & Assert
        StepVerifier.create(accountProviderService.updateProvider(TEST_ACCOUNT_ID, TEST_PROVIDER_ID, testAccountProviderDTO))
                .verifyComplete();

        verify(repository).findById(TEST_PROVIDER_ID);
        verifyNoMoreInteractions(mapper, repository);
    }

    @Test
    void deleteProvider_ShouldDeleteProvider_WhenProviderExistsForAccount() {
        // Arrange
        when(repository.findById(TEST_PROVIDER_ID)).thenReturn(Mono.just(testAccountProvider));
        when(repository.delete(testAccountProvider)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(accountProviderService.deleteProvider(TEST_ACCOUNT_ID, TEST_PROVIDER_ID))
                .verifyComplete();

        verify(repository).findById(TEST_PROVIDER_ID);
        verify(repository).delete(testAccountProvider);
    }

    @Test
    void deleteProvider_ShouldReturnEmptyMono_WhenProviderDoesNotExistForAccount() {
        // Arrange
        when(repository.findById(TEST_PROVIDER_ID)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(accountProviderService.deleteProvider(TEST_ACCOUNT_ID, TEST_PROVIDER_ID))
                .verifyComplete();

        verify(repository).findById(TEST_PROVIDER_ID);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deleteProvider_ShouldReturnEmptyMono_WhenProviderExistsButNotForSpecifiedAccount() {
        // Arrange
        AccountProvider providerForDifferentAccount = new AccountProvider();
        providerForDifferentAccount.setAccountProviderId(TEST_PROVIDER_ID);
        providerForDifferentAccount.setAccountId(999L); // Different account ID
        
        when(repository.findById(TEST_PROVIDER_ID)).thenReturn(Mono.just(providerForDifferentAccount));

        // Act & Assert
        StepVerifier.create(accountProviderService.deleteProvider(TEST_ACCOUNT_ID, TEST_PROVIDER_ID))
                .verifyComplete();

        verify(repository).findById(TEST_PROVIDER_ID);
        verifyNoMoreInteractions(repository);
    }
}