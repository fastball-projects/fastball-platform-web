package dev.fastball.platform.web.controller;

import dev.fastball.core.Result;
import dev.fastball.core.component.DataResult;
import dev.fastball.platform.context.PortalContext;
import dev.fastball.platform.entity.User;
import dev.fastball.platform.web.feature.business.context.BusinessContextItem;
import dev.fastball.platform.web.feature.business.context.WebPortalBusinessContextAccessor;
import dev.fastball.platform.web.feature.message.Message;
import dev.fastball.platform.web.feature.message.WebPortalMessageAccessor;
import dev.fastball.platform.web.model.ApplicationDTO;
import dev.fastball.platform.web.model.CurrentUser;
import dev.fastball.platform.web.service.WebPortalBusinessContextService;
import dev.fastball.platform.web.service.WebPortalDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/portal/web/")
public class WebPortalController {

    private final WebPortalBusinessContextService businessContextService;
    private final WebPortalMessageAccessor messageAccessor;
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

    @GetMapping("/hasUnreadMessage")
    public Result<Boolean> hasUnreadMessage() {
        return Result.success(messageAccessor.hasUnreadMessage(PortalContext.currentUser()));
    }

    @GetMapping("/loadMessage")
    public Result<DataResult<Message>> loadMessage(@RequestParam Long current) {
        return Result.success(messageAccessor.loadMessages(PortalContext.currentUser(), current));
    }

    @PostMapping("/readMessage/{messageId}")
    public Result<Boolean> hasUnreadMessage(@PathVariable String messageId) {
        return Result.success(messageAccessor.readMessage(PortalContext.currentUser(), messageId));
    }

    @GetMapping("/business-context/{businessContextKey}")
    public Result<Collection<? extends BusinessContextItem>> loadBusinessContextItems(@PathVariable String businessContextKey) {
        WebPortalBusinessContextAccessor<?> businessContext = businessContextService.getBusinessContext(businessContextKey);
        if (businessContext != null) {
            return Result.success(businessContext.listBusinessContextItems());
        }
        return Result.success();
    }
}
