package io.javabrains.coronatracker.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.javabrains.coronatracker.models.LocationStats;
import io.javabrains.coronatracker.services.CoronaVirusDataService;

@Controller
public class HomeController {
		
	@Autowired
	CoronaVirusDataService coronaVirusDataService;
	
	@GetMapping("/")
	public String home(Model model) {

		List<LocationStats> allStats = null;
		try {
			allStats = coronaVirusDataService.fetchVirusData();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
		int totalNewCases = allStats.stream().mapToInt(stat -> stat.getDiffFromPreviousDay()).sum();
		model.addAttribute("locationStats", allStats);
		model.addAttribute("totalReportedCases", totalReportedCases);
		model.addAttribute("totalNewCases", totalNewCases);
		return "home";
	}

}
