package com.myShop.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Cart {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User user;

    private Set<CartItem> cartItems=new HashSet<>();

    private double totalSellingPrice;

    private int totalItem;

    private int totalMrpPrice;

    private int discount;

    private String couponCode;


}
