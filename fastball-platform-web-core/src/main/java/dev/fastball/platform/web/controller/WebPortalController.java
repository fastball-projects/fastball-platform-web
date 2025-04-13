package dev.fastball.platform.web.controller;

import dev.fastball.core.Result;
import dev.fastball.platform.context.PortalContext;
import dev.fastball.platform.entity.User;
import dev.fastball.platform.web.model.ApplicationDTO;
import dev.fastball.platform.web.model.CurrentUser;
import dev.fastball.platform.web.service.WebPortalDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/portal/web/")
public class WebPortalController {

    private final WebPortalDataService portalDataService;


    @GetMapping("/currentUser")
    public Result<CurrentUser> getCurrentUser() {
        User user = PortalContext.currentUser();
        CurrentUser currentUser = new CurrentUser();
        currentUser.setNickname(user.getNickname());
        currentUser.setUsername(user.getUsername());
        currentUser.setApplications(portalDataService.getUserApplicationsWithMenu(PortalContext.currentUser().getId()));
        return Result.success(currentUser);
    }

    @GetMapping("/application")
    public Result<List<ApplicationDTO>> applications() {
        User user = PortalContext.currentUser();
        return Result.success(portalDataService.getUserApplications(user.getId()));
    }

    @GetMapping("/application/{applicationKey}")
    public Result<ApplicationDTO> application(String applicationKey) {
        return Result.success(portalDataService.getUserApplicationWithMenu(PortalContext.currentUser().getId(), applicationKey));
    }
}
