package com.dto.resquestDto;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyConversion {
    @NotEmpty(message = "The country is required.")
    private String country;
    @Min(1)
    private double amount;
    @NotEmpty(message = "The target currency is required.")
    private String targetCurrency;
}
