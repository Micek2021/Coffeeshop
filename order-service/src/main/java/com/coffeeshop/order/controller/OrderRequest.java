package com.coffeeshop.order.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest{
    private Long productId;
    private int quantity;
    private String customerName;
}
