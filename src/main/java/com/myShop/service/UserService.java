package com.myShop.service;

import com.myShop.entity.User;
import com.myShop.exceptions.UserException;

public interface UserService {

    User findUserByJwtToken(String jwt) throws Exception;
    User findUserByEmail(String email) throws UserException;
}
