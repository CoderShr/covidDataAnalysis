package com.mindtree.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mindtree.dao.CovidDataRepository;
import com.mindtree.entity.CovidData;
import com.mindtree.exception.InvalidDateException;
import com.mindtree.exception.InvalidDateRangeException;
import com.mindtree.exception.InvalidInputException;
import com.mindtree.exception.InvalidStateCodeException;
import com.mindtree.exception.NoDataFoundException;
import com.mindtree.validator.DateFormatValidator;
import com.mindtree.validator.DateValidator;
//import com.mindtree.validator.InputValidator;

@Service
@Transactional
public class CovidAnalysisServiceImpl implements CovidAnalysisService {

	static Logger log = LogManager.getLogger(CovidAnalysisServiceImpl.class);

	@Autowired
	public CovidDataRepository covidDataRepository;
	
//	@Autowired
//	public InputValidator inputValidator;
	
	@Autowired
	public ScannerAsker asker;

	CovidAnalysisServiceImpl(CovidDataRepository covidDataRepository, ScannerAsker asker
			//			, InputValidator inputValidator
			) {
		this.covidDataRepository = covidDataRepository;
		this.asker = asker;
//		this.inputValidator = inputValidator;
	}
	
	@Override
	public List<CovidData> getCovidDataReport(){
		List<CovidData> coviddata = null; 
		try {
			String optionChoosen = getChoice();
			coviddata = getCovidDataFromDB();
//			inputValidator.checkIfDataPresent(coviddata);
			switch(optionChoosen) {
			
			case "1" :
				getStateNames(coviddata, "readOnly");
				optionSeperatorMethod();
				getCovidDataReport();
				break;
			case "2" :
				getDistrictsForStateCodeChoosenMethod(coviddata);
				getCovidDataReport();
				break;
			case "3" :
				getDataUponGivenDateMethod(coviddata);
				optionSeperatorMethod();
				getCovidDataReport();
				break;
			case "4" :
				getDataUponGivenDateRangeAndCodesMethod( coviddata);
				optionSeperatorMethod();
				getCovidDataReport();
				break;
			case "5" :
				optionSeperatorMethod();
				System.out.print("Thank you... Exiting \n");
				log.info("Closing scanner ... ");
				log.info("Scanner closed.");
				break;
			default : 
				optionSeperatorMethod();
				System.out.print("Invalid entry, please check your input \n");
				getCovidDataReport();
				break;
			}
		} catch (InvalidDateRangeException | InvalidDateException  |
				NoDataFoundException | InvalidStateCodeException | InvalidInputException exp ) {
			System.out.println(exp.getMessage());
			getCovidDataReport();
		} catch(Exception exception) {
			System.out.println(exception.getMessage());
			getCovidDataReport();
		}
		return coviddata;
	}

	private void optionSeperatorMethod() {
		System.out.println("**********************************************************");
		System.out.println();
	}

	@Cacheable("covidData")
	@Transactional(readOnly = true)
	public List<CovidData> getCovidDataFromDB() {
		log.info("Fetching covid data from covidDataDb");
		return  covidDataRepository.findAll();
	}
	
	public Set<String> getStateNames(List<CovidData> covidData, String type) throws NoDataFoundException, Exception {
		log.info("populating states from covid data");
		Set<String> stateList = null ;
		try {
//			inputValidator.
			checkIfDataPresent(covidData);
			stateList = covidData.stream().map(e -> e.getState()).collect(Collectors.toSet());
			
			if(type == "readOnly") {
				System.out.println("\n" + stateList.stream().collect(Collectors.joining("\n")));
			}
		} catch(NoDataFoundException noDataFoundException) {
			throw new NoDataFoundException(noDataFoundException.getMessage());
		} catch(Exception e) {
			throw new Exception("Some exception occured");
		}
		log.info("Retrieved states code from covid data");
		return stateList;
	}

	public Set<String> getDistrictsForStateCodeChoosenMethod(List<CovidData> covidData) 
			throws InvalidStateCodeException, NoDataFoundException, Exception {	
		Set<String> districtNames;
		try {
			String stateCode = getStateCode(covidData, "please enter state code : ");
			districtNames = populateDistrictName(stateCode, covidData);
		} catch(InvalidStateCodeException e) {
			throw new InvalidStateCodeException(e.getMessage());
		} catch(Exception e) {
			throw new Exception("Some exception occured");
		}
		log.info("Retrieved District names from covid data list for state code opted");
		optionSeperatorMethod();
		return districtNames;
	}
	
