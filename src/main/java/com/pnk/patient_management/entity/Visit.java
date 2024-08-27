package com.pnk.patient_management.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "visit")
public class Visit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String visitId;

    LocalDateTime visitDate;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "patientId")
    @JsonIgnoreProperties("visitList")
    Patient patient;
}
