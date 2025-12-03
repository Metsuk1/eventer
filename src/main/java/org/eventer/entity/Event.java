package org.eventer.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Event {
    private Long id;
    private Long groupId;
    private String title;
    private String description;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;

    @Override
    public String toString() {
        return "Event [id=" + id + ", groupId=" + groupId + ", title=" + title + ", description=" + description;
    }
}
