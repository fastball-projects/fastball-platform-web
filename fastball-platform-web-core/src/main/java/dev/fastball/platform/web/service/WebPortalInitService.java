package dev.fastball.platform.web.service;

import org.springframework.beans.factory.InitializingBean;

public interface WebPortalInitService extends InitializingBean {

    void initMenusAndMenuPermissions();

    void initPermissions();

    @Override
    default void afterPropertiesSet() {
        initMenusAndMenuPermissions();
    }
}
