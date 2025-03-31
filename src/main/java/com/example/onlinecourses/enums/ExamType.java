package com.example.onlinecourses.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ExamType {
    HOMEWORK("HOMEWORK"),
    PRACTICE("PRACTICE"),
    MIDTERM("MIDTERM"),
    FINAL("FINAL");

    private final String type;

    ExamType(String type) {
        this.type = type;
    }
}
