package vn.elca.training.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "`GROUP`")
public class Group {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Employee groupLeader;

    @Column(nullable = false)
    @Version
    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getGroupLeader() {
        return groupLeader;
    }

    public void setGroupLeader(Employee groupLeader) {
        this.groupLeader = groupLeader;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
