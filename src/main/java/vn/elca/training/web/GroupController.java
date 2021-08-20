package vn.elca.training.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.elca.training.model.dto.GroupDto;
import vn.elca.training.service.GroupService;
import vn.elca.training.util.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://127.0.0.1:4200")
@RestController
@RequestMapping("/group")
public class GroupController {
    @Autowired
    GroupService groupService;

    @GetMapping("/list")
    @ResponseBody
    public List<GroupDto> list() {
        return groupService.findAll()
                .stream()
                .map(Mapper::groupToGroupDto)
                .collect(Collectors.toList());
    }
}
