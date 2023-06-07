package io.javabrains.coronatracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableScheduling
public class CoronaTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoronaTrackerApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

	@Bean
	public WebClient getWebClient ()
	{
		return WebClient.builder ()
				.baseUrl ("http://localhost:8081")
				.defaultHeader (HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(5000 * 1024))
				.build ();
	}


}
