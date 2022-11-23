package com.example.shop.product.service;

import com.example.shop.common.model.Product;
import com.example.shop.common.model.Review;
import com.example.shop.common.repository.ProductRepository;
import com.example.shop.common.repository.ReviewRepository;
import com.example.shop.product.service.dto.ProductDTO;
import com.example.shop.product.service.dto.ReviewDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private ProductRepository productRepository;
    private ReviewRepository reviewRepository;

    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public ProductDTO getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug).orElseThrow();
        List<Review> reviews = reviewRepository.findAllByProductIdAndModerated(product.getId(), true);
        return mapToProductDTO(product, reviews);
    }

    @Transactional(readOnly = true)
    ProductDTO mapToProductDTO(Product product, List<Review> reviews) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .categoryId(product.getCategoryId())
                .description(product.getDescription())
                .fullDescription(product.getFullDescription())
                .price(product.getPrice())
                .currency(product.getCurrency())
                .image(product.getImage())
                .slug(product.getSlug())
                .reviews(reviews.stream()
                        .map(review -> mapToReviewDTO(review))
                        .toList())
                .build();
    }

    @Transactional(readOnly = true)
    ReviewDTO mapToReviewDTO(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .productId(review.getProductId())
                .authorName(review.getAuthorName())
                .content(review.getContent())
                .moderate(review.isModerated())
                .build();
    }
}

