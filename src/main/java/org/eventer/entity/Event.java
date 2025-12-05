package org.eventer.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Event {

    @EqualsAndHashCode.Include
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
