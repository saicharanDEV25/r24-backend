package com.r24.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponse {

    private long totalCategories;
    private long totalProducts;
    private long totalGalleryImages;
    private long totalBookings;
    private long totalMessages;
}