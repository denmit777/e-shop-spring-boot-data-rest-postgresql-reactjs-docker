package com.training.eshop.controller;

import com.training.eshop.dto.FeedbackDto;
import com.training.eshop.model.Feedback;
import com.training.eshop.service.FeedbackService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@AllArgsConstructor
@RequestMapping(value = "/orders/{orderId}/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody FeedbackDto feedbackDto,
                                  @PathVariable("orderId") Long orderId,
                                  Authentication authentication) {
        Feedback savedFeedback = feedbackService.save(feedbackDto, orderId, authentication.getName());

        String currentUri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        String savedFeedbackLocation = currentUri + "/" + savedFeedback.getId();

        return ResponseEntity.status(CREATED)
                .header(HttpHeaders.LOCATION, savedFeedbackLocation)
                .body(savedFeedback);
    }

    @GetMapping
    public ResponseEntity<List<FeedbackDto>> getAllByOrderId(@PathVariable("orderId") Long orderId,
                                                             @RequestParam(value = "buttonValue", defaultValue = "default") String buttonValue) {
        List<FeedbackDto> feedbacks = feedbackService.getAllByOrderId(orderId, buttonValue);

        return ResponseEntity.ok(feedbacks);
    }
}
