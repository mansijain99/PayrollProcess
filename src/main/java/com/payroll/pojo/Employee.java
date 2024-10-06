package com.payroll.pojo;

import javax.persistence.*;

@Entity
@Table(name = "employees_data")
public class Employee {
	
	@Id
	@Column(name = "EmpID")
	private String empId;
	
	@Column(name = "EmpFName")
	private String firstName;
	
	@Column(name = "EmpLName")
	private String lastName;
	
	@Column(name = "Designation")
	private String designation;

	// Constructors
	public Employee() {
	}

	public Employee(String empId, String firstName, String lastName, String designation) {
		this.empId = empId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.designation = designation;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	@Override
	public String toString() {
		return "Employee [empId=" + empId + ", firstName=" + firstName + ", lastName=" + lastName + ", designation="
				+ designation + "]";
	}

}
