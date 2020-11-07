package com.capgemini.rest.employeepayroll;

import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollRestService {
	private List<Employee> employees;

	public EmployeePayrollRestService(List<Employee> employees) {
		this.employees = new ArrayList<Employee>(employees);
	}

	/**
	 * UC1
	 * 
	 * @param employee
	 */
	public void addEmployee(Employee employee) {
		employees.add(employee);
	}

	public List<Employee> getEmployeeList() {
		return employees;
	}

	public int getCount() {
		return employees.size();
	}
}