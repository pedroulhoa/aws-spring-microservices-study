package com.aws.logeventsconsumerapi.dto.event;

import com.aws.logeventsconsumerapi.entity.dynamodb.ProductEventLog;
import com.aws.logeventsconsumerapi.enums.EventType;

public class ProductEventLogDto {

    private final String code;
    private final EventType eventType;
    private final long productId;
    private final String username;
    private final long timestamp;
    private final String messageId;

    public ProductEventLogDto(ProductEventLog productEventLog) {
        this.code = productEventLog.getPk();
        this.eventType = productEventLog.getEventType();
        this.productId = productEventLog.getProductId();
        this.username = productEventLog.getUsername();
        this.timestamp = productEventLog.getTimestamp();
        this.messageId = productEventLog.getMessageId();
    }

    public String getCode() {
        return code;
    }

    public EventType getEventType() {
        return eventType;
    }

    public long getProductId() {
        return productId;
    }

    public String getUsername() {
        return username;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMessageId() {
        return messageId;
    }
}
