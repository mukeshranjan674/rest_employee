package com.capgemini.rest.employeepayroll;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		employeePayrollRestService = new EmployeePayrollRestService(getEmployeeListFromJsonServer());
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

	/**
	 * UC2
	 */
	@Test
	public void givenEmployeeListWhenAddedShouldGetAddedToTheJsonServer() {
		List<Employee> employees = getNewEmployeeList();
		employeePayrollRestService.addEmployeeList(employees);
		addMultipleEmployeeUsingThreads(employees);
		assertEquals(7, getEmployeeListFromJsonServer().size());
	}

	/**
	 * UC3
	 */
	@Test
	public void givenEmployeeShouldGetUpdatedInTheJsonServer() {
		employeePayrollRestService.updateEmployee("Shivam", 1548545.0);
		Employee employee = employeePayrollRestService.getEmployee("Shivam");
		String empJson = new Gson().toJson(employee);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(empJson);
		Response response = request.put("/employees/" + employee.getId());
		assertEquals(200, response.getStatusCode());
	}

	/**
	 * UC4
	 */
	@Test
	public void whenEmployeeListIsRetrievedFromJsonServerShouldMatchTheCount() {
		List<Employee> employees = this.getEmployeeListFromJsonServer();
		assertEquals(7, employees.size());
	}
	
	/**
	 * UC5
	 */
	@Test
	public void givenEmployeeWhenDeletedShouldGetDeletedFromJsonServer() {
		Employee employee = employeePayrollRestService.getEmployee("Harish");
		employeePayrollRestService.deleteEmployee(employee);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		Response response = request.delete("/employees/" + employee.getId());
		assertEquals(200, response.getStatusCode());
	}

	private void addMultipleEmployeeUsingThreads(List<Employee> employees) {
		Map<Integer, Boolean> addEmployeeStatus = new HashMap<>();
		for (Employee employee : employees) {
			addEmployeeStatus.put(employee.getId(), false);
			Runnable task = () -> {
				System.out.println("Employee Being Added : " + Thread.currentThread().getName());
				Response response = addEmployeeToJsonServer(employee);
				if (response.getStatusCode() == 201)
					System.out.println("Employee Added : " + Thread.currentThread().getName());
				addEmployeeStatus.put(employee.getId(), true);
			};
			Thread thread = new Thread(task, employee.getName());
			thread.setPriority(6);
			thread.start();
		}
		while (addEmployeeStatus.containsValue(false)) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private List<Employee> getNewEmployeeList() {
		Employee[] employees = { new Employee(5, "Jay", 60000.0), new Employee(6, "Shivam", 70000.0),
				new Employee(7, "Vishnu", 80000.0) };
		return Arrays.asList(employees);
	}

	private Response addEmployeeToJsonServer(Employee employee) {
		String employeeJson = new Gson().toJson(employee);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(employeeJson);
		return request.post("/employees");
	}

	private List<Employee> getEmployeeListFromJsonServer() {
		Response response = RestAssured.get("/employees");
		Employee[] employees = new Gson().fromJson(response.asString(), Employee[].class);
		return Arrays.asList(employees);
	}
}