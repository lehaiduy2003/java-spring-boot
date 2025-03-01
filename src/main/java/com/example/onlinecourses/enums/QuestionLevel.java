package com.example.onlinecourses.enums;

import lombok.Getter;

@Getter
public enum QuestionLevel {
    RECOGNITION("RECOGNITION"),
    COMPREHENSION("COMPREHENSION"),
    APPLICATION("APPLICATION"),
    ADVANCED("ADVANCED");

    private final String level;

    QuestionLevel(String level) {
        this.level = level;
    }
}
