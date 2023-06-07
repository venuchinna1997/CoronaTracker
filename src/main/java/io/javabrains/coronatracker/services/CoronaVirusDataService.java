package io.javabrains.coronatracker.services;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import io.javabrains.coronatracker.models.LocationStats;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CoronaVirusDataService {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	WebClient webClient;

	@Value("${VIRUS_DATA_URL}")
	String data_url;

	@PostConstruct
	@Scheduled(cron="* * 1 * * *")
	public List<LocationStats> fetchVirusData() throws IOException, InterruptedException {
		
		List<LocationStats> newStats = new ArrayList<>();
		
//    	HttpClient client = HttpClient.newHttpClient();
//
//		HttpRequest request = HttpRequest.newBuilder()
//				.uri(URI.create(data_url))
//				.build();
//
//		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		//String response = restTemplate.getForObject(data_url,String.class);
		String response = webClient.get().uri(data_url).retrieve().toEntity(String.class).block().getBody();

		StringReader csvBodyReader = null;
		if(!ObjectUtils.isEmpty(response)) {
			csvBodyReader = new StringReader(response);
		}
		Iterable<CSVRecord> records = null;
		if(!ObjectUtils.isEmpty(csvBodyReader)) {
			records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
		}
		if(!ObjectUtils.isEmpty(records)) {
			for (CSVRecord record : records) {
				LocationStats locationStat = new LocationStats();
				locationStat.setState(record.get("Province/State"));
				locationStat.setCountry(record.get("Country/Region"));
				int latestCases = Integer.parseInt(record.get(record.size() - 1));
				int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
				locationStat.setLatestTotalCases(latestCases);
				locationStat.setDiffFromPreviousDay(latestCases - prevDayCases);

				newStats.add(locationStat);

			}
		}
			return newStats;
		}

}
