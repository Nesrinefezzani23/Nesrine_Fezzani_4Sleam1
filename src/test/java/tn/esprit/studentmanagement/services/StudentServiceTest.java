package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.repositories.StudentRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student1;
    private Student student2;

    @BeforeEach
    public void setUp() {
        student1 = new Student();
        student1.setIdStudent(1L);
        student1.setFirstName("Nesrine");
        student1.setLastName("Fezzani");

        student2 = new Student();
        student2.setIdStudent(2L);
        student2.setFirstName("Ahmed");
        student2.setLastName("Ben Ali");
    }

    @Test
    public void testGetAllStudents() {
        // Arrange
        List<Student> studentList = Arrays.asList(student1, student2);
        when(studentRepository.findAll()).thenReturn(studentList);

        // Act
        List<Student> result = studentService.getAllStudents();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Nesrine", result.get(0).getFirstName());
        assertEquals("Ahmed", result.get(1).getFirstName());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllStudentsEmpty() {
        // Arrange
        when(studentRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Student> result = studentService.getAllStudents();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    public void testGetStudentById() {
        // Arrange
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));

        // Act
        Student result = studentService.getStudentById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdStudent());
        assertEquals("Nesrine", result.getFirstName());
        assertEquals("Fezzani", result.getLastName());
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetStudentByIdNotFound() {
        // Arrange
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Student result = studentService.getStudentById(999L);

        // Assert
        assertNull(result);
        verify(studentRepository, times(1)).findById(999L);
    }

    @Test
    public void testSaveStudent() {
        // Arrange
        when(studentRepository.save(student1)).thenReturn(student1);

        // Act
        Student result = studentService.saveStudent(student1);

        // Assert
        assertNotNull(result);
        assertEquals("Nesrine", result.getFirstName());
        assertEquals("Fezzani", result.getLastName());
        verify(studentRepository, times(1)).save(student1);
    }

    @Test
    public void testSaveNewStudent() {
        // Arrange
        Student newStudent = new Student();
        newStudent.setFirstName("Mohamed");
        newStudent.setLastName("Salah");

        Student savedStudent = new Student();
        savedStudent.setIdStudent(3L);
        savedStudent.setFirstName("Mohamed");
        savedStudent.setLastName("Salah");

        when(studentRepository.save(newStudent)).thenReturn(savedStudent);

        // Act
        Student result = studentService.saveStudent(newStudent);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getIdStudent());
        assertEquals("Mohamed", result.getFirstName());
        assertEquals("Salah", result.getLastName());
        verify(studentRepository, times(1)).save(newStudent);
    }

    @Test
    public void testDeleteStudent() {
        // Arrange
        Long studentId = 1L;
        doNothing().when(studentRepository).deleteById(studentId);

        // Act
        studentService.deleteStudent(studentId);

        // Assert
        verify(studentRepository, times(1)).deleteById(studentId);
    }

    @Test
    public void testDeleteStudentMultipleTimes() {
        // Arrange
        Long studentId1 = 1L;
        Long studentId2 = 2L;
        doNothing().when(studentRepository).deleteById(anyLong());

        // Act
        studentService.deleteStudent(studentId1);
        studentService.deleteStudent(studentId2);

        // Assert
        verify(studentRepository, times(1)).deleteById(studentId1);
        verify(studentRepository, times(1)).deleteById(studentId2);
        verify(studentRepository, times(2)).deleteById(anyLong());
    }

    @Test
    public void testStudentRepositoryInteraction() {
        // Arrange
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1));

        // Act
        List<Student> students = studentService.getAllStudents();

        // Assert
        assertFalse(students.isEmpty());
        verify(studentRepository).findAll();
        verifyNoMoreInteractions(studentRepository);
    }
}