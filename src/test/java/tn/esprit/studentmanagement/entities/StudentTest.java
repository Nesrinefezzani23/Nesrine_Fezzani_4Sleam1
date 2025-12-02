package tn.esprit.studentmanagement.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {

    private Student student;

    @BeforeEach
    public void setUp() {
        student = new Student();
    }

    @Test
    public void testStudentCreation() {
        // Arrange & Act
        student.setIdStudent(1L);
        student.setFirstName("Nesrine");
        student.setLastName("Fezzani");

        // Assert
        assertNotNull(student);
        assertEquals(1L, student.getIdStudent());
        assertEquals("Nesrine", student.getFirstName());
        assertEquals("Fezzani", student.getLastName());
    }

    @Test
    public void testStudentSettersAndGetters() {
        // Act
        student.setFirstName("Ahmed");
        student.setLastName("Ben Ali");

        // Assert
        assertEquals("Ahmed", student.getFirstName());
        assertEquals("Ben Ali", student.getLastName());
    }

    @Test
    public void testStudentNotNull() {
        // Assert
        assertNotNull(student);
    }

    @Test
    public void testStudentIdAssignment() {
        // Arrange
        Long expectedId = 100L;

        // Act
        student.setIdStudent(expectedId);

        // Assert
        assertEquals(expectedId, student.getIdStudent());
    }
}