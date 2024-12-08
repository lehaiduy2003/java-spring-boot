package enums;

import lombok.Getter;

@Getter
public enum Role {
    STUDENT("Student"),
    TEACHER("Teacher");

    private final String role;

    Role(String role) {
        this.role = role;
    }

}