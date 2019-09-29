package litequest.com.litetask.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonView;
import litequest.com.litetask.domain.views.Views;


@JsonView(Views.Id.class)
public class WsEvnDto {

    private ObjectType objectType;
    private EventType eventType;
    @JsonRawValue
    private String body;

    public WsEvnDto() {
    }

    public WsEvnDto(ObjectType objectType, EventType eventType, String body) {
        this.objectType = objectType;
        this.eventType = eventType;
        this.body = body;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
