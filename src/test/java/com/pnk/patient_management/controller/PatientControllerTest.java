package com.pnk.patient_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pnk.patient_management.dto.PatientDTO;
import com.pnk.patient_management.exception.BadRequestException;
import com.pnk.patient_management.exception.ResourceNotFoundException;
import com.pnk.patient_management.exception.UnsupportedMediaType;
import com.pnk.patient_management.model.Patient;
import com.pnk.patient_management.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class PatientControllerTest {

    @Mock
    private PatientService mockPatientService;

    @InjectMocks
    private PatientController mockPatientController;

    private MockMvc mockMvc;

    private ObjectMapper mockObjectMapper;

    private static final String ENDPOINT_PATH = "/patient";
    ModelMapper modelMapper = new ModelMapper();

    Patient patient1;
    PatientDTO patientDTO1;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mockPatientController).build();
        mockObjectMapper = new ObjectMapper();
        patient1 = new Patient("33c41c818ceb", "John", new Date(1999, 7, 28), "123 A St", List.of());
        patientDTO1 = modelMapper.map(patient1, PatientDTO.class);
    }


    @Test
    void testCreatePatientSuccess() throws Exception {
        String requestBody = mockObjectMapper.writeValueAsString(patientDTO1);

        when(mockPatientService.registerPatient(patientDTO1))
                .thenReturn(patient1);

        mockMvc.perform(post(ENDPOINT_PATH)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(patient1.getName()))
//                .andExpect(jsonPath("$.dob").value("7/28/1999"))
                .andExpect(jsonPath("$.address", is(patient1.getAddress())))
                .andDo(print());
    }


    @Test
    void testCreatePatientIllegalArgument() throws Exception {
        String requestBody = mockObjectMapper.writeValueAsString(patientDTO1);

        when(mockPatientService.registerPatient(patientDTO1))
                .thenThrow(new IllegalArgumentException());

        mockMvc.perform(post(ENDPOINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testCreatePatientUnsupportedMediaType() throws Exception {
        String requestBody = mockObjectMapper.writeValueAsString(patientDTO1);

        when(mockPatientService.registerPatient(any(PatientDTO.class)))
                .thenThrow(new UnsupportedMediaType(""));

        mockMvc.perform(post(ENDPOINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnsupportedMediaType());
    }


    @Test
    void testGetPatientById_Ok() throws Exception {
        when(mockPatientService.getPatientById(patient1.getId()))
                .thenReturn(Optional.of(Collections.singletonList(patient1)));

        mockMvc.perform(get(ENDPOINT_PATH).param("id", patient1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(mockObjectMapper.writeValueAsString(Collections.singletonList(patient1))))
                .andDo(print());

        verify(mockPatientService, times(1)).getPatientById(patient1.getId());
    }


    @Test
    void testGetPatientById_PatientNotFound() throws Exception {
        String patientId = "unknown";
        when(mockPatientService.getPatientById(patientId))
                .thenReturn(Optional.empty());

        mockMvc.perform(get(ENDPOINT_PATH)
                        .param("id", patientId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void testGetPatientById_Null_patientId() throws Exception {
        mockMvc.perform(get(ENDPOINT_PATH).param("id", ""))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }


    @Test
    void testGetPatientById_ResourceNotFoundException() throws Exception {
        when(mockPatientService.getPatientById("unknown"))
                .thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(ENDPOINT_PATH).param("id", "99"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


    @Test
    void testGetPatientByName_Ok() throws Exception {
        when(mockPatientService.getPatientByName(patient1.getName()))
                .thenReturn(Collections.singletonList(patient1));

        mockMvc.perform(get(ENDPOINT_PATH).param("name", patient1.getName()))
                .andExpect(status().isOk())
                .andExpect(content().string(mockObjectMapper.writeValueAsString(Collections.singletonList(patient1))))
                .andDo(print());

        verify(mockPatientService, times(1)).getPatientByName(patient1.getName());
    }


    @Test
    void testGetPatientByName_PatientNotFound() throws Exception {
        String notFoundName = "NotFound";
        when(mockPatientService.getPatientByName(notFoundName))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get(ENDPOINT_PATH)
                        .param("name", notFoundName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void testGetPatientByName_Null_patientName() throws Exception {
        when(mockPatientService.getPatientByName(""))
                .thenThrow(BadRequestException.class);

        mockMvc.perform(get(ENDPOINT_PATH).param("name", ""))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }


    @Test
    void testGetPatientByName_ResourceNotFoundException() throws Exception {
        when(mockPatientService.getPatientByName("NoName"))
                .thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(ENDPOINT_PATH).param("name", "NoName"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


    @Test
    void testGetAllPatients() throws Exception {
        // Uncomment and adjust this part when you implement getAllPatients in your service
        //List<Patient> patients = Arrays.asList(new Patient(1L, "John Doe"), new Patient(2L, "Jane Doe"));
        //when(patientService.getAllPatients()).thenReturn(patients);

        mockMvc.perform(get(ENDPOINT_PATH)
                        .param("all", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        //.andExpect(jsonPath("$[0].id").value(1L))
        //.andExpect(jsonPath("$[0].name").value("John Doe"))
        //.andExpect(jsonPath("$[1].id").value(2L))
        //.andExpect(jsonPath("$[1].name").value("Jane Doe"))
        ;
    }


    @Test
    void testBadRequest() throws Exception {
        mockMvc.perform(get(ENDPOINT_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}