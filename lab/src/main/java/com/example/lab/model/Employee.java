package com.example.lab.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class Employee {
    private Long id;

    @NotBlank(message = "Employee name cannot be empty")
    private String name;

    @NotBlank(message = "Department cannot be empty")
    private String department;

    @NotBlank(message = "Status cannot be empty")
    private String status;

    public Employee() {}

    public Employee(Long id, String name, String department, String status) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}