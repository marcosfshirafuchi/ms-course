package com.marcosfshirafuchi.hroauth.dto;

import java.util.List;

public record UserDTO(Long id, String name, String email, String password, List<RoleDTO> roles) {
}