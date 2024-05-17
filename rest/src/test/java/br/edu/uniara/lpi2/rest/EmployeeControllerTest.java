package br.edu.uniara.lpi2.rest;

import br.edu.uniara.lpi2.rest.controler.EmployeeController;
import br.edu.uniara.lpi2.rest.model.Employee;
import br.edu.uniara.lpi2.rest.model.EmployeePagingRepository;
import br.edu.uniara.lpi2.rest.model.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {

    @Mock
    private EmployeeRepository repository;

    @Mock
    private EmployeePagingRepository employeeRepository;

    @InjectMocks
    private EmployeeController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEmployeeById() {
        Employee employee = new Employee();
        employee.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(employee));

        Employee result = controller.one(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void testGetAllEmployees() {
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();
        Pageable pageable = PageRequest.of(0, 2);
        Page<Employee> page = new PageImpl<>(Arrays.asList(employee1, employee2));

        when(employeeRepository.findAll(pageable)).thenReturn(page);

        ResponseEntity<?> response = controller.all(0, 2);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(Object[].class, response.getBody());
        assertEquals(2, ((Object[]) response.getBody()).length);
    }

    @Test
    void testInsertEmployee() {
        Employee employee = new Employee();
        employee.setId(1L);

        when(repository.save(any(Employee.class))).thenReturn(employee);

        ResponseEntity<Employee> response = controller.insert(employee);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, Objects.requireNonNull(response.getBody()).getId());
    }

    @Test
    void testDeleteEmployee() {
        when(repository.existsById(1L)).thenReturn(true);

        ResponseEntity<?> response = controller.delete(1L);

        verify(repository, times(1)).deleteById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("1 was removed", response.getBody());
    }
}
