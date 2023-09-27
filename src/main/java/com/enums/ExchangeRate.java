package com.enums;

import com.exception.ResourceNotFoundException;

public enum ExchangeRate {

    EURTONGN("EUR", "NGN", 493.06),
    USDTONGN("USD", "NGN", 460.72),
    JPYTONGN("JPY", "NGN", 3.28),
    GBPTONGN("GBP", "NGN", 570.81),
    EURTOUGX("EUR", "UGX", 4),
    USDTOUGX("USD", "UGX", 3),
    JPYTOUGX("JPY", "UGX", 26.62),
    GBPTOUGX("GBP", "UGX", 4),
    NGNTOEUR("NGN", "EUR", 0.00203),
    NGNTOUSD("NGN", "USD", 0.00217),
    NGNTOJPY("NGN", "JPY", 0.305),
    NGNTOGBP("NGN", "GBP", 0.00175),
    UGXTOEUR("UGX", "EUR", 0.25),
    UGXTOUSD("UGX", "USD", 0.33),
    UGXTOJPY("UGX", "JPY", 0.0376),
    UGXTOGBP("UGX", "GBP", 0.25);


    private final String sourceCurrency;
    private final String targetCurrency;
    private final double rate;

    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public double getRate() {
        return rate;
    }

    ExchangeRate(String sourceCurrency, String targetCurrency, double rate) {
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public static double getRate(String sourceCurrency, String targetCurrency) {

        for (ExchangeRate response : values()) {
            if (response.getSourceCurrency().equals(sourceCurrency) && response.getTargetCurrency().equals(targetCurrency))
                return response.getRate();
        } throw new ResourceNotFoundException("Exchange rate not found");
    }


}
