package com.klasha.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.klasha.dto.responseDto.CurrencyConversionResponse;
import com.klasha.dto.resquestDto.CurrencyConversion;
import com.klasha.service.main.MainService;
import com.klasha.service.main.MainServiceImpl;
import com.klasha.service.population.PopulationService;
import com.klasha.service.population.PopulationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CountryTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PopulationService populationService;

    @MockBean
    private MainService mainService;

    @Autowired
    private ObjectMapper objectMapper;

    Map<String, String> countryDetails;

    @BeforeEach
    public void setup(){

        countryDetails = new HashMap<String, String>() {{
            put("population", "111111");
            put("capital city", "abuja");
            put("location", "long=8");
            put("currency", "NGN");
            put("ISO2", "NG");
            put("ISO3", "NGN");
        }};
    }

    @Test
    public void getCountryInfoTest() throws Exception {

        given(mainService.getCountryInfo("Nigeria")).willReturn(countryDetails);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/country/info")
                .param("country", "Nigeria"));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    public void currencyConversion() throws Exception {

        CurrencyConversion currencyConversion = new CurrencyConversion();
        currencyConversion.setCountry("Nigeria");
        currencyConversion.setAmount(1000);
        currencyConversion.setTargetCurrency("USD");

        CurrencyConversionResponse currencyConversionResponse = new CurrencyConversionResponse();
        currencyConversionResponse.setTargetCurrency("USD");
        currencyConversionResponse.setAmountConverted(1.5);

        given(mainService.currencyConversion(currencyConversion)).willReturn(currencyConversionResponse);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/country/currency/conversion")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(currencyConversion)));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }
}
