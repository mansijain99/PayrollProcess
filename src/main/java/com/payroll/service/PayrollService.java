package com.payroll.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.payroll.pojo.Employee;
import com.payroll.pojo.PayrollEvent;
import com.payroll.repository.EmployeeRepository;
import com.payroll.repository.PayrollEventRepository;

@Service
public class PayrollService {
	
	private static final String EVENT_ONBOARD = "ONBOARD";
	private static final String EVENT_SALARY = "SALARY";
	private static final String EVENT_BONUS = "BONUS";
	private static final String EVENT_EXIT = "EXIT";
	private static final String EVENT_REIMBURSEMENT = "REIMBURSEMENT";

    private static final String DATE_FORMAT = "MM-dd-yyyy";
    private static final String ORIGINAL_DATE_FORMAT = "dd-MM-yyyy";
	
	
	@Autowired
    private EmployeeRepository employeeRepository;
	
	@Autowired
    private PayrollEventRepository payrollEventRepository;
	
    
    // Process uploaded employee payroll data from CSV/TXT files
    public void processPayrollFile(String tempFilePath, String originalFileName) throws IOException {
    	System.out.println("Processing payroll file: " + originalFileName);
    	
        try (BufferedReader reader = new BufferedReader(new FileReader(tempFilePath))) {
            String line;

            // Read and discard the header line
            if ((line = reader.readLine()) != null) {
                System.out.println("Skipping header: " + line);
            }
            // Process subsequent lines
            while ((line = reader.readLine()) != null) {
                processRecord(line);
            }
        }catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            throw e;
        }
    }
    
    private void processRecord(String record) {
        String[] fields = record.split(",");
        
        final int expectedLength = 9;

        // Validate the number of fields
        if (fields.length < expectedLength) {
            System.err.println("Invalid record: " + record);
            return;
        }
        
        String eventType = fields[5];
        System.out.println("Processing record: " + record);
        System.out.println("Event type: " + eventType);

        String notes = fields[8];

        switch (eventType) {
            case EVENT_ONBOARD:
                handleEvent(fields, eventType, 6, notes);
                break;
            case EVENT_SALARY:
                handleEvent(fields, eventType, 3, notes);
                break;
            case EVENT_BONUS:
                handleEvent(fields, eventType, 3, notes);
                break;
            case EVENT_EXIT:
                handleEvent(fields, eventType, 3, notes);
                break;
            case EVENT_REIMBURSEMENT:
                handleEvent(fields, eventType, 3, notes);
                break;
            default:
                throw new IllegalArgumentException("Invalid event type: " + eventType);
        }
    }

    private LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.equals("null")) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        try {
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format: " + dateString);
            return null;
        }
    }

    private LocalDate parseOriginalDate(String dateString) {
        if (dateString == null || dateString.equals("null")) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ORIGINAL_DATE_FORMAT);
        try {
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            System.err.println("Invalid original date format: " + dateString);
            return null;
        }
    }


    private void handleEvent(String[] fields, String eventType, int valueIndex, String notes) {
        try {
            if (fields[1] == null || fields[1].equals("null")) {
                System.err.println("Employee ID is null for event: " + eventType);
                return;
            }

            String empId = fields[1];
            if (EVENT_ONBOARD.equals(eventType) && employeeRepository.findByEmpId(empId).isPresent()) {
                System.out.println("Employee with ID " + empId + " already exists. Skipping.");
                return;
            }

            String empFirstName = fields[2].equals("null") ? "Unknown" : fields[2];
            String empLastName = fields[3].equals("null") ? "Unknown" : fields[3];
            String designation = fields[4].equals("null") ? "Not Specified" : fields[4];

            // Attempt to parse field(6) as a date
            LocalDate eventValue = parseDate(fields[6]);
            LocalDate eventDate = parseOriginalDate(fields[7]);
            String valueEvent = "";
            if (valueEvent != null) {
                // If it successfully parsed as a date
                valueEvent = eventValue.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
            } else {
                // If parsing as a date fails, try to parse as an amount
                try {
                    double amount = Double.parseDouble(fields[6]);
                    valueEvent = String.valueOf(amount);
                } catch (NumberFormatException nfe) {
                    System.err.println("Invalid format for event value: " + fields[6]);
                    return;
                }
            }
            // Handle the event as a date
            PayrollEvent payrollEvent = new PayrollEvent();
            payrollEvent.setSequenceNo(Integer.parseInt(fields[0]));
            payrollEvent.setEmpId(empId);
            payrollEvent.setEventType(eventType);
            payrollEvent.setEventValue(valueEvent);
            payrollEvent.setEventDate(eventDate.format(DateTimeFormatter.ofPattern(ORIGINAL_DATE_FORMAT)));
            payrollEvent.setNotes(notes);
            payrollEventRepository.save(payrollEvent);

            // Save employee if it's an ONBOARD event
            if (EVENT_ONBOARD.equals(eventType)) {
                Employee employee = new Employee();
                employee.setEmpId(empId);
                employee.setFirstName(empFirstName);
                employee.setLastName(empLastName);
                employee.setDesignation(designation);
                employeeRepository.save(employee);
            }
        } catch (Exception e) {
            System.err.println("Error processing " + eventType + " event: " + e.getMessage());
        }
    }
}
