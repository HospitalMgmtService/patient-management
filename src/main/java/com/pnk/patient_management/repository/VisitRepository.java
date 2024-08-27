package com.pnk.patient_management.repository;

import com.pnk.patient_management.entity.Patient;
import com.pnk.patient_management.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface VisitRepository extends JpaRepository<Visit, String> {

    List<Visit> findByPatient(Patient patient);

}
