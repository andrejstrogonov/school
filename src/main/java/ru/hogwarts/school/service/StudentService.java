package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(String name, int age) {
        logger.info("Called addStudent method");
        Student student = new Student(null, name, age);
        return studentRepository.save(student);
    }

    public Student getStudent(Long id) {
        logger.info("Called getStudent method");
        return studentRepository.findById(id).orElse(null);
    }

    public Student updateStudent(Long id, String name, int age) {
        logger.info("Called updateStudent method");
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            student.setName(name);
            student.setAge(age);
            return studentRepository.save(student);
        }
        return null;
    }

    public boolean deleteStudent(Long id) {
        logger.info("Called deleteStudent method");
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Student> getStudentsByAge(int age) {
        logger.info("Called getStudentsByAge method");
        return studentRepository.findAll().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }

    public void setFaculty(Long studentId, Faculty faculty) {
        logger.info("Called setFaculty method");
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            student.setFaculty(faculty);
            studentRepository.save(student);
        }
    }

    public long getTotalStudentCount() {
        logger.info("Called getTotalStudentCount method");
        return studentRepository.getTotalStudentCount();
    }

    public double getAverageStudentAge() {
        logger.info("Called getAverageStudentAge method");
        return studentRepository.getAverageStudentAge();
    }

    public List<Student> getLastFiveStudents() {
        logger.info("Called getLastFiveStudents method");
        return studentRepository.findLastFiveStudents();
    }

    public List<String> getStudentNamesStartingWithA() {
        logger.info("Called getStudentNamesStartingWithA method");
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .filter(name -> name.startsWith("A"))
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());
    }
    
    public double calculateAverageAge() {
        logger.info("Called calculateAverageAge method");
        OptionalDouble average = studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average();
        return average.orElse(0.0);
    }
}
