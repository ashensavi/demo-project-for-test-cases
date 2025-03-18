package edu.icet.ecom.service;

import edu.icet.ecom.dto.Student;
import edu.icet.ecom.entity.StudentEntity;
import edu.icet.ecom.repository.StudentRepository;
import edu.icet.ecom.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student studentDto;
    private StudentEntity studentEntity;

    @BeforeEach
    public void setup() {
        // Initialize test data
        studentDto = new Student(1L, "John Doe", "john@example.com", 3.75);
        studentEntity = new StudentEntity(1L, "John Doe", "john@example.com", 3.75);
    }

    // Test methods will go here
    @Test
    public void testCreateStudent() {
        // Given
        when(studentRepository.save(any(StudentEntity.class))).thenReturn(studentEntity);

        // When
        Student savedStudent = studentService.createStudent(studentDto);

        // Then
        assertNotNull(savedStudent);
        assertEquals(studentDto.getId(), savedStudent.getId());
        assertEquals(studentDto.getName(), savedStudent.getName());
        assertEquals(studentDto.getEmail(), savedStudent.getEmail());
        assertEquals(studentDto.getGpa(), savedStudent.getGpa());

        verify(studentRepository, times(1)).save(any(StudentEntity.class));
    }

    @Test
    public void testGetStudentById_Success() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.of(studentEntity));

        // When
        Student foundStudent = studentService.getStudentById(1L);

        // Then
        assertNotNull(foundStudent);
        assertEquals(1L, foundStudent.getId());
        assertEquals("John Doe", foundStudent.getName());

        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetStudentById_NotFound() {
        // Given
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentService.getStudentById(99L);
        });

        assertEquals("Student not found with id: 99", exception.getMessage());
        verify(studentRepository, times(1)).findById(99L);
    }

    @Test
    public void testGetAllStudents() {
        // Given
        StudentEntity student2 = new StudentEntity(2L, "Jane Doe", "jane@example.com", 3.9);
        List<StudentEntity> studentEntities = Arrays.asList(studentEntity, student2);

        when(studentRepository.findAll()).thenReturn(studentEntities);

        // When
        List<Student> students = studentService.getAllStudents();

        // Then
        assertNotNull(students);
        assertEquals(2, students.size());
        assertEquals("John Doe", students.get(0).getName());
        assertEquals("Jane Doe", students.get(1).getName());

        verify(studentRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateStudent_Success() {
        // Given
        Student updatedStudentDto = new Student(1L, "John Updated", "john.updated@example.com", 3.8);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(studentEntity));

        // Setup the save to return the updated entity
        when(studentRepository.save(any(StudentEntity.class))).thenAnswer(invocation -> {
            StudentEntity savedEntity = invocation.getArgument(0);
            return savedEntity; // Return the entity being saved
        });

        // When
        Student result = studentService.updateStudent(1L, updatedStudentDto);

        // Then
        assertNotNull(result);
        assertEquals("John Updated", result.getName());
        assertEquals("john.updated@example.com", result.getEmail());
        assertEquals(3.8, result.getGpa());

        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(any(StudentEntity.class));
    }

    @Test
    public void testUpdateStudent_NotFound() {
        // Given
        Student updatedStudentDto = new Student(99L, "John Updated", "john.updated@example.com", 3.8);

        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentService.updateStudent(99L, updatedStudentDto);
        });

        assertEquals("Student not found with id: 99", exception.getMessage());
        verify(studentRepository, times(1)).findById(99L);
        verify(studentRepository, never()).save(any(StudentEntity.class));
    }

    @Test
    public void testDeleteStudent_Success() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.of(studentEntity));
        doNothing().when(studentRepository).delete(any(StudentEntity.class));

        // When
        studentService.deleteStudent(1L);

        // Then
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).delete(any(StudentEntity.class));
    }

    @Test
    public void testDeleteStudent_NotFound() {
        // Given
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentService.deleteStudent(99L);
        });

        assertEquals("Student not found with id: 99", exception.getMessage());
        verify(studentRepository, times(1)).findById(99L);
        verify(studentRepository, never()).delete(any(StudentEntity.class));
    }
}