package com.training.eshop.service;

import com.training.eshop.dto.CommentDto;
import com.training.eshop.model.Comment;

import java.util.List;

public interface CommentService {

    Comment save(CommentDto commentDto, Long orderId);

    List<CommentDto> getAllByOrderId(Long orderId, String buttonValue);
}
