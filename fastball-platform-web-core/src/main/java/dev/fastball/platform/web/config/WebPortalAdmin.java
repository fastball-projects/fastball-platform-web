package dev.fastball.platform.web.config;

import dev.fastball.platform.web.WebPlatformConstants;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Geng Rong
 */
@Getter
@Setter
public class WebPortalAdmin {
    private Boolean init = false;

    private String defaultNickname = WebPlatformConstants.Admin.ADMIN_DEFAULT_NICKNAME;

    private String defaultUsername = WebPlatformConstants.Admin.ADMIN_DEFAULT_USERNAME;

    private String defaultPassword = WebPlatformConstants.Admin.ADMIN_DEFAULT_PASSWORD;

    private String defaultMobile = WebPlatformConstants.Admin.ADMIN_DEFAULT_MOBILE;

    private String adminRoleName = WebPlatformConstants.Admin.ADMIN_ROLE_NAME;

    private String adminRoleCode = WebPlatformConstants.Admin.ADMIN_ROLE_CODE;

    private String adminRoleDescription = WebPlatformConstants.Admin.ADMIN_ROLE_DESCRIPTION;
}
