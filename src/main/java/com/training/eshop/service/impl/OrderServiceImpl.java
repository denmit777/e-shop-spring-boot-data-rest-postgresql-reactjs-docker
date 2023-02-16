package com.training.eshop.service.impl;

import com.training.eshop.converter.OrderConverter;
import com.training.eshop.dto.GoodBuyerDto;
import com.training.eshop.dto.OrderAdminViewDto;
import com.training.eshop.dto.OrderBuyerDto;
import com.training.eshop.exception.OrderNotFoundException;
import com.training.eshop.exception.OrderNotPlacedException;
import com.training.eshop.exception.ProductNotFoundException;
import com.training.eshop.exception.ProductNotSelectedException;
import com.training.eshop.mail.service.EmailService;
import com.training.eshop.model.Good;
import com.training.eshop.model.Order;
import com.training.eshop.model.User;
import com.training.eshop.repository.GoodRepository;
import com.training.eshop.repository.OrderRepository;
import com.training.eshop.repository.UserRepository;
import com.training.eshop.service.GoodService;
import com.training.eshop.service.HistoryService;
import com.training.eshop.service.OrderService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private static final Logger LOGGER = LogManager.getLogger(OrderServiceImpl.class.getName());

    private static final String ORDER_NOT_PLACED = "Your order not placed yet";
    private static final String ORDER_NOT_FOUND = "Order with id %s not found";
    private static final String PRODUCT_NOT_SELECTED = "You should select the product first";
    private static final String PRODUCT_NOT_FOUND = "Product with title %s and price %s $ is not in the cart";
    private static final String PRODUCT_IS_OVER = "Product with title %s and price %s $ out of stock";

    private final List<Good> goods = new ArrayList<>();

    private final EmailService emailService;
    private final GoodService goodService;
    private final HistoryService historyService;
    private final OrderConverter orderConverter;
    private final UserRepository userRepository;
    private final GoodRepository goodRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public Order save(Order order, String login) {
        setOrderParameters(order, login);

        if (!order.getGoods().isEmpty()) {
            orderRepository.save(order);

            historyService.saveHistoryForCreatedOrder(order);

            emailService.sendOrderDetailsMessage(order.getId(), login);

            LOGGER.info("New order: {}", order);

            return order;
        } else {
            throw new OrderNotPlacedException(ORDER_NOT_PLACED);
        }
    }

    @Override
    @Transactional
    public void addGoodToOrder(GoodBuyerDto goodBuyerDto) {
        if (!goodBuyerDto.getTitle().isEmpty()) {
            Good good = goodService.getByTitleAndPrice(goodBuyerDto.getTitle(), String.valueOf(goodBuyerDto.getPrice()));

            Good orderGood = new Good();

            Long last = good.getQuantity() - 1L;
            Long amount = good.getQuantity() - last;

            if (good.getQuantity() >= 1) {
                setOrderGoodParameters(good, orderGood, amount);

                goods.add(orderGood);

                historyService.saveHistoryForAddedGoods(good);
            } else {
                LOGGER.error(String.format(PRODUCT_IS_OVER, goodBuyerDto.getTitle(), goodBuyerDto.getPrice()));

                throw new ProductNotFoundException(String.format(PRODUCT_IS_OVER, goodBuyerDto.getTitle(), goodBuyerDto.getPrice()));
            }

            good.setQuantity(last);

            LOGGER.info("Your goods: {}", goods);
        } else {
            LOGGER.error(PRODUCT_NOT_SELECTED);

            throw new ProductNotSelectedException(PRODUCT_NOT_SELECTED);
        }
    }

    @Override
    @Transactional
    public void deleteGoodFromOrder(GoodBuyerDto goodBuyerDto) {
        if (!goodBuyerDto.getTitle().isEmpty()) {
            Good good = goodService.getByTitleAndPrice(goodBuyerDto.getTitle(), String.valueOf(goodBuyerDto.getPrice()));

            if (isProductPresent(goodBuyerDto)) {
                Good orderGood = new Good();

                Long last = good.getQuantity() + 1L;

                setOrderGoodParameters(good, orderGood, 1L);

                goods.remove(orderGood);

                historyService.saveHistoryForRemovedGoods(good);

                good.setQuantity(last);

                LOGGER.info("Your goods after removing {} : {}", goodBuyerDto.getTitle(), goods);
            } else {
                LOGGER.error(String.format(PRODUCT_NOT_FOUND, goodBuyerDto.getTitle(), goodBuyerDto.getPrice()));

                throw new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND, goodBuyerDto.getTitle(), goodBuyerDto.getPrice()));
            }
        } else {
            LOGGER.error(PRODUCT_NOT_SELECTED);

            throw new ProductNotSelectedException(PRODUCT_NOT_SELECTED);
        }
    }

    @Override
    @Transactional
    public OrderBuyerDto getById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(String.format(ORDER_NOT_FOUND, id)));

        LOGGER.info("Order № {} : {}", id, order);

        return orderConverter.convertToOrderBuyerDto(order);
    }

    @Override
    @Transactional
    public List<OrderAdminViewDto> getAll(String sortField, String sortDirection, int pageSize, int pageNumber) {
        List<Order> orders;

        if (sortField.equals("user")) {
            orders = getAllSortedByUser(sortDirection, pageSize, pageNumber);
        } else {
            orders = orderRepository.findAll(PageRequest.of(pageNumber, pageSize,
                    Sort.by(Sort.Direction.fromString(sortDirection), sortField)));
        }

        LOGGER.info("All orders : {}", orders);

        return orders.stream()
                .map(orderConverter::convertToOrderAdminViewDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Good> getCartGoods() {
        return goods;
    }

    @Override
    @Transactional
    public void updateDataForCancelledOrder(Order order) {
        for (Good orderGood : goods) {
            for (Good good : goodRepository.findAll()) {
                setGoodQuantityIfOrderIsCancelled(good, orderGood);
            }
        }
        historyService.saveHistoryForCanceledOrder();

        goods.clear();

        order.setGoods(goods);
    }

    @Override
    @Transactional
    public void updateDataAfterPlacingOrder(OrderBuyerDto orderBuyerDto) {
        for (Good good : goodRepository.findAll()) {
            if (good.getQuantity() < 1L) {
                goodRepository.deleteByIdAfterQuantityEqualsZero(good.getId());
            }
        }
        goods.clear();

        orderBuyerDto.setGoods(orderConverter.convertToListGoodDto(goods));
    }

    @Override
    @Transactional
    public long getTotalAmount() {
        return orderRepository.count();
    }

    private String getOrderedGoods(Order order) {
        StringBuilder sb = new StringBuilder();

        int count = 1;

        for (Good good : order.getGoods()) {
            sb.append(count)
                    .append(") ")
                    .append(good.getTitle())
                    .append(" ")
                    .append(good.getPrice())
                    .append(" $\n");

            count++;
        }

        return sb.append("\nTotal: $ ").append(getTotalPrice(order)).toString();
    }

    private BigDecimal getTotalPrice(Order order) {
        BigDecimal count = BigDecimal.valueOf(0);

        for (Good good : order.getGoods()) {
            count = count.add(good.getPrice());
        }

        LOGGER.info("Total price: {}", count);

        return count;
    }

    private List<Order> getAllSortedByUser(String sortDirection, int pageSize, int pageNumber) {
        if (sortDirection.equals("desc")) {
            return orderRepository.findAllSortedByUserNameDescOrderAndId(PageRequest.of(pageNumber, pageSize));
        }

        return orderRepository.findAllSortedByUserNameAndId(PageRequest.of(pageNumber, pageSize));
    }

    private boolean isProductPresent(GoodBuyerDto goodBuyerDto) {
        String title = goodBuyerDto.getTitle();
        String price = goodService.getPriceFromDropMenu(String.valueOf(goodBuyerDto.getPrice()));

        return goods.stream().anyMatch(good -> title.equals(good.getTitle())
                && price.equals(String.valueOf(good.getPrice())));
    }

    private void setOrderParameters(Order order, String login) {
        User user = userRepository.findByEmail(login).get();

        order.setUser(user);
        order.setGoods(goods);

        BigDecimal totalPrice = getTotalPrice(order);
        String description = getOrderedGoods(order);

        order.setTotalPrice(totalPrice);
        order.setDescription(description);
    }

    private void setGoodQuantityIfOrderIsCancelled(Good good, Good orderGood) {
        if (Objects.equals(orderGood.getId(), good.getId())) {
            if (good.getQuantity() < 1L) {
                good.setQuantity(orderGood.getQuantity());
            } else {
                good.setQuantity(good.getQuantity() + orderGood.getQuantity());
            }
        }
    }

    private void setOrderGoodParameters(Good good, Good orderGood, Long quantity) {
        orderGood.setId(good.getId());
        orderGood.setTitle(good.getTitle());
        orderGood.setPrice(good.getPrice());
        orderGood.setQuantity(quantity);
        orderGood.setDescription(good.getDescription());
    }
}
