package com.example.employee;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/api/v1/")
@RestController
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ObjectMapper objectMapper;

    public EmployeeController(EmployeeService employeeService, ObjectMapper objectMapper) {
        this.employeeService = employeeService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEmployee(@RequestBody Employee employee) throws IOException {

        // Save the employee with the image information
        Employee employee1 = employeeService.addEmployee(employee);

        return new ResponseEntity<>(employee1, HttpStatus.CREATED);
    }


    //get all employees
    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getEmployeeList();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<?> getAllEmployeeById(@PathVariable("id") long id) {
        try {
            Optional<Employee> employee = employeeService.getEmployeeById(id);

            if (employee.isPresent()) {
                return ResponseEntity.ok(employee.get());
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while processing the request");
        }
    }


    @PutMapping("/employee/update/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable("id") long id, @RequestBody Employee updatedEmployee) {
        try {
            Employee updated = employeeService.updateEmployee(id, updatedEmployee);

            if (updated != null) {
                return ResponseEntity.ok(updated);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while processing the request");
        }
    }



    @DeleteMapping("/employee/delete/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") long id) {
        try {
            boolean deleted = employeeService.deleteEmployee(id);

            if (deleted) {
                return ResponseEntity.noContent().build(); // 204 No Content
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while processing the request");
        }
    }



}
