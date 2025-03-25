package com.myShop.service.impl;

import com.myShop.config.JwtProvider;
import com.myShop.entity.User;
import com.myShop.exceptions.UserException;
import com.myShop.repository.UserRepository;
import com.myShop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public User findUserByJwtToken(String jwt) throws UserException {

        String email=jwtProvider.getEmailFromJwtToken(jwt);
        return this.findUserByEmail(email);

    }

    @Override
    public User findUserByEmail(String email) throws UserException {
        User user=userRepository.findByEmail(email);
        if(user==null)
        {
            throw new UserException("user not found with email - "+email);
        }
        return user;
    }
}
