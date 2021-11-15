package com.aws.productapi.controller;

import com.aws.productapi.dto.event.enums.EventType;
import com.aws.productapi.dto.request.ProductRequest;
import com.aws.productapi.dto.response.ProductResponse;
import com.aws.productapi.entity.Product;
import com.aws.productapi.repository.ProductRepository;
import com.aws.productapi.service.ProductPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductPublisher productPublisher;

    @Autowired
    public ProductController(ProductRepository productRepository, ProductPublisher productPublisher) {
        this.productRepository = productRepository;
        this.productPublisher = productPublisher;
    }

    @GetMapping()
    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable("id") Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductResponse> save(@Valid @RequestBody ProductRequest request) {
        Product productCreate = productRepository.save(new Product(request));
        productPublisher.publishProductEvent(productCreate, EventType.PRODUCT_CREATED, "user_created");
        return ResponseEntity.status(HttpStatus.CREATED).body(new ProductResponse(productCreate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@Valid @RequestBody ProductRequest request, @PathVariable("id") Long id) {
        Optional<Product> productUpdate = productRepository.findById(id);

        if (productUpdate.isPresent()) {
            Product product = new Product(id, request);
            productRepository.save(product);
            productPublisher.publishProductEvent(product, EventType.PRODUCT_UPDATE, "user_update");
            return ResponseEntity.ok(new ProductResponse(product));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        Optional<Product> productDelete = productRepository.findById(id);

        if (productDelete.isPresent()) {
            productRepository.deleteById(id);
            productPublisher.publishProductEvent(productDelete.get(), EventType.PRODUCT_DELETED, "user_delete");
            ResponseEntity.noContent().build();
        } else {
            ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/bycode")
    public ResponseEntity<Product> findById(@RequestParam String code) {
        return productRepository.findByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
