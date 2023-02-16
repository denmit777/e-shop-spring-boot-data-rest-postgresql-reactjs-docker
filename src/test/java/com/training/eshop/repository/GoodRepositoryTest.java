package com.training.eshop.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.training.eshop.model.Good;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodRepositoryTest {

    @Autowired
    private GoodRepository goodRepository;

    @Before
    public void resetDb() {
        goodRepository.deleteAll();
    }

    @Test
    public void givenProduct_whenSave_thenReturnSavedProduct() {
        Good good = createTestProduct("Phone", BigDecimal.valueOf(4.2), 3L, "This is a phone");

        Good savedGood = goodRepository.save(good);

        assertThat(savedGood).isNotNull();
        assertThat(savedGood.getId()).isGreaterThan(0);
    }

    @Test
    public void givenGoodsList_whenFindAll_thenGoodsList() {
        Good good1 = createTestProduct("Phone", BigDecimal.valueOf(4.2), 3L, "This is a phone");
        Good good2 = createTestProduct("Book", BigDecimal.valueOf(5), 1L, "This is a book");

        goodRepository.save(good1);
        goodRepository.save(good2);

        List<Good> goods = (List<Good>) goodRepository.findAll();

        assertThat(goods).isNotNull();
        assertThat(goods.size()).isEqualTo(2);
    }

    @Test
    public void givenGoodsList_whenFindAllByPageOne_IfPageSizeIsOneAndPageNumberIsOne_thenGoodsList() {
        Good good1 = createTestProduct("Phone", BigDecimal.valueOf(4.2), 3L, "This is a phone");
        Good good2 = createTestProduct("Book", BigDecimal.valueOf(5), 1L, "This is a book");

        goodRepository.save(good1);
        goodRepository.save(good2);

        List<Good> goods = goodRepository.findAll(PageRequest.of(0, 1));

        assertThat(goods).isNotNull();
        assertThat(goods.size()).isEqualTo(1);
    }

    @Test
    public void givenGoodsList_whenFindAllSortedByTitle_thenGoodsList() {
        Good good1 = createTestProduct("Phone", BigDecimal.valueOf(100.25), 3L, "This is a phone");
        Good good2 = createTestProduct("Book", BigDecimal.valueOf(5.54), 1L, "This is a book");
        Good good3 = createTestProduct("Smartphone", BigDecimal.valueOf(100.32), 2L, "This is a phone");

        goodRepository.save(good1);
        goodRepository.save(good2);
        goodRepository.save(good3);

        List<Good> goods = Stream.of(good1, good2, good3)
                .sorted(Comparator.comparing(Good::getPrice))
                .collect(Collectors.toList());

        List<Good> sortedGoods = goodRepository.findAll(PageRequest.of(0, 10,
                Sort.by(Sort.Direction.fromString("asc"), "price")));

        assertThat(sortedGoods).isNotNull();
        assertThat(sortedGoods.size()).isEqualTo(3);
        assertThat(sortedGoods.toString()).isEqualTo(goods.toString());
    }

    @Test
    public void givenGoodsList_whenFindAllById_thenGoodsList() {
        Good good1 = createTestProduct("Phone", BigDecimal.valueOf(4.2), 3L, "This is a phone");
        Good good2 = createTestProduct("Book", BigDecimal.valueOf(5), 1L, "This is a book");
        Good good3 = createTestProduct("Smartphone", BigDecimal.valueOf(8), 2L, "This is a phone");

        goodRepository.save(good1);
        goodRepository.save(good2);
        goodRepository.save(good3);

        List<Good> goods = goodRepository.findAllById(String.valueOf(good1.getId()), PageRequest.of(0, 10));

        assertThat(goods).isNotNull();
        assertThat(goods.size()).isEqualTo(1);
    }

    @Test
    public void givenGoodsList_whenFindAllByTitle_thenGoodsList() {
        Good good1 = createTestProduct("Phone", BigDecimal.valueOf(4.2), 3L, "This is a phone");
        Good good2 = createTestProduct("Book", BigDecimal.valueOf(5), 1L, "This is a book");
        Good good3 = createTestProduct("Smartphone", BigDecimal.valueOf(8), 2L, "This is a phone");

        goodRepository.save(good1);
        goodRepository.save(good2);
        goodRepository.save(good3);

        List<Good> goods = goodRepository.findAllByTitle("ph", PageRequest.of(0, 10));

        assertThat(goods).isNotNull();
        assertThat(goods.size()).isEqualTo(2);
    }

    @Test
    public void givenGoodsList_whenFindAllByPrice_thenGoodsList() {
        Good good1 = createTestProduct("Phone", BigDecimal.valueOf(100), 3L, "This is a phone");
        Good good2 = createTestProduct("Book", BigDecimal.valueOf(5), 1L, "This is a book");
        Good good3 = createTestProduct("Smartphone", BigDecimal.valueOf(100), 2L, "This is a phone");

        goodRepository.save(good1);
        goodRepository.save(good2);
        goodRepository.save(good3);

        List<Good> goods = goodRepository.findAllByPrice("100", PageRequest.of(0, 10));

        assertThat(goods).isNotNull();
        assertThat(goods.size()).isEqualTo(2);
    }

    @Test
    public void givenGoodsList_whenFindAllForBuyer_thenGoodsList() {
        Good good1 = createTestProduct("Phone", BigDecimal.valueOf(100.25), 3L, "This is a phone");
        Good good2 = createTestProduct("Book", BigDecimal.valueOf(5.54), 1L, "This is a book");
        Good good3 = createTestProduct("Smartphone", BigDecimal.valueOf(100.32), 2L, "This is a phone");

        goodRepository.save(good1);
        goodRepository.save(good2);
        goodRepository.save(good3);

        List<Good> goods = Stream.of(good1, good2, good3)
                .sorted(Comparator.comparing(Good::getTitle))
                .collect(Collectors.toList());

        List<Good> sortedGoods = goodRepository.findAllForBuyer();

        assertThat(sortedGoods).isNotNull();
        assertThat(sortedGoods.size()).isEqualTo(3);
        assertThat(sortedGoods.toString()).isEqualTo(goods.toString());
    }

    @Test
    public void givenProduct_whenFindById_thenReturnProduct() {
        Good good = createTestProduct("Phone", BigDecimal.valueOf(4.2), 3L, "This is a phone");
        goodRepository.save(good);

        Optional<Good> searchedGood = goodRepository.findById(good.getId());

        assertThat(searchedGood).isNotEmpty();
    }

    @Test
    public void givenProduct_whenFindByIdButDoesNotExist_thenReturnEmptiness() {
        Good good = createTestProduct("Phone", BigDecimal.valueOf(4.2), 3L, "This is a phone");
        goodRepository.save(good);

        Optional<Good> searchedGood = goodRepository.findById(good.getId() + 1);

        assertThat(searchedGood).isEmpty();
    }

    @Test
    public void givenProduct_whenUpdateProduct_thenReturnUpdatedProduct() {
        Good good = createTestProduct("Phone", BigDecimal.valueOf(4.2), 3L, "This is a phone");
        goodRepository.save(good);

        Good savedGood = goodRepository.findById(good.getId()).get();

        savedGood.setTitle("Smartphone");
        savedGood.setPrice(BigDecimal.valueOf(100));

        Good updatedGood = goodRepository.save(savedGood);

        assertThat(updatedGood.getTitle()).isEqualTo("Smartphone");
        assertThat(updatedGood.getPrice()).isEqualTo(BigDecimal.valueOf(100));
    }

    @Test
    public void givenProduct_whenDelete_thenRemoveProduct() {
        Good good = createTestProduct("Phone", BigDecimal.valueOf(4.2), 3L, "This is a phone");

        goodRepository.save(good);

        goodRepository.deleteById(good.getId());

        Optional<Good> goodOptional = goodRepository.findById(good.getId());

        assertThat(goodOptional).isEmpty();
    }

    private Good createTestProduct(String title, BigDecimal price, Long quantity, String description) {
        Good good = new Good();

        good.setTitle(title);
        good.setPrice(price);
        good.setQuantity(quantity);
        good.setDescription(description);

        return good;
    }
}
