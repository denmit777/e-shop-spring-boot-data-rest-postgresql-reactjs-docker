package com.training.eshop.service;

import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ValidationService {

    List<String> generateErrorMessage(BindingResult bindingResult);

    String getWrongSearchParameterError(String parameter);

    List<String> validateUploadFile(MultipartFile file);
}
