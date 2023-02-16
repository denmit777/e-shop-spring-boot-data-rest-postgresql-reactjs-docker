package com.training.eshop.service;

import com.training.eshop.dto.GoodBuyerDto;
import com.training.eshop.dto.OrderAdminViewDto;
import com.training.eshop.dto.OrderBuyerDto;
import com.training.eshop.model.Good;
import com.training.eshop.model.Order;

import java.util.List;

public interface OrderService {

    Order save(Order order, String login);

    void addGoodToOrder(GoodBuyerDto goodBuyerDto);

    void deleteGoodFromOrder(GoodBuyerDto goodBuyerDto);

    OrderBuyerDto getById(Long id);

    List<OrderAdminViewDto> getAll(String sortField, String sortDirection, int pageSize, int pageNumber);

    List<Good> getCartGoods();

    void updateDataForCancelledOrder(Order order);

    void updateDataAfterPlacingOrder(OrderBuyerDto orderBuyerDto);

    long getTotalAmount();
}

