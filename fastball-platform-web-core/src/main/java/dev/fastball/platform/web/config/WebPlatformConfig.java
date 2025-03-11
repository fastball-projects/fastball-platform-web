package dev.fastball.platform.web.config;

import lombok.Data;

import java.util.Map;

/**
 * @author gr@fastball.dev
 * @since 2023/1/4
 */
@Data
public class WebPlatformConfig {

    private String theme;
    private String logo;
    private String title;
    private String description;
    private String copyright;
    private String devServerUrl;
    private String menuIconfontUrl;
    private boolean enableNotice;
    private Map<String, String> customNpmDependencies;
    private Map<String, WebApplication> applications;

    private WebPortalAdmin admin = new WebPortalAdmin();
    private WebPortalUser user = new WebPortalUser();
}
