package com.example.onlinecourses.enums;

import lombok.Getter;

@Getter
public enum ExamType {
    HOMEWORK("HOMEWORK"),
    PRACTICE("PRACTICE"),
    MIDTERM("MIDTERM"),
    FINAL("FINAL");

    private final String examType;

    ExamType(String examType) {
        this.examType = examType;
    }
}
