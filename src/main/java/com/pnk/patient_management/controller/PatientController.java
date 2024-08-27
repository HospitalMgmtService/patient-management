package com.pnk.patient_management.controller;

import com.pnk.patient_management.dto.request.PatientCreationRequest;
import com.pnk.patient_management.dto.response.ApiResponse;
import com.pnk.patient_management.dto.response.PatientResponse;
import com.pnk.patient_management.service.PatientService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor // injected by Constructor, no longer need of @Autowire
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PatientController {

    PatientService patientService;


    @PostMapping("/registration")
    ApiResponse<PatientResponse> createPatient(@RequestBody PatientCreationRequest patientCreationRequest) {
        log.info(">> createPatient::patientCreationRequest: {}", patientCreationRequest);
        PatientResponse persistedPatient = patientService.registerPatient(patientCreationRequest);
        log.info(">> createPatient::persistedPatient: {} created successfully.", persistedPatient);
        return ApiResponse.<PatientResponse>builder()
                .result(persistedPatient)
                .build();
    }


    @GetMapping
    ApiResponse<List<PatientResponse>> getPatientByFields(
            @RequestParam(value = "all", required = false) String allPatients,
            @RequestParam(value = "id", required = false) String patientId,
            @RequestParam(value = "name", required = false) String patientName
    ) {
        List<PatientResponse> patients = new ArrayList<>();

        if (patientId != null) {
            log.info(">> getPatientByFields >> Patient with Id: {}", patientId);

            PatientResponse patientResponse = patientService.getPatientById(patientId);
            log.info(">> getPatientByFields::patientResponse: {}", patientResponse);

            patients.add(patientResponse);
        } else if (patientName != null) {
            log.info(">> getPatientByFields >> Patient with name: {}", patientName);
            patients = patientService.getPatientByName(patientName);
        }

        return ApiResponse.<List<PatientResponse>>builder()
                .result(patients)
                .build();
    }
}