package com.training.eshop.repository;

import com.training.eshop.model.History;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends CrudRepository<History, Long> {

    List<History> findAllByOrderIdOrderByDate(Long orderId);

    List<History> findAllByOrderId(Long orderId, Pageable pageable);
}
