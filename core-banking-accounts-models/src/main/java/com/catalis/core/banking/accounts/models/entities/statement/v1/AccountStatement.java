package com.catalis.core.banking.accounts.models.entities.statement.v1;

import com.catalis.core.banking.accounts.interfaces.enums.statement.v1.StatementDeliveryMethodEnum;
import com.catalis.core.banking.accounts.models.entities.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a periodic account statement.
 * Statements provide a summary of account activity for a specific period.
 *
 * Business Rules:
 * - Each account can have multiple statements
 * - Statements are generated according to the account's statement frequency
 * - Statements can be delivered through various methods
 * - Statements must include opening and closing balances
 * - Statements must include the statement period
 */
@Table("account_statement")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountStatement extends BaseEntity {

    /**
     * Unique identifier for the statement record
     * Example: 1000001
     */
    @Id
    private Long accountStatementId;

    /**
     * Reference to the associated account
     * Example: 100001 (links to Account.accountId)
     */
    private Long accountId;

    /**
     * Statement number or reference
     * Examples:
     * - "STMT-2024-01-001"
     * - "JAN2024-100001"
     */
    private String statementNumber;

    /**
     * Start date of the statement period
     * Example: 2024-01-01
     */
    private LocalDate periodStartDate;

    /**
     * End date of the statement period
     * Example: 2024-01-31
     */
    private LocalDate periodEndDate;

    /**
     * Opening balance at the start of the period
     * Example: 1000.0000
     */
    private BigDecimal openingBalance;

    /**
     * Closing balance at the end of the period
     * Example: 1250.0000
     */
    private BigDecimal closingBalance;

    /**
     * Total deposits during the period
     * Example: 500.0000
     */
    private BigDecimal totalDeposits;

    /**
     * Total withdrawals during the period
     * Example: 250.0000
     */
    private BigDecimal totalWithdrawals;

    /**
     * Total fees charged during the period
     * Example: 10.0000
     */
    private BigDecimal totalFees;

    /**
     * Total interest earned during the period
     * Example: 5.0000
     */
    private BigDecimal totalInterest;

    /**
     * When the statement was generated
     * Example: 2024-02-01T00:05:00.000Z
     */
    private LocalDateTime generationDateTime;

    /**
     * Method used to deliver the statement
     * Examples:
     * - MAIL: Sent by postal mail
     * - EMAIL: Sent by email
     * - ONLINE: Available online only
     */
    private StatementDeliveryMethodEnum deliveryMethod;

    /**
     * When the statement was delivered
     * Null if not yet delivered
     * Example: 2024-02-01T08:30:00.000Z
     */
    private LocalDateTime deliveryDateTime;

    /**
     * URL or path to the statement document
     * Example: "https://storage.example.com/statements/100001/2024-01.pdf"
     */
    private String documentUrl;

    /**
     * Whether the statement has been viewed by the customer
     * True if viewed, false if not
     */
    private Boolean isViewed;

    /**
     * When the statement was first viewed
     * Null if not yet viewed
     * Example: 2024-02-02T18:45:00.000Z
     */
    private LocalDateTime firstViewedDateTime;

    /**
     * Additional metadata in JSON format
     * Used to store space-specific information or other custom data
     * Example: {"accountSpaceId":1000001,"spaceName":"Vacation Fund","spaceType":"VACATION"}
     */
    private String metadata;
}
