package com.mindtree.service;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.springframework.stereotype.Component;

import com.mindtree.exception.InvalidInputException;

@Component
public class ScannerAsker {

	    private final Scanner scanner;
//	    private final PrintStream out;

	    public ScannerAsker() {
	        scanner = new Scanner(System.in);
//	        this.out = out;
	    }

	    public String ask(String message) throws InvalidInputException {
	        System.out.println(message);
	        if(!scanner.hasNext()) {
	        	throw new InvalidInputException("Please enter valid input");
	        }
	        return scanner.next();
	    }
	}
