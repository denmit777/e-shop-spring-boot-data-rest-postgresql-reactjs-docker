package com.training.eshop.service.impl;

import com.training.eshop.converter.HistoryConverter;
import com.training.eshop.dto.HistoryDto;
import com.training.eshop.model.*;
import com.training.eshop.model.enums.Status;
import com.training.eshop.repository.HistoryRepository;
import com.training.eshop.repository.OrderRepository;
import com.training.eshop.service.HistoryService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HistoryServiceImpl implements HistoryService {
    private static final Logger LOGGER = LogManager.getLogger(HistoryServiceImpl.class.getName());

    private static final String ACTION_ADD_GOODS_TO_ORDER = "Product was added";
    private static final String DESCRIPTION_ADD_GOODS_TO_ORDER = "%s (%s $) was added to cart";
    private static final String ACTION_REMOVE_GOODS_FROM_ORDER = "Product was removed";
    private static final String DESCRIPTION_REMOVE_GOODS_FROM_ORDER = "%s (%s $) was removed from cart";
    private static final String ACTION_CREATE_NEW_ORDER = "Order was created";
    private static final String DESCRIPTION_CREATE_NEW_ORDER = "Order %s (%s $) was created";
    private static final String ACTION_ATTACH_FILE_TO_ORDER = "File was attached";
    private static final String DESCRIPTION_ATTACH_FILE_TO_ORDER = "File %s was attached to order %s";
    private static final String ACTION_REMOVE_FILE_FROM_ORDER = "File was removed";
    private static final String DESCRIPTION_REMOVE_FILE_FROM_ORDER = "File %s was removed from order %s";
    private static final String ACTION_ADD_COMMENT_TO_ORDER = "Comment was added";
    private static final String DESCRIPTION_ADD_COMMENT_TO_ORDER = "Comment from %s was added to order %s";
    private static final String ACTION_LEAVE_FEEDBACK_TO_ORDER = "Feedback was left";
    private static final String DESCRIPTION_LEAVE_FEEDBACK_TO_ORDER = "Feedback was left by %s to order %s";

    private final HistoryRepository historyRepository;
    private final HistoryConverter historyConverter;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public List<HistoryDto> getAllByOrderId(Long orderId, String buttonValue) {
        List<History> history;

        if (buttonValue.equals("Show All")) {
            history = historyRepository.findAllByOrderIdOrderByDate(orderId);

            LOGGER.info("All history for order {}: {}", orderId, history);
        } else {
            history = historyRepository.findAllByOrderId(orderId, PageRequest.of(0, 5,
                    Sort.by(Sort.Direction.DESC, "date")));

            LOGGER.info("Last 5 history for order {}: {}", orderId, history);
        }

        return history.stream()
                .map(historyConverter::convertToHistoryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveHistoryForCreatedOrder(Order order) {
        Long orderNumber = order.getId();
        String action = ACTION_CREATE_NEW_ORDER;
        String description = String.format(DESCRIPTION_CREATE_NEW_ORDER, orderNumber, order.getTotalPrice());

        saveHistoryParametersForGoods(action, description);

        setHistoryParametersIfOrderIsCreated(order);
    }

    @Override
    @Transactional
    public void saveHistoryForAddedGoods(Good good) {
        String action = ACTION_ADD_GOODS_TO_ORDER;
        String description = String.format(DESCRIPTION_ADD_GOODS_TO_ORDER, good.getTitle(), good.getPrice());

        saveHistoryParametersForGoods(action, description);
    }

    @Override
    @Transactional
    public void saveHistoryForRemovedGoods(Good good) {
        String action = ACTION_REMOVE_GOODS_FROM_ORDER;
        String description = String.format(DESCRIPTION_REMOVE_GOODS_FROM_ORDER, good.getTitle(), good.getPrice());

        saveHistoryParametersForGoods(action, description);
    }

    @Override
    @Transactional
    public void saveHistoryForAttachedFile(Attachment attachment, Long orderId) {
        String action = ACTION_ATTACH_FILE_TO_ORDER;
        String description = String.format(DESCRIPTION_ATTACH_FILE_TO_ORDER, attachment.getName(), orderId);

        saveHistoryParametersForAttachments(action, description, orderId);
    }

    @Override
    @Transactional
    public void saveHistoryForRemovedFile(String fileName, Long orderId) {
        String action = ACTION_REMOVE_FILE_FROM_ORDER;
        String description = String.format(DESCRIPTION_REMOVE_FILE_FROM_ORDER, fileName, orderId);

        saveHistoryParametersForAttachments(action, description, orderId);
    }

    @Override
    @Transactional
    public void saveHistoryForAddedComment(Comment comment, Long orderId) {
        String action = ACTION_ADD_COMMENT_TO_ORDER;
        String description = String.format(DESCRIPTION_ADD_COMMENT_TO_ORDER, comment.getUser().getName(), orderId);

        saveHistoryParametersForAttachments(action, description, orderId);
    }

    @Override
    @Transactional
    public void saveHistoryForLeftFeedback(Feedback feedback, Long orderId) {
        String action = ACTION_LEAVE_FEEDBACK_TO_ORDER;
        String description = String.format(DESCRIPTION_LEAVE_FEEDBACK_TO_ORDER, feedback.getUser().getName(), orderId);

        saveHistoryParametersForAttachments(action, description, orderId);
    }

    @Override
    @Transactional
    public void saveHistoryForCanceledOrder() {
        for (History history : historyRepository.findAll()) {
            if (history.getStatus().equals(Status.IN_PROGRESS)) {
                history.setStatus(Status.CANCELED);
            }
        }
    }

    private void saveHistoryParametersForGoods(String action, String description) {
        History history = new History();

        history.setDate(LocalDateTime.now());
        history.setAction(action);
        history.setDescription(description);
        history.setStatus(Status.IN_PROGRESS);

        historyRepository.save(history);
    }

    private void saveHistoryParametersForAttachments(String action, String description, Long orderId) {
        History history = new History();

        Order order = orderRepository.findById(orderId).get();

        history.setDate(LocalDateTime.now());
        history.setAction(action);
        history.setDescription(description);
        history.setOrder(order);
        history.setStatus(Status.READY);
        history.setUser(order.getUser());

        historyRepository.save(history);
    }

    private void setHistoryParametersIfOrderIsCreated(Order order) {
        for (History history : historyRepository.findAll()) {
            if (history.getStatus().equals(Status.IN_PROGRESS)) {
                history.setOrder(order);
                history.setStatus(Status.READY);
                history.setUser(order.getUser());
            }
        }
    }
}
