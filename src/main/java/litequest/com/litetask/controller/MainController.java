package litequest.com.litetask.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import litequest.com.litetask.domain.User;
import litequest.com.litetask.domain.views.Views;
import litequest.com.litetask.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
@RequestMapping("/")
public class MainController {
    private final TaskRepository tasks;

    @Value("${spring.profiles.active}")
    private String profile;
    private final ObjectWriter writer;

    @Autowired
    public MainController(TaskRepository tasks, ObjectMapper mapper) {
        this.tasks = tasks;
        this.writer = mapper.setConfig(mapper.getSerializationConfig()).writerWithView(Views.Full.class);

    }

    @GetMapping
    public String main(Model model, @AuthenticationPrincipal User user) throws JsonProcessingException {
        HashMap<Object, Object> data = new HashMap<>();
        if(user!=null) {
            data.put("profile", user);
            String tasks = writer.writeValueAsString(this.tasks.findAll());
            model.addAttribute("tasks", tasks);
        }else{
            model.addAttribute("tasks", "[]");
        }
        model.addAttribute("frontendData", data);
        model.addAttribute("isDevelop","dev".equals(profile));
        return "index";
    }


}
