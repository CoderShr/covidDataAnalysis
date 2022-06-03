//package com.mindtree.validator;
//
//import java.text.ParseException;
//import java.util.Date;
//import java.util.List;
//import java.util.Objects;
//import java.util.Set;
//
//import org.springframework.stereotype.Component;
//
//import com.mindtree.entity.CovidData;
//import com.mindtree.exception.InvalidDateException;
//import com.mindtree.exception.InvalidDateRangeException;
//import com.mindtree.exception.InvalidStateCodeException;
//import com.mindtree.exception.NoDataFoundException;
//
//@Component
//public class InputValidator {
//
//	
//	public void checkIfDataPresent(List<CovidData> covidData) throws NoDataFoundException {
//		if(Objects.isNull(covidData)) {
//			throw new NoDataFoundException("No data present from covid data DB");
//		}
//	}
//	
//	public boolean isStateCodeValid(String stateCode, Set<String> covidDataStateNames) {
//		boolean isCodeExist = covidDataStateNames.stream().anyMatch(e -> e.equalsIgnoreCase(stateCode));
//		if (stateCode == null || !isCodeExist) {
//			throw new InvalidStateCodeException("Invalid state code, please check your input");
//		}
//		 return isCodeExist;
//	}
//
//	public void isDateRangeValid(Date startDate, Date endDate) {
//		if(startDate.after(endDate)) {
//			throw new InvalidDateRangeException("Invalid end date, please check your input");
//		}
//	}
//	
//	public void isDateValid(String startDate, String startOrEndDate) throws InvalidDateException, ParseException {
//		DateValidator  validator = new DateFormatValidator();
////		Predicate<Date> validate = date-> {
////			DateFormatValidator validator = new DateFormatValidator();
////			return validator.isValid(date);
////		};
//		
//		if(!validator.isValid(startDate)) {
//			throw new InvalidDateException("Invalid " + startOrEndDate + " date, please check your input");
//		}
//	}
//}
