package com.mindtree.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.text.ParseException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.mindtree.controller.AbstractTest;
import com.mindtree.entity.CovidData;

@DataJpaTest
@ActiveProfiles("test")
public class CovidDataRepositoryTest extends AbstractTest {

	  	@Autowired
	    private CovidDataRepository covidDataRepository;
	  	
	  	List<CovidData> covidData;
	  	
	    @Test
	    public void shouldGetData() throws ParseException {
	    	covidData = getTestCovidData();
	        covidDataRepository.saveAll(covidData);
	        List<CovidData> data = covidDataRepository.findAll();
	        assertNotNull(data);
	        assertEquals("KL", data.get(6).getState());
	        assertEquals("24", data.get(6).getConfirmed());
	        assertEquals("Malappuram", data.get(6).getDistrict());
	        assertEquals("0", data.get(6).getTested());
	        
	    }
}
