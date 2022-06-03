package com.mindtree.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mindtree.controller.AbstractTest;
import com.mindtree.dao.CovidDataRepository;
import com.mindtree.entity.CovidData;
import com.mindtree.exception.InvalidDateException;
import com.mindtree.exception.InvalidDateRangeException;
import com.mindtree.exception.InvalidStateCodeException;
import com.mindtree.exception.NoDataFoundException;
import com.mindtree.validator.DateFormatValidator;
import com.mindtree.validator.DateValidator;

@ExtendWith(MockitoExtension.class)
public class CovidAnalysisServiceTest extends AbstractTest{
	
	@InjectMocks 
	private CovidAnalysisServiceImpl covidService;
	
	@Mock
	private CovidDataRepository covidDataRepository;
	
	ScannerAsker asker = mock(ScannerAsker.class);

	@Captor ArgumentCaptor<String> arg;
	
	@BeforeEach
	@Override
	public void setUp() throws ParseException {
		
		this.covidService = new CovidAnalysisServiceImpl(covidDataRepository, asker);
	}

	
	// get state name method test
	
	@Test
	@DisplayName("Get states")
	public void testGetStates() throws NoDataFoundException, ParseException, Exception {
	    Set<String> stateNames = covidService.getStateNames(getTestCovidData(), "readOnly");
	    assertNotNull(stateNames);
	    assertEquals(3, stateNames.size());
	    assertTrue(stateNames.contains("KL"));
	    assertFalse(stateNames.isEmpty());
	}
	
	@Test
	@DisplayName("Get states for validation with no covid data, throws excpetion NoDataFoundException")
	public void testGetStatesWithNoDataException() throws NoDataFoundException, ParseException, Exception  {
		Assertions.assertThrows(NoDataFoundException.class, () -> covidService.getStateNames(null, "validation"));
	    Assertions.assertThrows(NoDataFoundException.class, () -> covidService.getStateNames(new ArrayList<CovidData>(), "validation"));
	}
	
	
  	@Test
    @DisplayName("Test Should give state names for console print")
    void shouldGetStates() throws NoDataFoundException, Exception {
  		Set<String> stateNames = new HashSet<>();
  		stateNames.add("UP");
  		stateNames.add("TN");
  		stateNames.add("KL");
        assertEquals(stateNames, covidService.getStateNames(getTestCovidData(), "readOnly"));
    }
  	
  	@Test
    @DisplayName("Get states for console print, throw exception as data not found")
    void shouldGetStatesDataNotFound(){
  		NoDataFoundException thrown = Assertions.assertThrows(NoDataFoundException.class, () -> 
  							{ covidService.getStateNames(null, "readOnly");
  		}, "No data present from covid data DB");
  		
  		Assertions.assertEquals("No data present from covid data DB", thrown.getMessage());
    }
  	
  	// get district name test method
  	
	
  	@Test
    @DisplayName("Test Should give district names and pass")
    void shouldGetDistrict() throws ParseException {
  		Set<String> districtNames = new HashSet<>();
  		districtNames.add("Malappuram");
  		districtNames.add("Palakkad");
  		districtNames.add("Thrissur");
  		districtNames.add("Kozhikode");
  		districtNames.add("Kannur");
  		districtNames.add("Kollam");
  		districtNames.add("Ernakulam");
  		districtNames.add("Kottayam");
        assertEquals(districtNames, covidService.populateDistrictName("KL", getTestCovidData()));
    }
  	
	@Test
    @DisplayName("Get state district names")
	public void testGetDistrictsForStateCode() throws NoDataFoundException, ParseException, Exception  {
		Mockito.when(asker.ask("please enter state code : ")).thenReturn("KL");
		Set<String> districtNames = covidService.getDistrictsForStateCodeChoosenMethod(getTestCovidData());
		Mockito.verify(asker, Mockito.times(1)).ask(Mockito.anyString());
		assertNotNull(districtNames);
	    assertEquals(8, districtNames.size());
	    assertTrue(districtNames.contains("Thrissur"));
	    assertFalse(districtNames.isEmpty());
	    }
	
