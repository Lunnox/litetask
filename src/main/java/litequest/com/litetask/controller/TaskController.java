package litequest.com.litetask.controller;

import com.fasterxml.jackson.annotation.JsonView;
import litequest.com.litetask.domain.Task;
import litequest.com.litetask.domain.User;
import litequest.com.litetask.domain.views.Views;
import litequest.com.litetask.dto.EventType;
import litequest.com.litetask.dto.MetaDto;
import litequest.com.litetask.dto.ObjectType;
import litequest.com.litetask.repository.TaskRepository;
import litequest.com.litetask.utils.WsSender;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
@RequestMapping("tasks")
public class TaskController {

    private static String URL_PATTERN="https?:\\/\\/?[\\w\\d\\._\\-%\\/\\?=&#]+";
    private static String IMAGE_PATTERN="\\.(jpeg|jpg|gif|png)$";
    private static Pattern URL_REGEX = Pattern.compile(URL_PATTERN,Pattern.CASE_INSENSITIVE);
    private static Pattern IMG_REGEX = Pattern.compile(IMAGE_PATTERN,Pattern.CASE_INSENSITIVE);

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
    public Task create(
            @RequestBody Task task,
            @AuthenticationPrincipal User user

    ) throws IOException {
        task.setCreateDate(LocalDateTime.now());
        //убрать в другой функционал
        fillMeta(task);
        task.setAuthor(user);
        Task saveTask = tasks.save(task);

        sender.accept(EventType.CREATE,saveTask);


        return saveTask;
    }

    @PutMapping("{id}")
    public Task update(
            @PathVariable("id") Task taskFromDB,
            @RequestBody Task task
    ) throws IOException {
        BeanUtils.copyProperties(task,taskFromDB,"id");
        //убрать в другой функционал
        fillMeta(taskFromDB);

        Task updateTask = tasks.save(taskFromDB);
        sender.accept(EventType.UPDATE,updateTask);
        return updateTask;
    }

    @DeleteMapping("{id}")
    public void delete( @PathVariable("id") Task task){

        tasks.delete(task);
        sender.accept(EventType.REMOVE,task);
    }

    //Для предпросмотра ссылок, вынестив  другой функционал
    private void fillMeta(Task task) throws IOException {
        String text = task.getTheme();
        Matcher matcher = URL_REGEX.matcher(text);

        if (matcher.find()){
            String url = text.substring(matcher.start(), matcher.end());
            matcher = IMG_REGEX.matcher(url);
            task.setLink(url);

            if (matcher.find()){
                task.setLinkCover(url);
            }else if (!url.contains("youtu")){
                MetaDto meta=getMeta(url);

                task.setLinkCover(meta.getCover());
                task.setLinkTitle(meta.getTitle());
                task.setLinkDescription(meta.getDescription());
            }
        }

    }
    private MetaDto getMeta(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements title = doc.select("meta[name$=title],meta[property$=title]");
        Elements description = doc.select("meta[name$=description],meta[property$=description]");
        Elements cover = doc.select("meta[name$=image],meta[property$=image]");

        return new MetaDto(
                getContent(title.first()),
                getContent(description.first()),
                getContent(cover.first())
        );
    }
    private String getContent(Element elm){
        return elm==null?"" : elm.attr("content");

    }

}
