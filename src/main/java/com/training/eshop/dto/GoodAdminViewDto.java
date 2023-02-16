package com.training.eshop.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
public class GoodAdminViewDto {

    private Long id;

    private String title;

    private BigDecimal price;

    private Long quantity;

    private String description;
}
