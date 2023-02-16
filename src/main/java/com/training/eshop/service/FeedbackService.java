package com.training.eshop.service;

import com.training.eshop.dto.FeedbackDto;
import com.training.eshop.model.Feedback;

import java.util.List;

public interface FeedbackService {

    Feedback save(FeedbackDto feedbackDto, Long orderId, String login);

    List<FeedbackDto> getAllByOrderId(Long orderId, String buttonValue);
}
