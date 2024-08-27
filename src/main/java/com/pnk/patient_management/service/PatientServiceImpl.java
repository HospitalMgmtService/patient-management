package com.pnk.patient_management.service;

import com.pnk.patient_management.dto.request.PatientCreationRequest;
import com.pnk.patient_management.dto.response.PatientResponse;
import com.pnk.patient_management.entity.Patient;
import com.pnk.patient_management.exception.AppException;
import com.pnk.patient_management.exception.BadRequestException;
import com.pnk.patient_management.exception.ErrorCode;
import com.pnk.patient_management.exception.ResourceNotFoundException;
import com.pnk.patient_management.repository.PatientRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor // injected by Constructor, no longer need of @Autowire
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PatientServiceImpl implements PatientService {

    PatientRepository patientRepository;
    ModelMapper modelMapper = new ModelMapper();


    /**
     * Registers a new patient.
     *
     * @param patientCreationRequest the data transfer object containing patient information
     * @return the registered Patient entity
     * @throws IllegalArgumentException if the patientCreationRequest is null
     */
    @Override
    public PatientResponse registerPatient(PatientCreationRequest patientCreationRequest) {
        log.info(">> registerPatient::patientCreationRequest: {}", patientCreationRequest);

        if (Objects.isNull(patientCreationRequest)) {
            log.info(">> registerPatient >> Failed to create new patient: patientCreationRequest is null.");
            throw new IllegalArgumentException("Request body does not contain patientCreationRequest");
        }

        Patient patient = modelMapper.map(patientCreationRequest, Patient.class);
        patient.setVisitList(new ArrayList<>());

        log.info(">> registerPatient:: patient: {}", patient);

        try {
            patient = patientRepository.save(patient);
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        PatientResponse patientResponse = modelMapper.map(patient, PatientResponse.class);
        log.info(">> registerPatient::patientResponse: {}", patientResponse);

        return patientResponse;
    }


    @Override
    public PatientResponse getPatientById(String patientId) {
        if (patientId == null) {
            log.info(">> getPatientById >> Invalid patientId");
            throw new BadRequestException("Invalid patientId");
        }

        Optional<Patient> retrievedPatient = patientRepository.findById(patientId);

        if (retrievedPatient.isPresent()) {
            log.info(">> getPatientById >> Patient is found: {}", retrievedPatient.get());
            return modelMapper.map(retrievedPatient.get(), PatientResponse.class);
        }

        log.info(">> getPatientById >> Patient with Id: {} is not found.", patientId);
        throw new ResourceNotFoundException("Patient with Id: " + patientId + " is not found.");
    }


    @Override
    public List<PatientResponse> getPatientByName(String patientName) {
        if (patientName == null) {
            log.info(">> getPatientById >> Invalid patientName");
            throw new BadRequestException("Invalid patientName");
        }

        List<Patient> retrievedPatients = patientRepository.findByNameContains(patientName);

        if (retrievedPatients.isEmpty()) {
            log.info(">> getPatientById >> No patients found with name: {}", patientName);
            return Collections.emptyList();
        }

        List<PatientResponse> patientResponses = retrievedPatients.stream()
                .map(patient -> modelMapper.map(patient, PatientResponse.class)).toList();

        log.info(">> getPatientById >> Found {} patients with name: {}", patientResponses.size(), patientName);
        return patientResponses;
    }
}