  	@Test
    @DisplayName("Get state code throw exception for invalid state code")
    void shouldGetStatesCode() throws NoDataFoundException, ParseException, Exception{
  		Mockito.doReturn("FG").when(asker).ask("please enter state code : ");
  		InvalidStateCodeException thrown = Assertions.assertThrows(InvalidStateCodeException.class, () -> 
  							{ covidService.getStateCode(getTestCovidData(), "please enter state code : ");
  		}, "Invalid state code, please check your input");
  		Assertions.assertEquals("Invalid state code, please check your input", thrown.getMessage());
    }
	
	
	@Test
    @DisplayName("Get district name throw exception for invalid state code given")
	public void testGetDistrictsForInvalidStateCodeException() throws NoDataFoundException, ParseException, Exception  {
		Mockito.when(asker.ask("please enter state code : ")).thenReturn("SE");
		InvalidStateCodeException thrown = Assertions.assertThrows(InvalidStateCodeException.class, () -> 
		{ covidService.getDistrictsForStateCodeChoosenMethod(getTestCovidData());
		}, "Invalid state code, please check your input");
		Assertions.assertEquals("Invalid state code, please check your input", thrown.getMessage());
	}
	
	
	// Get data method test for dates given
	
	@Test
    @DisplayName("Get data for given date range")
	public void testGetDataForDateRange() throws NoDataFoundException, ParseException, Exception  {
		
	}
	
	@Test
    @DisplayName("Get data for given date throw exception for invalid start date given")
	public void testGetDataForDatesInvalidStartDate() throws NoDataFoundException, ParseException, Exception  {
		Mockito.when(asker.ask("please enter start date (yyyy-mm-dd) : ")).thenReturn("2022-08-05");
		Mockito.when(asker.ask("please enter end date (yyyy-mm-dd) : ")).thenReturn("2021-07-12");
		InvalidDateException thrown = Assertions.assertThrows(InvalidDateException.class, () -> 
		{ covidService.getDataUponGivenDateMethod(getTestCovidData());
		}, "Invalid start date, please check your input");
		Mockito.verify(asker, Mockito.times(1)).ask(Mockito.anyString());
		Assertions.assertEquals("Invalid start date, please check your input", thrown.getMessage());
		Mockito.when(asker.ask("please enter start date (yyyy-mm-dd) : ")).thenReturn("2020-81-05");
		Mockito.when(asker.ask("please enter end date (yyyy-mm-dd) : ")).thenReturn("2021-07-12");
		InvalidDateException throwne = Assertions.assertThrows(InvalidDateException.class, () -> 
		{ covidService.getDataUponGivenDateMethod(getTestCovidData());
		}, "Invalid start date, please check your input");
//		Mockito.verify(asker, Mockito.times(1)).ask(Mockito.anyString());
		Assertions.assertEquals("Invalid start date, please check your input", throwne.getMessage());
	}
	
	@Test
	@DisplayName("Get data for given date throw exception for invalid end date given")
	public void testGetDataForDatesInvalidEndDate() throws NoDataFoundException, ParseException, Exception  {
		Mockito.when(asker.ask("please enter start date (yyyy-mm-dd) : ")).thenReturn("2020-08-05");
		Mockito.when(asker.ask("please enter end date (yyyy-mm-dd) : ")).thenReturn("2023-07-12");		
		InvalidDateException thrown = Assertions.assertThrows(InvalidDateException.class, () -> 
		{ covidService.getDataUponGivenDateMethod(getTestCovidData());
		}, "Invalid end date, please check your input");
		Mockito.verify(asker, Mockito.times(2)).ask(Mockito.anyString());
		Assertions.assertEquals("Invalid end date, please check your input", thrown.getMessage());
		Mockito.when(asker.ask("please enter start date (yyyy-mm-dd) : ")).thenReturn("2020-08-05");
		Mockito.when(asker.ask("please enter end date (yyyy-mm-dd) : ")).thenReturn("2023-07-121");		
		InvalidDateException throwe = Assertions.assertThrows(InvalidDateException.class, () -> 
		{ covidService.getDataUponGivenDateMethod(getTestCovidData());
		}, "Invalid end date, please check your input");
		Mockito.verify(asker, Mockito.times(4)).ask(Mockito.anyString());
		Assertions.assertEquals("Invalid end date, please check your input", throwe.getMessage());
	}
	
