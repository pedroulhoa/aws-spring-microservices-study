package com.aws.logeventsconsumerapi.service;

import com.aws.logeventsconsumerapi.dto.event.EventData;
import com.aws.logeventsconsumerapi.dto.event.ProductEvent;
import com.aws.logeventsconsumerapi.dto.event.SnsMessage;
import com.aws.logeventsconsumerapi.entity.dynamodb.ProductEventLog;
import com.aws.logeventsconsumerapi.repository.ProductEventLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@Service
public class ProductEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(ProductEventConsumer.class);

    private final ObjectMapper objectMapper;
    private final ProductEventLogRepository productEventLogRepository;

    @Autowired
    public ProductEventConsumer(ObjectMapper objectMapper, ProductEventLogRepository productEventLogRepository) {
        this.objectMapper = objectMapper;
        this.productEventLogRepository = productEventLogRepository;
    }

    @JmsListener(destination = "${aws.sqs.queue.product-events-name}")
    public void receiveProductEvent(TextMessage textMessage)
            throws JMSException, IOException {

        SnsMessage snsMessage = objectMapper.readValue(textMessage.getText(),
                SnsMessage.class);

        EventData eventData = objectMapper.readValue(snsMessage.getMessage(),
                EventData.class);

        ProductEvent productEvent = objectMapper.readValue(
                eventData.getData(), ProductEvent.class);

        log.info("Product event received - Event: {} - ProductId: {} - MessageId: {}",
                eventData.getEventType(),
                productEvent.getProductId(),
                snsMessage.getMessageId());

        ProductEventLog productEventLog = buildProductEventLog(eventData, productEvent, snsMessage.getMessageId());
        productEventLogRepository.save(productEventLog);
    }

    private ProductEventLog buildProductEventLog(EventData eventData, ProductEvent productEvent, String messageId) {
        long timestamp = Instant.now().toEpochMilli();

        ProductEventLog productEventLog = new ProductEventLog();
        productEventLog.setPk(productEvent.getCode());
        productEventLog.setSk(eventData.getEventType() + "_" + timestamp);
        productEventLog.setEventType(eventData.getEventType());
        productEventLog.setProductId(productEvent.getProductId());
        productEventLog.setUsername(productEvent.getUsername());
        productEventLog.setTimestamp(timestamp);
        productEventLog.setTtl(Instant.now().plus(Duration.ofMinutes(10)).getEpochSecond());
//        productEventLog.setMessageId(messageId);
        return productEventLog;
    }
}
