package com.klasha.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klasha.dto.responseDto.currency.CurrencyConversionResponse;
import com.klasha.dto.resquestDto.CurrencyConversion;
import com.klasha.service.main.MainService;
import com.klasha.service.population.PopulationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
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

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getCountryInfoTest() throws Exception {

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

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/country/currency/conversion")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(currencyConversion)));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }


    @Test
    public void getCountryCitiesTest() throws Exception {

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/country/cities")
                .param("limit", "3"));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    public void getStateAndCitiesTest() throws Exception {

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/country/state/cities")
                .param("country", "Nigeria"));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }
}
