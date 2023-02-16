package com.training.eshop.controller;

import com.training.eshop.dto.GoodBuyerDto;
import com.training.eshop.dto.OrderAdminViewDto;
import com.training.eshop.dto.OrderBuyerDto;
import com.training.eshop.exception.OrderNotPlacedException;
import com.training.eshop.model.Order;
import com.training.eshop.service.*;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "/orders")
@AllArgsConstructor
public class OrderController {

    private static final Logger LOGGER = LogManager.getLogger(OrderController.class.getName());

    private static final String ORDER_NOT_PLACED = "Your order not placed yet";

    private final OrderService orderService;
    private final ValidationService validationService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody GoodBuyerDto goodBuyerDto,
                                  BindingResult bindingResult,
                                  Principal principal,
                                  @RequestParam(value = "buttonValue", defaultValue = "default") String buttonValue) {
        List<String> errorMessage = validationService.generateErrorMessage(bindingResult);

        if (checkErrors(errorMessage)) {
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        return clickingActions(buttonValue, goodBuyerDto, principal);
    }

    @GetMapping
    public ResponseEntity<List<OrderAdminViewDto>> getAll(@RequestParam(value = "sortField", defaultValue = "id") String sortField,
                                                          @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection,
                                                          @RequestParam(value = "pageSize", defaultValue = "15") int pageSize,
                                                          @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber) {
        List<OrderAdminViewDto> orders = orderService.getAll(sortField, sortDirection, pageSize, pageNumber);

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderBuyerDto> getById(@PathVariable("id") Long id,
                                                 @RequestParam(value = "buttonValue", defaultValue = "default") String buttonValue) {
        OrderBuyerDto order = orderService.getById(id);

        if (buttonValue.equals("LogOut")) {
            orderService.updateDataAfterPlacingOrder(order);
        }

        return ResponseEntity.ok(order);
    }

    @GetMapping("/total")
    public ResponseEntity<?> getTotalAmount() {
        return ResponseEntity.ok(orderService.getTotalAmount());
    }

    private ResponseEntity<?> clickingActions(String buttonValue, GoodBuyerDto goodBuyerDto, Principal principal) {
        Order order = new Order();

        switch (buttonValue) {
            case "Add Goods":
                orderService.addGoodToOrder(goodBuyerDto);

                return new ResponseEntity<>(orderService.getCartGoods(), HttpStatus.OK);
            case "Remove Goods":
                orderService.deleteGoodFromOrder(goodBuyerDto);

                return new ResponseEntity<>(orderService.getCartGoods(), HttpStatus.OK);

            case "Submit":
                if (goodBuyerDto.getTitle() != null && goodBuyerDto.getPrice() != null) {

                    Order savedOrder = orderService.save(order, principal.getName());

                    String currentUri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
                    String savedOrderLocation = currentUri + "/" + savedOrder.getId();

                    return ResponseEntity.status(CREATED)
                            .header(HttpHeaders.LOCATION, savedOrderLocation)
                            .body(savedOrder);
                } else {
                    LOGGER.error(ORDER_NOT_PLACED);

                    throw new OrderNotPlacedException(ORDER_NOT_PLACED);
                }
        }

        orderService.updateDataForCancelledOrder(order);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean checkErrors(List<String> errorMessage) {
        return !errorMessage.isEmpty();
    }
}
