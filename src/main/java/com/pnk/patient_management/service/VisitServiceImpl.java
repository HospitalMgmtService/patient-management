package com.pnk.patient_management.service;

import com.pnk.patient_management.dto.request.VisitCreationRequest;
import com.pnk.patient_management.dto.response.VisitResponse;
import com.pnk.patient_management.entity.Patient;
import com.pnk.patient_management.entity.Visit;
import com.pnk.patient_management.exception.ResourceNotFoundException;
import com.pnk.patient_management.repository.PatientRepository;
import com.pnk.patient_management.repository.VisitRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor // injected by Constructor, no longer need of @Autowire
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class VisitServiceImpl implements VisitService {
    PatientRepository patientRepository;

    VisitRepository visitRepository;
    ModelMapper modelMapper = new ModelMapper();


    @Override
    public VisitResponse registerVisit(VisitCreationRequest visitCreationRequest) {
        String patientId = visitCreationRequest.getPatientId();
        VisitResponse visitResponse = new VisitResponse();

        try {
            Patient existingPatient = patientRepository.findById(visitCreationRequest.getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

            visitResponse.setVisitDate(LocalDateTime.now());
            visitResponse.setDescription(visitResponse.getDescription());
            visitResponse.setPatient(existingPatient);

            Visit visit = modelMapper.map(visitResponse, Visit.class);

            visitRepository.save(visit);
            return visitResponse;
        } catch (IllegalArgumentException e) {
            log.info(">> registerVisit >> Failed to register visitCreationRequest: {}", visitCreationRequest);
            throw new RuntimeException("Failed to register visitCreationRequest");

        }
    }


    @Override
    public List<VisitResponse> getVisitsOfAPatient(String patientId) {
        return List.of();
    }
}
