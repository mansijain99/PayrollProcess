package com.payroll.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.payroll.pojo.Employee;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
	
	// Custom query methods
    Optional<Employee> findByEmpId(String empId);

}
