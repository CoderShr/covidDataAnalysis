package com.mindtree.validator;

import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.Date;

//@FunctionalInterface
public interface DateValidator {

	boolean isValid(String dateStr) throws ParseException ;
}