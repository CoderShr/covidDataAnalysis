package com.mindtree.controller;

import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mindtree.entity.CovidData;
import com.mindtree.service.CovidAnalysisService;
import com.mindtree.service.ScannerAsker;

@RestController
@RequestMapping("/api")
public class CovidDataRetrievalReportController {

private static Logger log = LogManager.getLogger(CovidDataRetrievalReportController.class);
	
	@Autowired
	public CovidAnalysisService covidAnalysisService;
	
	@Autowired
	public ScannerAsker asker;
	
	CovidDataRetrievalReportController(CovidAnalysisService covidAnalysisService, ScannerAsker asker){
		this.covidAnalysisService = covidAnalysisService;
		this.asker = asker;
	}

	@GetMapping("/data")
    	public ResponseEntity<List<CovidData>> searchAllProducts() {
	    log.info("Request to choose option");
	    List<CovidData> data = null;
			try {
				data = covidAnalysisService.getCovidDataReport();
				
//			} catch (ProductNotFoundException e) {
//				new ProductNotFoundException("Product not found");
//			} catch (Exception e) {
//				log.error("Some exception occured while fetching for producs");
//			}
				log.info("Option select request served");
			
			}catch (Exception e) {
				log.error("Some exception occured while fetching for producs");
			}
				return new ResponseEntity<List<CovidData>>(data , HttpStatus.OK);
			
	}
	
//	private String getChoice() {
////		System.out.println("1. Get States Name.\r\n" + 
////				"2. Get District name for given states\r\n" + 
////				"3. Display Data by state with in date range\r\n" + 
////				"4. Display Confirmed cases by comparing two states for a given date range.\r\n" + 
////				"5. Exit");
////		System.out.println("please select option : ");
////		Scanner sc = new Scanner(System.in);
//		String input = asker.ask("1. Get States Name.\r\n" + 
//				"2. Get District name for given states\r\n" + 
//				"3. Display Data by state with in date range\r\n" + 
//				"4. Display Confirmed cases by comparing two states for a given date range.\r\n" + 
//				"5. Exit \r\n please select option : ");
//		return input;
//	}
}
