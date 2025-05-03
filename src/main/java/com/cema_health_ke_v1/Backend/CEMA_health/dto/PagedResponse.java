
package com.cema_health_ke_v1.Backend.CEMA_health.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PagedResponse<T> {
    private final int statusCode;
    private final String statusDescription;
    private final int messageCode;
    private final String messageDescription;
    private final List<T> data;
    private final int currentPage;
    private final int totalPages;
    private final long totalItems;
    private final int itemsPerPage;

    public PagedResponse(int statusCode, String statusDescription,
                         int messageCode, String messageDescription,
                         List<T> data, int currentPage,
                         int totalPages, long totalItems,
                         int itemsPerPage) {
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
        this.messageCode = messageCode;
        this.messageDescription = messageDescription;
        this.data = data;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
        this.itemsPerPage = itemsPerPage;
    }

    // Simplified constructor for single-item responses
    public PagedResponse(int statusCode, String message, List<T> data,
                         int currentPage, int totalPages,
                         long totalItems, int itemsPerPage) {
        this(statusCode, "Success", 1000, message,
                data, currentPage, totalPages, totalItems, itemsPerPage);
    }
}