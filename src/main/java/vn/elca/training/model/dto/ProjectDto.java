package vn.elca.training.model.dto;

import vn.elca.training.model.entity.Employee;
import vn.elca.training.model.entity.Group;
import vn.elca.training.model.entity.Status;
import vn.elca.training.model.validator.ProjectEndDayValid;
import vn.elca.training.model.validator.ProjectStatusValid;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * @author gtn
 */
@ProjectEndDayValid
@ProjectStatusValid
public class ProjectDto {
    private Long id;
    @NotNull
    private Group group;

    @NotNull(message = "Project number can't be null")
    @Max(value = 9999, message = "Project number must less than 10000")
    private Integer projectNumber;

    @NotNull(message = "Name can't be null")
    private String name;

    @NotNull(message = "Customer can't be null")
    private String customer;

    @NotNull(message = "Status can't be null")
    private Status status;
    private Set<Employee> employees = new HashSet<>();

    @NotNull(message = "Start day can't be null")
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer version;

    public ProjectDto() {
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Integer getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(Integer projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

}
