/*
 * TaskService
 *
 * Project: KStA ZHQUEST
 *
 * Copyright 2014 by ELCA Informatik AG
 * Steinstrasse 21, CH-8036 Zurich
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of ELCA Informatik AG ("Confidential Information"). You
 * shall not disclose such "Confidential Information" and shall
 * use it only in accordance with the terms of the license
 * agreement you entered into with ELCA.
 */

package vn.elca.training.service;

import vn.elca.training.model.entity.Project;
import vn.elca.training.model.exception.DeadlineGreaterThanProjectFinishingDateException;

import java.time.LocalDate;
import java.util.List;

/**
 * @author vlp
 */
public interface TaskService {
    List<Project> findProjectsByTaskName(String taskName);

    List<String> listNumberOfTasks(List<Project> projects);

    List<String> listProjectNameOfRecentTasks();

    void updateDeadline(Long taskId, LocalDate deadline) throws DeadlineGreaterThanProjectFinishingDateException;

    void createTaskForProject(String taskName, LocalDate deadline, Project project);

}
