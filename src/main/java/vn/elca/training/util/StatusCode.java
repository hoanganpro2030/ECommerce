package vn.elca.training.util;

public enum StatusCode {
    PNUM_NULL("error.project-num.null"),
    PNUM_INVAL("error.project-num.inval"),
    PNAME_NULL("error.project-name.null"),
    PCUSTOMER_NULL("error.project-customer.null"),
    PGROUP_NULL("error.project-group.null"),
    PGROUP_INVAL("error.project-group.inval"),
    PEMP_INVAL("error.project-emp.inval"),
    PSTATUS_INVAL("error.project-status.inval"),
    PSTARTD_NULL("error.project-start.null"),
    PENDD_INVAL("error.project-end.inval"),
    PDELETE_NOTNEW("error.project-del.notnew"),
    PNUM_CHANGE("error.project-num.change"),
    PNOT_FOUND("error.project.not-found"),
    NOT_FOUND("error.entity.not-found"),
    PCONCUR_UPD("error.project.concur-upd"),
    PNUM_EXISTED("error.project-num.existed"),
    INTERNAL_ERROR("error.server.internal"),
    PDELETE_OK("success.project.delete"),
    DELETE_OK("success.entity.delete"),
    DELETE_NOT_FOUND("error.not-found.delete");
    private final String code;

    StatusCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

