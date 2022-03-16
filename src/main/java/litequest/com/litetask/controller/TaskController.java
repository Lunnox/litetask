package litequest.com.litetask.controller;

import com.fasterxml.jackson.annotation.JsonView;
import litequest.com.litetask.domain.Task;
import litequest.com.litetask.domain.User;
import litequest.com.litetask.domain.views.Views;
import litequest.com.litetask.dto.TaskPageDto;
import litequest.com.litetask.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("tasks")
public class TaskController {
    public static final int PAGE_SIZE=5;
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @JsonView(Views.Full.class)
    public TaskPageDto getTasks(
            @PageableDefault(size = PAGE_SIZE,sort = {"id"} , direction = Sort.Direction.DESC) Pageable pageable
    ){
        return taskService.findAll(pageable);
    }

    @GetMapping("{id}")
    @JsonView(Views.Full.class)
    public Task getTask(@PathVariable("id") Task task){
        return task;
    }

    @PostMapping
    public Task create(
            @RequestBody Task task,
            @AuthenticationPrincipal User user

    ) throws IOException {
       return taskService.create(task,user);
    }

    @PutMapping("{id}")
    public Task update(
            @PathVariable("id") Task taskFromDB,
            @RequestBody Task task
    ) throws IOException {
       
    }
}
