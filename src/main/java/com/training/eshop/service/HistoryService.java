package com.training.eshop.service;

import com.training.eshop.dto.HistoryDto;
import com.training.eshop.model.*;

import java.util.List;

public interface HistoryService {

    List<HistoryDto> getAllByOrderId(Long orderId, String buttonValue);

    void saveHistoryForCreatedOrder(Order order);

    void saveHistoryForCanceledOrder();

    void saveHistoryForAddedGoods(Good good);

    void saveHistoryForRemovedGoods(Good good);

    void saveHistoryForAttachedFile(Attachment attachment, Long orderId);

    void saveHistoryForRemovedFile(String fileName, Long orderId);

    void saveHistoryForAddedComment(Comment comment, Long orderId);

    void saveHistoryForLeftFeedback(Feedback feedback, Long orderId);
}
