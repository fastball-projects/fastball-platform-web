package dev.fastball.platform.web;

import static dev.fastball.core.Constants.FASTBALL_RESOURCE_PREFIX;
import static dev.fastball.core.Constants.FASTBALL_VIEW_SUFFIX;

public interface WebPlatformConstants {

    String WEB_PLATFORM = "web";

    int DEV_SERVER_PORT = 12300;
    String DEV_SERVER_HOST = "localhost";

    interface Admin {
        String ADMIN_DEFAULT_NICKNAME = "系统管理员";
        String ADMIN_DEFAULT_USERNAME = "admin";
        String ADMIN_DEFAULT_PASSWORD = "admin";
        String ADMIN_DEFAULT_MOBILE = "18888888888";
        String ADMIN_ROLE_CODE = "admin";
        String ADMIN_ROLE_NAME = "系统管理员";
        String ADMIN_ROLE_DESCRIPTION = "系统管理员";
    }

    interface User {
        String USER_DEFAULT_PASSWORD = "fastball";
    }

    interface BusinessContext {
        String BUSINESS_CONTEXT_KEY_HEADER = "X-Business-Context-Key";
        String BUSINESS_CONTEXT_ID_HEADER = "X-Business-Context-Id";
    }

    interface Defaults {
        String LOGO_PATH = "/logo.svg";
        String DEV_SERVER_URL = "http://localhost:8080";
        String TITLE = "Fastball";
        String DESC = "一款面向 Java 开发人员的界面开发框架";
        String COPYRIGHT = "©2023 杭州范数科技有限公司";
    }

    interface Portal {

        String SOURCE_PATH = "/portal/";

        String PACKAGE_FILE_SOURCE_PATH = SOURCE_PATH + "package.json";

    }

    interface PermissionType {
        String APPLICATION = "Application";
        String MENU = "Menu";
        String VIEW = "View";
        String ACTION = "Action";
    }

    interface Generate {
        String[] NEED_COPY_RESOURCES = new String[]{
                "tsconfig.json", "vite.config.ts", "index.html", "public/logo.svg", "types/index.d.ts", "src/main.tsx", "src/main.scss", "src/change-password.tsx", "src/business-context.tsx", "src/login.tsx", "src/utils.ts", "src/login.scss", "src/message.tsx", "src/route-builder.tsx"
        };
    }

    String GENERATED_PATH = "generated-fastball";
    String COMPONENT_PATH = "src/";
    String ROUTES_PATH = "src/routes.tsx";
    String PACKAGE_FILE_NAME = "package.json";

    String CONFIG_FILE_NAME = "config.json";

    String ASSETS_FILE_NAME = "assets.json";
    String COMPONENT_SUFFIX = ".tsx";
    String COMPONENT_MAPPER_FILE_NAME = "component-mapper.tsx";

    String VIEW_FILE_PATH = "classpath*:/" + FASTBALL_RESOURCE_PREFIX + "**/*" + FASTBALL_VIEW_SUFFIX;
}
