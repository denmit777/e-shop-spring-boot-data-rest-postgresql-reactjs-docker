package com.training.eshop.controller;

import com.training.eshop.dto.CommentDto;
import com.training.eshop.model.Comment;
import com.training.eshop.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@AllArgsConstructor
@RequestMapping(value = "/orders/{orderId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody CommentDto commentDto, @PathVariable("orderId") Long orderId) {

        Comment savedComment = commentService.save(commentDto, orderId);

        String currentUri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        String savedCommentLocation = currentUri + "/" + savedComment.getId();

        return ResponseEntity.status(CREATED)
                .header(HttpHeaders.LOCATION, savedCommentLocation)
                .body(savedComment);
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllByOrderId(@PathVariable("orderId") Long orderId,
                                                            @RequestParam(value = "buttonValue", defaultValue = "default") String buttonValue) {
        List<CommentDto> comments = commentService.getAllByOrderId(orderId, buttonValue);

        return ResponseEntity.ok(comments);
    }
}
