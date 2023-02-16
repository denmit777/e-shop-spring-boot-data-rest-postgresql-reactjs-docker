package com.training.eshop.converter.impl;

import com.training.eshop.dto.GoodAdminCreationDto;
import com.training.eshop.dto.GoodAdminViewDto;
import com.training.eshop.dto.GoodBuyerDto;
import com.training.eshop.model.Good;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParseException;

import static org.junit.Assert.*;

public class GoodConverterImplTest {

    private GoodConverterImpl goodConverter;

    @Before
    public void setUp() throws ParseException {
        goodConverter = new GoodConverterImpl();
    }

    @Test
    public void convertToGoodBuyerDtoTest() {
        Good good = new Good();

        good.setTitle("Book");
        good.setPrice(BigDecimal.valueOf(5.5));

        GoodBuyerDto goodDto = goodConverter.convertToGoodBuyerDto(good);

        assertEquals(good.getTitle(), goodDto.getTitle());
        assertEquals(good.getPrice(), goodDto.getPrice());
    }

    @Test
    public void convertToGoodAdminViewDtoTest() {
        Good good = new Good();

        good.setId(1L);
        good.setTitle("Book");
        good.setPrice(BigDecimal.valueOf(5.5));
        good.setQuantity(2L);
        good.setDescription("This is a book");

        GoodAdminViewDto goodDto = goodConverter.convertToGoodAdminViewDto(good);

        assertEquals(good.getId(), goodDto.getId());
        assertEquals(good.getTitle(), goodDto.getTitle());
        assertEquals(good.getPrice(), goodDto.getPrice());
        assertEquals(good.getQuantity(), goodDto.getQuantity());
        assertEquals(good.getDescription(), goodDto.getDescription());
    }

    @Test
    public void fromGoodAdminViewDtoTest() {
        Long id = 1L;
        String title = "Phone";
        BigDecimal price = BigDecimal.valueOf(500);
        Long quantity = 2L;
        String description = "This is a phone";

        Good good = goodConverter.fromGoodAdminViewDto(new GoodAdminViewDto() {{
            setId(id);
            setTitle(title);
            setPrice(price);
            setQuantity(quantity);
            setDescription(description);
        }});

        assertEquals(id, good.getId());
        assertEquals(title, good.getTitle());
        assertEquals(price, good.getPrice());
        assertEquals(quantity, good.getQuantity());
        assertEquals(description, good.getDescription());
    }

    @Test
    public void fromGoodAdminCreationDtoTest() {
        String title = "Phone";
        BigDecimal price = BigDecimal.valueOf(500);
        Long quantity = 2L;
        String description = "This is a phone";

        Good good = goodConverter.fromGoodAdminCreationDto(new GoodAdminCreationDto() {{
            setTitle(title);
            setPrice(price);
            setQuantity(quantity);
            setDescription(description);
        }});

        assertEquals(title, good.getTitle());
        assertEquals(price, good.getPrice());
        assertEquals(quantity, good.getQuantity());
        assertEquals(description, good.getDescription());
    }
}