package com.pnk.patient_management.service;

import com.pnk.patient_management.dto.PatientDTO;
import com.pnk.patient_management.exception.ResourceNotFoundException;
import com.pnk.patient_management.model.Patient;
import com.pnk.patient_management.repository.PatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Slf4j
@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }


    /**
     * Registers a new patient.
     *
     * @param patientDTO the data transfer object containing patient information
     * @return the registered Patient entity
     * @throws IllegalArgumentException if the patientDTO is null
     */
    @Override
    public Patient registerPatient(PatientDTO patientDTO) {
        if (Objects.isNull(patientDTO)) {
            log.warn("PatientServiceImpl >> registerPatient >> Failed to create new patient: patientDTO is null.");
            throw new IllegalArgumentException("Request body does not contain patientDTO");
        }

        Patient patient = modelMapper.map(patientDTO, Patient.class);
        log.info("PatientServiceImpl >> registerPatient >> Patient created successfully: {}", patient);
        return patientRepository.save(patient);
    }


    @Override
    public Optional<List<Patient>> getPatientById(Long patientId) {
        Optional<Patient> retrievedPatient = patientRepository.findById(patientId);

        if (retrievedPatient.isPresent()) {
            log.info("PatientServiceImpl >> getPatientById >> Patient is found: {}", retrievedPatient.get());
            return Optional.of(Collections.singletonList(retrievedPatient.get()));
        }

        log.warn("PatientServiceImpl >> getPatientById >> Patient with Id: {} is not found.", patientId);
        throw new ResourceNotFoundException("PatientServiceImpl >> getPatientById >> Patient with Id: " + patientId + " is not found.");
    }
}
