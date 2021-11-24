package com.aws.logeventsconsumerapi.controller;

import com.aws.logeventsconsumerapi.dto.event.ProductEventLogDto;
import com.aws.logeventsconsumerapi.repository.ProductEventLogRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@Api(tags = "Events")
@RequestMapping("/events")
public class ProductEventLogController {

    private final ProductEventLogRepository productEventLogRepository;

    @Autowired
    public ProductEventLogController(ProductEventLogRepository productEventLogRepository) {
        this.productEventLogRepository = productEventLogRepository;
    }

    @GetMapping()
    public List<ProductEventLogDto> getAllEvents() {
        return StreamSupport.stream(productEventLogRepository.findAll().spliterator(), false)
                .map(ProductEventLogDto::new)
                .toList();
    }

    @GetMapping("/{code}")
    public List<ProductEventLogDto> findByCode(@PathVariable String code) {
        return productEventLogRepository.findAllByPk(code)
                .stream()
                .map(ProductEventLogDto::new)
                .toList();
    }

    @GetMapping("/{code}/{event}")
    public List<ProductEventLogDto> findByCodeAndEventType(@PathVariable String code,
                                                           @PathVariable String event) {
        return productEventLogRepository.findAllByPkAndSkStartsWith(code, event)
                .stream()
                .map(ProductEventLogDto::new)
                .toList();
    }
}
