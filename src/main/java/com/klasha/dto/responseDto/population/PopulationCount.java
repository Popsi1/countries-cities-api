package com.klasha.dto.responseDto.population;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class PopulationCount {


    private String year;
    private String value;

    private String sex;
    private String reliabilty;
}
