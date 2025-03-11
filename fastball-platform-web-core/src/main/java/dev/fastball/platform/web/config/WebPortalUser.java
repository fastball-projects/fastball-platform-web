package dev.fastball.platform.web.config;

import dev.fastball.platform.web.WebPlatformConstants;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Geng Rong
 */
@Getter
@Setter
public class WebPortalUser {
    private String defaultPassword = WebPlatformConstants.User.USER_DEFAULT_PASSWORD;
}
