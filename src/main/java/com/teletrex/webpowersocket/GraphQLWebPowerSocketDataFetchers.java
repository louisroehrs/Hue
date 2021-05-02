package com.teletrex.webpowersocket;

import com.teletrex.webpowersocket.model.Outlet;
import graphql.schema.DataFetcher;
import io.netty.handler.logging.LogLevel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@Component
public class GraphQLWebPowerSocketDataFetchers {

    private static String chairIP = "192.168.1.200";
    private static String username = "admin";
    private static String password = "1234";
    private static String protocolIp = "http://" + chairIP;

    private static WebClient powerSocketClient;

    static {
        try {
            HttpClient httpClient = HttpClient
                    .create()
                    .wiretap("reactor.netty.http.client.HttpClient",
                            LogLevel.INFO, AdvancedByteBufFormat.TEXTUAL);
            powerSocketClient = WebClient
                    .builder()
// debugging                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .baseUrl(protocolIp)
                    .defaultCookie("cookieKey", "cookieValue")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .defaultHeader("X-CSRF", "true")
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic "+ Base64.getEncoder().encodeToString((username +":" + password).getBytes("UTF-8")))
                    .defaultUriVariables(Collections.singletonMap("url", protocolIp))
                    .build();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public DataFetcher getAllOutlets () {
        return dataFetchingEnvironment -> {
            return getOutlets();
        };
    }

    public List<Outlet> getOutlets () {
        AtomicInteger index = new AtomicInteger(0);

        List<Outlet> outlets = (List<Outlet>) getOutletsUrl()
                .retrieve()
                .bodyToFlux(Outlet.class)
                .collectList()
                .block();

        return outlets;
    }

    private static WebClient.RequestBodySpec getOutletsUrl () {
        WebClient.RequestBodySpec requestBodySpec =
                (WebClient.RequestBodySpec) powerSocketClient
                .get()
                .uri("/restapi/relay/outlets/");
        return requestBodySpec;
    }

    private WebClient.RequestBodySpec turnSocketOnByIdUri(String id) {
        WebClient.RequestBodySpec requestBodySpec =
                (WebClient.RequestBodySpec) powerSocketClient
                        .put()
                        .uri("/restapi/relay/outlets/"+id.trim()+"/state/");
        return requestBodySpec;
    }

}
