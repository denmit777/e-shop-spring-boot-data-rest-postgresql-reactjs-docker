package com.training.eshop.service;

import com.training.eshop.dto.AttachmentDto;
import com.training.eshop.model.Attachment;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AttachmentService {

    List<AttachmentDto> getAllByOrderId(Long orderId);

    AttachmentDto getById(Long attachmentId, Long orderId);

    AttachmentDto getChosenAttachment(MultipartFile file) throws IOException;

    Attachment save(@NonNull AttachmentDto attachmentDto, Long orderId);

    void deleteByName(String name, Long orderId);
}
