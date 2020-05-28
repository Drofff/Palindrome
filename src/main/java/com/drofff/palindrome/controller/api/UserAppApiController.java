package com.drofff.palindrome.controller.api;

import com.drofff.palindrome.document.User;
import com.drofff.palindrome.document.UserProfile;
import com.drofff.palindrome.dto.RestResponseDto;
import com.drofff.palindrome.dto.UserProfileDto;
import com.drofff.palindrome.mapper.UserProfileDtoMapper;
import com.drofff.palindrome.service.DriverService;
import com.drofff.palindrome.service.PoliceService;
import com.drofff.palindrome.utils.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-app")
public class UserAppApiController {

    private final PoliceService policeService;
    private final DriverService driverService;
    private final UserProfileDtoMapper userProfileDtoMapper;

    @Value("${application.url}")
    private String applicationUrl;

    @Autowired
    public UserAppApiController(PoliceService policeService, DriverService driverService,
                                UserProfileDtoMapper userProfileDtoMapper) {
        this.policeService = policeService;
        this.driverService = driverService;
        this.userProfileDtoMapper = userProfileDtoMapper;
    }

    @GetMapping("/profile")
    public ResponseEntity<RestResponseDto> getUserProfile() {
        User currentUser = AuthenticationUtils.getCurrentUser();
        UserProfile userProfile = getUserProfile(currentUser);
        UserProfileDto userProfileDto = toUserProfileDtoOfUser(userProfile, currentUser);
        return ResponseEntity.ok(userProfileDto);
    }

    private UserProfile getUserProfile(User user) {
        return user.isPolice() ? policeService.getPoliceByUserId(user.getId()) :
                driverService.getDriverByUserId(user.getId());
    }

    private UserProfileDto toUserProfileDtoOfUser(UserProfile userProfile, User user) {
        UserProfileDto userProfileDto = userProfileDtoMapper.toDto(userProfile);
        userProfileDto.setEmail(user.getUsername());
        String photoUrl = generateUrlToPhotoOfUser(user);
        userProfileDto.setPhotoUrl(photoUrl);
        return userProfileDto;
    }

    private String generateUrlToPhotoOfUser(User user) {
        String baseSegment = "/api" + ( user.isPolice() ? "/polices/" : "/drivers/" );
        return applicationUrl + baseSegment + user.getId() + "/photo";
    }

}