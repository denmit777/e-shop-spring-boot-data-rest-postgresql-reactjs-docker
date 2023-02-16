package com.training.eshop.converter;

import com.training.eshop.dto.HistoryDto;
import com.training.eshop.model.History;

public interface HistoryConverter {

    HistoryDto convertToHistoryDto(History history);
}
