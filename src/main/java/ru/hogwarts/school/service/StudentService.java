package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Student;

import java.util.HashMap;
import java.util.Map;

public class StudentService {
    private Map<Long, Student> studentMap = new HashMap<>();
    private long currentId = 0;

    public Student addStudent(String name, int age) {
        long newId = ++currentId;
        Student student = new Student(newId, name, age);
        studentMap.put(newId, student);
        return student;
    }
    public Student getStudent(Long id) {
        return studentMap.get(id);
    }

    public Student updateStudent(Long id, String name, int age) {
        Student student = studentMap.get(id);
        if (student != null) {
            student.setName(name);
            student.setAge(age);
        }
        return student;
    }

    public boolean deleteStudent(Long id) {
        return studentMap.remove(id) != null;
    }

    public boolean containsStudent(Long id) {
        return studentMap.containsKey(id);
    }
}
