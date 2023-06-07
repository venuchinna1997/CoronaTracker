package io.javabrains.coronatracker.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationStats {

	private String state;
	private String country;
	private int latestTotalCases;
	private int diffFromPreviousDay;

}
