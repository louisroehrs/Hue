package com.teletrex.hue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.teletrex.hue.model.Light;
import com.teletrex.hue.model.Lights;
import graphql.schema.DataFetcher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import javax.xml.crypto.Data;
import java.util.*;

@Component
public class GraphQLDataFetchers {

    private static String baseUrl = "http://192.168.1.77/api/DMN24zKLkv4uClyfk3smBMB1VyB8BeVpxq1YOndO";
    private static WebClient hueHubClient =  WebClient
            .builder()
            .baseUrl(baseUrl)
            .defaultCookie("cookieKey", "cookieValue")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultUriVariables(Collections.singletonMap("url", baseUrl))
            .build();

    private static WebClient.RequestBodySpec getLightsUri =
            (WebClient.RequestBodySpec) hueHubClient.get().uri("/lights");



    private static List<Map<String, String>> books = Arrays.asList(
            ImmutableMap.of("id", "book-1",
                    "name", "Harry Potter and the Philosopher's Stone",
                    "pageCount", "223",
                    "authorId", "author-1"),
            ImmutableMap.of("id", "book-2",
                    "name", "Moby Dick",
                    "pageCount", "635",
                    "authorId", "author-2"),
            ImmutableMap.of("id", "book-3",
                    "name", "Interview with the vampire",
                    "pageCount", "371",
                    "authorId", "author-3")
    );

    private static List<Map<String, String>> authors = Arrays.asList(
            ImmutableMap.of("id", "author-1",
                    "firstName", "Joanne",
                    "lastName", "Rowling"),
            ImmutableMap.of("id", "author-2",
                    "firstName", "Herman",
                    "lastName", "Melville"),
            ImmutableMap.of("id", "author-3",
                    "firstName", "Anne",
                    "lastName", "Rice")
    );


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


}
