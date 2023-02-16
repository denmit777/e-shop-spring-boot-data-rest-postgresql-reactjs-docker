package com.training.eshop.security.service;

import com.training.eshop.exception.UserNotFoundException;
import com.training.eshop.model.User;
import com.training.eshop.repository.UserRepository;
import com.training.eshop.security.model.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final String USER_NOT_FOUND = "User with login %s not found";

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) {
        Optional<User> user = userRepository.findByEmail(login);

        if (!user.isPresent()) {
            throw new UserNotFoundException(String.format(USER_NOT_FOUND, login));
        }

        return new CustomUserDetails(user.get());
    }
}

