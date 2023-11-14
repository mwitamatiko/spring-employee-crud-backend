package com.example.employee;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EmployeeService {
    private final EmployeeRepository  employeeRepository;
    private final ObjectMapper objectMapper;
    private List<Employee> employeeList;
    public EmployeeService(EmployeeRepository employeeRepository, ObjectMapper objectMapper){

        this.employeeRepository = employeeRepository;
        this.objectMapper = objectMapper;
    }

    /**
     *
     * @param employee
     * @return
     */
    public Employee addEmployee(Employee employee){
        Employee employee2 = null;
        try{
            Employee employee1 = new Employee();
            employee1.setId(employee.getId());
            employee1.setFirst_name(employee.getFirst_name());
            employee1.setLast_name(employee.getLast_name());
            employee1.setEmail(employee.getEmail());

            employee2 = employeeRepository.save(employee1);
            log.info("new employee created -----> {}",objectMapper.writeValueAsString(employee2));
        }catch (Exception e){
            log.error("Exception -----> {}",e.getLocalizedMessage());
        }
        return employee2;
    }


    /**
     *
     * @return
     */

    public List<Employee> getEmployeeList(){
        try{
            employeeList = employeeRepository.findAll();
            log.info("employee list -----> {}",objectMapper.writeValueAsString(employeeList));
            return employeeList;
        }catch (Exception e){
            log.error("Exception -----> {}",e.getLocalizedMessage());
            return null;
        }
    }

    /**
     *
     * @param id
     * @return
     */

    public Optional<Employee> getEmployeeById(long id) {
        try {
            Optional<Employee> employee = employeeRepository.findById(id);

            if (employee.isPresent()) {
                Employee actualEmployee = employee.get();
                log.info("employee -----> {}", objectMapper.writeValueAsString(actualEmployee));
            } else {
                log.info("Employee not found for id: {}", id);
            }
            return employee;

        } catch (Exception e) {
            log.error("Exception -----> {}", e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    /**
     *
     * @param id
     * @param updatedEmployee
     * @return
     */

    public Employee updateEmployee(long id,Employee updatedEmployee){

        try {
            Optional<Employee> optionalEmployee = employeeRepository.findById(id);

            if (optionalEmployee.isPresent()) {
                Employee existingEmployee = optionalEmployee.get();
                log.info("employee to be updated -----> {}", objectMapper.writeValueAsString(existingEmployee));

                //update employee
                existingEmployee.setFirst_name(updatedEmployee.getFirst_name());
                existingEmployee.setLast_name(updatedEmployee.getLast_name());
                existingEmployee.setEmail(updatedEmployee.getEmail());

                log.info("new employee details -----> {}", objectMapper.writeValueAsString(existingEmployee));


                Employee savedEmployee = employeeRepository.save(existingEmployee);
                log.info("employee updated -----> {}", objectMapper.writeValueAsString(savedEmployee));

                return savedEmployee;
            } else {
                log.info("Employee not found for id: {}", id);
                throw new EmployeeNotFoundException("Employee not found for id: "+id);
            }

        } catch (Exception e) {
            log.error("Exception -----> {}", e.getLocalizedMessage());
            throw new EmployeeNotFoundException("Could not update employee -----> "+e.getLocalizedMessage());
        }

    }

    public boolean deleteEmployee(long id) {
        try {
            Optional<Employee> optionalEmployee = employeeRepository.findById(id);

            if (optionalEmployee.isPresent()) {
                employeeRepository.deleteById(id);
                log.info("Employee with id {} deleted successfully", id);
                return true;
            } else {
                log.info("Employee not found for id: {}", id);
                throw new EmployeeNotFoundException("Employee not found for id: " + id);
            }
        } catch (Exception e) {
            log.error("Exception while deleting employee -----> {}", e.getLocalizedMessage());
            throw new EmployeeNotFoundException("Error while deleting employee"+ e);
        }
    }


}
