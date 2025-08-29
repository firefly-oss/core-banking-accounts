package com.firefly.core.banking.accounts.models.config;

import com.firefly.core.banking.accounts.interfaces.enums.core.v1.AccountStatusEnum;
import com.firefly.core.banking.accounts.interfaces.enums.core.v1.AccountSubTypeEnum;
import com.firefly.core.banking.accounts.interfaces.enums.core.v1.AccountTypeEnum;
import com.firefly.core.banking.accounts.interfaces.enums.core.v1.BalanceTypeEnum;
import com.firefly.core.banking.accounts.interfaces.enums.interest.v1.InterestAccrualMethodEnum;
import com.firefly.core.banking.accounts.interfaces.enums.interest.v1.InterestPaymentFrequencyEnum;
import com.firefly.core.banking.accounts.interfaces.enums.notification.v1.NotificationTypeEnum;
import com.firefly.core.banking.accounts.interfaces.enums.parameter.v1.ParamTypeEnum;
import com.firefly.core.banking.accounts.interfaces.enums.provider.v1.ProviderStatusEnum;
import com.firefly.core.banking.accounts.interfaces.enums.regulatory.v1.RegulatoryStatusEnum;
import com.firefly.core.banking.accounts.interfaces.enums.regulatory.v1.TaxReportingStatusEnum;
import com.firefly.core.banking.accounts.interfaces.enums.restriction.v1.RestrictionTypeEnum;
import com.firefly.core.banking.accounts.interfaces.enums.space.v1.AccountSpaceTypeEnum;
import com.firefly.core.banking.accounts.interfaces.enums.space.v1.TransferFrequencyEnum;
import com.firefly.core.banking.accounts.interfaces.enums.status.v1.StatusCodeEnum;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.client.SSLMode;
import io.r2dbc.postgresql.codec.EnumCodec;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class R2dbcConfiguration extends AbstractR2dbcConfiguration {

    @Value("${DB_HOST:localhost}")
    private String host;

    @Value("${DB_PORT:5432}")
    private int port;

    @Value("${DB_NAME:postgres}")
    private String database;

    @Value("${DB_USERNAME:postgres}")
    private String username;

    @Value("${DB_PASSWORD:postgres}")
    private String password;

    @Value("${DB_SSL_MODE:disable}")
    private String sslMode;

    @WritingConverter
    static class AccountTypeEnumConverter implements Converter<AccountTypeEnum, AccountTypeEnum> {
        @Override
        public AccountTypeEnum convert(AccountTypeEnum source) {
            return source;
        }
    }

    @WritingConverter
    static class AccountStatusEnumConverter implements Converter<AccountStatusEnum, AccountStatusEnum> {
        @Override
        public AccountStatusEnum convert(AccountStatusEnum source) {
            return source;
        }
    }

    @WritingConverter
    static class AccountSubTypeEnumConverter implements Converter<AccountSubTypeEnum, AccountSubTypeEnum> {
        @Override
        public AccountSubTypeEnum convert(AccountSubTypeEnum source) {
            return source;
        }
    }

    @WritingConverter
    static class BalanceTypeEnumConverter implements Converter<BalanceTypeEnum, BalanceTypeEnum> {
        @Override
        public BalanceTypeEnum convert(BalanceTypeEnum source) {
            return source;
        }
    }

    @WritingConverter
    static class InterestAccrualMethodEnumConverter implements Converter<InterestAccrualMethodEnum, InterestAccrualMethodEnum> {
        @Override
        public InterestAccrualMethodEnum convert(InterestAccrualMethodEnum source) {
            return source;
        }
    }

    @WritingConverter
    static class InterestPaymentFrequencyEnumConverter implements Converter<InterestPaymentFrequencyEnum, InterestPaymentFrequencyEnum> {
        @Override
        public InterestPaymentFrequencyEnum convert(InterestPaymentFrequencyEnum source) {
            return source;
        }
    }

    @WritingConverter
    static class NotificationTypeEnumConverter implements Converter<NotificationTypeEnum, NotificationTypeEnum> {
        @Override
        public NotificationTypeEnum convert(NotificationTypeEnum source) {
            return source;
        }
    }

    @WritingConverter
    static class ParamTypeEnumConverter implements Converter<ParamTypeEnum, ParamTypeEnum> {
        @Override
        public ParamTypeEnum convert(ParamTypeEnum source) {
            return source;
        }
    }

    @WritingConverter
    static class ProviderStatusEnumConverter implements Converter<ProviderStatusEnum, ProviderStatusEnum> {
        @Override
        public ProviderStatusEnum convert(ProviderStatusEnum source) {
            return source;
        }
    }

    @WritingConverter
    static class RegulatoryStatusEnumConverter implements Converter<RegulatoryStatusEnum, RegulatoryStatusEnum> {
        @Override
        public RegulatoryStatusEnum convert(RegulatoryStatusEnum source) {
            return source;
        }
    }

    @WritingConverter
    static class TaxReportingStatusEnumConverter implements Converter<TaxReportingStatusEnum, TaxReportingStatusEnum> {
        @Override
        public TaxReportingStatusEnum convert(TaxReportingStatusEnum source) {
            return source;
        }
    }

    @WritingConverter
    static class RestrictionTypeEnumConverter implements Converter<RestrictionTypeEnum, RestrictionTypeEnum> {
        @Override
        public RestrictionTypeEnum convert(RestrictionTypeEnum source) {
            return source;
        }
    }

    @WritingConverter
    static class AccountSpaceTypeEnumConverter implements Converter<AccountSpaceTypeEnum, AccountSpaceTypeEnum> {
        @Override
        public AccountSpaceTypeEnum convert(AccountSpaceTypeEnum source) {
            return source;
        }
    }

    @WritingConverter
    static class TransferFrequencyEnumConverter implements Converter<TransferFrequencyEnum, TransferFrequencyEnum> {
        @Override
        public TransferFrequencyEnum convert(TransferFrequencyEnum source) {
            return source;
        }
    }

    @WritingConverter
    static class StatusCodeEnumConverter implements Converter<StatusCodeEnum, StatusCodeEnum> {
        @Override
        public StatusCodeEnum convert(StatusCodeEnum source) {
            return source;
        }
    }

    @Override
    protected List<Object> getCustomConverters() {
        List<Object> converters = new ArrayList<>();
        converters.add(new AccountTypeEnumConverter());
        converters.add(new AccountStatusEnumConverter());
        converters.add(new AccountSubTypeEnumConverter());
        converters.add(new BalanceTypeEnumConverter());
        converters.add(new InterestAccrualMethodEnumConverter());
        converters.add(new InterestPaymentFrequencyEnumConverter());
        converters.add(new NotificationTypeEnumConverter());
        converters.add(new ParamTypeEnumConverter());
        converters.add(new ProviderStatusEnumConverter());
        converters.add(new RegulatoryStatusEnumConverter());
        converters.add(new TaxReportingStatusEnumConverter());
        converters.add(new RestrictionTypeEnumConverter());
        converters.add(new AccountSpaceTypeEnumConverter());
        converters.add(new TransferFrequencyEnumConverter());
        converters.add(new StatusCodeEnumConverter());
        return converters;
    }

    @Bean
    @Primary
    @Override
    public ConnectionFactory connectionFactory() {
        return new PostgresqlConnectionFactory(
            PostgresqlConnectionConfiguration.builder()
                .host(host)
                .port(port)
                .username(username)
                .password(password)
                .database(database)
                .sslMode(SSLMode.valueOf(sslMode.toUpperCase()))
                .codecRegistrar(EnumCodec.builder()
                    .withEnum("account_type_enum", AccountTypeEnum.class)
                    .withEnum("account_status_enum", AccountStatusEnum.class)
                    .withEnum("account_sub_type_enum", AccountSubTypeEnum.class)
                    .withEnum("balance_type_enum", BalanceTypeEnum.class)
                    .withEnum("interest_accrual_method_enum", InterestAccrualMethodEnum.class)
                    .withEnum("interest_payment_frequency_enum", InterestPaymentFrequencyEnum.class)
                    .withEnum("notification_type_enum", NotificationTypeEnum.class)
                    .withEnum("param_type_enum", ParamTypeEnum.class)
                    .withEnum("provider_status_enum", ProviderStatusEnum.class)
                    .withEnum("regulatory_status_enum", RegulatoryStatusEnum.class)
                    .withEnum("tax_reporting_status_enum", TaxReportingStatusEnum.class)
                    .withEnum("restriction_type_enum", RestrictionTypeEnum.class)
                    .withEnum("account_space_type_enum", AccountSpaceTypeEnum.class)
                    .withEnum("transfer_frequency_enum", TransferFrequencyEnum.class)
                    .withEnum("status_code_enum", StatusCodeEnum.class)
                    .build())
                .build()
        );
    }
}
