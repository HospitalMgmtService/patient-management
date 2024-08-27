package com.pnk.patient_management.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "patient")
public class Patient implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String patientId;

    String name;

    LocalDate dob;

    String address;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("patient")
    private List<Visit> visitList = new ArrayList<>();

}
