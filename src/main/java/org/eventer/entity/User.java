package org.eventer.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private Long id;
    private String userName;
    private String email;
    private String password;
    private String role;
    private Long groupId;

    @Override
    public String toString() {
        return "User [id=" + id + ", userName=" + userName + ", email=" + email + ", password=" + password;
    }
}
