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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerRestTemplateTests {

    @Autowired
    private TestRestTemplate restTemplate;

    private Faculty faculty;

    @BeforeEach
    void setUp() {
        faculty = new Faculty();
        faculty.setName("Test Faculty");
        faculty.setColor("Blue");
    }

    @Test
    void testCreateFaculty() {
        ResponseEntity<Faculty> responseEntity = restTemplate.postForEntity("/faculty", faculty, Faculty.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    void testGetFaculty() {
        ResponseEntity<Faculty> postResponse = restTemplate.postForEntity("/faculty", faculty, Faculty.class);
        Long facultyId = postResponse.getBody().getId();

        ResponseEntity<Faculty> getResponse = restTemplate.getForEntity("/faculty/" + facultyId, Faculty.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().getName()).isEqualTo(faculty.getName());
    }

    @Test
    void testUpdateFaculty() {
        ResponseEntity<Faculty> postResponse = restTemplate.postForEntity("/faculty", faculty, Faculty.class);
        Long facultyId = postResponse.getBody().getId();
        faculty.setName("Updated Faculty");

        ResponseEntity<Faculty> putResponse = restTemplate.exchange("/faculty/" + facultyId, HttpMethod.PUT, new HttpEntity<>(faculty), Faculty.class);
        assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(putResponse.getBody().getName()).isEqualTo("Updated Faculty");
    }

    @Test
    void testDeleteFaculty() {
        ResponseEntity<Faculty> postResponse = restTemplate.postForEntity("/faculty", faculty, Faculty.class);
        Long facultyId = postResponse.getBody().getId();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange("/faculty/" + facultyId, HttpMethod.DELETE, null, Void.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<Faculty> getResponse = restTemplate.getForEntity("/faculty/" + facultyId, Faculty.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetFacultiesByColor() {
        restTemplate.postForEntity("/faculty", faculty, Faculty.class);
        
        ResponseEntity<List> responseEntity = restTemplate.getForEntity("/faculty/color/" + faculty.getColor(), List.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotEmpty();
    }

    @Test
    void testFindFaculties() {
        restTemplate.postForEntity("/faculty", faculty, Faculty.class);
        
        ResponseEntity<List> responseEntity = restTemplate.getForEntity("/faculty/find?nameOrColor=Blue", List.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotEmpty();
    }

    @Test
    void testGetStudentsByFaculty() {
        ResponseEntity<Faculty> postResponse = restTemplate.postForEntity("/faculty", faculty, Faculty.class);
        Long facultyId = postResponse.getBody().getId();

        ResponseEntity<List> responseEntity = restTemplate.getForEntity("/faculty/" + facultyId + "/students", List.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);  // Assuming no students initially, adjust accordingly.
    }
}