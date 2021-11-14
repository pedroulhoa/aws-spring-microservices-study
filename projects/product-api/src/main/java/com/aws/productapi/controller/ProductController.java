package com.aws.productapi.controller;

import com.aws.productapi.dto.request.ProductRequest;
import com.aws.productapi.dto.response.ProductResponse;
import com.aws.productapi.entity.Product;
import com.aws.productapi.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    private final ProductRepository productRepository;

    @Autowired
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(new ProductResponse(productCreate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@Valid @RequestBody ProductRequest request, @PathVariable("id") Long id) {
        Optional<Product> productUpdate = productRepository.findById(id);

        if (productUpdate.isPresent()) {
            Product product = new Product(id, request);
            productRepository.save(product);
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
