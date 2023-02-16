package com.training.eshop.controller;

import com.training.eshop.dto.HistoryDto;
import com.training.eshop.service.HistoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@AllArgsConstructor
@RequestMapping(value = "/orders/{orderId}/history")
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping
    public ResponseEntity<List<HistoryDto>> getAllByOrderId(@PathVariable("orderId") Long orderId,
                                                            @RequestParam(value = "buttonValue", defaultValue = "default") String buttonValue) {
        List<HistoryDto> history = historyService.getAllByOrderId(orderId, buttonValue);

        return ResponseEntity.ok(history);
    }
}
