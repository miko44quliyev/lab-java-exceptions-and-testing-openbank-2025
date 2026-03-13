package com.example.lab.service;

import com.example.lab.exception.ResourceNotFoundException;
import com.example.lab.model.Employee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {

    private final Map<Long, Employee> employees = new HashMap<>();
    private long nextId = 1;

    public Employee create(Employee employee) {
        employee.setId(nextId++);
        employees.put(employee.getId(), employee);
        return employee;
    }

    public List<Employee> getAllDoctors() {
        return new ArrayList<>(employees.values());
    }

    public Employee getDoctorById(Long id) {
        Employee employee = employees.get(id);
        if (employee == null) {
            throw new ResourceNotFoundException("Employee with id " + id + " not found");
        }
        return employee;
    }

    public List<Employee> getDoctorsByStatus(String status) {
        List<Employee> result = new ArrayList<>();
        for (Employee e : employees.values()) {
            if (status.equals(e.getStatus())) {
                result.add(e);
            }
        }
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Employees with status '" + status + "' not found");
        }
        return result;
    }

    public List<Employee> getDoctorsByDepartment(String department) {
        List<Employee> result = new ArrayList<>();
        for (Employee e : employees.values()) {
            if (department.equals(e.getDepartment())) {
                result.add(e);
            }
        }
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Employees in department '" + department + "' not found");
        }
        return result;
    }
    public Employee fullUpdate(Long id, Employee updatedEmployee) {
        Employee existing = getDoctorById(id);
        existing.setName(updatedEmployee.getName());
        existing.setStatus(updatedEmployee.getStatus());
        existing.setDepartment(updatedEmployee.getDepartment());
        return existing;
    }
    public Employee partialUpdate(Long id, Employee updatedEmployee) {
        Employee existing = getDoctorById(id);
        if (updatedEmployee.getName() != null) existing.setName(updatedEmployee.getName());
        if (updatedEmployee.getStatus() != null) existing.setStatus(updatedEmployee.getStatus());
        if (updatedEmployee.getDepartment() != null) existing.setDepartment(updatedEmployee.getDepartment());
        return existing;
    }
    public Employee updateStatus(Long id, String status) {
        Employee employee = getDoctorById(id);
        employee.setStatus(status);
        return employee;
    }

    public Employee updateDepartment(Long id, String department) {
        Employee employee = getDoctorById(id);
        employee.setDepartment(department);
        return employee;
    }

    public void delete(Long id) {
        if (!employees.containsKey(id)) {
            throw new ResourceNotFoundException("Cannot delete. Employee not found with id: " + id);
        }
        employees.remove(id);
    }
}