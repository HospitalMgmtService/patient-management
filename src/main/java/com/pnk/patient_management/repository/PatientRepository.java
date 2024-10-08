package com.pnk.patient_management.repository;

import com.pnk.patient_management.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {

    List<Patient> findByNameContains(String name);

}
