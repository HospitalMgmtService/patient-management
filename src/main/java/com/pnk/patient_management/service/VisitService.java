package com.pnk.patient_management.service;

import com.pnk.patient_management.dto.request.VisitCreationRequest;
import com.pnk.patient_management.dto.response.VisitResponse;

import java.util.List;


public interface VisitService {

    VisitResponse registerVisit(VisitCreationRequest visitCreationRequest);

    List<VisitResponse> getVisitsOfAPatient(String patientId);

}
