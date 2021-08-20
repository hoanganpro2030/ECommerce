package vn.elca.training.model.exception;

public class ProjectNumberAlreadyExistedExcetion extends Exception {
    private Integer projectNumber;
    public ProjectNumberAlreadyExistedExcetion(Integer projectNumber, String message) {
//        super("Project not existed " + projectNumber);
        super(message);
    }

}
