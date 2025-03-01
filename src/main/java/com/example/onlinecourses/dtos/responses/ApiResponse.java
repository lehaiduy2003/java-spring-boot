package com.example.onlinecourses.dtos.responses;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
}
