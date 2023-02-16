package com.training.eshop.service.impl;

import com.training.eshop.service.ValidationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ValidationServiceImpl implements ValidationService {

    private static final Logger LOGGER = LogManager.getLogger(ValidationServiceImpl.class.getName());

    private static final String EMPTY_VALUE = "";
    private static final String WRONG_SEARCH_PARAMETER = "Search should be in latin letters or figures";
    private static final String DOWNLOADABLE_FILE_FORMAT = "jpg|pdf|doc|docx|png|jpeg";
    private static final String DOWNLOADABLE_FILE_FORMAT_ERROR_MESSAGE = "The selected file type is not allowed. Please select a file of " +
            "one of the following types: pdf, png, doc, docx, jpg, jpeg.";
    private static final Double ALLOWED_MAXIMUM_SIZE = 5.0;
    private static final String ALLOWED_MAXIMUM_SIZE_ERROR_MESSAGE = "The size of the attached file should not be greater than 5 Mb. " +
            "Please select another file.";

    @Bean(name = "multipartResolver")
    public MultipartResolver getMultipartResolver() {
        return new CommonsMultipartResolver();
    }

    @Override
    public List<String> generateErrorMessage(BindingResult bindingResult) {
        return Optional.of(bindingResult)
                .filter(BindingResult::hasErrors)
                .map(this::getErrors)
                .orElseGet(ArrayList::new);
    }

    @Override
    public String getWrongSearchParameterError(String parameter) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]+");
        Matcher matcher = pattern.matcher(parameter);

        if (!(parameter.equals(EMPTY_VALUE)) && !matcher.find()) {
            LOGGER.error(WRONG_SEARCH_PARAMETER);

            return WRONG_SEARCH_PARAMETER;
        }

        return EMPTY_VALUE;
    }

    @Override
    public List<String> validateUploadFile(MultipartFile file) {
        List<String> fileUploadErrors = new ArrayList<>();

        if (file != null) {
            if (!getFileExtension(file).matches(DOWNLOADABLE_FILE_FORMAT)) {
                LOGGER.error(DOWNLOADABLE_FILE_FORMAT_ERROR_MESSAGE);

                fileUploadErrors.add(DOWNLOADABLE_FILE_FORMAT_ERROR_MESSAGE);
            }

            if (getFileSizeMegaBytes(file) > ALLOWED_MAXIMUM_SIZE) {
                LOGGER.error(ALLOWED_MAXIMUM_SIZE_ERROR_MESSAGE);

                fileUploadErrors.add(ALLOWED_MAXIMUM_SIZE_ERROR_MESSAGE);
            }
        }
        return fileUploadErrors;
    }

    private List<String> getErrors(BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors();

        return errors.stream()
                .map(e -> e.getField() + " : " + e.getDefaultMessage())
                .collect(Collectors.toList());
    }

    private String getFileExtension(MultipartFile file) {
        if (file == null) {
            return EMPTY_VALUE;
        }
        String fileName = file.getOriginalFilename();
        assert fileName != null;

        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return EMPTY_VALUE;
        }
    }

    private double getFileSizeMegaBytes(MultipartFile file) {
        return (double) file.getSize() / (1024 * 1024);
    }
}