	@Test
    @DisplayName("Get data for given date throw exception for invalid date range")
	public void testGetDataForDatesForInvalidDateRange() throws NoDataFoundException, ParseException, Exception  {
		Mockito.when(asker.ask("please enter start date (yyyy-mm-dd) : ")).thenReturn("2021-08-05");
		Mockito.when(asker.ask("please enter end date (yyyy-mm-dd) : ")).thenReturn("2019-08-05");
		InvalidDateRangeException thrown = Assertions.assertThrows(InvalidDateRangeException.class, () -> 
		{ covidService.getDataUponGivenDateRangeAndCodesMethod(getTestCovidData());
		}, "Invalid end date, please check your input");
		Mockito.verify(asker, Mockito.times(2)).ask(Mockito.anyString());
		Assertions.assertEquals("Invalid end date, please check your input", thrown.getMessage());
	}
	
	@Test
    @DisplayName("Get data for given date throw exception for no data for given date range")
	public void testGetDataForDatesForNoDataFoundException() throws NoDataFoundException, ParseException, Exception  {
		Mockito.when(asker.ask("please enter start date (yyyy-mm-dd) : ")).thenReturn("2019-08-05");
		Mockito.when(asker.ask("please enter end date (yyyy-mm-dd) : ")).thenReturn("2019-12-12");
		NoDataFoundException thrown = Assertions.assertThrows(NoDataFoundException.class, () -> 
		{ covidService.getDataUponGivenDateMethod(getTestCovidData());
		}, "No data present for given date");
		Mockito.verify(asker, Mockito.times(2)).ask(Mockito.anyString());
		Assertions.assertEquals("No data present for given date", thrown.getMessage());
	}
	
	
	// Get data for date and code given
	
	@Test
	public void testGetDataForDateRangeAndCode() throws NoDataFoundException, ParseException, Exception  {
		
	}
	
	@Test
	public void testGetDataForDateRangeAndCodeForInvalidStartDate() throws NoDataFoundException, ParseException, Exception  {
		Mockito.when(asker.ask("please enter start date (yyyy-mm-dd) : ")).thenReturn("20190-908-05");
		Mockito.when(asker.ask("please enter end date (yyyy-mm-dd) : ")).thenReturn("2019-12-12");
		Mockito.when(asker.ask("please enter first state code : ")).thenReturn("KL");
		Mockito.when(asker.ask("please enter second state code : ")).thenReturn("MH");
		InvalidDateException thrown = Assertions.assertThrows(InvalidDateException.class, () -> 
		{ covidService.getDataUponGivenDateRangeAndCodesMethod(getTestCovidData());
		}, "Invalid start date, please check your input");
		Mockito.verify(asker, Mockito.times(1)).ask(Mockito.anyString());
		Assertions.assertEquals("Invalid start date, please check your input", thrown.getMessage());
	}	
	
	@Test
	public void testGetDataForDateRangeAndCodeForInvalidEndDate() throws NoDataFoundException, ParseException, Exception  {
		Mockito.when(asker.ask("please enter start date (yyyy-mm-dd) : ")).thenReturn("2019-08-05");
		Mockito.when(asker.ask("please enter end date (yyyy-mm-dd) : ")).thenReturn("20190-908-05");
		Mockito.when(asker.ask("please enter first state code : ")).thenReturn("KL");
		Mockito.when(asker.ask("please enter second state code : ")).thenReturn("MH");
		InvalidDateException thrown = Assertions.assertThrows(InvalidDateException.class, () -> 
		{ covidService.getDataUponGivenDateRangeAndCodesMethod(getTestCovidData());
		}, "Invalid end date, please check your input");
		Mockito.verify(asker, Mockito.times(2)).ask(Mockito.anyString());
		Assertions.assertEquals("Invalid end date, please check your input", thrown.getMessage());
	}
	
	@Test
	public void testGetDataForDateRangeAndCodeForInvalidDateRangeCode() throws NoDataFoundException, ParseException, Exception  {
		Mockito.when(asker.ask("please enter start date (yyyy-mm-dd) : ")).thenReturn("2021-08-05");
		Mockito.when(asker.ask("please enter end date (yyyy-mm-dd) : ")).thenReturn("2019-08-05");
		Mockito.when(asker.ask("please enter first state code : ")).thenReturn("NL");
		Mockito.when(asker.ask("please enter second state code : ")).thenReturn("MH");
		InvalidDateRangeException thrown = Assertions.assertThrows(InvalidDateRangeException.class, () -> 
		{ covidService.getDataUponGivenDateRangeAndCodesMethod(getTestCovidData());
		}, "Invalid end date, please check your input");
		Mockito.verify(asker, Mockito.times(2)).ask(Mockito.anyString());
		Assertions.assertEquals("Invalid end date, please check your input", thrown.getMessage());
	}
	
