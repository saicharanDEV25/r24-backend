package com.r24.repository;

import com.r24.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByCustomerId(Long customerId);

    Optional<Favorite> findByCustomerIdAndProductId(Long customerId, Long productId);

    void deleteByProductId(Long productId);
}
