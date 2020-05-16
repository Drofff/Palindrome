package com.drofff.palindrome.controller.mvc;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.dto.BlockUserDto;
import com.drofff.palindrome.dto.CreateUserDto;
import com.drofff.palindrome.dto.UsersUserDto;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.grep.pattern.UserPattern;
import com.drofff.palindrome.mapper.CreateUserDtoMapper;
import com.drofff.palindrome.mapper.UsersUserDtoMapper;
import com.drofff.palindrome.service.UserBlockService;
import com.drofff.palindrome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.drofff.palindrome.constants.EndpointConstants.ADMIN_USERS_ENDPOINT;
import static com.drofff.palindrome.constants.PageableConstants.DEFAULT_PAGE;
import static com.drofff.palindrome.constants.ParameterConstants.*;
import static com.drofff.palindrome.grep.Filter.grepByPattern;
import static com.drofff.palindrome.utils.ModelUtils.*;

@Controller
@RequestMapping(ADMIN_USERS_ENDPOINT)
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminUserController {

	private static final String CREATE_USER_VIEW = "createUserPage";

	private static final String ROLES_PARAM = "roles";

	private final UserService userService;
	private final UserBlockService userBlockService;
	private final UsersUserDtoMapper usersUserDtoMapper;
	private final CreateUserDtoMapper createUserDtoMapper;

	@Autowired
	public AdminUserController(UserService userService, UserBlockService userBlockService,
							   UsersUserDtoMapper usersUserDtoMapper, CreateUserDtoMapper createUserDtoMapper) {
		this.userService = userService;
		this.userBlockService = userBlockService;
		this.usersUserDtoMapper = usersUserDtoMapper;
		this.createUserDtoMapper = createUserDtoMapper;
	}

	@GetMapping
	public String getUsersPage(@RequestParam(required = false, name = MESSAGE_PARAM) String message,
	                           @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page,
	                           UserPattern userPattern, Model model) {
		Page<User> allUsersPage = userService.getAllUsersAtPage(page);
		List<UsersUserDto> usersUserDtos = applyToPageContent(this::toUsersUserDto, allUsersPage);
		model.addAttribute("users", grepByPattern(usersUserDtos, userPattern));
		model.addAttribute(ROLES_PARAM, userService.getAllRoles());
		model.addAttribute(PATTERN_PARAM, userPattern);
		model.addAttribute(MESSAGE_PARAM, message);
		putPageIntoModel(allUsersPage, model);
		return "adminUsersPage";
	}

	private UsersUserDto toUsersUserDto(User user) {
		UsersUserDto usersUserDto = usersUserDtoMapper.toDto(user);
		boolean blocked = userBlockService.isUserBlocked(user);
		usersUserDto.setBlocked(blocked);
		return usersUserDto;
	}

	@GetMapping("/create")
	public String getCreateUserPage(Model model) {
		model.addAttribute(ROLES_PARAM, userService.getAllRoles());
		return CREATE_USER_VIEW;
	}

	@PostMapping("/create")
	public String createUser(CreateUserDto createUserDto, Model model) {
		User user = createUserDtoMapper.toEntity(createUserDto);
		try {
			userService.generateUserOfRole(user.getUsername(), user.getRole());
			return redirectToWithMessage("/admin/users", "User account has been successfully created");
		} catch(ValidationException e) {
			model.addAttribute(USER_PARAM, user);
			model.addAttribute(ROLES_PARAM, userService.getAllRoles());
			putValidationExceptionIntoModel(e, model);
			return CREATE_USER_VIEW;
		}
	}

	@PostMapping("/block")
	public String blockUserById(BlockUserDto blockUserDto, HttpServletRequest request) {
		try {
			User user = userService.getUserById(blockUserDto.getUserId());
			userBlockService.blockUserByReason(user, blockUserDto.getReason());
			return redirectToReferrerOfRequestWithMessage(request, "Successfully blocked user");
		} catch(ValidationException e) {
			return errorPageWithMessage(e.getMessage());
		}
	}

	@PostMapping("/unblock")
	public String unblockUserById(String id, HttpServletRequest request) {
		try {
			User user = userService.getUserById(id);
			userBlockService.unblockUser(user);
			return redirectToReferrerOfRequestWithMessage(request, "Successfully unblocked user");
		} catch(ValidationException e) {
			return errorPageWithMessage(e.getMessage());
		}
	}

}
