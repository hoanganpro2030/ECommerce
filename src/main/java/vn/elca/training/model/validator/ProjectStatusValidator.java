package vn.elca.training.model.validator;

import vn.elca.training.model.dto.ProjectDto;
import vn.elca.training.model.entity.Status;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class ProjectStatusValidator implements ConstraintValidator<ProjectStatusValid, ProjectDto> {
    @Override
    public void initialize(ProjectStatusValid constraintAnnotation) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(ProjectDto value, ConstraintValidatorContext context) {
        Status status = value.getStatus();
        List<Status> statusPosible = Arrays.asList(Status.PLA, Status.NEW, Status.INP, Status.FIN);
        for (Status st : statusPosible) {
            if (st.equals(status)) {
                return true;
            }
        }
        return false;
    }
}
