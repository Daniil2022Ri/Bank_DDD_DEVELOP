package com.bank.account.controller;

import com.bank.account.util.ApplicationConstants;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RootController {

    private final ApplicationConstants constants;

    public RootController(ApplicationConstants constants) {
        this.constants = constants;
    }

    @GetMapping("/health")
    @Operation(summary = "Проверка здоровья сервиса")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                constants.HEALTH_STATUS_KEY, constants.HEALTH_STATUS_UP,
                constants.HEALTH_SERVICE_KEY, constants.SERVICE_NAME
        ));
    }

    @GetMapping("/")
    public ResponseEntity<String> root() {
        return ResponseEntity.ok(constants.ROOT_MESSAGE);
    }
}
