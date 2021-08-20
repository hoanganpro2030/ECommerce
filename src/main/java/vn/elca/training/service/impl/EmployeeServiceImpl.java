package vn.elca.training.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.elca.training.model.entity.Employee;
import vn.elca.training.model.entity.QEmployee;
import vn.elca.training.model.exception.InValidVisasException;
import vn.elca.training.repository.EmployeeRepository;
import vn.elca.training.service.EmployeeService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    public EmployeeRepository employeeRepository;

    @Override
    public Set<Employee> validateVisas(Set<String> visas) throws InValidVisasException {
        List<Employee> employees = (List<Employee>) employeeRepository.findAll(
                QEmployee.employee.visa.in(visas
                        .stream()
                        .map(String::toUpperCase)
                        .collect(Collectors.toSet())));

        if (employees.size() < visas.size()) {
            Set<String> validVisas = employees.stream().map(Employee::getVisa).collect(Collectors.toSet());
            visas.removeAll(validVisas);
            throw new InValidVisasException(String.join(",", visas));
        }

        return new HashSet<>(employees);
    }

    @Override
    public List<Employee> getAllEmployee() {
        return employeeRepository.findAll();
    }


}
