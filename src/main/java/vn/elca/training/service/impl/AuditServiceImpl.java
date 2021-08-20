/*
 * TaskAuditService
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

package vn.elca.training.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.elca.training.model.entity.Task;
import vn.elca.training.model.entity.TaskAudit;
import vn.elca.training.model.entity.TaskAudit.AuditType;
import vn.elca.training.model.entity.TaskAudit.Status;
import vn.elca.training.repository.TaskAuditRepository;
import vn.elca.training.service.AuditService;

import javax.transaction.Transactional;

/**
 * @author vlp
 */
@Service
@Transactional
public class AuditServiceImpl implements AuditService {
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private TaskAuditRepository taskAuditRepository;

    @Override
    public void saveAuditDataForTask(Task task, AuditType auditType, Status status, String message) {
        try {
            TaskAudit taskAudit = new TaskAudit(task, auditType, status, message);
            taskAuditRepository.save(taskAudit);
        } catch (Exception e) {
            // it's OK to log exception here because this is only audit data, it contains information for tracing error
            // not business.
            logger.error("Can't " + auditType.name().toLowerCase() + " audit data for Task with Name " + task.getName(),
                    e);
        }
    }
}
