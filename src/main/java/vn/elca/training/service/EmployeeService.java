package vn.elca.training.service;

import vn.elca.training.model.entity.Employee;
import vn.elca.training.model.exception.InValidVisasException;

import java.util.List;
import java.util.Set;

public interface EmployeeService {
    Set<Employee> validateVisas(Set<String> visas) throws InValidVisasException;
    List<Employee> getAllEmployee();
}
