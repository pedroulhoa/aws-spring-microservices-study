package com.aws.productapi.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;

public class ProductRequest {

    @NotNull
    @Size(min = 3, max = 32)
    private String name;

    @Size(min = 3, max = 24)
    private String model;

    @Size(min = 3, max = 8)
    private String code;

    private BigDecimal price;

    public ProductRequest(String name, String model, String code, BigDecimal price) {
        this.name = name;
        this.model = model;
        this.code = code;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductRequest that = (ProductRequest) o;
        return Objects.equals(name, that.name) && Objects.equals(model, that.model) && Objects.equals(code, that.code) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, model, code, price);
    }
}
