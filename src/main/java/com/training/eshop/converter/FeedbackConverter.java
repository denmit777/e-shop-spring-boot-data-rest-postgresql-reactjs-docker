package com.training.eshop.converter;

import com.training.eshop.dto.FeedbackDto;
import com.training.eshop.model.Feedback;

public interface FeedbackConverter {

    FeedbackDto convertToFeedbackDto(Feedback feedback);

    Feedback fromFeedbackDto(FeedbackDto feedbackDto, Long orderId);
}
