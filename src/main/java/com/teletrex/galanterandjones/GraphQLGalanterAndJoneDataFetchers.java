package com.teletrex.galanterandjones;

import com.teletrex.galanterandjones.model.Chair;
import com.teletrex.hue.model.Light;
import com.teletrex.hue.model.Sensor;
import graphql.schema.DataFetcher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@Component
public class GraphQLGalanterAndJoneDataFetchers {

    private static String chairIP = "192.168.1.200";
    private static String username = "admin";
    private static String password = "1234";
    private static String baseUrl = "http://" + chairIP + "/script.cgi";
    private static WebClient hueHubClient;

    private static final HashMap<String,String> scriptName = new HashMap<>();
    static {
        scriptName.put("on1", "requestChairOne");
        scriptName.put("on2", "requestChairTwo");
        scriptName.put("on3", "requestChairThree");
        scriptName.put("off1","chairOffOne");
        scriptName.put("off2","chairOffTwo");
        scriptName.put("off3","chairOffThree");
        scriptName.put("alloff", "allChairsOff");
        scriptName.put("chairsOn","start_scripts");
    }

    static {
        try {
            hueHubClient = WebClient
                    .builder()
                    .baseUrl(baseUrl)
                    .defaultCookie("cookieKey", "cookieValue")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic "+ Base64.getEncoder().encodeToString((username +":" + password).getBytes("UTF-8")))
                    .defaultUriVariables(Collections.singletonMap("url", baseUrl))
                    .build();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static WebClient.RequestBodySpec getLightsUri =
            (WebClient.RequestBodySpec) hueHubClient.get().uri("/lights");

    private Boolean[] chairRequests = new Boolean[] { false, false, false};

    public List<Chair> getChairs() {
        AtomicInteger index = new AtomicInteger(0);
        List<Chair> chairs = Arrays.stream(chairRequests).map(chairRequest ->
        {
            Chair chair = new Chair();
            chair.setId(index.intValue()+1);
            chair.setPoweredOn(true);  // get actual value via api.  rewrite to get from api and then map.
            chair.setRequested(chairRequests[index.getAndIncrement()]);
            return chair;
        }).collect(toList());
        return chairs;
    }

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

    public DataFetcher requestChair() {
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument("chair");
            Boolean on = dataFetchingEnvironment.getArgument("on");
            Map<String, Object> body = new HashMap<>();
            body.put("user_function",on);
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
