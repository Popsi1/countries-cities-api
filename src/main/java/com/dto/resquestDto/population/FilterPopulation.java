package com.dto.resquestDto.population;

import lombok.Builder;
import lombok.Data;

@Data
public class FilterPopulation {

    private int limit;

    private String order;

    private String orderBy;

    private String country;

}
