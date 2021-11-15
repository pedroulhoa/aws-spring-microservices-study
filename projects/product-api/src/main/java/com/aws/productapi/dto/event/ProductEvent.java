package com.aws.productapi.dto.event;

import com.aws.productapi.entity.Product;

public class ProductEvent {

    private long productId;
    private String code;
    private  String username;

    public ProductEvent(Product product, String username) {
        this.productId = product.getId();
        this.code = product.getCode();
        this.username = username;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
