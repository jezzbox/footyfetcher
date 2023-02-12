package com.jezzbox.footyfetcher.controllers;

import com.azure.security.keyvault.secrets.SecretClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jezzbox.footyfetcher.FootyfetcherApplication;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
public class FootyController {
    private final SecretClient secretClient;

    public FootyController(SecretClient secretClient) {
        this.secretClient = secretClient;
    }

    private static final Logger log = LoggerFactory.getLogger(FootyfetcherApplication.class);
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private WebClient.Builder webClientBuilder;

    private static String nowAsString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddhhmmss").withZone(ZoneId.of("UTC"));
        Instant now = Instant.now();
        String nowFormatted = formatter.format(now);
        return nowFormatted;
    }

    private void uploadBlob(String jsonString, String endpoint) {
        Resource storageBlobResource = resourceLoader.getResource("azure-blob://apifootball" + endpoint + "/" + nowAsString() + ".json");
        try (OutputStream blobos = ((WritableResource) storageBlobResource).getOutputStream()) {
            blobos.write(jsonString.getBytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping("/*")
    @ResponseBody
    public String get(HttpServletRequest request) throws JsonProcessingException {
        String requestUri = request.getRequestURI();
        StringBuilder endpoint = new StringBuilder(requestUri);
        String queryString = request.getQueryString();

        if (queryString != null) {
            endpoint.append("?");
            endpoint.append(queryString);
        }
        String response = webClientBuilder.build().get().uri(endpoint.toString()).retrieve().bodyToMono(String.class).block();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseMap = mapper.readValue(response, Map.class);
        if (responseMap.containsKey("errors") && responseMap.get("errors") instanceof List) {
            List<Map<String, String>> errors = (List<Map<String, String>>) responseMap.get("errors");
            if (errors.size() == 0) {
                uploadBlob(response, requestUri);
                return "success";
            }
            else {
                return errors.toString();
            }
        }
        else if (responseMap.containsKey("errors") && responseMap.get("errors") instanceof Map) {
            Map<String, String> errors = (Map<String, String>) responseMap.get("errors");
            if (errors.size() == 0) {
                uploadBlob(response, requestUri);
                return "success";
            }
            else {
                return errors.toString();
            }
        }
        return endpoint.toString();
    }
}
