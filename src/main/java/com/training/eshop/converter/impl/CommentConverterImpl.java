package com.training.eshop.converter.impl;

import com.training.eshop.converter.CommentConverter;
import com.training.eshop.dto.CommentDto;
import com.training.eshop.model.Comment;
import com.training.eshop.model.Order;
import com.training.eshop.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class CommentConverterImpl implements CommentConverter {

    private final OrderRepository orderRepository;

    @Override
    public CommentDto convertToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();

        commentDto.setDate(comment.getDate());
        commentDto.setUser(comment.getUser().getName());
        commentDto.setText(comment.getText());

        return commentDto;
    }

    @Override
    public Comment fromCommentDto(CommentDto commentDto, Long orderId) {
        Comment comment = new Comment();

        Order order = orderRepository.findById(orderId).get();

        comment.setOrder(order);
        comment.setDate(LocalDateTime.now());
        comment.setUser(order.getUser());
        comment.setText(commentDto.getText());

        return comment;
    }
}
