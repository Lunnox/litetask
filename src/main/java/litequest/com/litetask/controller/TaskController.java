package litequest.com.litetask.controller;

import com.fasterxml.jackson.annotation.JsonView;
import litequest.com.litetask.domain.Task;
import litequest.com.litetask.domain.views.Views;
import litequest.com.litetask.dto.EventType;
import litequest.com.litetask.dto.ObjectType;
import litequest.com.litetask.repository.TaskRepository;
import litequest.com.litetask.utils.WsSender;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;


@RestController
@RequestMapping("tasks")
public class TaskController {
    private final TaskRepository tasks;
    private final BiConsumer<EventType, Task> sender;

    @Autowired
    public TaskController(TaskRepository tasks, WsSender sender) {
        this.tasks = tasks;
        this.sender = sender.getSender(ObjectType.TASK,Views.IdName.class);
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
        Task saveTask = tasks.save(task);

        sender.accept(EventType.CREATE,saveTask);


        return saveTask;
    }

    @PutMapping("{id}")
    public Task update(
            @PathVariable("id") Task taskFromDB,
            @RequestBody Task task
    ){
        BeanUtils.copyProperties(task,taskFromDB,"id");
        Task updateTask = tasks.save(taskFromDB);
        sender.accept(EventType.UPDATE,updateTask);
        return updateTask;
    }

    @DeleteMapping("{id}")
    public void delete( @PathVariable("id") Task task){

        tasks.delete(task);
        sender.accept(EventType.REMOVE,task);
    }
}
