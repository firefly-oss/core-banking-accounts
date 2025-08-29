package com.firefly.core.banking.accounts.interfaces.dtos.provider.v1;

import com.firefly.core.banking.accounts.interfaces.dtos.BaseDTO;
import com.firefly.core.banking.accounts.interfaces.enums.provider.v1.ProviderStatusEnum;
import com.firefly.core.utils.annotations.FilterableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccountProviderDTO extends BaseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long accountProviderId;

    @FilterableId
    private Long accountId;

    private String providerName;
    private String externalReference;
    private ProviderStatusEnum status;

    @FilterableId
    private Long accountSpaceId;
}
