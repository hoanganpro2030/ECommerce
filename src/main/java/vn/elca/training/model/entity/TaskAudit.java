/*
 * TaskAudit
 *
 * Project: Training
 *
 * Copyright 2015 by ELCA
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of ELCA. ("Confidential Information"). You
 * shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license
 * agreement you entered into with ELCA.
 */

package vn.elca.training.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author vlp
 */
@Entity
public class TaskAudit {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    @NotNull
    private AuditType auditType;
    @Column(nullable = false)
    @NotNull
    private Status status;
    @Column(nullable = false)
    @NotNull
    private String taskName;
    @Column(nullable = false)
    @NotNull
    private LocalDate taskDeadline;
    @Column
    private long projectId;
    @Column(length = 1000)
    private String message;


    public TaskAudit() {
    }

    public TaskAudit(Task task, AuditType auditType, Status status, String message) {
        setProjectId(task.getProject().getId());
        setTaskName(task.getName());
        setTaskDeadline(task.getDeadline());
        setAuditType(auditType);
        setStatus(status);
        setMessage(message);
    }

    public AuditType getAuditType() {
        return auditType;
    }

    public void setAuditType(AuditType auditType) {
        this.auditType = auditType;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public LocalDate getTaskDeadline() {
        return taskDeadline;
    }

    public void setTaskDeadline(LocalDate deadline) {
        this.taskDeadline = deadline;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public enum AuditType {
        INSERT, UPDATE, DELETE
    }

    public enum Status {
        SUCCESS, FAILED
    }
}
