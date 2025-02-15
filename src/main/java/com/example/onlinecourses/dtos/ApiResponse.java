package com.example.onlinecourses.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;
}
