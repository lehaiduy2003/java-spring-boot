package com.example.onlinecourses.dtos.responses.data;

import com.example.onlinecourses.dtos.models.RoleDTO;
import lombok.*;

import java.util.Date;

@Getter
@ToString
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDataDTO {
    private Long id;
    private String fullname;
    private String avatar;
    private String bio;
    private Date dob;
    private String createdAt;
    private String updatedAt;
    private String username;
    private RoleDTO[] roles;
}
