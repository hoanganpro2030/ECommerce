package vn.elca.training.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

/**
 * @author vlp
 */

@Entity
public class Project {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Group group;

    @Column(nullable = false, unique = true)
    @NotNull(message = "Project number can't be null")
    private Integer projectNumber;

    @Column(nullable = false, length = 50)
    @NotNull(message = "Name can't be null")
    private String name;

    @Column(nullable = false, length = 50)
    @NotNull(message = "Customer can't be null")
    private String customer;

    @Column(nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status can't be null")
    private Status status;

    @Column(nullable = false)
    @NotNull(message = "Start day can't be null")
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Column(nullable = false)
    @Version
    private Integer version;

    @ManyToMany
    @JoinTable(
            name = "PROJECT_EMPLOYEE",
            joinColumns = {@JoinColumn(name = "PROJECT_ID")},
            inverseJoinColumns = {@JoinColumn(name = "EMPLOYEE_id")}
    )
    @JsonIgnore
    private Set<Employee> employees;

    public Project() {
    }

    public Project(Group group, Integer projectNumber, String name, String customer, Status status, LocalDate startDate, LocalDate endDate, Integer version, Set<Employee> employees) {
        this.group = group;
        this.projectNumber = projectNumber;
        this.name = name;
        this.customer = customer;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.version = version;
        this.employees = employees;
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

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return id.equals(project.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
