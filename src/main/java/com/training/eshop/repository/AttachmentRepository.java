package com.training.eshop.repository;

import com.training.eshop.model.Attachment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends CrudRepository<Attachment, Long> {

    Attachment findByIdAndOrderId(Long id, Long orderId);

    List<Attachment> findAllByOrderId(Long orderId);

    void deleteByNameAndOrderId(String name, Long orderId);
}
