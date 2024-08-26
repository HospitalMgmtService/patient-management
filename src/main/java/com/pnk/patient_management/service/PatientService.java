package com.pnk.patient_management.service;

import com.pnk.patient_management.dto.PatientDTO;
import com.pnk.patient_management.model.Patient;

import java.util.List;
import java.util.Optional;


public interface PatientService {

    // Register Patients:
    // Assign a unique patient ID for tracking.
    Patient registerPatient(PatientDTO patientDTO);

    // Collect and store patient personal information, including name, address, contact details, date of birth, etc.
    Optional<List<Patient>> getPatientById(String patientId);

    List<Patient> getPatientByName(String patientName);

}


/* TODO list
 * 1. Patient Management:
    - Register Patients:

    - Collect and store patient personal information, including name, address, contact details, date of birth, etc.

    - Assign a unique patient ID for tracking.

    - Maintain Patient Records:

    - Store detailed medical history, including past treatments, surgeries, allergies, ongoing medications, and diagnoses.

    - Update patient records with new treatment details, test results, and doctor notes.

    - Schedule Appointments:

    - Provide a user-friendly interface for scheduling and managing appointments.

    - Implement a queue management system to streamline patient flow.

    - Notify patients of their appointment status via SMS or email.
 * */