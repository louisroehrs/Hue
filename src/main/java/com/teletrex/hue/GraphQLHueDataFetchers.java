package com.teletrex.hue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import com.teletrex.hue.model.Light;
import com.teletrex.hue.model.Lights;
import com.teletrex.hue.model.Sensor;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.URL;
import java.util.*;

@Component
@PropertySource("classpath:application.properties")
public class GraphQLHueDataFetchers {

    private static String hueBridgeIP = "192.168.1.97";
    private static String apiKey = "DMN24zKLkv4uClyfk3smBMB1VyB8BeVpxq1YOndO";

    private static String baseUrl = "http://" + hueBridgeIP + "/api/"+ apiKey;
    private static WebClient hueHubClient =  WebClient
            .builder()
            .baseUrl(baseUrl)
            .defaultCookie("cookieKey", "cookieValue")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultUriVariables(Collections.singletonMap("url", baseUrl))
            .build();

    private static WebClient.RequestBodySpec getLightsUri =
            (WebClient.RequestBodySpec) hueHubClient.get().uri("/lights");


    public DataFetcher getLightById() {
        return dataFetchingEnvironment -> {
            String lightId = dataFetchingEnvironment.getArgument("id");
            Light result = getLightByIdUri(lightId).retrieve().bodyToMono(Light.class).block();
            result.setId(lightId);
            return result;
        };
    }

    public DataFetcher getAllLights() {
        return dataFetchingEnvironment -> {
            // Convert a Map returned from an API to a list of objects, and stuff the key id into each object.
            return getLights();
        };
    }

  //  @org.jetbrains.annotations.NotNull
    private Collection<Light> getLights() {
        ParameterizedTypeReference<Map<String, Light>> myLights = new ParameterizedTypeReference<Map<String,Light>>() {};
        Map<String, Light> lights = (Map<String, Light>) getAllLightsUri()
                .retrieve()
                .bodyToMono(myLights)
                .block();
        lights.forEach( (k,v) -> v.setId(k) );
        return lights.values();
    }

    public DataFetcher turnLightOn() {
        System.out.println(hueBridgeIP);
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument("id");
            Boolean on = dataFetchingEnvironment.getArgument("on");
            Map<String, Object> body = new HashMap<>();
            body.put("on",on);
            String string = (String) turnLightOnByIdUri(id)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
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
            body.put("hue",hue);
            body.put("bri",bri);
            body.put("sat",sat);
            String string = (String) setLightColorByIdUri(id)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return getLights();
        };
    }

    public DataFetcher getAllSensors() {
        return dataFetchingEnvironment -> {
            // Convert a Map returned from an API to a list of objects, and stuff the key id into each object.
            return getSensors();
        };
    }

    //  @org.jetbrains.annotations.NotNull
    private Collection<Sensor> getSensors() {
        ParameterizedTypeReference<Map<String, Sensor>> mySensors = new ParameterizedTypeReference<Map<String,Sensor>>() {};
        Map<String, Sensor> sensors = (Map<String, Sensor>) getAllSensorsUri()
                .retrieve()
                .bodyToMono(mySensors)
                .block();
        sensors.forEach( (k,v) -> v.setId(k));
        return sensors.values();
    }

    private WebClient.RequestBodySpec getLightByIdUri(String id) {
        WebClient.RequestBodySpec requestBodySpec =  (WebClient.RequestBodySpec) hueHubClient.get().uri("/lights/" + id.trim());
        return requestBodySpec;
    }

    private WebClient.RequestBodySpec getAllLightsUri() {
        WebClient.RequestBodySpec requestBodySpec =  (WebClient.RequestBodySpec) hueHubClient.get().uri("/lights/");
        return requestBodySpec;
    }

    private WebClient.RequestBodySpec turnLightOnByIdUri(String id) {
        WebClient.RequestBodySpec requestBodySpec = (WebClient.RequestBodySpec) hueHubClient.put().uri("/lights/"+id.trim()+"/state");
        return requestBodySpec;
    }
    private WebClient.RequestBodySpec setLightColorByIdUri(String id) {
        WebClient.RequestBodySpec requestBodySpec = (WebClient.RequestBodySpec) hueHubClient.put().uri("/lights/"+id.trim()+"/state");
        return requestBodySpec;
    }

    private WebClient.RequestBodySpec getAllSensorsUri() {
        WebClient.RequestBodySpec requestBodySpec =  (WebClient.RequestBodySpec) hueHubClient.get().uri("/sensors/");
        return requestBodySpec;
    }

}
