package com.training.eshop.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserRegisterDto {

    private static final String FIELD_IS_EMPTY = "Fields shouldn't be empty";
    private static final String WRONG_SIZE_OF_NAME_OR_PASSWORD = "Login or password shouldn't be less than 4 symbols";
    private static final String INVALID_LOGIN_OR_PASSWORD = "Login or password should contains Latin letters";
    private static final String INVALID_NUMBER_OF_SYMBOLS_FOR_EMAIL = "Email shouldn't be less than 6 symbols";
    private static final String INVALID_EMAIL = "Email should contain symbol @";

    @NotBlank(message = FIELD_IS_EMPTY)
    @Size(min = 4, message = WRONG_SIZE_OF_NAME_OR_PASSWORD)
    @Pattern(regexp = "^[^А-Яа-я]*$", message = INVALID_LOGIN_OR_PASSWORD)
    private String name;

    @NotBlank(message = FIELD_IS_EMPTY)
    @Size(min = 4, message = WRONG_SIZE_OF_NAME_OR_PASSWORD)
    @Pattern(regexp = "^[^А-Яа-я]*$", message = INVALID_LOGIN_OR_PASSWORD)
    private String password;

    @NotBlank(message = FIELD_IS_EMPTY)
    @Size(min = 6, message = INVALID_NUMBER_OF_SYMBOLS_FOR_EMAIL)
    @Email(regexp = "^[^@|\\.].+@.+\\..+[^@|\\.]$", message = INVALID_EMAIL)
    private String email;
}
