package com.teletrex.galanterandjones;

import com.teletrex.galanterandjones.model.Chair;
import com.teletrex.webpowersocket.model.Outlet;
import graphql.schema.DataFetcher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.Arrays;

import static java.util.stream.Collectors.toList;

@Component
public class GraphQLGalanterAndJonesDataFetchers {

    private static String chairIP = "192.168.1.200";
    private static String username = "admin";
    private static String password = "1234";
    private static String protocolIp = "http://" + chairIP;

    private final RestTemplate restTemplate;
    private final HttpHeaders headers;

    private static final HashMap<String,String> scriptName = new HashMap<>();
    static {
        scriptName.put("on1", "requestChairOne");
        scriptName.put("on2", "requestChairTwo");
        scriptName.put("on3", "requestChairThree");
        scriptName.put("off1", "chairOffOne");
        scriptName.put("off2", "chairOffTwo");
        scriptName.put("off3", "chairOffThree");
        scriptName.put("alloff", "allChairsOff");
        scriptName.put("chairsOn", "start_scripts");
    }

    public GraphQLGalanterAndJonesDataFetchers() {
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("X-CSRF", "true");
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        headers.add(HttpHeaders.AUTHORIZATION, "Basic " + new String(encodedAuth));
    }

    public DataFetcher getChairs() {
        return dataFetchingEnvironment -> getChairsFromWebSocket();
    }

    private List<Chair> getChairsFromWebSocket() {
        // Get requested chairs status
        ResponseEntity<Map<String, Boolean>> requestedChairsResponse = restTemplate.exchange(
                protocolIp + "/restapi/script/variables/",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<Map<String, Boolean>>() {}
        );
        Map<String, Boolean> requestedChairs = requestedChairsResponse.getBody();

        // Get outlets status
        ResponseEntity<List<Outlet>> outletsResponse = restTemplate.exchange(
                protocolIp + "/restapi/relay/outlets/",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<Outlet>>() {}
        );
        List<Outlet> outlets = outletsResponse.getBody();

        return Arrays.stream(new int[]{1, 2, 3}).mapToObj(chairId -> {
            Outlet outletForChair = outlets.get(chairId-1);
            Chair chair = new Chair();
            chair.setId(chairId);
            chair.setName(outletForChair.getName());
            chair.setPoweredOn(outletForChair.getPhysical_state());
            chair.setRequested(requestedChairs.get("chairRequest" + chairId));
            return chair;
        }).collect(toList());
    }

    public DataFetcher requestChairOn() {
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument("id");
            Boolean on = dataFetchingEnvironment.getArgument("on");

            MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
            formData.add("value#", on ? "true" : "false");

            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(formData, headers);
            
            restTemplate.exchange(
                protocolIp + "/restapi/script/variables/chairRequest" + id.trim() + "/",
                HttpMethod.PUT,
                request,
                String.class
            );

            return getChairsFromWebSocket();
        };
    }

    private HttpEntity<?> createHttpEntity() {
        return new HttpEntity<>(headers);
    }

    private HttpEntity<MultiValueMap<String, Object>> createHttpEntity(MultiValueMap<String, Object> body) {
        return new HttpEntity<>(body, headers);
    }
}
