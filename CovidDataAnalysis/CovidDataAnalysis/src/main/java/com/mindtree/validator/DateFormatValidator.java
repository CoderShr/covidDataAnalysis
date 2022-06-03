package com.mindtree.validator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.function.Predicate;

public class DateFormatValidator implements DateValidator {

	 @Override
	 public boolean isValid(String inputDate) {
		 boolean valid = false;
	        try {
	            LocalDate date = LocalDate.parse(inputDate, DateTimeFormatter.ofPattern("uuuu-MM-dd"));
	            valid = isValidDate(date) ? true : false;
	        } catch (DateTimeParseException e) {
	        	valid = false;
	        }
	        return valid;
	    }
	 
	private boolean isValidDate(LocalDate inputDate) {
//		int month = inputDate.getMonthValue();
//		int day = inputDate.getDayOfMonth();
		LocalDate currentDate = LocalDate.now();
		Predicate<LocalDate> validateMethod = (date) -> {
			return (inputDate.isBefore(currentDate)); 
		};
		return validateMethod.test(inputDate);
	
	}
	
}