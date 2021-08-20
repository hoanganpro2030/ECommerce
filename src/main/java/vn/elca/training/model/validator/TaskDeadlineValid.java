/*
 * TaskDeadlineValid
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

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author vlp
 */
@Documented
@Constraint(validatedBy = TaskDeadlineConstraintValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TaskDeadlineValid {
    String message() default "Task deadline must not be greater than the project finishingDate.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
