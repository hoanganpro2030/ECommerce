package vn.elca.training.service.impl;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import vn.elca.training.model.entity.*;
import vn.elca.training.model.exception.*;
import vn.elca.training.model.response.ListResponse;
import vn.elca.training.repository.EmployeeRepository;
import vn.elca.training.repository.ProjectRepository;
import vn.elca.training.service.ProjectService;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author vlp
 */
@Service
@Transactional(rollbackFor = {InValidVisasException.class,
                              ProjectNotFoundException.class,
                              ProjectNumberAlreadyExistedExcetion.class,
                              ProjectStatusNewCanNotBeDeleteException.class,
                              ProjectNumberCanNotBeChangedException.class,
                              MethodArgumentNotValidException.class})
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ProjectRepository projectRepository;

    public Set<Employee> validateProjectEmployee(Set<Employee> employees) throws InValidVisasException {
        Set<String> visas = employees.stream().map(Employee::getVisa).collect(Collectors.toSet());
        List<Employee> employeesReal = (List<Employee>) employeeRepository.findAll(
                QEmployee.employee.visa.in(visas
                        .stream()
                        .map(String::toUpperCase)
                        .collect(Collectors.toSet())));

        if (employeesReal.size() < visas.size()) {
            Set<String> validVisas = employeesReal.stream().map(Employee::getVisa).collect(Collectors.toSet());
            visas.removeAll(validVisas);
            throw new InValidVisasException(String.join(",", visas));
        }
        return new HashSet<>(employeesReal);
    }

    @Override
    public ListResponse<Project> findAll(int page) {
        ListResponse<Project> response = projectRepository.findAllPagination(page,5);
        response.getData().sort(Comparator.comparingInt(Project::getProjectNumber));
        return response;
    }

    @Override
    public Project findOneById(Long id) throws ProjectNotFoundException {
        Project project = projectRepository.findOneProject(id);
        if (project == null) {
            throw new ProjectNotFoundException(id.toString());
        }
        Hibernate.initialize(project.getEmployees());
//        Hibernate.initialize(project.getGroup());
        return project;
    }

    @Override
    public Project createProject(Project project) throws ProjectNumberAlreadyExistedExcetion, InValidVisasException {
        Set<Employee> employees = validateProjectEmployee(project.getEmployees());
        project.setEmployees(employees);
        Project projectOld = projectRepository.findOne(QProject.project.projectNumber.eq(project.getProjectNumber()));
        if (projectOld == null) {
            return projectRepository.save(project);
        } else {
            throw new ProjectNumberAlreadyExistedExcetion(project.getProjectNumber(),"Create error");
        }
    }

    @Override
    public Project updateProject(Project project) throws ProjectNotFoundException, ProjectNumberCanNotBeChangedException, InValidVisasException {
        Set<Employee> employees = validateProjectEmployee(project.getEmployees());
        project.setEmployees(employees);
        Project projectOld = projectRepository.findOne(project.getId());
        if (projectOld == null) {
            throw new ProjectNotFoundException(project.getId().toString());
        }
        if (!project.getProjectNumber().equals(projectOld.getProjectNumber())) {
            throw new ProjectNumberCanNotBeChangedException("Update error");
        }
        return projectRepository.save(project);
    }

    @Override
    public void deleteProject(Long id) throws ProjectNotFoundException, ProjectStatusNewCanNotBeDeleteException {
        Project project = projectRepository.findOne(id);
        if (project == null) {
            throw new ProjectNotFoundException(id.toString());
        }
        if (Status.NEW.equals(project.getStatus())) {
            projectRepository.delete(id);
        } else {
            throw new ProjectStatusNewCanNotBeDeleteException(id.toString());
        }
    }

    @Override
    public void deleteMultiProjects(List<Long> ids) throws ProjectNotFoundException, ProjectStatusNewCanNotBeDeleteException {
        List<Project> projects = projectRepository.findAll(ids);
        if (ids.size() > projects.size()) {
            throw new ProjectNotFoundException(ids.toString());
        }
        long countInvalid = projects.stream().filter(p -> !Status.NEW.equals(p.getStatus())).count();
        if (countInvalid > 0L) {
            throw new ProjectStatusNewCanNotBeDeleteException(ids.toString());
        }
        projectRepository.delete(projects);
    }

    @Override
    public ListResponse<Project> searchProject(String searchText, Status searchStatus, int page) {

        ListResponse<Project> lp = projectRepository.searchProject(searchText, searchStatus, page);
        return lp;
    }
}
