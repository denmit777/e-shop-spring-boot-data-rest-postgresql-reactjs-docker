package com.training.eshop.service.impl;

import com.training.eshop.converter.AttachmentConverter;
import com.training.eshop.dto.AttachmentDto;
import com.training.eshop.exception.AttachmentNotFoundException;
import com.training.eshop.model.Attachment;
import com.training.eshop.repository.AttachmentRepository;
import com.training.eshop.service.AttachmentService;
import com.training.eshop.service.HistoryService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private static final Logger LOGGER = LogManager.getLogger(AttachmentServiceImpl.class.getName());

    private static final String FILE_NOT_FOUND = "This file is absent";

    private final AttachmentRepository attachmentRepository;
    private final AttachmentConverter attachmentConverter;
    private final HistoryService historyService;

    @Override
    @Transactional
    public Attachment save(@NonNull AttachmentDto attachmentDto, Long orderId) {
        Attachment attachment = attachmentConverter.fromAttachmentDto(attachmentDto, orderId);

        attachmentRepository.save(attachment);

        historyService.saveHistoryForAttachedFile(attachment, orderId);

        LOGGER.info("New file {} has just been added to order {}", attachmentDto.getName(), orderId);

        return attachment;
    }

    @Override
    @Transactional
    public void deleteByName(String fileName, Long orderId) {
        if (isFilePresent(fileName, orderId)) {

            historyService.saveHistoryForRemovedFile(fileName, orderId);

            attachmentRepository.deleteByNameAndOrderId(fileName, orderId);

            LOGGER.info("File {} has just been deleted from order {}", fileName, orderId);
        } else {
            throw new AttachmentNotFoundException(FILE_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public List<AttachmentDto> getAllByOrderId(Long orderId) {
        List<Attachment> attachments = attachmentRepository.findAllByOrderId(orderId);

        LOGGER.info("All files for order {} : {}", orderId, attachments);

        return attachments.stream()
                .map(attachmentConverter::convertToAttachmentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AttachmentDto getById(Long attachmentId, Long orderId) {
        Attachment attachment = attachmentRepository.findByIdAndOrderId(attachmentId, orderId);

        LOGGER.info("File {} is downloaded", attachment.getName());

        return attachmentConverter.convertToAttachmentDto(attachment);
    }

    @Override
    public AttachmentDto getChosenAttachment(@NonNull MultipartFile file) throws IOException {
        AttachmentDto attachmentDto = new AttachmentDto();

        attachmentDto.setName(file.getOriginalFilename());
        attachmentDto.setFile(file.getBytes());

        return attachmentDto;
    }

    private boolean isFilePresent(String fileName, Long orderId) {
        return attachmentRepository.findAllByOrderId(orderId).stream()
                .anyMatch(file -> fileName.equals(file.getName()));
    }
}

