package com.aws.productapi.repository;

import com.aws.productapi.entity.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {

    Optional<Product> findByCode(String code);
}
