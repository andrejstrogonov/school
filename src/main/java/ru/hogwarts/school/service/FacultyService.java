package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty addFaculty(String name, String color) {
        Faculty faculty = new Faculty(null, name, color);
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(Long id) {
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty updateFaculty(Long id, String name, String color) {
        Optional<Faculty> optionalFaculty = facultyRepository.findById(id);
        if (optionalFaculty.isPresent()) {
            Faculty faculty = optionalFaculty.get();
            faculty.setName(name);
            faculty.setColor(color);
            return facultyRepository.save(faculty);
        }
        return null;
    }

    public boolean deleteFaculty(Long id) {
        if (facultyRepository.existsById(id)) {
            facultyRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean containsFaculty(Long id) {
        return facultyRepository.existsById(id);
    }

    public List<Faculty> getFacultiesByColor(String color) {
        return facultyRepository.findAll().stream()
                .filter(faculty -> faculty.getColor().equalsIgnoreCase(color))
                .collect(Collectors.toList());
    }

    public List<Faculty> findByNameOrColorIgnoreCase(String nameOrColor) {
        return facultyRepository.findAll().stream()
                .filter(faculty ->
                        faculty.getName().toLowerCase().contains(nameOrColor.toLowerCase()) ||
                                faculty.getColor().toLowerCase().contains(nameOrColor.toLowerCase()))
                .collect(Collectors.toList());
    }

}