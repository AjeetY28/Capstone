package com.myShop.response;

import com.myShop.domain.USER_ROLE;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
public class AuthResponse {
    private String jwt;
    private String message;
    private USER_ROLE role;


}
