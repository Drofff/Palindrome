package com.drofff.palindrome.service;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.enums.Role;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    Page<User> getAllUsersAtPage(int page);

    long countUsers();

    List<Role> getAllRoles();

    void markUserWithIdAsActive(String userId);

    void generateUserOfRole(String username, Role role);

    void createUser(User user);

    void updatePasswordForUserWithId(String password, String userId);

    User getUserById(String id);

    User getUserByUsername(String username);

    void validateIsPasswordOfUser(String password, User user);

}