package com.training.eshop.mail.service;

import java.util.Map;

public interface EmailService {

    void sendMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel, String template);

    void sendOrderDetailsMessage(Long orderId, String login);

    void sendFeedbackMessage(Long orderId, Long feedbackId, String login);
}
