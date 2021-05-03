package com.teletrex.webpowersocket;

import com.teletrex.webpowersocket.model.Outlet;
import graphql.schema.DataFetcher;
import io.netty.handler.logging.LogLevel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class GraphQLWebPowerSocketDataFetchers {

    private static String outletIp = "192.168.1.200";
    private static String username = "admin";
    private static String password = "1234";
    private static String protocolIp = "http://" + outletIp;

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

    public DataFetcher switchOutlet() {
        System.out.println(outletIp);
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument("id");
            Boolean on = dataFetchingEnvironment.getArgument("on");
            String string = (String) turnOutletOnByIdUri(id)
                    .bodyValue("value="+ (on ? "true":"false"))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return getOutlets();
        };
    }

    private static WebClient.RequestBodySpec getOutletsUrl () {
        WebClient.RequestBodySpec requestBodySpec =
                (WebClient.RequestBodySpec) powerSocketClient
                .get()
                .uri("/restapi/relay/outlets/");
        return requestBodySpec;
    }

    private WebClient.RequestBodySpec turnOutletOnByIdUri(String id) {
        WebClient.RequestBodySpec requestBodySpec =
                (WebClient.RequestBodySpec) powerSocketClient
                        .put()
                        .uri("/restapi/relay/outlets/"+id.trim()+"/state/");
        return requestBodySpec;
    }

}
