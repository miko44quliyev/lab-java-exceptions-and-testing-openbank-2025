package com.example.lab.service;

import com.example.lab.exception.ResourceNotFoundException;
import com.example.lab.model.Patient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PatientService {

    private final Map<Long, Patient> patients = new HashMap<>();
    private long nextId = 1;

    public Patient create(Patient patient) {
        patient.setId(nextId++);
        patients.put(patient.getId(), patient);
        return patient;
    }

    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients.values());
    }

    public Patient getPatientById(Long id) {
        Patient patient = patients.get(id);
        if (patient == null) {
            throw new ResourceNotFoundException("Patient with id " + id + " not found");
        }
        return patient;
    }

    public List<Patient> getPatientsByBirthRange(LocalDate start, LocalDate end) {
        List<Patient> result = new ArrayList<>();
        for (Patient p : patients.values()) {
            if (!p.getDateOfBirth().isBefore(start) && !p.getDateOfBirth().isAfter(end)) {
                result.add(p);
            }
        }
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Patients with this birth range not found");
        }
        return result;
    }

    public List<Patient> getPatientsByDoctorDepartment(String department) {
        List<Patient> result = new ArrayList<>();
        for (Patient p : patients.values()) {
            if (p.getAdmittedBy() != null && department.equals(p.getAdmittedBy().getDepartment())) {
                result.add(p);
            }
        }
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Patients with this admitting doctor's department not found");
        }
        return result;
    }

    public List<Patient> getPatientsWithDoctorStatusOFF() {
        List<Patient> result = new ArrayList<>();
        for (Patient p : patients.values()) {
            if (p.getAdmittedBy() != null && "OFF".equals(p.getAdmittedBy().getStatus())) {
                result.add(p);
            }
        }
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Patients with admitting doctor whose status is OFF not found");
        }
        return result;
    }

    public Patient fullUpdate(Long id, Patient updatedPatient) {
        Patient existing = getPatientById(id);
        existing.setName(updatedPatient.getName());
        existing.setDateOfBirth(updatedPatient.getDateOfBirth());
        existing.setAdmittedBy(updatedPatient.getAdmittedBy());
        return existing;
    }

    public Patient partialUpdate(Long id, Patient updatedPatient) {
        Patient existing = getPatientById(id);
        if (updatedPatient.getName() != null) existing.setName(updatedPatient.getName());
        if (updatedPatient.getDateOfBirth() != null) existing.setDateOfBirth(updatedPatient.getDateOfBirth());
        if (updatedPatient.getAdmittedBy() != null) existing.setAdmittedBy(updatedPatient.getAdmittedBy());
        return existing;
    }

    public void delete(Long id) {
        if (!patients.containsKey(id)) {
            throw new ResourceNotFoundException("Cannot delete. Patient not found with id: " + id);
        }
        patients.remove(id);
    }
}