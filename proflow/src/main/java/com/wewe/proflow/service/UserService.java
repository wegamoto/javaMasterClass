package com.wewe.proflow.service;

import com.wewe.proflow.dto.UserDTO;
import com.wewe.proflow.model.Role;
import com.wewe.proflow.model.User;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO dto);
    UserDTO getUserById(Long id);
    List<UserDTO> getAllUsers();
    UserDTO getByEmail(String email);
    void deleteUser(Long id);
    List<User> findByRole(Role role);
    void updateUser(UserDTO dto);        // <-- เพิ่ม
}
