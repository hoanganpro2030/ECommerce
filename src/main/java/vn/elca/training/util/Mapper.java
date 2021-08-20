package vn.elca.training.util;

import vn.elca.training.model.dto.EmployeeDto;
import vn.elca.training.model.dto.GroupDto;
import vn.elca.training.model.dto.ProjectDto;
import vn.elca.training.model.entity.Employee;
import vn.elca.training.model.entity.Group;
import vn.elca.training.model.entity.Project;

/**
 * @author gtn
 */
public class Mapper {
    public Mapper() {
        // Mapper utility class
    }

    public static ProjectDto projectToProjectDto(Project entity) {
        ProjectDto dto = new ProjectDto();
        dto.setId(entity.getId());
        dto.setProjectNumber(entity.getProjectNumber());
        dto.setName(entity.getName());
        dto.setCustomer(entity.getCustomer());
        dto.setStatus(entity.getStatus());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setVersion(entity.getVersion());
        return dto;
    }

    public static Project projectDtoToProject(ProjectDto dto) {
        Project project = new Project();
        project.setId(dto.getId());
        project.setGroup(dto.getGroup());
        project.setProjectNumber(dto.getProjectNumber());
        project.setName(dto.getName());
        project.setCustomer(dto.getCustomer());
        project.setStatus(dto.getStatus());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        if (dto.getVersion() == null) {
            project.setVersion(1);
        } else {
            project.setVersion(dto.getVersion());
        }
        if (dto.getEmployees() != null) {
            project.setEmployees(dto.getEmployees());
        }

        return project;
    }

    public static GroupDto groupToGroupDto(Group entity) {
        GroupDto dto = new GroupDto();
        dto.setId(entity.getId());
        dto.setGroupLeader(entity.getGroupLeader());
        dto.setVersion(entity.getVersion());
        return dto;
    }

    public static Group groupDtoToGroup(GroupDto dto) {
        Group group = new Group();
        group.setId(dto.getId());
        group.setGroupLeader(dto.getGroupLeader());
        group.setVersion(dto.getVersion());
        return group;
    }

    public static EmployeeDto employeeToEmployeeDto(Employee entity) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(entity.getId());
        dto.setBirthDay(entity.getBirthDay());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setProjects(entity.getProjects());
        dto.setVersion(entity.getVersion());
        dto.setVisa(entity.getVisa());
        return dto;
    }

    public static Employee employeeDtoToEmployee(EmployeeDto dto) {
        Employee employee = new Employee();
        employee.setId(dto.getId());
        employee.setBirthDay(dto.getBirthDay());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setProjects(dto.getProjects());
        employee.setVersion(dto.getVersion());
        employee.setVisa(dto.getVisa());
        return employee;
    }
}
