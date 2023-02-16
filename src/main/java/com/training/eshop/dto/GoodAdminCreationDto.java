package com.training.eshop.dto;

import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class GoodAdminCreationDto {

    private static final String TITLE_FIELD_IS_EMPTY = "Title field shouldn't be empty";
    private static final String PRICE_FIELD_IS_EMPTY = "Price field shouldn't be empty";
    private static final String QUANTITY_FIELD_IS_EMPTY = "Quantity field shouldn't be empty";
    private static final String WRONG_SIZE_OF_TITLE = "Title shouldn't be more than 15 symbols";
    private static final String WRONG_SIZE_OF_DESCRIPTION = "Description shouldn't be more than 100 symbols";
    private static final String WRONG_TITLE = "Title should be in latin letters";
    private static final String WRONG_PRICE_VALUE = "The value of price should be at least 1";
    private static final String WRONG_QUANTITY_VALUE = "The value of quantity should be at least 1";

    @NotEmpty(message = TITLE_FIELD_IS_EMPTY)
    @Size(max = 15, message = WRONG_SIZE_OF_TITLE)
    @Pattern(regexp = "^[A-za-z]*$", message = WRONG_TITLE)
    private String title;

    @NotNull(message = PRICE_FIELD_IS_EMPTY)
    @Min(value = 1, message = WRONG_PRICE_VALUE)
    private BigDecimal price;

    @NotNull(message = QUANTITY_FIELD_IS_EMPTY)
    @Min(value = 1, message = WRONG_QUANTITY_VALUE)
    private Long quantity;

    @Size(max = 100, message = WRONG_SIZE_OF_DESCRIPTION)
    private String description;
}
