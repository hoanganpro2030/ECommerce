package vn.elca.training.model.validator;

import vn.elca.training.model.dto.ProjectDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ProjectEndDateConstraintValidator implements ConstraintValidator<ProjectEndDayValid, ProjectDto> {
    @Override
    public void initialize(ProjectEndDayValid constraintAnnotation) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(ProjectDto value, ConstraintValidatorContext context) {
        if (value.getEndDate() == null) {
            return true;
        }
        return value.getEndDate().isAfter(value.getStartDate());
    }
}
