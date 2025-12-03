package org.eventer.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Group {
    private Long id;
    private String name;

    @Override
    public String toString() {
        return "Group [id=" + id + ", name=" + name + "]";
    }
}
