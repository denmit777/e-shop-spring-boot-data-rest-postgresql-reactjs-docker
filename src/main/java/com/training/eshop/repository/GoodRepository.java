package com.training.eshop.repository;

import com.training.eshop.model.Good;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodRepository extends CrudRepository<Good, Long> {

    @Query("from Good")
    List<Good> findAll(Pageable pageable);

    @Query("from Good g where str(g.id) like concat('%',:id,'%')")
    List<Good> findAllById(@Param("id") String id, Pageable pageable);

    @Query("from Good g where lower(g.title) like lower(concat('%',:title,'%'))")
    List<Good> findAllByTitle(@Param("title") String title, Pageable pageable);

    @Query("from Good g where str(g.price) like concat('%',:price,'%')")
    List<Good> findAllByPrice(@Param("price") String price, Pageable pageable);

    @Query("from Good g where lower(g.description) like lower(concat('%',:description,'%'))")
    List<Good> findAllByDescription(@Param("description") String description, Pageable pageable);

    @Query("from Good order by title")
    List<Good> findAllForBuyer();

    @Modifying
    @Query("delete from Good g where g.id =:id")
    void deleteByIdAfterQuantityEqualsZero(@Param("id") Long id);
}
