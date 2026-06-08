package com.fresh.service;

import com.fresh.entity.Product;
import com.fresh.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> listAll() {
        return productRepository.findByActiveTrue();
    }

    public List<Product> listByCategory(String category) {
        return productRepository.findByCategoryAndActiveTrue(category);
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("商品不存在: " + id));
    }

    public Product create(Product product) {
        return productRepository.save(product);
    }

    public Product update(Long id, Product product) {
        Product existing = getById(id);
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setImageUrl(product.getImageUrl());
        existing.setPrice(product.getPrice());
        existing.setOriginalPrice(product.getOriginalPrice());
        existing.setUnit(product.getUnit());
        existing.setStock(product.getStock());
        existing.setCategory(product.getCategory());
        existing.setProductType(product.getProductType());
        existing.setFreshnessCondition(product.getFreshnessCondition());
        existing.setActive(product.getActive());
        return productRepository.save(existing);
    }

    public void delete(Long id) {
        Product existing = getById(id);
        existing.setActive(false);
        productRepository.save(existing);
    }
}