	@Test
	public void testGetDataForDateRangeAndCodeForInvalidStartCode() throws NoDataFoundException, ParseException, Exception  {
		Mockito.when(asker.ask("please enter start date (yyyy-mm-dd) : ")).thenReturn("2020-08-05");
		Mockito.when(asker.ask("please enter end date (yyyy-mm-dd) : ")).thenReturn("2021-08-05");
		Mockito.when(asker.ask("please enter first state code : ")).thenReturn("SE");
		Mockito.when(asker.ask("please enter second state code : ")).thenReturn("MH");
		InvalidStateCodeException thrown = Assertions.assertThrows(InvalidStateCodeException.class, () -> 
		{ covidService.getDataUponGivenDateRangeAndCodesMethod(getTestCovidData());
		}, "Invalid state code, please check your input");
		Mockito.verify(asker, Mockito.times(3)).ask(Mockito.anyString());
		Assertions.assertEquals("Invalid state code, please check your input", thrown.getMessage());
	}
	
	@Test
	public void testGetDataForDateRangeAndCodeForInvalidEndCode() throws NoDataFoundException, ParseException, Exception  {
		Mockito.when(asker.ask("please enter start date (yyyy-mm-dd) : ")).thenReturn("2020-08-05");
		Mockito.when(asker.ask("please enter end date (yyyy-mm-dd) : ")).thenReturn("2021-08-05");
		Mockito.when(asker.ask("please enter first state code : ")).thenReturn("TN");
		Mockito.when(asker.ask("please enter second state code : ")).thenReturn("MRG");
		InvalidStateCodeException thrown = Assertions.assertThrows(InvalidStateCodeException.class, () -> 
		{ covidService.getDataUponGivenDateRangeAndCodesMethod(getTestCovidData());
		}, "Invalid state code, please check your input");
		Mockito.verify(asker, Mockito.times(4)).ask(Mockito.anyString());
		Assertions.assertEquals("Invalid state code, please check your input", thrown.getMessage());
	}
	
	
	// Date validations
	
		@Test
	    void test_date_valid() throws ParseException {
				DateValidator val = new DateFormatValidator();
				assertTrue(val.isValid("2021-01-01"));
				assertTrue(val.isValid("2002-12-31"));
				assertFalse(val.isValid("20211-01-01"));
				assertFalse(val.isValid("2021-01-51"));
				assertFalse(val.isValid("2090-21-01"));
				assertFalse(val.isValid("2023-13-01"));
				assertFalse(val.isValid("2021-01-011"));
				assertFalse(val.isValid("2021-018-01"));
			
	    }
		
		@Test
	    void test_date_not_valid() throws ParseException {
			assertThrows(InvalidDateException.class, () -> covidService.isDateValid("20211-01-01", "start"));
			assertThrows(InvalidDateException.class, () -> covidService.isDateValid("2090-21-01", "start"));
			assertThrows(InvalidDateException.class, () -> covidService.isDateValid("2021-018-01", "start"));
			assertThrows(InvalidDateException.class, () -> covidService.isDateValid("2021-01-011", "start"));
	    }
		
		@Test
	    void test_date_range_inValid() throws ParseException {
				SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-DD");
				assertThrows(InvalidDateRangeException.class, 
							() -> covidService.isDateRangeValid(formatter.parse("2022-05-01"), formatter.parse("2020-01-01")));
				assertThrows(InvalidDateRangeException.class, 
						() -> covidService.isDateRangeValid(formatter.parse("2022-01-01"), formatter.parse("2019-01-01")));
				assertThrows(InvalidDateRangeException.class, 
						() -> covidService.isDateRangeValid(formatter.parse("2021-01-01"), formatter.parse("2020-12-31")));		
	    }

		@Test
	    @DisplayName("Test date formatter")
	    void shouldGetFormattedDates() throws ParseException {
	  		Assertions.assertInstanceOf(Date.class, covidService.formatDate("2020-01-09"));
	    }


}
