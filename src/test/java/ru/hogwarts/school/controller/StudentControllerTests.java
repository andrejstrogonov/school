package ru.hogwarts.school.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerRestTemplateTests {

    @Autowired
    private TestRestTemplate restTemplate;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setName("Test Student");
        student.setAge(20);
    }

    @Test
    void testCreateStudent() {
        ResponseEntity<Student> responseEntity = restTemplate.postForEntity("/student", student, Student.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    void testGetStudent() {
        ResponseEntity<Student> postResponse = restTemplate.postForEntity("/student", student, Student.class);
        Long studentId = postResponse.getBody().getId();

        ResponseEntity<Student> getResponse = restTemplate.getForEntity("/student/" + studentId, Student.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().getName()).isEqualTo(student.getName());
    }

    @Test
    void testUpdateStudent() {
        ResponseEntity<Student> postResponse = restTemplate.postForEntity("/student", student, Student.class);
        Long studentId = postResponse.getBody().getId();
        student.setName("Updated Student");

        ResponseEntity<Student> putResponse = restTemplate.exchange("/student/" + studentId, HttpMethod.PUT, new HttpEntity<>(student), Student.class);
        assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(putResponse.getBody().getName()).isEqualTo("Updated Student");
    }

    @Test
    void testDeleteStudent() {
        ResponseEntity<Student> postResponse = restTemplate.postForEntity("/student", student, Student.class);
        Long studentId = postResponse.getBody().getId();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange("/student/" + studentId, HttpMethod.DELETE, null, Void.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<Student> getResponse = restTemplate.getForEntity("/student/" + studentId, Student.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetStudentsByAge() {
        restTemplate.postForEntity("/student", student, Student.class);
        
        ResponseEntity<List> responseEntity = restTemplate.getForEntity("/student/age/" + student.getAge(), List.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotEmpty();
    }

    @Test
    void testGetFacultyByStudent() {
        ResponseEntity<Student> postResponse = restTemplate.postForEntity("/student", student, Student.class);
        Long studentId = postResponse.getBody().getId();

        ResponseEntity<Faculty> responseEntity = restTemplate.getForEntity("/student/" + studentId + "/faculty", Faculty.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);  // Assuming no faculty, adjust accordingly.
    }
}