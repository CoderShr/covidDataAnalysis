package com.mindtree.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindtree.entity.CovidData;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public abstract class AbstractTest {
   protected MockMvc mvc;
   @Autowired
   WebApplicationContext webApplicationContext;

   protected void setUp() throws ParseException {
      mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
   }

	public <T> T mapFromJson(String json, Class<T> data) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
	  return objectMapper.readValue(json, data);
	}
	
	public static List<CovidData> getTestCovidData() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");
  		List<CovidData> covidDataExpected = new ArrayList<>();
  		covidDataExpected.add(new CovidData(format.parse("2020-08-06"), "UP", "AMBEDKAR NAGAR", "0", "24", "14", 837));
  		covidDataExpected.add(new CovidData(format.parse("2020-08-06"), "TN", "Chennai", "2150", "664", "370", 838));
  		covidDataExpected.add(new CovidData(format.parse("2020-08-06"), "UP", "Mau", "28", "29", "7", 839));
  		covidDataExpected.add(new CovidData(format.parse("2021-08-06"), "UP", "Sultanpur", "209", "58", "20", 840));
  		covidDataExpected.add(new CovidData(format.parse("2021-03-06"), "KL", "Thrissur", "0", "24", "14", 837));
  		covidDataExpected.add(new CovidData(format.parse("2020-08-06"), "KL", "Palakkad", "0", "24", "14", 842));
  		covidDataExpected.add(new CovidData(format.parse("2020-08-06"), "KL", "Malappuram", "0", "24", "14", 843));
  		covidDataExpected.add(new CovidData(format.parse("2020-08-06"), "KL", "Kozhikode", "0", "24", "14", 844));
  		covidDataExpected.add(new CovidData(format.parse("2021-08-06"), "KL", "Ernakulam", "0", "24", "14", 845));
  		covidDataExpected.add(new CovidData(format.parse("2021-08-06"), "KL", "Kannur", "0", "24", "14", 846));
  		covidDataExpected.add(new CovidData(format.parse("2020-08-06"), "KL", "Kottayam", "0", "24", "14", 848));
  		covidDataExpected.add(new CovidData(format.parse("2021-08-06"), "KL", "Kollam", "0", "24", "14", 847));
  		covidDataExpected.add(new CovidData(format.parse("2021-08-06"), "UP", "Pilibhit" , "1300", "54", "76", 841));
		return covidDataExpected;
	}

}
