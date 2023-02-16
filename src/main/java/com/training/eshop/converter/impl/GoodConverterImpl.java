package com.training.eshop.converter.impl;

import com.training.eshop.converter.GoodConverter;
import com.training.eshop.dto.GoodAdminCreationDto;
import com.training.eshop.dto.GoodAdminViewDto;
import com.training.eshop.dto.GoodBuyerDto;
import com.training.eshop.model.Good;
import org.springframework.stereotype.Component;

@Component
public class GoodConverterImpl implements GoodConverter {

    @Override
    public GoodBuyerDto convertToGoodBuyerDto(Good good) {
        GoodBuyerDto goodBuyerDto = new GoodBuyerDto();

        goodBuyerDto.setTitle(good.getTitle());
        goodBuyerDto.setPrice(good.getPrice());

        return goodBuyerDto;
    }

    @Override
    public GoodAdminViewDto convertToGoodAdminViewDto(Good good) {
        GoodAdminViewDto goodDto = new GoodAdminViewDto();

        goodDto.setId(good.getId());
        goodDto.setTitle(good.getTitle());
        goodDto.setPrice(good.getPrice());
        goodDto.setQuantity(good.getQuantity());
        goodDto.setDescription(good.getDescription());

        return goodDto;
    }

    @Override
    public Good fromGoodAdminViewDto(GoodAdminViewDto goodDto) {
        Good good = new Good();

        good.setId(goodDto.getId());
        good.setTitle(goodDto.getTitle());
        good.setPrice(goodDto.getPrice());
        good.setQuantity(goodDto.getQuantity());
        good.setDescription(goodDto.getDescription());

        return good;
    }

    @Override
    public Good fromGoodAdminCreationDto(GoodAdminCreationDto goodDto) {
        Good good = new Good();

        good.setTitle(goodDto.getTitle());
        good.setPrice(goodDto.getPrice());
        good.setQuantity(goodDto.getQuantity());
        good.setDescription(goodDto.getDescription());

        return good;
    }
}
