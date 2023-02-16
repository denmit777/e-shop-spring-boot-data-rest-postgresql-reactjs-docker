package com.training.eshop.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class GoodBuyerDto {

    private String title;

    private BigDecimal price;
}
