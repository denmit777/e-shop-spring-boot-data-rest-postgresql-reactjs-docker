package com.training.eshop.converter;

import com.training.eshop.dto.GoodAdminCreationDto;
import com.training.eshop.dto.GoodAdminViewDto;
import com.training.eshop.dto.GoodBuyerDto;
import com.training.eshop.model.Good;

public interface GoodConverter {

    GoodBuyerDto convertToGoodBuyerDto(Good good);

    GoodAdminViewDto convertToGoodAdminViewDto(Good good);

    Good fromGoodAdminViewDto(GoodAdminViewDto goodDto);

    Good fromGoodAdminCreationDto(GoodAdminCreationDto goodDto);
}