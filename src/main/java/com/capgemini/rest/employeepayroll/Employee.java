package com.capgemini.rest.employeepayroll;

public class Employee {
	private int id;
	private String name;
	private double salary;
	
	public Employee(int id, String name, double salary) {
		this.name = name;
		this.id = id;
		this.salary = salary;
	}
}
