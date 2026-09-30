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
import ru.hogwarts.school.service.StudentService;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerWebMVCTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setName("Test Student");
        student.setAge(20);
    }

    @Test
    void testCreateStudent() throws Exception {
        given(studentService.addStudent(any(), any())).willReturn(student);

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Student"));
    }

    @Test
    void testGetStudent() throws Exception {
        given(studentService.getStudent(1L)).willReturn(student);

        mockMvc.perform(get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Student"));
    }

    @Test
    void testUpdateStudent() throws Exception {
        given(studentService.updateStudent(eq(1L), any(), any())).willReturn(student);

        mockMvc.perform(put("/student/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Student"));
    }

    @Test
    void testDeleteStudent() throws Exception {
        given(studentService.deleteStudent(1L)).willReturn(true);

        mockMvc.perform(delete("/student/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetStudentsByAge() throws Exception {
        given(studentService.getStudentsByAge(20)).willReturn(Collections.singletonList(student));

        mockMvc.perform(get("/student/age/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Student"));
    }

    @Test
    void testGetFacultyByStudent() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Test Faculty");
        student.setFaculty(faculty);

        given(studentService.getStudent(1L)).willReturn(student);

        mockMvc.perform(get("/student/1/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Faculty"));
    }

    @Test
    void testPrintStudentsParallelSuccess() throws Exception {
        given(studentService.getLastSixStudents()).willReturn(Arrays.asList(
                new Student(1L, "Student1", 20),
                new Student(2L, "Student2", 21),
                new Student(3L, "Student3", 22),
                new Student(4L, "Student4", 23),
                new Student(5L, "Student5", 24),
                new Student(6L, "Student6", 25)
        ));

        mockMvc.perform(get("/student/print-parallel"))
                .andExpect(status().isOk());
        
        verify(studentService).getLastSixStudents();
    }

    @Test
    void testPrintStudentsParallelInsufficientStudents() throws Exception {
        given(studentService.getLastSixStudents()).willReturn(Arrays.asList(
                new Student(1L, "Student1", 20),
                new Student(2L, "Student2", 21),
                new Student(3L, "Student3", 22),
                new Student(4L, "Student4", 23)
        ));

        mockMvc.perform(get("/student/print-parallel"))
                .andExpect(status().isBadRequest());
        
        verify(studentService).getLastSixStudents();
    }

    @Test
    void testPrintStudentsSynchronizedSuccess() throws Exception {
        given(studentService.getLastSixStudents()).willReturn(Arrays.asList(
                new Student(1L, "Student1", 20),
                new Student(2L, "Student2", 21),
                new Student(3L, "Student3", 22),
                new Student(4L, "Student4", 23),
                new Student(5L, "Student5", 24),
                new Student(6L, "Student6", 25)
        ));

        mockMvc.perform(get("/student/print-synchronized"))
                .andExpect(status().isOk());

        verify(studentService).getLastSixStudents();
    }

    @Test
    void testPrintStudentsSynchronizedInsufficientStudents() throws Exception {
        given(studentService.getLastSixStudents()).willReturn(Arrays.asList(
                new Student(1L, "Student1", 20),
                new Student(2L, "Student2", 21),
                new Student(3L, "Student3", 22),
                new Student(4L, "Student4", 23)
        ));

        mockMvc.perform(get("/student/print-synchronized"))
                .andExpect(status().isBadRequest());

        verify(studentService).getLastSixStudents();
    }
}
