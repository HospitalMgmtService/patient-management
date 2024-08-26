package com.pnk.patient_management.controller;

import com.pnk.patient_management.dto.PatientDTO;
import com.pnk.patient_management.exception.UnsupportedMediaType;
import com.pnk.patient_management.model.Patient;
import com.pnk.patient_management.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@Slf4j
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }


    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody PatientDTO patientDTO) {
        try {
            log.info("PatientController >> createPatient >> patientDTO:: {}", patientDTO);
            Patient persistedPatient = patientService.registerPatient(patientDTO);
            log.info("PatientController >> createPatient >> persistedPatient:: {} created successfully.", persistedPatient);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(persistedPatient);
        } catch (IllegalArgumentException e) {
            log.warn("PatientController >> createPatient >> Failed to create new patient: {}", patientDTO, e);
            return ResponseEntity.badRequest().build();
        } catch (UnsupportedMediaType e) {
            log.warn("PatientController >> createPatient >> Unsupported media type: {}", patientDTO, e);
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
        }
    }


    @GetMapping
    public ResponseEntity<List<Patient>> getPatientByFields(
            @RequestParam(value = "all", required = false) String allPatients,
            @RequestParam(value = "id", required = false) Long patientId,
            @RequestParam(value = "name", required = false) String patientName
    ) {
        if (patientId != null) {
            log.info("PatientController >> getPatientByFields >> Patient with Id: {}", patientId);
            Optional<List<Patient>> patient = patientService.getPatientById(patientId);
            if (patient.isPresent()) {
                return ResponseEntity.ok(patient.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } else if (patientName != null) {
            log.info("PatientController >> getPatientByFields >> Patient with name: {}", patientName);
            List<Patient> patients = patientService.getPatientByName(patientName);
            if (!patients.isEmpty()) {
                return ResponseEntity.ok(patients);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } else if ("true".equalsIgnoreCase(allPatients)) {
            /*List<Patient> patients = patientService.getAllPatients();
            return ResponseEntity.ok(patients);*/
            return null;
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }
}