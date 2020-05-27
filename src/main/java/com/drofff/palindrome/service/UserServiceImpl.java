package com.drofff.palindrome.service;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.enums.Role;
import com.drofff.palindrome.exception.PalindromeException;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.repository.UserRepository;
import com.drofff.palindrome.type.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.drofff.palindrome.utils.MailUtils.getCredentialsMail;
import static com.drofff.palindrome.utils.ValidationUtils.*;
import static java.lang.Boolean.TRUE;

@Service
public class UserServiceImpl implements UserService {

    private static final int ALL_USERS_PAGE_SIZE = 12;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, MailService mailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    @Override
    public Page<User> getAllUsersAtPage(int page) {
        Pageable pageable = PageRequest.of(page, ALL_USERS_PAGE_SIZE);
        return userRepository.findAll(pageable);
    }

    @Override
    public long countUsers() {
        return userRepository.count();
    }

    @Override
    public List<Role> getAllRoles() {
        Role[] roles = Role.values();
        return Arrays.asList(roles);
    }

    @Override
    public void markUserWithIdAsActive(String userId) {
        User user = getUserById(userId);
        user.setActive(TRUE);
        userRepository.save(user);
    }

    @Override
    public void generateUserOfRole(String username, Role role) {
        User user = userOfUsernameWithRole(username, role);
        user.setActive(TRUE);
        String generatedPassword = generatePassword();
        user.setPassword(generatedPassword);
        createUser(user);
        sendCredentialsByMail(user.getUsername(), generatedPassword);
    }

    private User userOfUsernameWithRole(String username, Role role) {
        User user = new User();
        user.setUsername(username);
        user.setRole(role);
        return user;
    }

    private String generatePassword() {
        return UUID.randomUUID().toString();
    }

    private void sendCredentialsByMail(String username, String password) {
        Mail credentialsMail = getCredentialsMail(username, password);
        mailService.sendMailTo(credentialsMail, username);
    }

    @Override
    public void createUser(User user) {
        validate(user);
        validateHasUniqueUsername(user);
        encodeUserPassword(user);
        userRepository.save(user);
    }

    private void validateHasUniqueUsername(User user) {
        if(existsUserWithUsername(user.getUsername())) {
            throw new ValidationException("User with such username already exists");
        }
    }

    private boolean existsUserWithUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public void updatePasswordForUserWithId(String password, String userId) {
        User user = getUserById(userId);
        user.setPassword(password);
        validate(user);
        encodeUserPassword(user);
        userRepository.save(user);
    }

    private void encodeUserPassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    @Override
    public User getUserById(String id) {
        validateNotNull(id, "User id should be provided");
        return userRepository.findById(id)
                .orElseThrow(() -> new PalindromeException("User with such id doesn't exist"));
    }

    @Override
    public User getUserByUsername(String username) {
        validateNotNull(username, "Username should be provided");
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ValidationException("User with such username doesn't exist"));
    }

    @Override
    public void validateIsPasswordOfUser(String password, User user) {
        validateNotNullEntityHasId(user);
        validateNotNull(password, "Password should be provided");
        if(isNotPasswordOfUser(password, user)) {
            throw new ValidationException("Incorrect password");
        }
    }

    private boolean isNotPasswordOfUser(String password, User user) {
        return !isPasswordOfUser(password, user);
    }

    private boolean isPasswordOfUser(String password, User user) {
        String originalPassword = user.getPassword();
        return passwordEncoder.matches(password, originalPassword);
    }

}
