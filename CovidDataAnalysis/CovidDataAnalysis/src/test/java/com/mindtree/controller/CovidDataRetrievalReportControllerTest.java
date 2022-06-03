package com.mindtree.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.mindtree.service.CovidAnalysisService;
import com.mindtree.service.ScannerAsker;

@WebMvcTest
public class CovidDataRetrievalReportControllerTest extends AbstractTest{

	  	@MockBean
	    private CovidAnalysisService covidDataService;
	  	
	  	@MockBean
	    private ScannerAsker asker;
	  	
	  	@Autowired
	    private MockMvc mockMvc;

	    @Test
	    @DisplayName("Should List All data for covid report When making GET request to endpoint - /")
	    void shouldCreateGet() throws Exception {
			Mockito.when(covidDataService.getCovidDataReport()).thenReturn(getTestCovidData());
			 String uri = "/api/data";
			   MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
					      .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
			   
			   int status = mvcResult.getResponse().getStatus();
			   assertEquals(200, status);
			   
			   String content = mvcResult.getResponse().getContentAsString();
			   assertNotNull(content);
	    }
	  	
	   
}
