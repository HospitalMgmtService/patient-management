package com.pnk.patient_management.repository;

import com.pnk.patient_management.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PatientRepository extends JpaRepository<Patient, String> {

    List<Patient> findByNameContains(String name);

}
