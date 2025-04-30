package com.catalis.core.banking.accounts.sdk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Request object for filtering data.
 *
 * @param <T> The type of the entity to filter
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterRequest<T> {
    @Builder.Default
    private Map<String, Object> filters = new HashMap<>();
    
    private int page;
    private int size;
    private String sortBy;
    private String sortDirection;

    /**
     * Adds a filter criterion.
     *
     * @param field The field to filter on
     * @param value The value to filter by
     * @return This FilterRequest instance for chaining
     */
    public FilterRequest<T> addFilter(String field, Object value) {
        filters.put(field, value);
        return this;
    }

    /**
     * Sets the pagination parameters.
     *
     * @param page The page number (0-based)
     * @param size The page size
     * @return This FilterRequest instance for chaining
     */
    public FilterRequest<T> paginate(int page, int size) {
        this.page = page;
        this.size = size;
        return this;
    }

    /**
     * Sets the sorting parameters.
     *
     * @param sortBy The field to sort by
     * @param sortDirection The sort direction ("asc" or "desc")
     * @return This FilterRequest instance for chaining
     */
    public FilterRequest<T> sort(String sortBy, String sortDirection) {
        this.sortBy = sortBy;
        this.sortDirection = sortDirection;
        return this;
    }
}