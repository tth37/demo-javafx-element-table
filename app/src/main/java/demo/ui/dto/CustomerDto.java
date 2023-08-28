package demo.ui.dto;

import demo.ui.enums.Role;

import java.util.List;

public class CustomerDto {
    public Long id;
    public String name;
    public String email;
    public List<Role> roles;
}
