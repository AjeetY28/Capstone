package com.myShop.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorDetails {

    private String error;
    private String details;
    private LocalDateTime timestamp;


}
