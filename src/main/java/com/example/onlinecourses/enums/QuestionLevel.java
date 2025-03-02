package com.example.onlinecourses.enums;

import lombok.Getter;

@Getter
public enum QuestionLevel {
    RECOGNITION("RECOGNITION"),
    COMPREHENSION("COMPREHENSION"),
    APPLICATION("APPLICATION"),
    ADVANCED("ADVANCED");

    private final String questionLevel;

    QuestionLevel(String questionLevel) {
        this.questionLevel = questionLevel;
    }
}
