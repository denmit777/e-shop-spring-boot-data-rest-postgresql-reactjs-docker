package com.training.eshop.converter;

import com.training.eshop.model.User;
import com.training.eshop.dto.UserRegisterDto;

public interface UserConverter {

    User fromUserRegisterDto(UserRegisterDto userDto);
}
