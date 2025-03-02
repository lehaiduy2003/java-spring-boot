package com.example.onlinecourses.enums;

import lombok.Getter;

@Getter
public enum CourseLevel {
    ELEMENTARY("ELEMENTARY"),
    JUNIOR_HIGH("JUNIOR_HIGH"),
    HIGH_SCHOOL("HIGH_SCHOOL"),
    COLLEGE("COLLEGE"),
    MASTER("MASTER"),
    DOCTORATE("DOCTORATE"),
    BASIC("BASIC"),
    INTERMEDIATE("INTERMEDIATE"),
    ADVANCED("ADVANCED"),
    EXPERT("EXPERT"),
    ENGINEERING("ENGINEERING");

    private final String level;

    CourseLevel(String level) {
        this.level = level;
    }
}
