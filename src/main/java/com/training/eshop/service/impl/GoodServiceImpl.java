package com.training.eshop.service.impl;

import com.training.eshop.converter.GoodConverter;
import com.training.eshop.dto.GoodAdminCreationDto;
import com.training.eshop.dto.GoodAdminViewDto;
import com.training.eshop.dto.GoodBuyerDto;
import com.training.eshop.exception.AccessDeniedException;
import com.training.eshop.exception.ProductNotFoundException;
import com.training.eshop.model.Good;
import com.training.eshop.model.User;
import com.training.eshop.model.enums.Role;
import com.training.eshop.repository.GoodRepository;
import com.training.eshop.repository.UserRepository;
import com.training.eshop.service.GoodService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GoodServiceImpl implements GoodService {
    private static final Logger LOGGER = LogManager.getLogger(GoodServiceImpl.class.getName());

    private static final Map<String, List<Good>> SEARCH_MAP = new HashMap<>();

    private static final String PRODUCT_NOT_FOUND_BY_ID = "Product with id %s not found";
    private static final String PRODUCT_NOT_FOUND_BY_TITLE_AND_PRICE = "Product with title %s and price %s $ not found";
    private static final String ACCESS_DENIED_FOR_BUYER = "Access is allowed only for administrator";
    private static final String DOT = ".";
    private static final String ZERO = "0";
    private static final String TWO_ZEROS_LEFT = ".00";

    private final GoodRepository goodRepository;
    private final UserRepository userRepository;
    private final GoodConverter goodConverter;

    @Override
    @Transactional
    public Good save(GoodAdminCreationDto goodDto, String login) {
        Good good = goodConverter.fromGoodAdminCreationDto(goodDto);

        User user = userRepository.findByEmail(login).get();

        checkAccess(user);

        good.setUser(user);

        goodRepository.save(good);

        return good;
    }

    @Override
    @Transactional
    public List<GoodBuyerDto> getAllForBuyer() {
        return goodRepository.findAllForBuyer()
                .stream()
                .map(goodConverter::convertToGoodBuyerDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<GoodAdminViewDto> getAllForAdmin(String searchField, String parameter, String sortField,
                                                 String sortDirection, int pageSize, int pageNumber) {
        List<Good> goods = getAllFilteredAndSortedWithPages(searchField, parameter, sortField, sortDirection,
                pageSize, pageNumber);

        LOGGER.info("All goods : {}", goods);

        return goods.stream()
                .map(goodConverter::convertToGoodAdminViewDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GoodAdminViewDto getById(Long id) {
        Good good = goodRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_BY_ID, id)));

        return goodConverter.convertToGoodAdminViewDto(good);
    }

    @Override
    @Transactional
    public Good getByTitleAndPrice(String title, String price) {
        List<Good> goods = (List<Good>) goodRepository.findAll();

        return goods.stream()
                .filter(good -> title.equals(good.getTitle())
                        && getPriceFromDropMenu(price).equals(String.valueOf(good.getPrice())))
                .findAny()
                .orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_BY_TITLE_AND_PRICE, title, price)));
    }

    @Override
    @Transactional
    public Good update(Long id, GoodAdminCreationDto goodDto, String login) {
        Good good = goodConverter.fromGoodAdminCreationDto(goodDto);

        User user = userRepository.findByEmail(login).get();

        checkAccess(user);

        good.setUser(user);
        good.setId(id);

        goodRepository.save(good);

        if (id > 0) {
            LOGGER.info("Updated good: {}", good);
        } else {
            LOGGER.info("New good: {}", goodDto);
        }

        return good;
    }

    @Override
    @Transactional
    public void deleteById(Long id, String login) {
        User user = userRepository.findByEmail(login).get();

        checkAccess(user);

        goodRepository.deleteById(id);

        LOGGER.info("Goods after removing good with id = {} : {}", id, goodRepository.findAll());
    }

    @Override
    @Transactional
    public long getTotalAmount() {
        return goodRepository.count();
    }

    @Override
    public String getPriceFromDropMenu(String price) {
        if (price.contains(DOT)) {
            return isNumbersAfterDotLengthMoreThanOne(price) ? price : price + ZERO;
        }

        return price + TWO_ZEROS_LEFT;
    }

    private boolean isNumbersAfterDotLengthMoreThanOne(String price) {
        String numbersAfterDot = price.substring(price.indexOf(DOT) + 1);

        return numbersAfterDot.length() > 1;
    }

    private List<Good> getAllFilteredAndSortedWithPages(String searchField, String parameter, String sortField,
                                                        String sortDirection, int pageSize, int pageNumber) {
        SEARCH_MAP.put("id", goodRepository.findAllById(parameter, PageRequest.of(pageNumber, pageSize,
                Sort.by(Sort.Direction.fromString(sortDirection), sortField))));
        SEARCH_MAP.put("title", goodRepository.findAllByTitle(parameter, PageRequest.of(pageNumber, pageSize,
                Sort.by(Sort.Direction.fromString(sortDirection), sortField))));
        SEARCH_MAP.put("price", goodRepository.findAllByPrice(parameter, PageRequest.of(pageNumber, pageSize,
                Sort.by(Sort.Direction.fromString(sortDirection), sortField))));
        SEARCH_MAP.put("description", goodRepository.findAllByDescription(parameter, PageRequest.of(pageNumber, pageSize,
                Sort.by(Sort.Direction.fromString(sortDirection), sortField))));
        SEARCH_MAP.put("default", goodRepository.findAll(PageRequest.of(pageNumber, pageSize,
                Sort.by(Sort.Direction.fromString(sortDirection), sortField))));

        return SEARCH_MAP.entrySet().stream()
                .filter(pair -> pair.getKey().equals(searchField))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private void checkAccess(User user) {
        if (user.getRole().equals(Role.ROLE_BUYER)) {
            throw new AccessDeniedException(ACCESS_DENIED_FOR_BUYER);
        }
    }
}
