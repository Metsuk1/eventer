package org.eventer.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Group {
    private Long id;
    private String name;

    @Override
    public String toString() {
        return "Group [id=" + id + ", name=" + name + "]";
    }
}
