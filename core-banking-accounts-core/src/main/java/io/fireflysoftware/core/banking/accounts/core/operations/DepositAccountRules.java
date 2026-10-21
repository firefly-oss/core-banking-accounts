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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

/**
 * Rules for statements, limits, savings interest, term deposits, mandates, and a customer position.
 */
public final class DepositAccountRules {

    public enum MandateRole {
        OWNER,
        OPERATOR,
        VIEWER
    }

    public record StatementLine(BigDecimal amount) {
    }

    public record Statement(BigDecimal opening, BigDecimal credits, BigDecimal debits, BigDecimal closing) {
    }

    public record Position(BigDecimal availableFunds, BigDecimal creditRemaining, BigDecimal collateralValue,
                           BigDecimal taxWithheld) {
    }

    private DepositAccountRules() {
    }

    public static Statement statement(BigDecimal opening, List<StatementLine> lines) {
        BigDecimal start = opening == null ? BigDecimal.ZERO : opening;
        BigDecimal credits = BigDecimal.ZERO;
        BigDecimal debits = BigDecimal.ZERO;
        if (lines != null) {
            for (StatementLine line : lines) {
                if (line.amount() == null) {
                    continue;
                }
                if (line.amount().signum() >= 0) {
                    credits = credits.add(line.amount());
                } else {
                    debits = debits.add(line.amount().abs());
                }
            }
        }
        return new Statement(scale(start), scale(credits), scale(debits), scale(start.add(credits).subtract(debits)));
    }

    public static void checkWithdrawal(BigDecimal amount, BigDecimal balance, BigDecimal withdrawalLimit,
                                       BigDecimal overdraftLimit) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        BigDecimal limit = withdrawalLimit == null ? amount : withdrawalLimit;
        if (amount.compareTo(limit) > 0) {
            throw new IllegalArgumentException("Withdrawal exceeds the account limit");
        }
        BigDecimal available = (balance == null ? BigDecimal.ZERO : balance)
                .add(overdraftLimit == null ? BigDecimal.ZERO : overdraftLimit);
        if (amount.compareTo(available) > 0) {
            throw new IllegalArgumentException("Withdrawal exceeds available funds");
        }
    }

    public static boolean mayMoveMoney(MandateRole role, boolean active) {
        return active && (role == MandateRole.OWNER || role == MandateRole.OPERATOR);
    }

    public static BigDecimal savingsInterest(BigDecimal balance, BigDecimal annualRatePercent, int days) {
        if (balance == null || balance.signum() < 0 || annualRatePercent == null || days < 0) {
            throw new IllegalArgumentException("Savings interest needs a balance, a rate, and a day count");
        }
        return balance.multiply(annualRatePercent).multiply(BigDecimal.valueOf(days))
                .divide(new BigDecimal("36500"), 2, RoundingMode.HALF_UP);
    }

    public static BigDecimal termDepositPayout(BigDecimal principal, BigDecimal annualRatePercent, LocalDate open,
                                              LocalDate maturity, LocalDate withdrawalDate, BigDecimal penaltyRatePercent) {
        if (principal == null || principal.signum() <= 0 || open == null || maturity == null || withdrawalDate == null) {
            throw new IllegalArgumentException("A term deposit needs principal and dates");
        }
        if (withdrawalDate.isBefore(open)) {
            throw new IllegalArgumentException("Withdrawal cannot precede the open date");
        }
        int days = (int) (withdrawalDate.toEpochDay() - open.toEpochDay());
        BigDecimal interest = savingsInterest(principal, annualRatePercent == null ? BigDecimal.ZERO : annualRatePercent, days);
        if (!withdrawalDate.isBefore(maturity)) {
            return scale(principal.add(interest));
        }
        BigDecimal penalty = principal.multiply(penaltyRatePercent == null ? BigDecimal.ZERO : penaltyRatePercent)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        return scale(principal.add(interest).subtract(penalty));
    }

    public static Position customerPosition(BigDecimal balances, BigDecimal creditLimit, BigDecimal creditUsed,
                                            BigDecimal collateral, BigDecimal taxWithheld) {
        BigDecimal funds = balances == null ? BigDecimal.ZERO : balances;
        BigDecimal remaining = (creditLimit == null ? BigDecimal.ZERO : creditLimit)
                .subtract(creditUsed == null ? BigDecimal.ZERO : creditUsed);
        if (remaining.signum() < 0) {
            remaining = BigDecimal.ZERO;
        }
        BigDecimal tax = taxWithheld == null ? BigDecimal.ZERO : taxWithheld;
        return new Position(scale(funds.add(remaining).subtract(tax)), scale(remaining),
                scale(collateral == null ? BigDecimal.ZERO : collateral), scale(tax));
    }

    private static BigDecimal scale(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
