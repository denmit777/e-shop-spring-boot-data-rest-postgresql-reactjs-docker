package com.training.eshop.repository;

import com.training.eshop.model.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    @Query("from Order")
    List<Order> findAll(Pageable pageable);

    @Query("from Order o order by o.user.name, o.id")
    List<Order> findAllSortedByUserNameAndId(Pageable pageable);

    @Query("from Order o order by o.user.name desc, o.id")
    List<Order> findAllSortedByUserNameDescOrderAndId(Pageable pageable);
}
