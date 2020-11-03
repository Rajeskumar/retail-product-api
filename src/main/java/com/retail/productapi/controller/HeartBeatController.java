package com.retail.productapi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/heartbeat")
public class HeartBeatController {


    @Value("${application.name}")
    String appName;

    @Value("${application.version}")
    String appVersion;

    /**
     * This method returns system's metadata like application name and version.
     *
     * @return HeartBeatResponse
     */
    @GetMapping(value = "/", produces = "application/json")
    Map<String, String> getHeartBeat() {
        return Map.of("app-name", appName, "app-version", appVersion);
    }
}
