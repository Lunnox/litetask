package litequest.com.litetask.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import litequest.com.litetask.domain.User;
import litequest.com.litetask.domain.views.Views;
import litequest.com.litetask.dto.TaskPageDto;
import litequest.com.litetask.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

import static litequest.com.litetask.controller.TaskController.PAGE_SIZE;

@Controller
@RequestMapping("/")
public class MainController {
    private final TaskService taskService;

    @Value("${spring.profiles.active}")
    private String profile;
    private final ObjectWriter writer;

    @Autowired
    public MainController(TaskService taskService, ObjectMapper mapper) {
        this.taskService = taskService;
        this.writer = mapper.setConfig(mapper.getSerializationConfig()).writerWithView(Views.Full.class);

    }

    @GetMapping
    public String main(Model model, @AuthenticationPrincipal User user) throws JsonProcessingException {
        HashMap<Object, Object> data = new HashMap<>();
        if(user!=null) {
            PageRequest pageRequest = PageRequest.of(0, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "id"));
            TaskPageDto allTasks = this.taskService.findAll(pageRequest);

            String tasks = writer.writeValueAsString(allTasks.getTasks());

            data.put("profile", user);
            data.put("currentPage", allTasks.getCurrentPage());
            data.put("totalPage", allTasks.getTotalPage());

            model.addAttribute("tasks", tasks);
        }else{
            model.addAttribute("tasks", "[]");
        }
        model.addAttribute("frontendData", data);
        model.addAttribute("isDevelop","dev".equals(profile));
        return "index";
    }


}
