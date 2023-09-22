package com.klasha.dto.resquestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyConversion {
    private String country;
    private double amount;
    private String targetCurrency;
}
