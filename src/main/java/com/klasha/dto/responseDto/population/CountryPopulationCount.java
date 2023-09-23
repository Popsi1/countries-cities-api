package com.klasha.dto.responseDto.population;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryPopulationCount {
    private int year;
    private int value;
}
