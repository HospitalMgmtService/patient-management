package com.pnk.patient_management.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pnk.patient_management.entity.Patient;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VisitResponse {

    String visitId;

    LocalDateTime visitDate;

    private String description;

    @JsonIgnoreProperties("visitList")
    Patient patient;

}
