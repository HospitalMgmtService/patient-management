package com.pnk.patient_management.repository;

import com.pnk.patient_management.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PatientRepository extends JpaRepository<Patient, Long> {
}
