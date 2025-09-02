package com.firefly.core.banking.accounts.interfaces.dtos.space.v1;

import com.firefly.core.banking.accounts.interfaces.enums.space.v1.AccountSpaceTypeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Data Transfer Object for account space analytics.
 * Contains detailed analytics information for a specific account space over a period.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpaceAnalyticsDTO {
    // Space identification
    private UUID accountSpaceId;
    private UUID accountId;
    private String spaceName;
    private AccountSpaceTypeEnum spaceType;
    
    // Analysis period
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    
    // Balance metrics
    private BigDecimal openingBalance;
    private BigDecimal closingBalance;
    private BigDecimal lowestBalance;
    private BigDecimal highestBalance;
    private BigDecimal averageBalance;
    
    // Growth metrics
    private BigDecimal netChange;
    private BigDecimal netChangePercentage;
    private BigDecimal annualizedGrowthRate;
    
    // Transaction metrics
    private Integer totalTransactions;
    private Integer incomingTransactions;
    private Integer outgoingTransactions;
    private BigDecimal totalInflow;
    private BigDecimal totalOutflow;
    private BigDecimal averageTransactionAmount;
    
    // Time-series data
    private List<TimeSeriesDataPoint> balanceHistory;
    private Map<String, BigDecimal> categoryDistribution;
    
    // Goal tracking (if applicable)
    private BigDecimal goalProgress;
    private BigDecimal projectedCompletionPercentage;
    private LocalDateTime projectedCompletionDate;
    
    // Comparison to other spaces
    private BigDecimal percentageOfTotalAccountBalance;
    private Integer rankByBalance;
    private Integer rankByGrowth;
    
    /**
     * Represents a single data point in a time series.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeSeriesDataPoint {
        private LocalDateTime timestamp;
        private BigDecimal value;
    }
}
