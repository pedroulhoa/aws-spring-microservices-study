package com.aws.productapi.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.Topic;
import com.aws.productapi.dto.event.EventData;
import com.aws.productapi.dto.event.ProductEvent;
import com.aws.productapi.dto.event.enums.EventType;
import com.aws.productapi.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ProductPublisher {

    private static final Logger LOG = LoggerFactory.getLogger(ProductPublisher.class);

    private final AmazonSNS snsClient;
    private final Topic productEventsTopic;
    private final ObjectMapper objectMapper;

    @Autowired
    public ProductPublisher(AmazonSNS snsClient,
                            @Qualifier("productEventsTopic") Topic productEventsTopic,
                            ObjectMapper objectMapper) {
        this.snsClient = snsClient;
        this.productEventsTopic = productEventsTopic;
        this.objectMapper = objectMapper;
    }

    public void publishProductEvent(Product product, EventType eventType, String username) {
        ProductEvent productEvent = new ProductEvent(product, username);

        try {
            EventData eventData = new EventData();
            eventData.setEventType(eventType);
            eventData.setData(objectMapper.writeValueAsString(productEvent));

            PublishResult publishResult = snsClient
                    .publish(productEventsTopic.getTopicArn(), objectMapper.writeValueAsString(eventData));

            LOG.info("Product event publish - Event: {} - ProductId: {} - MessageId: {}",
                    eventData.getEventType(),
                    productEvent.getProductId(),
                    publishResult.getMessageId());
        } catch (JsonProcessingException e) {
            LOG.error("Failed to create product event message");
        }
    }

}
