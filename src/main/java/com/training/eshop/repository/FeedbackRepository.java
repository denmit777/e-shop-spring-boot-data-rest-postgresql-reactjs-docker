package com.training.eshop.repository;

import com.training.eshop.model.Feedback;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends CrudRepository<Feedback, Long> {

    Feedback findByIdAndOrderId(Long id, Long orderId);

    List<Feedback> findAllByOrderId(Long orderId);

    List<Feedback> findAllByOrderId(Long orderId, Pageable pageable);
}