	public Set<String> populateDistrictName(String stateCode, List<CovidData> covidData) {
		log.info("populating districts for state code " + stateCode);
		Set<String> districtsNameOfaState = covidData.stream().filter(e -> e.getState().equals(stateCode))
				.map(s -> s.getDistrict()).collect(Collectors.toSet());
		System.out.println("\n" + districtsNameOfaState.stream().collect(Collectors.joining("\n")));
		return districtsNameOfaState;
	}

	public Map<String, Map<String, Integer>> getDataUponGivenDateMethod(List<CovidData> coviddata) throws ParseException,
									InvalidDateRangeException, InvalidDateException, InvalidInputException,
								NoDataFoundException, Exception{
		try {			
			Date startDate = getInputDate("please enter start date (yyyy-mm-dd) : ", "start");				
			Date endDate = getInputDate("please enter end date (yyyy-mm-dd) : ", "end");				
//			inputValidator.
			isDateRangeValid(startDate, endDate);			
			return populateStateDataForDateRangeChoosen(startDate, endDate, coviddata);		
		} catch (InvalidDateRangeException de) {
			System.out.println(de.getMessage());
		} catch (InvalidDateException exp ) {
			throw new InvalidDateException(exp.getMessage());
		} catch (NoDataFoundException exp ) {
			throw new NoDataFoundException(exp.getMessage());
		}catch (Exception e) {
			throw new Exception("Some unexpected error occured");			
		}
		return null;
	}
	
	private Map<String, Map<String, Integer>> populateStateDataForDateRangeChoosen(Date startDate, Date endDate, List<CovidData> coviddata) throws NoDataFoundException, 
																									InvalidDateRangeException, ParseException {
		log.info("Fetching date wise collective data covid reports for all states");
		Predicate<CovidData> filterInput = e -> {return e.getDate().after(startDate) && e.getDate().before(endDate);};
		System.out.println("Date|      " + "State| "+ "Total confirmed|") ;		
		Map<String, Map<String, Integer>> covidDatas = populateCovidDataReportUponInputs(coviddata, new SimpleDateFormat("YYYY-MM-dd"), filterInput);
		if(covidDatas.isEmpty()) {
			throw new NoDataFoundException("No data present for given date");
		}
		for(Entry<String, Map<String, Integer>> map : covidDatas.entrySet()) {
			 for(Entry<String, Integer> map1 : map.getValue().entrySet()) {
				 System.out.println(map.getKey().concat("|   ").concat(map1.getKey()).concat("|   ").concat(map1.getValue().toString()));
			 }
		}		
		log.info("Retrieved date wise covid data reports for all states");
		return covidDatas;
	}

	public void getDataUponGivenDateRangeAndCodesMethod(List<CovidData> coviddata) 
											throws ParseException, InvalidDateException, InvalidDateRangeException,
											NoDataFoundException, InvalidStateCodeException {
		try {
			Date startDate = getInputDate("please enter start date (yyyy-mm-dd) : ", "start");				
			Date endDate = getInputDate("please enter end date (yyyy-mm-dd) : ", "end");				
//			inputValidator.
			isDateRangeValid(startDate, endDate);			
			String firstStateCode = getStateCode(coviddata, "please enter first state code : ");			
			String secondStateCode = getStateCode(coviddata, "please enter second state code : ");			
			populateStateDataForDateRangeAndStateCodeChoosen(startDate, endDate, firstStateCode, secondStateCode, coviddata);			
		} catch (InvalidDateRangeException exp ) {
			throw new InvalidDateRangeException(exp.getMessage());
		} catch (InvalidDateException exp ) {
			throw new InvalidDateException(exp.getMessage());
		} catch (NoDataFoundException exp ) {
			throw new NoDataFoundException(exp.getMessage());
		} catch (InvalidStateCodeException exp ) {
			throw new InvalidStateCodeException(exp.getMessage()); 
		} catch (Exception e) {
			System.out.println("Some unexpected exception occured");			
		}
	}
	
