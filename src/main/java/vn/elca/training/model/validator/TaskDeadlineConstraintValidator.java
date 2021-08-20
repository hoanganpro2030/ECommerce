/*
 * TaskDeadlineConstraintValidator
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

package vn.elca.training.model.validator;

import vn.elca.training.model.entity.Task;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author vlp
 */
public class TaskDeadlineConstraintValidator implements ConstraintValidator<TaskDeadlineValid, Task> {
    @Override
    public void initialize(TaskDeadlineValid constraintAnnotation) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(Task value, ConstraintValidatorContext context) {
        return value.getProject().getEndDate().isAfter(value.getDeadline());
    }

}
