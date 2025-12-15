package com.bank.account.controller;

import com.bank.account.util.ApplicationConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RootController.class)
class RootControllerTest {

    private static final String TEST_HEALTH_ENDPOINT = "/health";
    private static final String TEST_ROOT_ENDPOINT = "/";
    private static final String TEST_HEALTH_STATUS_KEY = "status";
    private static final String TEST_HEALTH_SERVICE_KEY = "service";
    private static final String TEST_JSON_PATH_STATUS = "$." + TEST_HEALTH_STATUS_KEY;
    private static final String TEST_JSON_PATH_SERVICE = "$." + TEST_HEALTH_SERVICE_KEY;

    private static final String TEST_HEALTH_STATUS_UP = "UP";
    private static final String TEST_SERVICE_NAME = "Account Service";
    private static final String TEST_ROOT_MESSAGE = "Account Service API is running";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationConstants constants;

    @BeforeEach
    void setUp() {
        given(constants.getHealthStatusKey()).willReturn(TEST_HEALTH_STATUS_KEY);
        given(constants.getHealthServiceKey()).willReturn(TEST_HEALTH_SERVICE_KEY);
        given(constants.getHealthStatusUp()).willReturn(TEST_HEALTH_STATUS_UP);
        given(constants.getServiceName()).willReturn(TEST_SERVICE_NAME);
        given(constants.getRootMessage()).willReturn(TEST_ROOT_MESSAGE);
    }

    @Test
    @DisplayName("GET /health - возвращает статус сервиса")
    void health_ShouldReturnUpStatus() throws Exception {
        mockMvc.perform(get(TEST_HEALTH_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath(TEST_JSON_PATH_STATUS, is(TEST_HEALTH_STATUS_UP)))
                .andExpect(jsonPath(TEST_JSON_PATH_SERVICE, is(TEST_SERVICE_NAME)));
    }

    @Test
    @DisplayName("GET / - возвращает root сообщение")
    void root_ShouldReturnMessage() throws Exception {
        mockMvc.perform(get(TEST_ROOT_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().string(TEST_ROOT_MESSAGE));
    }
}

