package com.teletrex.hue;


import com.teletrex.hue.model.Light;
import com.teletrex.hue.model.Sensor;
import com.teletrex.hue.service.HueDiscoveryService;

import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Component
public class GraphQLHueDataFetchers {


    private String hueBridgeIP;
    private String apiKey;

    private final RestTemplate restTemplate;
    private final HttpHeaders headers;
    private String baseUrl;

    public GraphQLHueDataFetchers(HueDiscoveryService hueDiscoveryService) {
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
        this.hueBridgeIP = hueDiscoveryService.getHueIp();
        this.apiKey = hueDiscoveryService.getApiKey();
        log.info("hueip = {}", hueBridgeIP);
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    private String getBaseUrl() {
        if (baseUrl == null) {
            System.out.println("hueBridgeIP:" + hueBridgeIP);
            System.out.println("apiKey:" + apiKey);
            baseUrl = "http://" + hueBridgeIP + "/api/" + apiKey;
        }
        return baseUrl;
    }

    public DataFetcher getLightById() {
        return dataFetchingEnvironment -> {
            String lightId = dataFetchingEnvironment.getArgument("id");
            ResponseEntity<Light> response = restTemplate.exchange(
                getBaseUrl() + "/lights/" + lightId.trim(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Light.class
            );
            Light result = response.getBody();
            result.setId(lightId);
            return result;
        };
    }

    public DataFetcher getAllLights() {
        return dataFetchingEnvironment -> getLights();
    }

    private Collection<Light> getLights() {
        ResponseEntity<Map<String, Light>> response = restTemplate.exchange(
            getBaseUrl() + "/lights/",
            HttpMethod.GET,
            new HttpEntity<>(headers),
            new ParameterizedTypeReference<Map<String, Light>>() {}
        );
        Map<String, Light> lights = response.getBody();
        lights.forEach((k, v) -> v.setId(k));
        return lights.values();
    }

    public DataFetcher turnLightOn() {
        System.out.println(hueBridgeIP);
        System.out.println(apiKey);
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument("id");
            Boolean on = dataFetchingEnvironment.getArgument("on");
            Map<String, Object> body = new HashMap<>();
            body.put("on", on);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            restTemplate.exchange(
                getBaseUrl() + "/lights/" + id.trim() + "/state",
                HttpMethod.PUT,
                request,
                String.class
            );

            return getLights();
        };
    }

    public DataFetcher setLightColor() {
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument("id");
            Integer bri = dataFetchingEnvironment.getArgument("bri");
            Integer hue = dataFetchingEnvironment.getArgument("hue");
            Integer sat = dataFetchingEnvironment.getArgument("sat");
            
            Map<String, Object> body = new HashMap<>();
            body.put("hue", hue);
            body.put("bri", bri);
            body.put("sat", sat);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            restTemplate.exchange(
                getBaseUrl() + "/lights/" + id.trim() + "/state",
                HttpMethod.PUT,
                request,
                String.class
            );

            return getLights();
        };
    }

    public DataFetcher getAllSensors() {
        return dataFetchingEnvironment -> getSensors();
    }

    private Collection<Sensor> getSensors() {
        ResponseEntity<Map<String, Sensor>> response = restTemplate.exchange(
            getBaseUrl() + "/sensors/",
            HttpMethod.GET,
            new HttpEntity<>(headers),
            new ParameterizedTypeReference<Map<String, Sensor>>() {}
        );
        Map<String, Sensor> sensors = response.getBody();
        sensors.forEach((k, v) -> v.setId(k));
        return sensors.values();
    }
}
