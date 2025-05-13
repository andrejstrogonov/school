package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student createdStudent = studentService.addStudent(student.getName(), student.getAge());
        return ResponseEntity.ok(createdStudent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student student = studentService.getStudent(id);
        return student != null ? ResponseEntity.ok(student) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        Student updatedStudent = studentService.updateStudent(id, student.getName(), student.getAge());
        return updatedStudent != null ? ResponseEntity.ok(updatedStudent) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        boolean deleted = studentService.deleteStudent(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/age/{age}")
    public ResponseEntity<List<Student>> getStudentsByAge(@PathVariable int age) {
        List<Student> students = studentService.getStudentsByAge(age);
        return students.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(students);
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<Faculty> getFacultyByStudent(@PathVariable Long id) {
        Student student = studentService.getStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        Faculty faculty = student.getFaculty();
        return faculty != null ? ResponseEntity.ok(faculty) : ResponseEntity.notFound().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalStudentCount() {
        long count = studentService.getTotalStudentCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/average-age")
    public ResponseEntity<Double> getAverageStudentAge() {
        double averageAge = studentService.getAverageStudentAge();
        return ResponseEntity.ok(averageAge);
    }

    @GetMapping("/last-five")
    public ResponseEntity<List<Student>> getLastFiveStudents() {
        List<Student> students = studentService.getLastFiveStudents();
        return students.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(students);
    }

    @GetMapping("/names-starting-with-a")
    public ResponseEntity<List<String>> getStudentNamesStartingWithA() {
        List<String> studentNames = studentService.getStudentNamesStartingWithA();
        return ResponseEntity.ok(studentNames);
    }
}
