package litequest.com.litetask.controller;

import litequest.com.litetask.domain.User;
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

    @Autowired
    public MainController(TaskRepository tasks) {
        this.tasks = tasks;
    }

    @GetMapping
    public String main(Model model, @AuthenticationPrincipal User user){
        HashMap<Object, Object> data = new HashMap<>();
        if(user!=null) {
            data.put("profile", user);
            data.put("tasks", tasks.findAll());
        }
        model.addAttribute("frontendData", data);
        model.addAttribute("isDevelop","dev".equals(profile));
        return "index";
    }


}
