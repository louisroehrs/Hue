package com.teletrex.hue.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
@Service
public class HueDiscoveryService {

    private static final String CONFIG_FILE = "hueconfig.properties";
    private static final String DISCOVERY_URL = "https://discovery.meethue.com/";
    private final RestTemplate restTemplate;
    private final Gson gson;

    @Value("${hue.ip:}")
    private String configuredHueIp;

    @Value("${hue.apikey:}")
    private String configuredApiKey;

    public HueDiscoveryService() {
        this.restTemplate = new RestTemplate();
        this.gson = new Gson();
    }

    @PostConstruct
    public void init() {
        // First try to load from saved configuration file
        try {
            Path configPath = Paths.get(System.getProperty("user.home"), ".hue", CONFIG_FILE);
            if (Files.exists(configPath)) {
                Properties props = new Properties();
                try (InputStream input = Files.newInputStream(configPath)) {
                    props.load(input);
                    String savedIp = props.getProperty("hue.ip");
                    String savedApiKey = props.getProperty("hue.apikey");
                    
                    if (savedIp != null && savedApiKey != null) {
                        this.configuredHueIp = savedIp;
                        this.configuredApiKey = savedApiKey;
                        log.info("Loaded configuration from file: IP={}", savedIp);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Failed to load saved configuration", e);
        }

        // Now check if the configuration is valid
        if (isConfigurationValid()) {
            log.info("Using configured Hue bridge at {} with existing API key", configuredHueIp);
            return;
        }

        try {
            discoverAndPairBridge();
        } catch (Exception e) {
            log.error("Failed to discover and pair with Hue bridge", e);
        }
    }

    private boolean isConfigurationValid() {
        if (configuredHueIp == null || configuredHueIp.isEmpty() || 
            configuredApiKey == null || configuredApiKey.isEmpty()) {
            return false;
        }

        try {
            // Test the connection and API key
            String testUrl = String.format("http://%s/api/%s/config", configuredHueIp, configuredApiKey);
            ResponseEntity<String> response = restTemplate.getForEntity(testUrl, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.warn("Failed to validate existing configuration", e);
            return false;
        }
    }

    public void discoverAndPairBridge() throws Exception {
        String bridgeIp = findBridgeIp();
        if (bridgeIp == null) {
            throw new RuntimeException("No Hue bridge found on the network");
        }

        log.info("Found Hue bridge at IP: {}", bridgeIp);
        log.info("Please press the link button on your Hue bridge...");

        String apiKey = null;
        int maxAttempts = 30; // Try for 30 seconds
        for (int i = 0; i < maxAttempts; i++) {
            apiKey = attemptPairing(bridgeIp);
            if (apiKey != null) {
                break;
            }
            Thread.sleep(1000); // Wait 1 second between attempts
        }

        if (apiKey == null) {
            throw new RuntimeException("Failed to pair with Hue bridge after " + maxAttempts + " attempts");
        }

        saveConfiguration(bridgeIp, apiKey);
        log.info("Successfully paired with Hue bridge!");
    }

    private String findBridgeIp() {
        // Try discovery.meethue.com first
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(DISCOVERY_URL, String.class);
            JsonArray bridges = gson.fromJson(response.getBody(), JsonArray.class);
            if (bridges != null && bridges.size() > 0) {
                return bridges.get(0).getAsJsonObject().get("internalipaddress").getAsString();
            }
        } catch (Exception e) {
            log.warn("Failed to discover bridge using meethue.com, trying UPnP...", e);
        }

        // Fall back to UPnP discovery
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);

            String query = "M-SEARCH * HTTP/1.1\r\n" +
                         "HOST: 239.255.255.250:1900\r\n" +
                         "MAN: \"ssdp:discover\"\r\n" +
                         "MX: 2\r\n" +
                         "ST: ssdp:all\r\n\r\n";

            DatagramPacket packet = new DatagramPacket(
                query.getBytes(),
                query.length(),
                InetAddress.getByName("239.255.255.250"),
                1900
            );

            socket.send(packet);

            byte[] buffer = new byte[1024];
            DatagramPacket response = new DatagramPacket(buffer, buffer.length);
            socket.setSoTimeout(2000);

            while (true) {
                try {
                    socket.receive(response);
                    String message = new String(response.getData(), 0, response.getLength());
                    if (message.contains("IpBridge")) {
                        String[] lines = message.split("\r\n");
                        for (String line : lines) {
                            if (line.startsWith("LOCATION: ")) {
                                URL url = new URL(line.substring(10).trim());
                                return url.getHost();
                            }
                        }
                    }
                } catch (SocketTimeoutException e) {
                    break;
                }
            }
        } catch (Exception e) {
            log.error("Failed to discover bridge using UPnP", e);
        }

        return null;
    }

    private String attemptPairing(String bridgeIp) {
        try {
            String pairUrl = String.format("http://%s/api", bridgeIp);
            String body = "{\"devicetype\":\"hue_app#java\"}";
            
            ResponseEntity<String> response = restTemplate.postForEntity(pairUrl, body, String.class);
            JsonArray jsonResponse = gson.fromJson(response.getBody(), JsonArray.class);
            
            if (jsonResponse != null && jsonResponse.size() > 0) {
                JsonObject firstResponse = jsonResponse.get(0).getAsJsonObject();
                if (firstResponse.has("success")) {
                    return firstResponse.getAsJsonObject("success").get("username").getAsString();
                }
            }
        } catch (Exception e) {
            log.debug("Pairing attempt failed", e);
        }
        return null;
    }

    private void saveConfiguration(String bridgeIp, String apiKey) throws IOException {
        Properties props = new Properties();
        props.setProperty("hue.ip", bridgeIp);
        props.setProperty("hue.apikey", apiKey);

        Path configPath = Paths.get(System.getProperty("user.home"), ".hue", CONFIG_FILE);
        Files.createDirectories(configPath.getParent());
        
        try (OutputStream output = Files.newOutputStream(configPath)) {
            props.store(output, "Hue Bridge Configuration");
        }

        // Update the current instance with the new values
        this.configuredHueIp = bridgeIp;
        this.configuredApiKey = apiKey;
        log.info("Updated configuration with IP: {} and API key: {}", bridgeIp, apiKey);
    }

    public String getHueIp() {
        return configuredHueIp;
    }

    public String getApiKey() {
        return configuredApiKey;
    }
} 