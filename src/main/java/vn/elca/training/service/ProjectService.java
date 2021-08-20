package vn.elca.training.service;

import vn.elca.training.model.entity.Project;
import vn.elca.training.model.entity.Status;
import vn.elca.training.model.exception.*;
import vn.elca.training.model.response.ListResponse;

import java.util.List;

/**
 * @author vlp
 */
public interface ProjectService {
    ListResponse<Project> findAll(int page);

    Project findOneById(Long id) throws ProjectNotFoundException;

    Project createProject(Project project) throws ProjectNumberAlreadyExistedExcetion, InValidVisasException;

    Project updateProject(Project project) throws ProjectNotFoundException, ProjectNumberCanNotBeChangedException, InValidVisasException;

    void deleteProject(Long id) throws ProjectNotFoundException, ProjectStatusNewCanNotBeDeleteException;

    void deleteMultiProjects(List<Long> ids) throws ProjectNotFoundException, ProjectStatusNewCanNotBeDeleteException;

    ListResponse<Project> searchProject(String searchText, Status searchStatus, int page);
}
