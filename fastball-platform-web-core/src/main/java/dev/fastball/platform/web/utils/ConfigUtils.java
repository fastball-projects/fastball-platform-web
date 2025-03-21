package dev.fastball.platform.web.utils;

import dev.fastball.meta.utils.YamlUtils;
import dev.fastball.platform.web.config.WebPlatformConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;

import static dev.fastball.platform.FastballPlatformConstants.PLATFORM_CONFIG_PATH_PREFIX;
import static dev.fastball.platform.FastballPlatformConstants.PLATFORM_CONFIG_SUFFIX;
import static dev.fastball.platform.web.WebPlatformConstants.PLATFORM;


public class ConfigUtils {

    private static final ResourceLoader resourceResolver = new PathMatchingResourcePatternResolver();
    private static WebPlatformConfig instance;

    private ConfigUtils() {
    }

    public static WebPlatformConfig getWebPlatformConfig() {
        if (instance == null) {
            Resource menuResource = resourceResolver.getResource(PLATFORM_CONFIG_PATH_PREFIX + PLATFORM + PLATFORM_CONFIG_SUFFIX);
            try (InputStream inputStream = menuResource.getInputStream()) {
                instance = YamlUtils.fromYaml(inputStream, WebPlatformConfig.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }
}
