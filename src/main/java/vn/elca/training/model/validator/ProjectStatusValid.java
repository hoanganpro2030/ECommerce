package vn.elca.training.model.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ProjectStatusValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProjectStatusValid {
    String message() default "Status code invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
