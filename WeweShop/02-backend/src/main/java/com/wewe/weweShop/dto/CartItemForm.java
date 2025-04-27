package com.wewe.weweShop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemForm {

    @NotNull
    private Long productId;

    @Min(1)
    private int quantity;
}
