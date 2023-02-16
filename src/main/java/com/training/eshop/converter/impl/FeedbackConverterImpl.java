package com.training.eshop.converter.impl;

import com.training.eshop.converter.FeedbackConverter;
import com.training.eshop.dto.FeedbackDto;
import com.training.eshop.model.Feedback;
import com.training.eshop.model.Order;
import com.training.eshop.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class FeedbackConverterImpl implements FeedbackConverter {

    private final OrderRepository orderRepository;

    @Override
    public FeedbackDto convertToFeedbackDto(Feedback feedback) {
        FeedbackDto feedbackDto = new FeedbackDto();

        feedbackDto.setUser(feedback.getUser().getName());
        feedbackDto.setRate(feedback.getRate());
        feedbackDto.setDate(feedback.getDate());
        feedbackDto.setText(feedback.getText());

        return feedbackDto;
    }

    @Override
    public Feedback fromFeedbackDto(FeedbackDto feedbackDto, Long orderId) {
        Feedback feedback = new Feedback();

        Order order = orderRepository.findById(orderId).get();

        feedback.setOrder(order);
        feedback.setUser(order.getUser());
        feedback.setRate(feedbackDto.getRate());
        feedback.setDate(LocalDateTime.now());
        feedback.setText(feedbackDto.getText());

        return feedback;
    }
}
