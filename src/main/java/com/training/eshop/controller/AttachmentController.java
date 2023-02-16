package com.training.eshop.controller;

import com.training.eshop.dto.AttachmentDto;
import com.training.eshop.service.AttachmentService;
import com.training.eshop.service.ValidationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@AllArgsConstructor
@RequestMapping(value = "/orders/{orderId}/attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;
    private final ValidationService validationService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @PathVariable("orderId") Long orderId) throws IOException {
        List<String> fileUploadErrors = validationService.validateUploadFile(file);

        if (checkErrors(fileUploadErrors)) {
            return new ResponseEntity<>(fileUploadErrors, HttpStatus.BAD_REQUEST);
        }

        AttachmentDto attachmentDto = attachmentService.getChosenAttachment(file);

        attachmentService.save(attachmentDto, orderId);

        return new ResponseEntity<>(attachmentService.getAllByOrderId(orderId), HttpStatus.OK);
    }

    @GetMapping("/{attachmentId}")
    public ResponseEntity<AttachmentDto> getById(@PathVariable("attachmentId") Long attachmentId,
                                                 @PathVariable("orderId") Long orderId,
                                                 HttpServletResponse response) throws IOException {
        AttachmentDto attachmentDto = attachmentService.getById(attachmentId, orderId);

        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = " + attachmentDto.getName();

        response.setHeader(headerKey, headerValue);

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            outputStream.write(attachmentDto.getFile());
        }

        return ResponseEntity.ok(attachmentDto);
    }

    @GetMapping
    public ResponseEntity<List<AttachmentDto>> getAllByOrderId(@PathVariable("orderId") Long orderId) {
        List<AttachmentDto> attachments = attachmentService.getAllByOrderId(orderId);

        return ResponseEntity.ok(attachments);
    }

    @DeleteMapping("/{attachmentName}")
    public ResponseEntity<?> deleteFile(@PathVariable("attachmentName") String attachmentName,
                                        @PathVariable("orderId") Long orderId) {
        attachmentService.deleteByName(attachmentName, orderId);

        return new ResponseEntity<>(attachmentService.getAllByOrderId(orderId), HttpStatus.OK);
    }

    private boolean checkErrors(List<String> fileUploadErrors) {
        return !fileUploadErrors.isEmpty();
    }
}
