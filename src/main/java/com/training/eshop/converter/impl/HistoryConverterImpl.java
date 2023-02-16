package com.training.eshop.converter.impl;

import com.training.eshop.converter.HistoryConverter;
import com.training.eshop.dto.HistoryDto;
import com.training.eshop.model.History;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HistoryConverterImpl implements HistoryConverter {

    @Override
    public HistoryDto convertToHistoryDto(History history) {
        HistoryDto historyDto = new HistoryDto();

        historyDto.setDate(history.getDate());
        historyDto.setUser(history.getUser().getName());
        historyDto.setAction(history.getAction());
        historyDto.setDescription(history.getDescription());

        return historyDto;
    }
}
