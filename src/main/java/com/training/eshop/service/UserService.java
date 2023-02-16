package com.training.eshop.service;

import com.training.eshop.dto.UserLoginDto;
import com.training.eshop.dto.UserRegisterDto;
import com.training.eshop.model.User;

import java.util.Map;

public interface UserService {

    User save(UserRegisterDto userDto, String checkBoxValue);

    Map<Object, Object> authenticateUser(UserLoginDto userDto);

    User getByLoginAndPassword(String login, String password);
}
