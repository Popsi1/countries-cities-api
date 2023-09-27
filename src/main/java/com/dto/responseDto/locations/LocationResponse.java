package com.dto.responseDto.locations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;

@Data
public class LocationResponse {
    private String name;
    private String iso2;
    @JsonProperty("long")
    private String longitude;
    private String lat;
}
