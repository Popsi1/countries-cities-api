package com.klasha.dto.responseDto.currency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyConversionResponse {
    private String targetCurrency;
    private double amountConverted;
}
