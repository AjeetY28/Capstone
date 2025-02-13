package com.myShop.entity;


import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankDetails {

    private String accountNumber;
    private String accountHolderName;
//    private String bankName;
    private String ifscCode;
}
