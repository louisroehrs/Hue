package com.teletrex.galanterandjones;

import com.teletrex.galanterandjones.model.Chair;
import com.teletrex.webpowersocket.model.Outlet;
import graphql.schema.DataFetcher;
import io.netty.handler.logging.LogLevel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;

@Component
public class GraphQLGalanterAndJonesDataFetchers {

    private static String chairIP = "192.168.1.200";
    private static String username = "admin";
    private static String password = "1234";
    private static String protocolIp = "http://" + chairIP;

    private static WebClient powerSocketClient;

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

    public DataFetcher getChairs() {
        return dataFetchingEnvironment -> {
            return getChairsFromWebSocket();
        };
    }

    private List<Chair> getChairsFromWebSocket () {
        AtomicInteger index = new AtomicInteger(0);
        /* get requested chairs and socket status for chairs */
        ParameterizedTypeReference<Map<String, Boolean>> variables = new ParameterizedTypeReference<Map<String,Boolean>>() {};
        Map<String, Boolean> requestedChairs = (Map<String, Boolean>) getRequestedChairsUrl()
                .retrieve()
                .bodyToMono(variables)
                .block();

        List<Outlet> myOutlets = new ArrayList<Outlet>() {};

        List<Outlet> outlets = (List<Outlet>) getOutletsUrl()
                .retrieve()
                .bodyToFlux(Outlet.class)
                .collectList()
                .block();

        List<Chair> chairs = Arrays.stream(new int[]{1, 2, 3}).mapToObj(chairId ->
        {
            Outlet outletForChair = outlets.get(chairId-1);
            Chair chair = new Chair();
            chair.setId(chairId);
            chair.setName(outletForChair.getName());
            chair.setPoweredOn(outletForChair.getPhysical_state());
            chair.setRequested(requestedChairs.get("chairRequest" + chairId));
            return chair;
        }).collect(toList());
        return chairs;
    }

    public DataFetcher requestChairOn() {
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument("id");
            Boolean on = dataFetchingEnvironment.getArgument("on");

            MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
            formData.add("value#", on?"true":"false");

            String string = (String) requestChairByIdUri(id)
                    .bodyValue(formData)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return getChairsFromWebSocket();
        };
    }


    private static WebClient.RequestBodySpec getOutletsUrl () {
        WebClient.RequestBodySpec requestBodySpec =
                (WebClient.RequestBodySpec) powerSocketClient
                .get()
                .uri("/restapi/relay/outlets/");
        return requestBodySpec;
    }

    private WebClient.RequestBodySpec getRequestedChairsUrl() {
        WebClient.RequestBodySpec requestBodySpec =
                (WebClient.RequestBodySpec) powerSocketClient
                .get().uri("/restapi/script/variables/");
        return requestBodySpec;
    }

    private WebClient.RequestBodySpec turnSocketOnByIdUri(String id) {
        WebClient.RequestBodySpec requestBodySpec =
                (WebClient.RequestBodySpec) powerSocketClient
                        .put()
                        .uri("/restapi/relay/outlets/"+id.trim()+"/state/");
        return requestBodySpec;
    }

    private WebClient.RequestBodySpec requestChairByIdUri(String id) {
        WebClient.RequestBodySpec requestBodySpec =
                (WebClient.RequestBodySpec) powerSocketClient
                        .put()
                        .uri("/restapi/script/variables/chairRequest" + id.trim() + "/");
        return requestBodySpec;
    }
}
