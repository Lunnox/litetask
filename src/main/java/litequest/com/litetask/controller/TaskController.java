package litequest.com.litetask.controller;

import litequest.com.litetask.exception.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("tasks")
public class TaskController {
    private int counter = 4;

    public List<Map<String,String>> tasks = new ArrayList<Map<String, String>>(){{
        add(new HashMap<String,String >(){{put("id","1"); put("theme","theme 1");}});
        add(new HashMap<String,String >(){{put("id","2"); put("theme","theme 2");}});
        add(new HashMap<String,String >(){{put("id","3"); put("theme","theme 3");}});
    }};
    @GetMapping
    public List<Map<String,String>> list(){
        return tasks;
    }

    @GetMapping("{id}")
    public Map<String,String> getOne(@PathVariable String id){
        return getTask(id);
    }

    private Map<String, String> getTask(@PathVariable String id) {
        return tasks.stream()
                .filter(task -> task.get("id").equals(id))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }

    @PostMapping
    public Map<String,String> create(@RequestBody Map<String,String> task){
        task.put("id", String.valueOf(counter++));
        tasks.add(task);
        return task;
    }

    @PutMapping("{id}")
    public Map<String,String> update(@PathVariable String id, @RequestBody Map<String,String> task){
        Map<String,String> taskFromDB = getTask(id);

        taskFromDB.putAll(task);
        taskFromDB.put("id",id);

        return taskFromDB;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        Map<String,String> task = getTask(id);
        tasks.remove(task);
    }
}
