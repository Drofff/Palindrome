package com.drofff.palindrome.controller;

import static com.drofff.palindrome.constants.ParameterConstants.MESSAGE_PARAM;
import static com.drofff.palindrome.utils.ModelUtils.errorPageWithMessage;
import static com.drofff.palindrome.utils.ModelUtils.redirectToReferrerOfRequestWithMessage;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.dto.BlockUserDto;
import com.drofff.palindrome.dto.UsersUserDto;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.mapper.UsersUserDtoMapper;
import com.drofff.palindrome.service.AuthenticationService;
import com.drofff.palindrome.service.UserBlockService;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

	private final AuthenticationService authenticationService;
	private final UserBlockService userBlockService;
	private final UsersUserDtoMapper usersUserDtoMapper;

	@Autowired
	public UserController(AuthenticationService authenticationService, UserBlockService userBlockService,
	                      UsersUserDtoMapper usersUserDtoMapper) {
		this.authenticationService = authenticationService;
		this.userBlockService = userBlockService;
		this.usersUserDtoMapper = usersUserDtoMapper;
	}

	@GetMapping
	public String getUsersPage(@RequestParam(required = false, name = MESSAGE_PARAM) String message, Model model) {
		List<User> allUsers = authenticationService.getAllUsers();
		model.addAttribute("users", toUsersUserDtos(allUsers));
		model.addAttribute("roles", authenticationService.getAllRoles());
		model.addAttribute(MESSAGE_PARAM, message);
		return "adminUsersPage";
	}

	private List<UsersUserDto> toUsersUserDtos(List<User> users) {
		return users.stream()
				.map(this::toUsersUserDto)
				.collect(Collectors.toList());
	}

	private UsersUserDto toUsersUserDto(User user) {
		UsersUserDto usersUserDto = usersUserDtoMapper.toDto(user);
		boolean blocked = userBlockService.isUserBlocked(user);
		usersUserDto.setBlocked(blocked);
		return usersUserDto;
	}

	@PostMapping("/block")
	public String blockUserById(BlockUserDto blockUserDto, HttpServletRequest request) {
		try {
			User user = authenticationService.getUserById(blockUserDto.getUserId());
			userBlockService.blockUserByReason(user, blockUserDto.getReason());
			return redirectToReferrerOfRequestWithMessage(request, "Successfully blocked user");
		} catch(ValidationException e) {
			return errorPageWithMessage(e.getMessage());
		}
	}

	@PostMapping("/unblock")
	public String unblockUserById(String id, HttpServletRequest request) {
		try {
			User user = authenticationService.getUserById(id);
			userBlockService.unblockUser(user);
			return redirectToReferrerOfRequestWithMessage(request, "Successfully unblocked user");
		} catch(ValidationException e) {
			return errorPageWithMessage(e.getMessage());
		}
	}

}
