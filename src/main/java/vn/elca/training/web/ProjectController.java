package vn.elca.training.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.elca.training.model.dto.ProjectDto;
import vn.elca.training.model.entity.Employee;
import vn.elca.training.model.entity.Project;
import vn.elca.training.model.entity.Status;
import vn.elca.training.model.exception.*;
import vn.elca.training.model.response.ListResponse;
import vn.elca.training.model.response.MessageReponse;
import vn.elca.training.service.EmployeeService;
import vn.elca.training.service.ProjectService;
import vn.elca.training.util.Mapper;
import vn.elca.training.util.StatusCode;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gtn
 */
@CrossOrigin(origins = "http://127.0.0.1:4200")
@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private EmployeeService employeeService;

//    @PostMapping("/query")
//    @ResponseBody
//    public List<ProjectDto> query() {
//        return projectService.findAll()
//                .stream()
//                .map(Mapper::projectToProjectDto)
//                .collect(Collectors.toList());
//    }

    @GetMapping("/list/{page}")
    @ResponseBody
    public ListResponse<ProjectDto> list(@PathVariable int page) {
        ListResponse<Project> paginate = projectService.findAll(page);
        List<ProjectDto> response =  paginate.getData()
                .stream()
                .map(Mapper::projectToProjectDto)
                .collect(Collectors.toList());
        return new ListResponse<>(paginate.getCurrent(), paginate.getTotal(), 5, response);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ProjectDto getOne(@PathVariable Long id) throws ProjectNotFoundException {
        Project project = projectService.findOneById(id);
        ProjectDto dto = Mapper.projectToProjectDto(project);
        dto.setEmployees(project.getEmployees());
        dto.setGroup(project.getGroup());
        return dto;
    }

    @PostMapping("/create")
    @ResponseBody
    public ProjectDto createProject(@Valid @RequestBody ProjectDto projectDto) throws InValidVisasException, ProjectNumberAlreadyExistedExcetion {
        Project project = Mapper.projectDtoToProject(projectDto);
        Project result = projectService.createProject(project);
        return Mapper.projectToProjectDto(result);
    }

    @PutMapping("/update")
    @ResponseBody
    public ProjectDto updateProject(@Valid @RequestBody ProjectDto projectDto) throws ProjectNotFoundException, InValidVisasException, ProjectNumberCanNotBeChangedException {
        Project project = Mapper.projectDtoToProject(projectDto);
        Project result = projectService.updateProject(project);
        return Mapper.projectToProjectDto(result);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<MessageReponse> deleteProject(@PathVariable Long id) throws ProjectStatusNewCanNotBeDeleteException, ProjectNotFoundException {
        projectService.deleteProject(id);
        MessageReponse mp = new MessageReponse(StatusCode.PDELETE_OK.getCode(), Collections.singletonList("Delete successfully project id: " + id));
        return new ResponseEntity<>(mp, HttpStatus.OK);
    }

    @PostMapping("/delete-multi")
    @ResponseBody
    public ResponseEntity<MessageReponse> deleteMultiProjects(@RequestBody List<Long> listId) throws ProjectStatusNewCanNotBeDeleteException, ProjectNotFoundException {
        projectService.deleteMultiProjects(listId);
        MessageReponse mp = new MessageReponse(StatusCode.PDELETE_OK.getCode(), Collections.singletonList("Delete successfully projects"));
        return new ResponseEntity<>(mp, HttpStatus.OK);
    }

    @GetMapping("/search/{page}")
    @ResponseBody
    public ListResponse<ProjectDto> searchProject(@RequestParam(defaultValue = "") String searchText,
                                                  @RequestParam(defaultValue = "") Status searchStatus,
                                                  @PathVariable int page) {
        ListResponse<Project> paginate = projectService.searchProject(searchText, searchStatus, page);
        List<ProjectDto> response = paginate.getData().stream().map(l->Mapper.projectToProjectDto(l)).collect(Collectors.toList());
        return new ListResponse<>(paginate.getCurrent(), paginate.getTotal(), paginate.getSize(), response);
    }

    @GetMapping("/get-employee")
    @ResponseBody
    public List<Employee> listAllEmp(){
        return employeeService.getAllEmployee();
    }
}
