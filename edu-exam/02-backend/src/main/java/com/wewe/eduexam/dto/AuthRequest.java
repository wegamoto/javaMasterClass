package com.wewe.eduexam.dto;

import com.wewe.eduexam.model.Role;
import lombok.*;

@Getter @Setter
@Data
public class AuthRequest {
    private String username;
    private String password;
    private Role role;
}


