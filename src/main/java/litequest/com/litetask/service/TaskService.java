package litequest.com.litetask.service;

import litequest.com.litetask.domain.Task;
import litequest.com.litetask.domain.User;
import litequest.com.litetask.domain.views.Views;
import litequest.com.litetask.dto.EventType;
import litequest.com.litetask.dto.MetaDto;
import litequest.com.litetask.dto.ObjectType;
import litequest.com.litetask.dto.TaskPageDto;
import litequest.com.litetask.repository.TaskRepository;
import litequest.com.litetask.utils.WsSender;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TaskService {

    private static String URL_PATTERN="https?:\\/\\/?[\\w\\d\\._\\-%\\/\\?=&#]+";
    private static String IMAGE_PATTERN="\\.(jpeg|jpg|gif|png)$";
    private static Pattern URL_REGEX = Pattern.compile(URL_PATTERN,Pattern.CASE_INSENSITIVE);
    private static Pattern IMG_REGEX = Pattern.compile(IMAGE_PATTERN,Pattern.CASE_INSENSITIVE);

    private final TaskRepository taskRepo;
    private final BiConsumer<EventType, Task> sender;

    @Autowired
    public TaskService(TaskRepository taskRepo, WsSender sender) {
        this.taskRepo = taskRepo;
        this.sender = sender.getSender(ObjectType.TASK, Views.IdName.class);
    }

    public Task create(Task task, User user) throws IOException {
        task.setCreateDate(LocalDateTime.now());
        //убрать в другой функционал
        fillMeta(task);
        task.setAuthor(user);
        Task saveTask = taskRepo.save(task);

        sender.accept(EventType.CREATE,saveTask);


        return saveTask;
    }

    public Task update(Task taskFromDB, Task task) throws IOException {
        BeanUtils.copyProperties(task,taskFromDB,"id");
        //убрать в другой функционал
        fillMeta(taskFromDB);

        Task updateTask = taskRepo.save(taskFromDB);
        sender.accept(EventType.UPDATE,updateTask);
        return updateTask;
    }

    public void delete(Task task) {
        taskRepo.delete(task);
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


    public TaskPageDto findAll(Pageable pageable) {
        Page<Task> page = taskRepo.findAll(pageable);
        return new TaskPageDto(page.getContent(), pageable.getPageNumber(), page.getTotalPages());
    }
}
