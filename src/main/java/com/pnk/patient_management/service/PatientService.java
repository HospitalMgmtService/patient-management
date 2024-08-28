package com.pnk.patient_management.service;

import com.pnk.patient_management.dto.request.PatientCreationRequest;
import com.pnk.patient_management.dto.response.PatientResponse;

import java.util.List;


public interface PatientService {

    // Register Patients:
    // Assign a unique patient ID for tracking.
    PatientResponse registerPatient(PatientCreationRequest patientCreationRequest);

    // Collect and store patient personal information, including name, address, contact details, date of birth, etc.
    PatientResponse getPatientById(String patientId);

    List<PatientResponse> getPatientByName(String patientName);

}
