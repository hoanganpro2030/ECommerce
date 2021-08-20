package vn.elca.training.repository.custom;

import vn.elca.training.model.entity.Project;
import vn.elca.training.model.entity.Status;
import vn.elca.training.model.response.ListResponse;

import java.util.List;
import java.util.Set;

public interface ProjectRepositoryCustom {
    ListResponse<Project> searchProject(String searchText, Status searchStatus, int page);
    ListResponse<Project> findAllPagination(int position, int pageSize);
    Project findOneProject(Long id);

}