	private void populateStateDataForDateRangeAndStateCodeChoosen(Date startDate, Date endDate, String firstSc,
														String secondSc, List<CovidData> coviddata) 
																throws ParseException, NoDataFoundException {
		log.info("Fetching date wise collective covid data reports for state code opted from covid data list");
		SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
		Predicate<CovidData> filterInput = e -> {return e.getDate().after(startDate) 
													&& e.getDate().before(endDate) 
													&& (e.getState().equals(secondSc) 
															|| e.getState().equals(firstSc));};
		Map<String, Map<String, Integer>> covidDatas = populateCovidDataReportUponInputs(coviddata, formatter, filterInput);		 
		if(covidDatas.isEmpty()) {
			throw new NoDataFoundException("No data found for given date or code");
		}	
		System.out.println("Date       |" + "First State | "+ "First State Total confirmed |" + "Second State| "+ "Second State Total confirmed|") ;
		
		for(Entry<String, Map<String, Integer>> map : covidDatas.entrySet()) {
			System.out.println(map.getKey().concat(" |") + map.getValue().entrySet().stream()
								.map(e -> e.getKey() + "          |" + e.getValue())
								.collect(Collectors.joining("                         |")) + "   ");
		}
		log.info("Retieved date wise collective covid data reports for state code opted from covid data list");			
	}						 


	private Date getInputDate(String message, String type) throws ParseException, InvalidInputException, InvalidDateException {
		String date = asker.ask(message);
//		isDateValid(date, type);
		Date startDate = formatDate(date);
//		inputValidator.
		isDateValid(date, type);
		return startDate;
	}

	public String getStateCode(List<CovidData> coviddata, String inputMessage) throws NoDataFoundException, InvalidStateCodeException, Exception {
		String stateCode = asker.ask(inputMessage);
		isStateCodeValid(stateCode, getStateNames(coviddata, "Validation"));
		return stateCode;
	}
	
	private Map<String, Map<String, Integer>> populateCovidDataReportUponInputs(List<CovidData> coviddata, SimpleDateFormat formatter,
																				 Predicate<CovidData> filterInput) {
		Map<String, Map<String, Integer>> covidDatas = coviddata.stream().filter(filterInput)
			   .collect(Collectors.groupingBy(e -> formatter.format(e.getDate()),
						Collectors.groupingBy(CovidData :: getState,
                        Collectors.reducing(0, e -> Integer.parseInt(e.getConfirmed()), Integer::sum))));
		return covidDatas;
	}

	Date formatDate(String date) throws ParseException {
	//	log.info("Format date to pattern YYYY-MM-dd");	
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
//		String dateFormatted =  simpleDateFormat.format(date);
		
		return simpleDateFormat.parse(date);
	}

	private String getChoice() throws ParseException, InvalidInputException {
		log.info("Select options for reports method invoked");
	    String input = asker.ask("1. Get States Name.\r\n" + "2. Get District name for given states\r\n"
				+ "3. Display Data by state with in date range\r\n"
				+ "4. Display Confirmed cases by comparing two states for a given date range.\r\n" + "5. Exit \r\n");
	    return input;
	}
	
	
	private void checkIfDataPresent(List<CovidData> covidData) throws NoDataFoundException {
		if(Objects.isNull(covidData) || covidData.isEmpty()) {
			throw new NoDataFoundException("No data present from covid data DB");
		}
	}
	
	private boolean isStateCodeValid(String stateCode, Set<String> covidDataStateNames) throws InvalidStateCodeException{
		boolean isCodeExist = covidDataStateNames.stream().anyMatch(e -> e.equalsIgnoreCase(stateCode));
		if (stateCode == null || !isCodeExist) {
			throw new InvalidStateCodeException("Invalid state code, please check your input");
		}
		 return isCodeExist;
	}

	public void isDateRangeValid(Date startDate, Date endDate) {
		if(startDate.after(endDate)) {
			throw new InvalidDateRangeException("Invalid end date, please check your input");
		}
	}
	
	public void isDateValid(String startDate, String startOrEndDate) throws InvalidDateException, ParseException {
		DateValidator  validator = new DateFormatValidator();
		if(!validator.isValid(startDate)) {
			throw new InvalidDateException("Invalid " + startOrEndDate + " date, please check your input");
		}
	}

}
