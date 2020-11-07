package com.capgemini.rest.employeepayroll;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EmployeePayrollRestServiceTest {
	EmployeePayrollRestService employeePayrollRestService;

	@Before
	public void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
		employeePayrollRestService = new EmployeePayrollRestService(getEmployeeList());
	}

	/**
	 * UC1
	 */
	@Test
	public void givenEmployeeWhenAddedShouldGetAddedToTheJsonServer() {
		Employee employee = new Employee(4, "Harish", 50000.0);
		employeePayrollRestService.addEmployee(employee);
		Response response = addEmployeeToJsonServer(employee);
		boolean result = response.getStatusCode() == 201 && employeePayrollRestService.getCount() == 4;
		assertTrue(result);
	}

	private Response addEmployeeToJsonServer(Employee employee) {
		String employeeJson = new Gson().toJson(employee);
		RequestSpecification requestSpecification = RestAssured.given();
		requestSpecification.header("Content-Type", "application/json");
		requestSpecification.body(employeeJson);
		return requestSpecification.post("/employees");
	}

	private List<Employee> getEmployeeList() {
		Response response = RestAssured.get("/employees");
		Employee[] employees = new Gson().fromJson(response.asString(), Employee[].class);
		return Arrays.asList(employees);
	}
}