/*
 * ITaskRepository
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

package vn.elca.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;
import vn.elca.training.model.entity.Task;
import vn.elca.training.repository.custom.TaskRepositoryCustom;

/**
 * @author vlp
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, QueryDslPredicateExecutor<Task>, TaskRepositoryCustom {

}
