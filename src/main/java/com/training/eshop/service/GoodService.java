package com.training.eshop.service;

import com.training.eshop.dto.GoodAdminCreationDto;
import com.training.eshop.dto.GoodAdminViewDto;
import com.training.eshop.dto.GoodBuyerDto;
import com.training.eshop.model.Good;

import java.util.List;

public interface GoodService {

    Good save(GoodAdminCreationDto goodDto, String login);

    List<GoodBuyerDto> getAllForBuyer();

    List<GoodAdminViewDto> getAllForAdmin(String searchField, String parameter, String sortField,
                                          String sortDirection, int pageSize, int pageNumber);

    GoodAdminViewDto getById(Long id);

    Good getByTitleAndPrice(String title, String price);

    Good update(Long id, GoodAdminCreationDto goodDto, String login);

    void deleteById(Long id, String login);

    long getTotalAmount();

    String getPriceFromDropMenu(String price);
}
