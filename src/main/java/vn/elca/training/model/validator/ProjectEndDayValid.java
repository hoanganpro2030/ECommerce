package vn.elca.training.model.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ProjectEndDateConstraintValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProjectEndDayValid {
    String message() default "Project startDay must not be greater than the project endDate.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
