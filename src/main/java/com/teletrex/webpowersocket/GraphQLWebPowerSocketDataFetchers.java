package com.teletrex.webpowersocket;

import com.teletrex.webpowersocket.model.Outlet;
import graphql.schema.DataFetcher;
import org.springframework.http.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;

@Component
public class GraphQLWebPowerSocketDataFetchers {

    private static String outletIp = "192.168.1.200";
    private static String username = "admin";
    private static String password = "1234";
    private static String protocolIp = "http://" + outletIp;

    private final RestTemplate restTemplate;
    private final HttpHeaders headers;

    public GraphQLWebPowerSocketDataFetchers() {
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("X-CSRF", "true");
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        headers.add(HttpHeaders.AUTHORIZATION, "Basic " + new String(encodedAuth));
    }

    public DataFetcher getAllOutlets() {
        return dataFetchingEnvironment -> getOutlets();
    }

    public List<Outlet> getOutlets() {
        ResponseEntity<List<Outlet>> response = restTemplate.exchange(
            protocolIp + "/restapi/relay/outlets/",
            HttpMethod.GET,
            new HttpEntity<>(headers),
            new ParameterizedTypeReference<List<Outlet>>() {}
        );
        return response.getBody();
    }

    public DataFetcher switchOutlet() {
        System.out.println(outletIp);
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument("id");
            Boolean on = dataFetchingEnvironment.getArgument("on");

            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("value", on ? "true" : "false");

            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.addAll(headers);
            requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, requestHeaders);
            
            restTemplate.exchange(
                protocolIp + "/restapi/relay/outlets/" + id.trim() + "/state/",
                HttpMethod.PUT,
                request,
                String.class
            );

            return getOutlets();
        };
    }
}
