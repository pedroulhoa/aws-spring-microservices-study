package com.aws.productapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    @GetMapping("/{name}")
    public ResponseEntity<?> productTest(@PathVariable String name) {
        LOG.info("Test controller - name: {}", name);
        return ResponseEntity.ok("name: " + name);
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<?> productBrandTest(@PathVariable String brand) {
        LOG.info("Test controller - brand: {}", brand);
        return ResponseEntity.ok("brand: " + brand);
    }

}
