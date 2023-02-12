package com.jezzbox.footyfetcher;

import com.azure.security.keyvault.secrets.SecretClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.web.reactive.function.client.WebClient;

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
	public WebClient.Builder webClientBuilder() {
		return WebClient.builder()
				.baseUrl("https://v3.football.api-sports.io")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader("x-apisports-key", this.secretClient.getSecret("api-football-key").getValue())
				.defaultHeader("x-rapidapi-host", "https://v3.football.api-sports.io");
	}
}
