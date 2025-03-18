package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.Student;
import edu.icet.ecom.entity.StudentEntity;
import edu.icet.ecom.repository.StudentRepository;
import edu.icet.ecom.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student createStudent(Student student) {
        StudentEntity studentEntity = mapToEntity(student);
        StudentEntity savedEntity = studentRepository.save(studentEntity);
        return mapToDTO(savedEntity);
    }

    @Override
    public Student getStudentById(Long id) {
        StudentEntity studentEntity = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        return mapToDTO(studentEntity);
    }

    @Override
    public List<Student> getAllStudents() {
        List<StudentEntity> studentEntities = studentRepository.findAll();
        return studentEntities.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Student updateStudent(Long id, Student student) {
        StudentEntity studentEntity = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        // Update entity properties
        studentEntity.setName(student.getName());
        studentEntity.setEmail(student.getEmail());
        studentEntity.setGpa(student.getGpa());

        StudentEntity updatedEntity = studentRepository.save(studentEntity);
        return mapToDTO(updatedEntity);
    }

    @Override
    public void deleteStudent(Long id) {
        StudentEntity studentEntity = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        studentRepository.delete(studentEntity);
    }

    // Helper methods to convert between DTO and Entity
    private Student mapToDTO(StudentEntity studentEntity) {
        return new Student(
                studentEntity.getId(),
                studentEntity.getName(),
                studentEntity.getEmail(),
                studentEntity.getGpa()
        );
    }

    private StudentEntity mapToEntity(Student student) {
        return new StudentEntity(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getGpa()
        );
    }
}