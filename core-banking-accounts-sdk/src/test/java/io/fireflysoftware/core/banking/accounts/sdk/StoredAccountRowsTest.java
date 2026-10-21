/*
 * Copyright 2025 Firefly Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.fireflysoftware.core.banking.accounts.sdk;

import io.fireflysoftware.core.banking.accounts.sdk.api.CustomerPositionApi;
import io.fireflysoftware.core.banking.accounts.sdk.api.DepositProductApi;
import io.fireflysoftware.core.banking.accounts.sdk.api.ServicingMandateApi;
import io.fireflysoftware.core.banking.accounts.sdk.invoker.ApiClient;
import io.fireflysoftware.core.banking.accounts.sdk.model.AccountLimitDTO;
import io.fireflysoftware.core.banking.accounts.sdk.model.AccountStatementDTO;
import io.fireflysoftware.core.banking.accounts.sdk.model.AccountSweepDTO;
import io.fireflysoftware.core.banking.accounts.sdk.model.CustomerPositionDTO;
import io.fireflysoftware.core.banking.accounts.sdk.model.SavingsInterestDTO;
import io.fireflysoftware.core.banking.accounts.sdk.model.ServicingMandateDTO;
import io.fireflysoftware.core.banking.accounts.sdk.model.TermDepositDTO;
import io.fireflysoftware.core.banking.accounts.web.AccountsApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = AccountsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.application.name=core-banking-accounts",
                "spring.application.version=26.10.3",
                "spring.application.description=Accounts core",
                "spring.application.team.name=Firefly Software Foundation",
                "spring.application.team.email=dev@fireflysoftware.io",
                "spring.r2dbc.url=r2dbc:postgresql://127.0.0.1:5432/firefly_accounts_it?sslMode=disable",
                "spring.r2dbc.username=ancongui",
                "spring.r2dbc.password=",
                "spring.flyway.url=jdbc:postgresql://127.0.0.1:5432/firefly_accounts_it?sslMode=disable",
                "spring.flyway.user=ancongui",
                "spring.flyway.password="
        })
class StoredAccountRowsTest {

    static {
        System.setProperty("DB_HOST", "127.0.0.1");
        System.setProperty("DB_PORT", "5432");
        System.setProperty("DB_NAME", "firefly_accounts_it");
        System.setProperty("DB_USERNAME", "ancongui");
        System.setProperty("DB_PASSWORD", "");
        System.setProperty("DB_SSL_MODE", "disable");
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/postgres", "ancongui", "");
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE DATABASE firefly_accounts_it");
        } catch (SQLException exception) {
            if (!"42P04".equals(exception.getSQLState()) && !"23505".equals(exception.getSQLState())) {
                throw new IllegalStateException(exception);
            }
        }
    }

    @LocalServerPort
    private int port;

    @Test
    void servicingMandateIsSavedAndReadBackThroughTheSdk() throws Exception {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://127.0.0.1:5432/firefly_accounts_it", "ancongui", "");
             Statement statement = connection.createStatement()) {
            statement.execute("""
                    INSERT INTO account (account_id, contract_id, account_number, account_type, currency, open_date, account_status, branch_id)
                    VALUES ('aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaa1', 'bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbbbbb1',
                            'MANDATE-ROW-1', 'CHECKING', 'USD', CURRENT_DATE, 'OPEN',
                            'cccccccc-cccc-4ccc-8ccc-ccccccccccc1')
                    ON CONFLICT (account_id) DO NOTHING
                    """);
        }
        UUID accountId = UUID.fromString("aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaa1");
        UUID partyId = UUID.randomUUID();
        ServicingMandateApi api = new ServicingMandateApi(client());
        ServicingMandateDTO created = api.createServicingMandate(accountId, new ServicingMandateDTO()
                .partyId(partyId)
                .role(ServicingMandateDTO.RoleEnum.OPERATOR)
                .active(true), "mandate-row").block();
        ServicingMandateDTO read = api.getServicingMandate(accountId, created.getServicingMandateId(), "mandate-read").block();
        assertThat(read.getPartyId()).isEqualTo(partyId);
        assertThat(read.getAccountId()).isEqualTo(accountId);
        assertThat(read.getRole()).isEqualTo(ServicingMandateDTO.RoleEnum.OPERATOR);
        assertThat(read.getActive()).isTrue();
    }

    @Test
    void customerPositionIsSavedAndReadBackThroughTheSdk() {
        UUID partyId = UUID.randomUUID();
        CustomerPositionApi api = new CustomerPositionApi(client());
        CustomerPositionDTO created = api.recordCustomerPosition(new CustomerPositionDTO()
                .partyId(partyId)
                .currency("USD")
                .balances(new BigDecimal("100.00"))
                .creditLimit(new BigDecimal("50.00"))
                .creditUsed(new BigDecimal("20.00"))
                .collateralValue(new BigDecimal("75.00"))
                .taxWithheld(new BigDecimal("5.00")), "position-row").block();
        CustomerPositionDTO read = api.getCustomerPosition(created.getCustomerPositionId(), "position-read").block();
        assertThat(read.getPartyId()).isEqualTo(partyId);
        assertThat(read.getAvailableFunds()).isEqualByComparingTo("125.00");
        assertThat(read.getCreditRemaining()).isEqualByComparingTo("30.00");
        assertThat(read.getCollateralValue()).isEqualByComparingTo("75.00");
        assertThat(read.getTaxWithheld()).isEqualByComparingTo("5.00");
    }

    @Test
    void statementLimitInterestTermAndSweepAreSavedAndReadBack() throws Exception {
        UUID accountId = UUID.fromString("aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaa1");
        try (Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://127.0.0.1:5432/firefly_accounts_it", "ancongui", "");
             Statement statement = connection.createStatement()) {
            statement.execute("""
                    INSERT INTO account (account_id, contract_id, account_number, account_type, currency, open_date, account_status, branch_id)
                    VALUES ('aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaa1', 'bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbbbbb1',
                            'MANDATE-ROW-1', 'CHECKING', 'USD', CURRENT_DATE, 'OPEN',
                            'cccccccc-cccc-4ccc-8ccc-ccccccccccc1')
                    ON CONFLICT (account_id) DO NOTHING
                    """);
            statement.execute("""
                    INSERT INTO account_space (account_space_id, account_id, space_name, space_type, balance, is_visible, is_frozen)
                    VALUES ('dddddddd-dddd-4ddd-8ddd-ddddddddddd1', 'aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaa1', 'Main', 'MAIN', 100, TRUE, FALSE),
                           ('dddddddd-dddd-4ddd-8ddd-ddddddddddd2', 'aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaa1', 'Savings', 'SAVINGS', 0, TRUE, FALSE)
                    ON CONFLICT (account_space_id) DO NOTHING
                    """);
        }
        DepositProductApi api = new DepositProductApi(client());
        AccountStatementDTO statement = api.recordAccountStatement(accountId, new AccountStatementDTO()
                .currency("USD").openingBalance(new BigDecimal("100.00"))
                .lineAmounts(List.of(new BigDecimal("25.00"), new BigDecimal("-10.00"))), "stmt").block();
        AccountStatementDTO readStatement = api.getAccountStatement(accountId, statement.getAccountStatementId(), "stmt-read").block();
        assertThat(readStatement.getCredits()).isEqualByComparingTo("25.00");
        assertThat(readStatement.getDebits()).isEqualByComparingTo("10.00");
        assertThat(readStatement.getClosingBalance()).isEqualByComparingTo("115.00");

        AccountLimitDTO limit = api.recordAccountLimit(accountId, new AccountLimitDTO()
                .currency("USD").withdrawalLimit(new BigDecimal("50.00")).overdraftLimit(new BigDecimal("10.00"))
                .balance(new BigDecimal("40.00")).withdrawalAmount(new BigDecimal("45.00")), "limit").block();
        AccountLimitDTO readLimit = api.getAccountLimit(accountId, limit.getAccountLimitId(), "limit-read").block();
        assertThat(readLimit.getAllowed()).isTrue();
        assertThat(readLimit.getWithdrawalLimit()).isEqualByComparingTo("50.00");

        SavingsInterestDTO interest = api.recordSavingsInterest(accountId, new SavingsInterestDTO()
                .currency("USD").balance(new BigDecimal("36500.00")).annualRatePercent(new BigDecimal("1"))
                .dayCount(1), "interest").block();
        SavingsInterestDTO readInterest = api.getSavingsInterest(accountId, interest.getSavingsInterestId(), "interest-read").block();
        assertThat(readInterest.getInterestAmount()).isEqualByComparingTo("1.00");

        TermDepositDTO term = api.recordTermDeposit(accountId, new TermDepositDTO()
                .currency("USD").principal(new BigDecimal("1000.00")).annualRatePercent(BigDecimal.ZERO)
                .openDate(LocalDate.of(2026, 1, 1)).maturityDate(LocalDate.of(2026, 12, 31))
                .withdrawalDate(LocalDate.of(2026, 6, 1)).penaltyRatePercent(new BigDecimal("1")), "term").block();
        TermDepositDTO readTerm = api.getTermDeposit(accountId, term.getTermDepositId(), "term-read").block();
        assertThat(readTerm.getPayoutAmount()).isEqualByComparingTo("990.00");

        AccountSweepDTO sweep = api.recordAccountSweep(accountId, new AccountSweepDTO()
                .sourceSpaceId(UUID.fromString("dddddddd-dddd-4ddd-8ddd-ddddddddddd1"))
                .targetSpaceId(UUID.fromString("dddddddd-dddd-4ddd-8ddd-ddddddddddd2"))
                .amount(new BigDecimal("15.00")).currency("USD"), "sweep").block();
        AccountSweepDTO readSweep = api.getAccountSweep(accountId, sweep.getAccountSweepId(), "sweep-read").block();
        assertThat(readSweep.getAmount()).isEqualByComparingTo("15.00");
        assertThat(readSweep.getCurrency()).isEqualTo("USD");
    }

    private ApiClient client() {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath("http://127.0.0.1:" + port);
        return apiClient;
    }
}
