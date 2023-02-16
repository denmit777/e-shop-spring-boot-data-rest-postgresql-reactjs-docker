package com.training.eshop.service.impl;

import com.training.eshop.converter.CommentConverter;
import com.training.eshop.dto.CommentDto;
import com.training.eshop.model.Comment;
import com.training.eshop.repository.CommentRepository;
import com.training.eshop.service.CommentService;
import com.training.eshop.service.HistoryService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private static final Logger LOGGER = LogManager.getLogger(CommentServiceImpl.class.getName());

    private final CommentRepository commentRepository;
    private final CommentConverter commentConverter;
    private final HistoryService historyService;

    @Override
    @Transactional
    public Comment save(CommentDto commentDto, Long orderId) {
        Comment comment = commentConverter.fromCommentDto(commentDto, orderId);

        historyService.saveHistoryForAddedComment(comment, orderId);

        commentRepository.save(comment);

        LOGGER.info("New commit has just been added to order {}: {}", orderId, comment.getText());

        return comment;
    }

    @Override
    @Transactional
    public List<CommentDto> getAllByOrderId(Long orderId, String buttonValue) {
        List<Comment> comments;

        if (buttonValue.equals("Show All")) {
            comments = commentRepository.findAllByOrderId(orderId);

            LOGGER.info("All comments for order {}: {}", orderId, comments);
        } else {
            comments = commentRepository.findAllByOrderId(orderId, PageRequest.of(0, 5,
                    Sort.by(Sort.Direction.DESC, "date")));

            LOGGER.info("Last 5 comments for order {}: {}", orderId, comments);
        }

        return comments.stream()
                .map(commentConverter::convertToCommentDto)
                .collect(Collectors.toList());
    }
}
