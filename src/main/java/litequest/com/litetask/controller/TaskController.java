package litequest.com.litetask.controller;

import com.fasterxml.jackson.annotation.JsonView;
import litequest.com.litetask.domain.Task;
import litequest.com.litetask.domain.views.Views;
import litequest.com.litetask.repository.TaskRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("tasks")
public class TaskController {
    private final TaskRepository tasks;

    @Autowired
    public TaskController(TaskRepository tasks) {
        this.tasks = tasks;
    }

    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Task> getTasks(){
        return tasks.findAll();
    }

    @GetMapping("{id}")
    @JsonView(Views.Full.class)
    public Task getTask(@PathVariable("id") Task task){
        return task;
    }

    @PostMapping
    public Task create(@RequestBody Task task){
        task.setCreateDate(LocalDateTime.now());
        return tasks.save(task);
    }

    @PutMapping("{id}")
    public Task update(
            @PathVariable("id") Task taskFromDB,
            @RequestBody Task task
    ){
        BeanUtils.copyProperties(task,taskFromDB,"id");
        return tasks.save(taskFromDB);
    }

    @DeleteMapping("{id}")
    public void delete( @PathVariable("id") Task task){
        tasks.delete(task);
    }
}
