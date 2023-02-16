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
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GoodServiceImplTest {

    private static final String ACCESS_DENIED_FOR_BUYER = "Access is allowed only for administrator";

    @Mock
    private GoodRepository goodRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GoodConverter goodConverter;

    @InjectMocks
    private GoodServiceImpl goodService;

    @Test
    public void getAllGoodsForBuyerTest() {
        Good productOne = createTestProduct(1L, "Book", BigDecimal.valueOf(5.5), 4L, "This is a book");
        Good productTwo = createTestProduct(2L, "Phone", BigDecimal.valueOf(100), 1L, "This is a phone");
        Good productThree = createTestProduct(3L, "Juice", BigDecimal.valueOf(2), 7L, "This is a juice");
        Good productFour = createTestProduct(4L, "Computer", BigDecimal.valueOf(500), 2L, "This is a computer");

        List<Good> goods = Stream.of(productOne, productTwo, productThree, productFour)
                .sorted(Comparator.comparing(Good::getTitle))
                .collect(Collectors.toList());

        List<GoodBuyerDto> expected = goods.stream()
                .map(goodConverter::convertToGoodBuyerDto)
                .collect(Collectors.toList());

        when(goodRepository.findAllForBuyer()).thenReturn(goods);

        List<GoodBuyerDto> actual = goodService.getAllForBuyer();

        Assert.assertEquals(4, actual.size());
        Assert.assertEquals(4, expected.size());
        Assert.assertEquals(expected, actual);

        verify(goodRepository, times(1)).findAllForBuyer();
    }

    @Test
    public void getAllGoodsForAdminTest() {
        Good productOne = createTestProduct(1L, "Book", BigDecimal.valueOf(2), 4L, "This is a book");
        Good productTwo = createTestProduct(2L, "Phone", BigDecimal.valueOf(100), 1L, "This is a phone");
        Good productThree = createTestProduct(3L, "Juice", BigDecimal.valueOf(2), 7L, "This is a juice");
        Good productFour = createTestProduct(4L, "Computer", BigDecimal.valueOf(500), 2L, "This is a computer");

        List<Good> goods = asList(productOne, productTwo, productThree, productFour);

        List<GoodAdminViewDto> expected = goods.stream()
                .map(goodConverter::convertToGoodAdminViewDto)
                .collect(Collectors.toList());

        when(goodRepository.findAll(PageRequest.of(0, 10,
                Sort.by(Sort.Direction.fromString("asc"), "id")))).thenReturn(goods);

        List<GoodAdminViewDto> actual = goodService.getAllForAdmin("default", "",
                "id", "asc", 10, 0);

        Assert.assertEquals(4, actual.size());
        Assert.assertEquals(4, expected.size());
        Assert.assertEquals(expected, actual);

        verify(goodRepository, times(1)).findAll(PageRequest.of(0, 10,
                Sort.by(Sort.Direction.fromString("asc"), "id")));
    }

    @Test
    public void getAllGoodsForAdminSearchedByTitleContainsLetterBAndSortedByIdInDescOrderTest() {
        Good productOne = createTestProduct(1L, "Book", BigDecimal.valueOf(5.55), 4L, "This is a book");
        Good productTwo = createTestProduct(2L, "Phone", BigDecimal.valueOf(100), 1L, "This is a phone");
        Good productThree = createTestProduct(3L, "Bear", BigDecimal.valueOf(3), 7L, "This is a juice");
        Good productFour = createTestProduct(4L, "Computer", BigDecimal.valueOf(500), 2L, "This is a computer");

        List<Good> goods = Stream.of(productOne, productTwo, productThree, productFour)
                .filter(good -> good.getTitle().toLowerCase().contains("b"))
                .collect(Collectors.toList());

        List<GoodAdminViewDto> expected = goods.stream()
                .sorted(Comparator.comparing(Good::getId).reversed())
                .map(goodConverter::convertToGoodAdminViewDto)
                .collect(Collectors.toList());

        when(goodRepository.findAllByTitle("b", PageRequest.of(0, 15,
                Sort.by(Sort.Direction.fromString("desc"), "id")))).thenReturn(goods);

        List<GoodAdminViewDto> actual = goodService.getAllForAdmin("title", "b",
                "id", "desc", 15, 0);

        Assert.assertEquals(2, actual.size());
        Assert.assertEquals(2, expected.size());
        Assert.assertEquals(expected, actual);

        verify(goodRepository, times(1)).findAllByTitle("b", PageRequest.of(0, 15,
                Sort.by(Sort.Direction.fromString("desc"), "id")));
    }

    @Test
    public void saveNewGoodTest() {
        User user = createTestUser(1L, "Den", "den_mogilev@yopmail.com", "1234", Role.ROLE_ADMIN);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        GoodAdminCreationDto goodDto = createTestProductDto("Book", BigDecimal.valueOf(5.55), 4L, "This is a book");

        Good good = getTestProductFromGoodAdminCreationDto(goodDto);

        when(goodConverter.fromGoodAdminCreationDto(goodDto)).thenReturn(good);

        Good result = goodService.save(goodDto, user.getEmail());

        Assert.assertNotNull(result);
        Assert.assertEquals(good.getTitle(), result.getTitle());

        verify(goodRepository, times(1)).save(goodConverter.fromGoodAdminCreationDto(goodDto));
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    public void saveNewGoodNegativeTest_IfUserRoleIsBuyer_ThenStatus403Forbidden() {
        User user = createTestUser(2L, "Asya", "asya_mogilev@yopmail.com", "5678", Role.ROLE_BUYER);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        GoodAdminCreationDto goodDto = createTestProductDto("Book", BigDecimal.valueOf(5.55), 4L, "This is a book");

        assertThrows(AccessDeniedException.class,
                () -> goodService.save(goodDto, user.getEmail()));

        try {
            goodService.save(goodDto, user.getEmail());
        } catch (AccessDeniedException e) {
            if (e.getMessage().equals(ACCESS_DENIED_FOR_BUYER)) {
                return;
            }
        }
        Assert.fail();
    }

    @Test
    public void getGoodByIdTest_ThenReturnGood() {
        Long id = 6L;

        Good good = createTestProduct(id, "Book", BigDecimal.valueOf(5.55), 4L, "This is a book");

        when(goodRepository.findById(id)).thenReturn(Optional.of(good));

        GoodAdminViewDto expected = goodConverter.convertToGoodAdminViewDto(good);

        GoodAdminViewDto actual = goodService.getById(id);

        Assert.assertEquals(expected, actual);

        verify(goodRepository, times(1)).findById(id);
    }

    @Test
    public void getGoodByTitleAndPriceTest_ThenReturnGood() {
        Good productOne = createTestProduct(1L, "Book", BigDecimal.valueOf(5.55), 4L, "This is a book");
        Good productTwo = createTestProduct(2L, "Phone", BigDecimal.valueOf(100), 1L, "This is a phone");
        Good productThree = createTestProduct(3L, "Bread", BigDecimal.valueOf(2.25), 7L, "This is a bread");
        Good productFour = createTestProduct(4L, "Computer", BigDecimal.valueOf(500), 2L, "This is a computer");

        List<Good> goods = asList(productOne, productTwo, productThree, productFour);

        String title = "Bread";
        String price = "2.25";

        when(goodRepository.findAll()).thenReturn(goods);

        Good actual = goodService.getByTitleAndPrice(title, price);

        Assert.assertEquals(productThree, actual);

        verify(goodRepository, times(1)).findAll();
    }

    @Test(expected = ProductNotFoundException.class)
    public void getGoodByTitleAndPriceNegativeTest_IfGoodIsAbsent_ThenReturnNotFound() {
        Good productOne = createTestProduct(1L, "Book", BigDecimal.valueOf(5.55), 4L, "This is a book");
        Good productTwo = createTestProduct(2L, "Phone", BigDecimal.valueOf(100), 1L, "This is a phone");
        Good productThree = createTestProduct(3L, "Bread", BigDecimal.valueOf(2.25), 7L, "This is a bread");
        Good productFour = createTestProduct(4L, "Computer", BigDecimal.valueOf(500), 2L, "This is a computer");

        List<Good> goods = asList(productOne, productTwo, productThree, productFour);

        String title = "Phone";
        String price = "200";

        when(goodRepository.findAll()).thenReturn(goods);

        goodService.getByTitleAndPrice(title, price);
    }

    @Test
    public void updateGoodTest() {
        Long id = 6L;

        User user = createTestUser(1L, "Den", "den_mogilev@yopmail.com", "1234", Role.ROLE_ADMIN);

        Good good = createTestProduct(id, "Book", BigDecimal.valueOf(5.55), 4L, "This is a book");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        GoodAdminCreationDto goodDto = new GoodAdminCreationDto();

        when(goodConverter.fromGoodAdminCreationDto(goodDto)).thenReturn(good);

        Good result = goodService.update(id, goodDto, user.getEmail());

        Assert.assertNotNull(result);
        Assert.assertEquals(good, result);

        verify(goodRepository, times(1)).save(goodConverter.fromGoodAdminCreationDto(goodDto));
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    public void updateGoodNegativeTest_IfUserRoleIsBuyer_ThenStatus403Forbidden() {
        Long id = 1L;

        User user = createTestUser(2L, "Asya", "asya_mogilev@yopmail.com", "5678", Role.ROLE_BUYER);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        GoodAdminCreationDto goodDto = createTestProductDto("Book", BigDecimal.valueOf(5.55), 4L, "This is a book");

        assertThrows(AccessDeniedException.class,
                () -> goodService.update(id, goodDto, user.getEmail()));

        try {
            goodService.update(id, goodDto, user.getEmail());
        } catch (AccessDeniedException e) {
            if (e.getMessage().equals(ACCESS_DENIED_FOR_BUYER)) {
                return;
            }
        }
        Assert.fail();
    }

    @Test
    public void givenGoodId_whenDeleteGood_thenNothing() {
        Long goodId = 1L;

        User user = createTestUser(1L, "Den", "den_mogilev@yopmail.com", "1234", Role.ROLE_ADMIN);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        willDoNothing().given(goodRepository).deleteById(goodId);

        goodService.deleteById(goodId, user.getEmail());

        verify(goodRepository, times(1)).deleteById(goodId);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    public void deleteGoodNegativeTest_IfUserRoleIsBuyer_ThenStatus403Forbidden() {
        Long goodId = 1L;

        User user = createTestUser(2L, "Asya", "asya_mogilev@yopmail.com", "5678", Role.ROLE_BUYER);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(AccessDeniedException.class,
                () -> goodService.deleteById(goodId, user.getEmail()));

        try {
            goodService.deleteById(goodId, user.getEmail());
        } catch (AccessDeniedException e) {
            if (e.getMessage().equals(ACCESS_DENIED_FOR_BUYER)) {
                return;
            }
        }
        Assert.fail();
    }

    private Good createTestProduct(Long id, String title, BigDecimal price, Long quantity, String description) {
        Good good = new Good();

        good.setId(id);
        good.setTitle(title);
        good.setPrice(price);
        good.setQuantity(quantity);
        good.setDescription(description);

        return good;
    }

    private GoodAdminCreationDto createTestProductDto(String title, BigDecimal price, Long quantity, String description) {
        GoodAdminCreationDto goodDto = new GoodAdminCreationDto();

        goodDto.setTitle(title);
        goodDto.setPrice(price);
        goodDto.setQuantity(quantity);
        goodDto.setDescription(description);

        return goodDto;
    }

    private Good getTestProductFromGoodAdminCreationDto(GoodAdminCreationDto goodDto) {
        Good good = new Good();

        good.setTitle(goodDto.getTitle());
        good.setPrice(goodDto.getPrice());
        good.setQuantity(goodDto.getQuantity());
        good.setDescription(goodDto.getDescription());

        return good;
    }

    private User createTestUser(Long id, String name, String email, String password, Role role) {
        User user = new User();

        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);

        return user;
    }
}
