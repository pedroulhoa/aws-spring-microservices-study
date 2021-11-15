package com.aws.logeventsconsumerapi.service;

import com.aws.logeventsconsumerapi.dto.event.EventData;
import com.aws.logeventsconsumerapi.dto.event.ProductEvent;
import com.aws.logeventsconsumerapi.dto.event.SnsMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.IOException;

@Service
public class ProductEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(ProductEventConsumer.class);

    private final ObjectMapper objectMapper;

    @Autowired
    public ProductEventConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
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
    }
}
