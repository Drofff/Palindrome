package com.drofff.palindrome.controller;

import static com.drofff.palindrome.constants.PageableConstants.DEFAULT_PAGE;
import static com.drofff.palindrome.constants.ParameterConstants.DRIVER_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.EMAIL_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.MESSAGE_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.PATTERN_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.PHOTO_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.POLICE_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.USER_ID_PARAM;
import static com.drofff.palindrome.constants.ParameterConstants.USER_PARAM;
import static com.drofff.palindrome.enums.DriverIdType.DRIVER_ID;
import static com.drofff.palindrome.utils.ListUtils.applyToEachListElement;
import static com.drofff.palindrome.utils.ModelUtils.errorPageWithMessage;
import static com.drofff.palindrome.utils.ModelUtils.putPageIntoModel;
import static com.drofff.palindrome.utils.ModelUtils.putValidationExceptionIntoModel;
import static com.drofff.palindrome.utils.ModelUtils.redirectToReferrerOfRequestWithMessage;
import static com.drofff.palindrome.utils.ModelUtils.redirectToWithMessage;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.drofff.palindrome.document.Driver;
import com.drofff.palindrome.document.Police;
import com.drofff.palindrome.document.User;
import com.drofff.palindrome.dto.BlockUserDto;
import com.drofff.palindrome.dto.CreateUserDto;
import com.drofff.palindrome.dto.PoliceFatDto;
import com.drofff.palindrome.dto.UsersUserDto;
import com.drofff.palindrome.enums.DriverIdType;
import com.drofff.palindrome.exception.ValidationException;
import com.drofff.palindrome.grep.Filter;
import com.drofff.palindrome.grep.pattern.UserPattern;
import com.drofff.palindrome.mapper.CreateUserDtoMapper;
import com.drofff.palindrome.mapper.PoliceFatDtoMapper;
import com.drofff.palindrome.mapper.UsersUserDtoMapper;
import com.drofff.palindrome.service.AuthenticationService;
import com.drofff.palindrome.service.DriverService;
import com.drofff.palindrome.service.MappingsResolver;
import com.drofff.palindrome.service.PhotoService;
import com.drofff.palindrome.service.PoliceService;
import com.drofff.palindrome.service.UserBlockService;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminUserController {

	private static final String CREATE_USER_VIEW = "createUserPage";

	private static final String ROLES_PARAM = "roles";
	private static final String BLOCKED_PARAM = "blocked";

	private static final DriverIdType DEFAULT_DRIVER_PAGE_ID_TYPE = DRIVER_ID;

	private final AuthenticationService authenticationService;
	private final UserBlockService userBlockService;
	private final DriverService driverService;
	private final PhotoService photoService;
	private final PoliceService policeService;
	private final UsersUserDtoMapper usersUserDtoMapper;
	private final CreateUserDtoMapper createUserDtoMapper;
	private final PoliceFatDtoMapper policeFatDtoMapper;
	private final MappingsResolver mappingsResolver;

	@Autowired
	public AdminUserController(AuthenticationService authenticationService, UserBlockService userBlockService,
	                           DriverService driverService, PhotoService photoService, PoliceService policeService,
	                           UsersUserDtoMapper usersUserDtoMapper, CreateUserDtoMapper createUserDtoMapper,
	                           PoliceFatDtoMapper policeFatDtoMapper, MappingsResolver mappingsResolver) {
		this.authenticationService = authenticationService;
		this.userBlockService = userBlockService;
		this.driverService = driverService;
		this.photoService = photoService;
		this.policeService = policeService;
		this.usersUserDtoMapper = usersUserDtoMapper;
		this.createUserDtoMapper = createUserDtoMapper;
		this.policeFatDtoMapper = policeFatDtoMapper;
		this.mappingsResolver = mappingsResolver;
	}

	@GetMapping
	public String getUsersPage(@RequestParam(required = false, name = MESSAGE_PARAM) String message,
	                           @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page,
	                           UserPattern userPattern, Model model) {
		Page<User> allUsers = authenticationService.getAllUsersAtPage(page);
		List<UsersUserDto> usersUserDtos = applyToEachListElement(this::toUsersUserDto, allUsers.getContent());
		model.addAttribute("users", Filter.grepByPattern(usersUserDtos, userPattern));
		model.addAttribute(ROLES_PARAM, authenticationService.getAllRoles());
		model.addAttribute(PATTERN_PARAM, userPattern);
		model.addAttribute(MESSAGE_PARAM, message);
		putPageIntoModel(allUsers, model);
		return "adminUsersPage";
	}

	private UsersUserDto toUsersUserDto(User user) {
		UsersUserDto usersUserDto = usersUserDtoMapper.toDto(user);
		boolean blocked = userBlockService.isUserBlocked(user);
		usersUserDto.setBlocked(blocked);
		return usersUserDto;
	}

	@GetMapping("/driver/{id}")
	public String viewDriverProfile(@PathVariable String id, @RequestParam(required = false) DriverIdType type, Model model) {
		Driver driver = getDriverByIdOfType(id, type);
		User user = authenticationService.getUserById(driver.getUserId());
		model.addAttribute(DRIVER_PARAM, driver);
		model.addAttribute(EMAIL_PARAM, user.getUsername());
		model.addAttribute(USER_ID_PARAM, user.getId());
		String encodedPhoto = photoService.loadEncodedPhotoByUri(driver.getPhotoUri());
		model.addAttribute(PHOTO_PARAM, encodedPhoto);
		model.addAttribute(BLOCKED_PARAM, userBlockService.isUserBlocked(user));
		return "viewDriverProfilePage";
	}

	private Driver getDriverByIdOfType(String id, DriverIdType type) {
		return Optional.ofNullable(type)
				.orElse(DEFAULT_DRIVER_PAGE_ID_TYPE)
				.getFindDriverFunctionFromService(driverService)
				.apply(id);
	}

	@GetMapping("/police/{id}")
	public String viewPoliceProfile(@PathVariable String id, Model model) {
		Police police = policeService.getPoliceByUserId(id);
		model.addAttribute(POLICE_PARAM, toPoliceFatDto(police));
		String encodedPhoto = photoService.loadEncodedPhotoByUri(police.getPhotoUri());
		model.addAttribute(PHOTO_PARAM, encodedPhoto);
		User user = authenticationService.getUserById(id);
		boolean blocked = userBlockService.isUserBlocked(user);
		model.addAttribute(BLOCKED_PARAM, blocked);
		return "viewPoliceProfilePage";
	}

	private PoliceFatDto toPoliceFatDto(Police police) {
		PoliceFatDto policeFatDto = policeFatDtoMapper.toDto(police);
		return mappingsResolver.resolveMappings(police, policeFatDto);
	}

	@GetMapping("/create")
	public String getCreateUserPage(Model model) {
		model.addAttribute(ROLES_PARAM, authenticationService.getAllRoles());
		return CREATE_USER_VIEW;
	}

	@PostMapping("/create")
	public String createUser(CreateUserDto createUserDto, Model model) {
		User user = createUserDtoMapper.toEntity(createUserDto);
		try {
			authenticationService.createUser(user);
			return redirectToWithMessage("/admin/users", "User account has been successfully created");
		} catch(ValidationException e) {
			model.addAttribute(USER_PARAM, user);
			model.addAttribute(ROLES_PARAM, authenticationService.getAllRoles());
			putValidationExceptionIntoModel(e, model);
			return CREATE_USER_VIEW;
		}
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
