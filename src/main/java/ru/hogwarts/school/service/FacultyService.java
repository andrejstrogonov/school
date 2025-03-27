package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;

import java.util.HashMap;
import java.util.Map;

public class FacultyService {
    private Map<Long, Faculty> facultyMap = new HashMap<>();
    private long currentId = 0;

    public Faculty addFaculty(String name, String color) {
        long newId = ++currentId;
        Faculty faculty = new Faculty(newId, name, color);
        facultyMap.put(newId, faculty);
        return faculty;
    }

    public Faculty getFaculty(Long id) {
        return facultyMap.get(id);
    }

    public Faculty updateFaculty(Long id, String name, String color) {
        Faculty faculty = facultyMap.get(id);
        if (faculty != null) {
            faculty.setName(name);
            faculty.setColor(color);
        }
        return faculty;
    }

    public boolean deleteFaculty(Long id) {
        return facultyMap.remove(id) != null;
    }

    public boolean containsFaculty(Long id) {
        return facultyMap.containsKey(id);
    }
}

