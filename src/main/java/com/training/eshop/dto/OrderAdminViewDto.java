package com.training.eshop.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderAdminViewDto {

    private Long id;

    private BigDecimal totalPrice;

    private String user;

    private String description;
}
