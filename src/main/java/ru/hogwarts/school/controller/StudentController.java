package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Print students' names in synchronized manner")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully printed student names in synchronized manner"),
        @ApiResponse(responseCode = "400", description = "Not enough students to perform synchronized printing")
    })
    @GetMapping("/print-synchronized")
    public ResponseEntity<Void> printStudentsSynchronized() {
        List<Student> students = studentService.getLastFiveStudents();
        
        if (students.size() < 6) {
            return ResponseEntity.badRequest().build(); // Not enough students
        }

        // Create a lock object for synchronization
        final Object lock = new Object();
        
        // Main thread prints first two names
        synchronized (lock) {
            System.out.println(students.get(0).getName());
            System.out.println(students.get(1).getName());
        }

        // Thread for third and fourth student
        Thread thread1 = new Thread(() -> {
            synchronized (lock) {
                System.out.println(students.get(2).getName());
                System.out.println(students.get(3).getName());
            }
        });

        // Thread for fifth and sixth student
        Thread thread2 = new Thread(() -> {
            synchronized (lock) {
                System.out.println(students.get(4).getName());
                System.out.println(students.get(5).getName());
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.status(500).build();
        }

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Print students' names in parallel")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully printed student names"),
        @ApiResponse(responseCode = "400", description = "Not enough students to perform parallel printing")
    })
    @GetMapping("/print-parallel")
    public ResponseEntity<Void> printStudentsParallel() {
        List<Student> students = studentService.getLastFiveStudents();
        
        if (students.size() < 5) {
            return ResponseEntity.badRequest().build(); // Not enough students
        }

        // Print first two names in the main thread
        System.out.println(students.get(0).getName());
        System.out.println(students.get(1).getName());

        // Create first thread to print next two names
        Thread thread1 = new Thread(() -> {
            System.out.println(students.get(2).getName());
            System.out.println(students.get(3).getName());
        });

        // Create second thread to print the last name
        Thread thread2 = new Thread(() -> {
            System.out.println(students.get(4).getName());
        });

        // Start both threads
        thread1.start();
        thread2.start();

        // Wait for threads to complete
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().build();
    }
}
