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


package io.fireflysoftware.core.banking.accounts.core.operations;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DepositAccountRulesTest {

    @Test
    void statementClosesOnCreditsAndDebits() {
        DepositAccountRules.Statement statement = DepositAccountRules.statement(new BigDecimal("100.00"),
                List.of(new DepositAccountRules.StatementLine(new BigDecimal("40.00")),
                        new DepositAccountRules.StatementLine(new BigDecimal("-15.00"))));
        assertThat(statement.credits()).isEqualByComparingTo("40.00");
        assertThat(statement.debits()).isEqualByComparingTo("15.00");
        assertThat(statement.closing()).isEqualByComparingTo("125.00");
    }

    @Test
    void withdrawalStopsAtTheLimitAndTheOverdraft() {
        DepositAccountRules.checkWithdrawal(new BigDecimal("50"), new BigDecimal("40"),
                new BigDecimal("80"), new BigDecimal("20"));
        assertThatThrownBy(() -> DepositAccountRules.checkWithdrawal(new BigDecimal("90"), new BigDecimal("100"),
                new BigDecimal("80"), new BigDecimal("20")))
                .hasMessageContaining("limit");
    }

    @Test
    void onlyAnActiveOwnerOrOperatorMayMoveMoney() {
        assertThat(DepositAccountRules.mayMoveMoney(DepositAccountRules.MandateRole.OPERATOR, true)).isTrue();
        assertThat(DepositAccountRules.mayMoveMoney(DepositAccountRules.MandateRole.VIEWER, true)).isFalse();
        assertThat(DepositAccountRules.mayMoveMoney(DepositAccountRules.MandateRole.OWNER, false)).isFalse();
    }

    @Test
    void savingsInterestAndEarlyTermDepositPenalty() {
        assertThat(DepositAccountRules.savingsInterest(new BigDecimal("36500.00"), new BigDecimal("1"), 1))
                .isEqualByComparingTo("1.00");
        BigDecimal early = DepositAccountRules.termDepositPayout(new BigDecimal("1000.00"), new BigDecimal("0"),
                LocalDate.of(2026, 1, 1), LocalDate.of(2027, 1, 1), LocalDate.of(2026, 6, 1), new BigDecimal("2"));
        assertThat(early).isEqualByComparingTo("980.00");
        BigDecimal mature = DepositAccountRules.termDepositPayout(new BigDecimal("1000.00"), new BigDecimal("0"),
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 1), new BigDecimal("2"));
        assertThat(mature).isEqualByComparingTo("1000.00");
    }

    @Test
    void customerPositionCombinesFundsCreditCollateralAndTax() {
        DepositAccountRules.Position position = DepositAccountRules.customerPosition(
                new BigDecimal("100.00"), new BigDecimal("50.00"), new BigDecimal("20.00"),
                new BigDecimal("75.00"), new BigDecimal("5.00"));
        assertThat(position.creditRemaining()).isEqualByComparingTo("30.00");
        assertThat(position.availableFunds()).isEqualByComparingTo("125.00");
        assertThat(position.collateralValue()).isEqualByComparingTo("75.00");
        assertThat(position.taxWithheld()).isEqualByComparingTo("5.00");
    }
}
