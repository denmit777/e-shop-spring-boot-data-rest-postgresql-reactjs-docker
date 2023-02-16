package com.training.eshop.controller;

import com.training.eshop.dto.UserLoginDto;
import com.training.eshop.dto.UserRegisterDto;
import com.training.eshop.model.User;
import com.training.eshop.service.UserService;
import com.training.eshop.service.ValidationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@AllArgsConstructor
@RequestMapping
public class UserController {

    private final UserService userService;
    private final ValidationService validationService;

    @PostMapping("/")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserRegisterDto userDto,
                                          BindingResult bindingResult,
                                          @RequestParam(value = "isUserCheck", defaultValue = "default") String checkBoxValue) {
        List<String> errorMessage = validationService.generateErrorMessage(bindingResult);

        if (checkErrors(errorMessage)) {
            return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
        }

        User savedUser = userService.save(userDto, checkBoxValue);

        String currentUri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        String savedUserLocation = currentUri + "/" + savedUser.getId();

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, savedUserLocation)
                .body(savedUser);
    }

    @PostMapping("/auth")
    public ResponseEntity<?> authenticationUser(@RequestBody @Valid UserLoginDto userDto,
                                                BindingResult bindingResult) {
        List<String> errorMessage = validationService.generateErrorMessage(bindingResult);

        if (checkErrors(errorMessage)) {
            return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
        }

        Map<Object, Object> response = userService.authenticateUser(userDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private boolean checkErrors(List<String> errorMessage) {
        return !errorMessage.isEmpty();
    }
}
