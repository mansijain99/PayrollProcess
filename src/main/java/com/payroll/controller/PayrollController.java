package com.payroll.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.payroll.service.PayrollService;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {
	
	@Autowired
    private PayrollService payrollService;

    @PostMapping("/upload")
    public String uploadPayrollFile(@RequestParam("file") MultipartFile file) {
        try {
            // Save the uploaded file to a temporary location
            File tempFile = File.createTempFile("payroll", ".tmp");
            file.transferTo(tempFile);
            
            // Optionally, get the original file name
            String originalFileName = file.getOriginalFilename();

            // Process the file
            payrollService.processPayrollFile(tempFile.getAbsolutePath(), originalFileName);
            return "Payroll file processed successfully!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error processing file: " + e.getMessage();
        }
    }

    @GetMapping("/test")
    public String sampleTest(){
        System.out.println("Inside sampleTest request::");
        return "Get Request Processed Successfully";
    }

}
