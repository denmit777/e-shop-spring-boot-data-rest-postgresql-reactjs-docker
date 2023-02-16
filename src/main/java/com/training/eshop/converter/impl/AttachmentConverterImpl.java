package com.training.eshop.converter.impl;

import com.training.eshop.converter.AttachmentConverter;
import com.training.eshop.dto.AttachmentDto;
import com.training.eshop.model.Attachment;
import com.training.eshop.model.Order;
import com.training.eshop.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AttachmentConverterImpl implements AttachmentConverter {

    private final OrderRepository orderRepository;

    @Override
    public AttachmentDto convertToAttachmentDto(Attachment attachment) {
        AttachmentDto attachmentDto = new AttachmentDto();

        attachmentDto.setId(attachment.getId());
        attachmentDto.setName(attachment.getName());
        attachmentDto.setFile(attachment.getFile());

        return attachmentDto;
    }

    @Override
    public Attachment fromAttachmentDto(AttachmentDto attachmentDto, Long orderId) {
        Attachment attachment = new Attachment();

        Order order = orderRepository.findById(orderId).get();

        attachment.setId(attachmentDto.getId());
        attachment.setName(attachmentDto.getName());
        attachment.setFile(attachmentDto.getFile());
        attachment.setOrder(order);

        return attachment;
    }
}
