package org.eventer.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @EqualsAndHashCode.Include
    private Long id;

    private String userName;
    private String email;
    private String password;
    private String role;
    private Long groupId;

    @Override
    public String toString() {
        return "User [id=" + id + ", userName=" + userName + ", email=" + email;
    }
}
