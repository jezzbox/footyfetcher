package com.jezzbox.footyfetcher;

import com.azure.security.keyvault.secrets.SecretClient;
import com.jezzbox.footyfetcher.models.Endpoint;
import com.jezzbox.footyfetcher.models.Fixture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class FootyfetcherApplication {

	private final SecretClient secretClient;

	public FootyfetcherApplication(SecretClient secretClient) {
		this.secretClient = secretClient;
	}

	private static final Logger log = LoggerFactory.getLogger(FootyfetcherApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(FootyfetcherApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Value("azure-blob://apifootball/test123.json")
	private Resource storageBlobResource;

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-apisports-key", secretClient.getSecret("api-football-key").getValue());
		headers.set("x-rapidapi-host", "https://v3.football.api-sports.io");
		headers.set("Accept", "application/json");

		return args -> {
//			HttpEntity<String> entity = new HttpEntity<>("", headers);
//			ResponseEntity<Endpoint> endpoint = restTemplate.exchange(
//					"https://v3.football.api-sports.io/fixtures?live=all", HttpMethod.GET, entity, Endpoint.class);
//			Endpoint test = endpoint.getBody();
//
//			try (OutputStream blobos = ((WritableResource) this.storageBlobResource).getOutputStream()) {
//				blobos.write(data.getBytes());
//			}
//			log.info(test.response().toString());
		};
		}
}
