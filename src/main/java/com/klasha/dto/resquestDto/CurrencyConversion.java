package com.klasha.dto.resquestDto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyConversion {
    @NotEmpty(message = "The country is required.")
    private String country;
    @NotNull(message = "The amount is required.")
    private double amount;
    @NotEmpty(message = "The target currency is required.")
    private String targetCurrency;
}
