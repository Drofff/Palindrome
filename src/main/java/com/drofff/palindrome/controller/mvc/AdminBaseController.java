package com.drofff.palindrome.controller.mvc;

import com.drofff.palindrome.service.ChangeRequestService;
import com.drofff.palindrome.service.UserBlockService;
import com.drofff.palindrome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.drofff.palindrome.cache.OnlineCache.getOnlineCounterValue;
import static com.drofff.palindrome.constants.EndpointConstants.RELATIVE_HOME_ENDPOINT;
import static com.drofff.palindrome.constants.ParameterConstants.*;
import static com.drofff.palindrome.utils.AuthenticationUtils.getCurrentUser;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminBaseController {

    private final UserService userService;
    private final UserBlockService userBlockService;
    private final ChangeRequestService changeRequestService;

    @Autowired
    public AdminBaseController(UserService userService, UserBlockService userBlockService,
                               ChangeRequestService changeRequestService) {
        this.userService = userService;
        this.userBlockService = userBlockService;
        this.changeRequestService = changeRequestService;
    }

    @GetMapping(RELATIVE_HOME_ENDPOINT)
    public String getAdminHomePage(@RequestParam(required = false, name = MESSAGE_PARAM) String message,
                                   Model model) {
        model.addAttribute(MESSAGE_PARAM, message);
        model.addAttribute(USER_PARAM, getCurrentUser());
        putAdminStatisticsIntoModel(model);
        return "adminHomePage";
    }

    private void putAdminStatisticsIntoModel(Model model) {
        model.addAttribute("online_counter", getOnlineCounterValue());
        model.addAttribute("users_count", userService.countUsers());
        model.addAttribute("blocked_users_count", userBlockService.countBlockedUsers());
        model.addAttribute(REQUESTS_PARAM, changeRequestService.countChangeRequests());
    }

}