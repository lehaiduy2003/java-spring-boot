package com.example.onlinecourses.enums;

import lombok.Getter;

@Getter
public enum QuestionType {
    MULTIPLE_CHOICE("MULTIPLE_CHOICE"),
    TRUE_FALSE("TRUE_FALSE"),
    SHORT_ANSWER("SHORT_ANSWER"),
    ESSAY("ESSAY");

    private final String type;

    QuestionType(String type) {
        this.type = type;
    }
}
