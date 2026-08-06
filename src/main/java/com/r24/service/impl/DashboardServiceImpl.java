package com.r24.service.impl;

import com.r24.dto.DashboardResponse;
import com.r24.repository.CategoryRepository;
import com.r24.repository.ChatLeadRepository;
import com.r24.repository.ContactMessageRepository;
import com.r24.repository.GalleryRepository;
import com.r24.repository.ProductRepository;
import com.r24.repository.ReviewRepository;
import com.r24.repository.ServiceBookingRepository;
import com.r24.service.DashboardService;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final GalleryRepository galleryRepository;
    private final ServiceBookingRepository bookingRepository;
    private final ContactMessageRepository messageRepository;
    private final ReviewRepository reviewRepository;
    private final ChatLeadRepository chatLeadRepository;

    public DashboardServiceImpl(CategoryRepository categoryRepository,
                                ProductRepository productRepository,
                                GalleryRepository galleryRepository,
                                ServiceBookingRepository bookingRepository,
                                ContactMessageRepository messageRepository,
                                ReviewRepository reviewRepository,
                                ChatLeadRepository chatLeadRepository) {

        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.galleryRepository = galleryRepository;
        this.bookingRepository = bookingRepository;
        this.messageRepository = messageRepository;
        this.reviewRepository = reviewRepository;
        this.chatLeadRepository = chatLeadRepository;
    }

    @Override
    public DashboardResponse getDashboardData() {

        return DashboardResponse.builder()
                .totalCategories(categoryRepository.count())
                .totalProducts(productRepository.count())
                .totalGalleryImages(galleryRepository.count())
                .totalBookings(bookingRepository.count())
                .totalMessages(messageRepository.count())
                .totalReviews(reviewRepository.count())
                .totalChatLeads(chatLeadRepository.count())
                .recentProducts(productRepository.findTop5ByOrderByIdDesc())
                .recentGallery(galleryRepository.findTop5ByOrderByIdDesc())
                .build();
    }
}
