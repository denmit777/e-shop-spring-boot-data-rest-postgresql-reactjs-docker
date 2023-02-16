package com.training.eshop.converter;

import com.training.eshop.dto.GoodBuyerDto;
import com.training.eshop.dto.OrderAdminViewDto;
import com.training.eshop.dto.OrderBuyerDto;
import com.training.eshop.model.Good;
import com.training.eshop.model.Order;

import java.util.List;

public interface OrderConverter {

    OrderBuyerDto convertToOrderBuyerDto(Order order);

    OrderAdminViewDto convertToOrderAdminViewDto(Order order);

    List<GoodBuyerDto> convertToListGoodDto(List<Good> goods);
}
