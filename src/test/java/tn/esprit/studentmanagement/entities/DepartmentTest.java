package tn.esprit.studentmanagement.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class DepartmentTest {

    private Department department;

    @BeforeEach
    public void setUp() {
        department = new Department();
    }

    @Test
    public void testDepartmentCreation() {
        // Arrange & Act
        department.setIdDepartment(1L);
        department.setName("Informatique");

        // Assert
        assertNotNull(department);
        assertEquals(1L, department.getIdDepartment());
        assertEquals("Informatique", department.getName());
    }

    @Test
    public void testDepartmentNameNotNull() {
        // Arrange
        department.setName("Génie Logiciel");

        // Assert
        assertNotNull(department.getName());
    }

    @Test
    public void testDepartmentSettersAndGetters() {
        // Act
        department.setName("Réseaux et Télécommunications");

        // Assert
        assertEquals("Réseaux et Télécommunications", department.getName());
    }
}