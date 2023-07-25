package com.springboot.lamp.data.repository;

import com.springboot.lamp.data.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
