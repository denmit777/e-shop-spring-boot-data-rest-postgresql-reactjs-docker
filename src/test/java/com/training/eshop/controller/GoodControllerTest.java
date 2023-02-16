package com.training.eshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.eshop.dto.GoodAdminCreationDto;
import com.training.eshop.dto.GoodAdminViewDto;
import com.training.eshop.dto.GoodBuyerDto;
import com.training.eshop.model.Good;
import com.training.eshop.service.GoodService;
import com.training.eshop.service.ValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GoodControllerTest {

    @Autowired
    private GoodController goodController;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GoodService goodService;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "den_mogilev@yopmail.com", password = "1234")
    void givenProduct_whenCreateValidProduct_thenStatus201andProductReturned() throws Exception {

        GoodAdminCreationDto newGood = new GoodAdminCreationDto();

        newGood.setTitle("Book");
        newGood.setPrice(BigDecimal.valueOf(5.53));
        newGood.setQuantity(2L);
        newGood.setDescription("This is a book");

        mockMvc.perform(
                        post("http://localhost:8081/goods/forAdmin")
                                .content(objectMapper.writeValueAsString(newGood))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Book"))
                .andExpect(jsonPath("$.price").value(5.53))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.description").value("This is a book"));

    }

    @Test
    @WithMockUser(username = "den_mogilev@yopmail.com", password = "1234")
    void givenError_whenCreateInvalidProduct_thenStatus400BadRequest() throws Exception {
        GoodAdminCreationDto newGood = new GoodAdminCreationDto();

        newGood.setTitle("");
        newGood.setPrice(BigDecimal.valueOf(5.53));
        newGood.setDescription("This is a book");

        mockMvc.perform(
                        post("http://localhost:8081/goods/forAdmin")
                                .content(objectMapper.writeValueAsString(newGood))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "asya_mogilev@yopmail.com", password = "5678")
    void givenError_whenBuyerDoesNotHaveAccessToCreateProduct_thenStatus403Forbidden() throws Exception {
        GoodAdminCreationDto newGood = new GoodAdminCreationDto();

        newGood.setTitle("Book");
        newGood.setPrice(BigDecimal.valueOf(5.53));
        newGood.setQuantity(2L);
        newGood.setDescription("This is a book");

        mockMvc.perform(
                        post("http://localhost:8081/goods/forAdmin")
                                .content(objectMapper.writeValueAsString(newGood))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.info").value("Access is allowed only for administrator"));
    }

    @Test
    @WithMockUser(username = "den_mogilev@yopmail.com", password = "P@ssword1")
    void givenId_whenGetExistingProduct_thenStatus200andProductReturned() throws Exception {
        long id = createTestProduct("Phone", BigDecimal.valueOf(4.2), 3L, "This is a phone",
                "den_mogilev@yopmail.com").getId();

        mockMvc.perform(
                        get("http://localhost:8081/goods/forAdmin/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Phone"))
                .andExpect(jsonPath("$.price").value(4.20))
                .andExpect(jsonPath("$.quantity").value(3))
                .andExpect(jsonPath("$.description").value("This is a phone"));

    }

    @Test
    @WithMockUser(username = "den_mogilev@yopmail.com", password = "1234")
    void givenError_whenGetNotExistingProduct_thenStatus404NotFound() throws Exception {
        long id = goodService.getTotalAmount();

        String error = "Product with id " + (id + 1) + " not found";

        mockMvc.perform(
                        get("http://localhost:8081/goods/forAdmin/{id}", id + 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.info").value(error));
    }

    @Test
    @WithMockUser(username = "den_mogilev@yopmail.com", password = "1234")
    void givenProduct_whenUpdate_thenStatus200andUpdatedProductReturned() throws Exception {
        long id = createTestProduct("Phone", BigDecimal.valueOf(4.2), 3L, "This is a phone",
                "den_mogilev@yopmail.com").getId();

        GoodAdminCreationDto updatedGood = new GoodAdminCreationDto();

        updatedGood.setTitle("Book");
        updatedGood.setPrice(BigDecimal.valueOf(5.53));
        updatedGood.setQuantity(2L);
        updatedGood.setDescription("This is a book");

        mockMvc.perform(
                        put("http://localhost:8081/goods/forAdmin/{id}", id)
                                .content(objectMapper.writeValueAsString(updatedGood))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Book"))
                .andExpect(jsonPath("$.price").value(5.53))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.description").value("This is a book"));
    }

    @Test
    @WithMockUser(username = "asya_mogilev@yopmail.com", password = "5678")
    void givenError_whenUpdateProductByBuyer_thenStatus403Forbidden() throws Exception {
        long id = createTestProduct("Phone", BigDecimal.valueOf(4.2), 3L, "This is a phone",
                "den_mogilev@yopmail.com").getId();

        GoodAdminCreationDto updatedGood = new GoodAdminCreationDto();

        updatedGood.setTitle("Book");
        updatedGood.setPrice(BigDecimal.valueOf(5.53));
        updatedGood.setQuantity(2L);
        updatedGood.setDescription("This is a book");

        mockMvc.perform(
                        put("http://localhost:8081/goods/forAdmin/{id}", id)
                                .content(objectMapper.writeValueAsString(updatedGood))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.info").value("Access is allowed only for administrator"));
    }

    @Test
    @WithMockUser(username = "asya_mogilev@yopmail.com", password = "5678")
    void givenAllGoods_whenIAmBuyer_thenStatus200() throws Exception {
        List<GoodBuyerDto> goods = goodService.getAllForBuyer();

        mockMvc.perform(
                        get("http://localhost:8081/goods/forBuyer"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(goods)));
    }

    @Test
    @WithMockUser(username = "den_mogilev@yopmail.com", password = "1234")
    void givenAllGoodsByDefault_whenIAmAdmin_thenStatus200() throws Exception {
        List<GoodAdminViewDto> goods = goodService.getAllForAdmin("default",
                "", "id", "asc", 25, 0);

        mockMvc.perform(
                        get("http://localhost:8081/goods/forAdmin"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(goods)));
    }

    @Test
    @WithMockUser(username = "maricel_mogilev@yopmail.com", password = "221182")
    void givenAllGoodsByParameters_whenIAmAdmin_thenStatus200() throws Exception {
        List<GoodAdminViewDto> goods = goodService.getAllForAdmin("title",
                "b", "id", "desc", 10, 1);

        mockMvc.perform(
                        get("http://localhost:8081/goods/forAdmin?pageSize=10&pageNumber=1&sortField=id" +
                                "&sortDirection=desc&searchField=title&parameter=b"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(goods)));
    }

    @Test
    @WithMockUser(username = "den_mogilev@yopmail.com", password = "1234")
    void givenError_whenISearchWrongParameters_thenStatus400BadRequest() throws Exception {
        String wrongSearchParameterError = validationService.getWrongSearchParameterError("книга");

        mockMvc.perform(
                        get("http://localhost:8081/goods/forAdmin?searchField=title&parameter=книга"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("Search should be in latin letters or figures",
                        wrongSearchParameterError));
    }

    @Test
    @WithMockUser(username = "jimmy_mogilev@yopmail.com", password = "1234")
    public void deleteProduct_whenDeleteProductById_thenStatus200() throws Exception {
        Good good = createTestProduct("Phone", BigDecimal.valueOf(4.2), 3L, "This is a phone",
                "jimmy_mogilev@yopmail.com");

        mockMvc.perform(
                        delete("http://localhost:8081/goods/forAdmin/{id}", good.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "asya_mogilev@yopmail.com", password = "5678")
    public void givenError_whenDeleteProductByBuyer_thenStatus403Forbidden() throws Exception {
        Good good = createTestProduct("Phone", BigDecimal.valueOf(4.2), 3L, "This is a phone",
                "den_mogilev@yopmail.com");

        mockMvc.perform(
                        delete("http://localhost:8081/goods/forAdmin/{id}", good.getId()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.info").value("Access is allowed only for administrator"));
    }

    private Good createTestProduct(String title, BigDecimal price, Long quantity, String description, String login) {
        GoodAdminCreationDto good = new GoodAdminCreationDto();

        good.setTitle(title);
        good.setPrice(price);
        good.setQuantity(quantity);
        good.setDescription(description);

        return goodService.save(good, login);
    }
}
