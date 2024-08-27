package com.pnk.patient_management.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pnk.patient_management.entity.Visit;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientResponse {

    String patientId;

    String name;

    LocalDate dob;

    String address;

    @JsonIgnoreProperties("patient")
    List<Visit> visitList;

}
