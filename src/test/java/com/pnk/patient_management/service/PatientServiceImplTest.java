package com.pnk.patient_management.service;

import com.pnk.patient_management.dto.PatientDTO;
import com.pnk.patient_management.exception.BadRequestException;
import com.pnk.patient_management.exception.ResourceNotFoundException;
import com.pnk.patient_management.model.Patient;
import com.pnk.patient_management.repository.PatientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;


class PatientServiceImplTest {

    @Mock
    private PatientService mockPatientService;

    @InjectMocks
    private PatientServiceImpl mockPatientServiceImpl;

    @Mock
    private PatientRepository mockPatientRepository;

    ModelMapper modelMapper = new ModelMapper();

    Patient patient1;
    PatientDTO patientDTO1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        patient1 = new Patient(1L, "John", new Date(1999, 7, 28), "123 A St");
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void testRegisterPatient() {
        // Given
        PatientDTO patientDTO = modelMapper.map(patient1, PatientDTO.class);
        when(mockPatientRepository.save(any(Patient.class))).thenReturn(patient1);

        // When
        Patient result = mockPatientServiceImpl.registerPatient(patientDTO);

        // Then
        assertEquals(patient1, result);
        assertThrows(IllegalArgumentException.class, () -> mockPatientServiceImpl.registerPatient(null));
    }


    @Test
    void testGetPatientById() {
        when(mockPatientRepository.findById(patient1.getPatientId()))
                .thenReturn(Optional.ofNullable(patient1));

        Optional<List<Patient>> result = mockPatientServiceImpl.getPatientById(patient1.getPatientId());

        assertEquals(1, result.get().size());
        assertTrue(result.get().contains(patient1));
    }


    @Test
    void testGetPatientById_BadRequest() {
        when(mockPatientRepository.findById(-99L))
                .thenThrow(BadRequestException.class);

        assertThrows(BadRequestException.class, () -> mockPatientServiceImpl.getPatientById(-99L));
        assertThrows(BadRequestException.class, () -> mockPatientServiceImpl.getPatientById(null));
    }


    @Test
    void testGetPatientById_NotFound() {
        when(mockPatientRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> mockPatientServiceImpl.getPatientById(99L));
    }


    @Test
    void testGetPatientByName() {
        when(mockPatientRepository.findByName(patient1.getName()))
                .thenReturn(Collections.singletonList(patient1));

        List<Patient> result = mockPatientServiceImpl.getPatientByName(patient1.getName());
        assertEquals(1, result.size());
        assertEquals(patient1, result.get(0));
    }


    @Test
    void testGetPatientByName_BadRequest() {
        String nonExistingName = "";
        when(mockPatientRepository.findByName(nonExistingName))
                .thenReturn(Collections.emptyList());

        assertThrows(BadRequestException.class, () -> mockPatientServiceImpl.getPatientByName(nonExistingName));
    }


    @Test
    void testGetPatientByName_NotFound() {
        String nonExistingName = "dne";
        when(mockPatientRepository.findByName(nonExistingName))
                .thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> mockPatientServiceImpl.getPatientByName(nonExistingName));
    }
}