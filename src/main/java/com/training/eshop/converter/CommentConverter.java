package com.training.eshop.converter;

import com.training.eshop.dto.CommentDto;
import com.training.eshop.model.Comment;

public interface CommentConverter {

    CommentDto convertToCommentDto(Comment comment);

    Comment fromCommentDto(CommentDto commentDto, Long orderId);
}
