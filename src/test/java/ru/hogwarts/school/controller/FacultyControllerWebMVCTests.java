package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
class FacultyControllerWebMVCTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacultyService facultyService;

    @Autowired
    private ObjectMapper objectMapper;

    private Faculty faculty;

    @BeforeEach
    void setUp() {
        faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Test Faculty");
        faculty.setColor("Blue");
    }

    @Test
    void testCreateFaculty() throws Exception {
        given(facultyService.addFaculty(any(), any())).willReturn(faculty);

        mockMvc.perform(post("/faculty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Faculty"));
    }

    @Test
    void testGetFaculty() throws Exception {
        given(facultyService.getFaculty(1L)).willReturn(faculty);

        mockMvc.perform(get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Faculty"));
    }

    @Test
    void testUpdateFaculty() throws Exception {
        given(facultyService.updateFaculty(eq(1L), any(), any())).willReturn(faculty);

        mockMvc.perform(put("/faculty/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Faculty"));
    }

    @Test
    void testDeleteFaculty() throws Exception {
        given(facultyService.deleteFaculty(1L)).willReturn(true);

        mockMvc.perform(delete("/faculty/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetFacultiesByColor() throws Exception {
        given(facultyService.getFacultiesByColor("Blue")).willReturn(Collections.singletonList(faculty));

        mockMvc.perform(get("/faculty/color/Blue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Faculty"));
    }

    @Test
    void testFindFaculties() throws Exception {
        given(facultyService.findByNameOrColorIgnoreCase("Blue")).willReturn(Collections.singletonList(faculty));

        mockMvc.perform(get("/faculty/find?nameOrColor=Blue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Faculty"));
    }

    @Test
    void testGetStudentsByFaculty() throws Exception {
        Student student = new Student();
        student.setName("Test Student");
        faculty.setStudents(Collections.singletonList(student));

        given(facultyService.getFaculty(1L)).willReturn(faculty);

        mockMvc.perform(get("/faculty/1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Student"));
    }
}
