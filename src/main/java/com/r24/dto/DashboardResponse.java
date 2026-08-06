package com.r24.dto;

import com.r24.entity.Gallery;
import com.r24.entity.Product;
import lombok.*;

import java.util.List;

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
    private long totalReviews;
    private long totalChatLeads;

    private List<Product> recentProducts;
    private List<Gallery> recentGallery;
}
