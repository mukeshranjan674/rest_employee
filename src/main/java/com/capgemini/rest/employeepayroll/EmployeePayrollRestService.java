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

	/**
	 * UC2
	 * 
	 * @param employees
	 */
	public void addEmployeeList(List<Employee> employees) {
		employees.forEach(employee -> this.employees.add(employee));
	}

	public List<Employee> getEmployeeList() {
		return employees;
	}

	public int getCount() {
		return employees.size();
	}

	/**
	 * UC3
	 * 
	 * @param name
	 * @param salary
	 */
	public void updateEmployee(String name, double salary) {
		Employee employee = this.employees.stream().filter(emp -> emp.getName().equals(name))
										  .findAny()
										  .orElse(null);
		employee.setSalary(salary);
	}

	public Employee getEmployee(String name) {
		return this.employees.stream().filter(emp -> emp.getName().equals(name))
								      .findAny()
								      .orElse(null);
	}
}