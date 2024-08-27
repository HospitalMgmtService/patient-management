package com.pnk.patient_management.controller;

import com.pnk.patient_management.dto.request.VisitCreationRequest;
import com.pnk.patient_management.dto.response.ApiResponse;
import com.pnk.patient_management.dto.response.PatientResponse;
import com.pnk.patient_management.dto.response.VisitResponse;
import com.pnk.patient_management.entity.Patient;
import com.pnk.patient_management.entity.Visit;
import com.pnk.patient_management.service.PatientService;
import com.pnk.patient_management.service.VisitService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequiredArgsConstructor // injected by Constructor, no longer need of @Autowire
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequestMapping("/visits")
public class VisitController {

    PatientService patientService;
    VisitService visitService;
    ModelMapper modelMapper = new ModelMapper();


    @PostMapping("/registration")
    ApiResponse<VisitResponse> createVisit(@RequestBody VisitCreationRequest visitCreationRequest) {
        log.info(">> createVisit::visitCreationRequest {}", visitCreationRequest);
        String patientId = visitCreationRequest.getPatientId();
        PatientResponse patientResponse = patientService.getPatientById(patientId);
        Patient patient = modelMapper.map(patientResponse, Patient.class);

        VisitResponse visitResponse = visitService.registerVisit(visitCreationRequest);
        visitResponse.setPatient(patient);
        visitResponse.setVisitDate(LocalDateTime.now());


        Visit visit = modelMapper.map(visitCreationRequest, Visit.class);
        List<Visit> visits = patient.getVisitList();
        visits.add(visit);
        patient.setVisitList(visits);

        log.info(">> createVisit::visitResponse: {}", visitResponse);

        return ApiResponse.<VisitResponse>builder()
                .result(visitResponse)
                .build();
    }
}
