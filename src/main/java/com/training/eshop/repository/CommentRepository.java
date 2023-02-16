package com.training.eshop.repository;

import com.training.eshop.model.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {

    List<Comment> findAllByOrderId(Long orderId);

    List<Comment> findAllByOrderId(Long orderId, Pageable pageable);
}
