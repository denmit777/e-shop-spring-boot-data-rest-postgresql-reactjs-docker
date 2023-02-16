package com.training.eshop.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderBuyerDto {

    private BigDecimal totalPrice;

    private List<GoodBuyerDto> goods;

    private String description;

    public OrderBuyerDto() {
        this.goods = new ArrayList();
    }
}
