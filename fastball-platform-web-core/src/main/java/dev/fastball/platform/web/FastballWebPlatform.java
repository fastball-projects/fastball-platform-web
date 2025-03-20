package dev.fastball.platform.web;

import com.google.auto.service.AutoService;
import dev.fastball.meta.component.ComponentInfo;
import dev.fastball.meta.utils.JsonUtils;
import dev.fastball.platform.core.FastballPlatform;
import dev.fastball.platform.core.exception.GenerateException;
import dev.fastball.platform.core.utils.ExecUtils;
import dev.fastball.platform.web.config.WebMenu;
import dev.fastball.platform.web.config.WebPlatformConfig;
import dev.fastball.platform.web.generator.ComponentCodeGenerator;
import dev.fastball.platform.web.generator.PackageJsonGenerator;
import dev.fastball.platform.web.model.FastballFrontendConfig;
import dev.fastball.platform.web.model.Route;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static dev.fastball.platform.web.WebPlatformConstants.*;
import static dev.fastball.platform.web.WebPlatformConstants.Generate.NEED_COPY_RESOURCES;


@Slf4j
@AutoService(FastballPlatform.class)
public class FastballWebPlatform implements FastballPlatform<WebPlatformConfig> {

    @Override
    public String platform() {
        return PLATFORM;
    }

    @Override
    public void run(File workspaceDir, List<ComponentInfo<?>> componentInfoList, OutputStream consoleInfoOut, OutputStream consoleErrorOut) {
        WebPlatformConfig config = loadPlatformConfig();
        copyProjectFiles(workspaceDir);
        PackageJsonGenerator.generate(workspaceDir, componentInfoList, WebPlatformConstants.Portal.PACKAGE_FILE_SOURCE_PATH, config);
        ComponentCodeGenerator.generate(workspaceDir, componentInfoList);
        generateRoutes(workspaceDir, componentInfoList, config);
        generateConfig(workspaceDir, config);
        try {
            ExecUtils.checkNode();
            ExecUtils.exec("npx pnpm i", workspaceDir, consoleInfoOut, consoleErrorOut);
            ExecUtils.execAsync("npm run dev --open", workspaceDir, consoleInfoOut, consoleErrorOut);
        } catch (IOException e) {
            throw new GenerateException(e);
        }
    }

    @Override
    public void build(File workspaceDir, File targetDir, List<ComponentInfo<?>> componentInfoList, OutputStream consoleInfoOut, OutputStream consoleErrorOut) {
        WebPlatformConfig config = loadPlatformConfig();
        copyProjectFiles(workspaceDir);
        PackageJsonGenerator.generate(workspaceDir, componentInfoList, WebPlatformConstants.Portal.PACKAGE_FILE_SOURCE_PATH, config);
        ComponentCodeGenerator.generate(workspaceDir, componentInfoList);
        generateRoutes(workspaceDir, componentInfoList, config);
        generateConfig(workspaceDir, config);
        try {
            ExecUtils.checkNode();
            ExecUtils.exec("npx pnpm i", workspaceDir, consoleInfoOut, consoleErrorOut);
            ExecUtils.exec("npm run build", workspaceDir, consoleInfoOut, consoleErrorOut);
            FileUtils.copyDirectory(new File(workspaceDir, "dist"), targetDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateConfig(File generatedCodeDir, WebPlatformConfig fastballConfig) {
        File packageJsonFile = new File(generatedCodeDir, CONFIG_FILE_NAME);
        FastballFrontendConfig config = new FastballFrontendConfig();
        String devServerUrl = fastballConfig.getDevServerUrl();
        if (!StringUtils.hasText(devServerUrl)) {
            devServerUrl = Defaults.DEV_SERVER_URL;
        }
        Map<String, String> devServerProxy = new HashMap<>();
        devServerProxy.put("/api", devServerUrl);
        devServerProxy.put("/favicon.ico", devServerUrl);
        if (StringUtils.hasText(fastballConfig.getLogo())) {
            devServerProxy.put(fastballConfig.getLogo(), devServerUrl);
            config.setLogo(fastballConfig.getLogo());
        } else {
            config.setLogo(Defaults.LOGO_PATH);
        }
        config.setDevServerProxy(devServerProxy);
        config.setEnableNotice(fastballConfig.isEnableNotice());
        if (StringUtils.hasText(fastballConfig.getTitle())) {
            config.setTitle(fastballConfig.getTitle());
        } else {
            config.setTitle(WebPlatformConstants.Defaults.TITLE);
        }
        if (StringUtils.hasText(fastballConfig.getDescription())) {
            config.setDescription(fastballConfig.getDescription());
        }
        if (StringUtils.hasText(fastballConfig.getMenuIconfontUrl())) {
            config.setMenuIconfontUrl(fastballConfig.getMenuIconfontUrl());
        }
        if (StringUtils.hasText(fastballConfig.getCopyright())) {
            config.setCopyright(fastballConfig.getCopyright());
        } else {
            config.setCopyright(WebPlatformConstants.Defaults.COPYRIGHT);
        }
        try {
            FileUtils.write(packageJsonFile, JsonUtils.toPrettyJson(config), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new GenerateException(e);
        }
    }

    private void copyProjectFiles(File workspaceDir) {
        for (String needCopyResource : NEED_COPY_RESOURCES) {
            try (InputStream inputStream = FastballWebPlatform.class.getResourceAsStream(WebPlatformConstants.Portal.SOURCE_PATH + needCopyResource)) {
                if (inputStream == null) {
                    throw new IllegalArgumentException("Copy resource[" + needCopyResource + "] not found");
                }
                FileUtils.copyInputStreamToFile(inputStream, new File(workspaceDir, needCopyResource));
            } catch (IOException e) {
                throw new GenerateException(e);
            }
        }
    }

//    private void copyProjectFiles(File generatedCodeDir) {
//        for (String needCopyResource : NEED_COPY_RESOURCES) {
//            GeneratorUtils.copyResourceFile(WebPlatformConstants.Portal.SOURCE_PATH + needCopyResource, new File(generatedCodeDir, needCopyResource));
//        }
//    }

    // use template engine?
    private void generateRoutes(File generatedCodeDir, List<ComponentInfo<?>> componentInfoList, WebPlatformConfig fastballConfig) {
        if (fastballConfig == null || fastballConfig.getApplications() == null || fastballConfig.getApplications().isEmpty()) {
            return;
        }
        File routesFile = new File(generatedCodeDir, ROUTES_PATH);
        List<Route> routes = new ArrayList<>();
        Map<String, ComponentInfo<?>> componentInfoMap = new HashMap<>();
        Set<ComponentInfo<?>> usedComponent = new HashSet<>();
        for (ComponentInfo<?> componentInfo : componentInfoList) {
            componentInfoMap.put(componentInfo.className(), componentInfo);
        }
        StringBuilder routesCode = new StringBuilder("import { MenuItemRoute } from '../types';\n\n");
        fastballConfig.getApplications().forEach((applicationKey, application) -> {
            if (!CollectionUtils.isEmpty(application.getMenus())) {
                application.getMenus().forEach((menuKey, menu) -> routes.add(buildMenu("/" + applicationKey + "/" + menuKey, menu, componentInfoMap, usedComponent)));
            }
        });
        usedComponent.forEach(componentInfo -> {
            routesCode.append("import ");
            routesCode.append(componentInfo.componentKey());
            routesCode.append(" from '@/");
            routesCode.append(componentInfo.componentPath());
            routesCode.append("';\n\n");
        });
        try {
            routesCode.append("const routes: MenuItemRoute[] = ");
            routesCode.append(JsonUtils.toComponentJson(routes));
            routesCode.append("\n\n");
            routesCode.append("export default routes;");
            FileUtils.write(routesFile, routesCode.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new GenerateException(e);
        }
    }


    protected Route buildMenu(String menuPath, WebMenu menu, Map<String, ComponentInfo<?>> componentInfoMap, Set<ComponentInfo<?>> usedComponent) {
        Route.RouteBuilder routeBuilder = Route.builder().path(menuPath).name(menu.getTitle()).icon(menu.getIcon()).params(menu.getParams());
        if (menu.getComponent() != null) {
            ComponentInfo<?> componentInfo = componentInfoMap.get(menu.getComponent());
            if (componentInfo != null) {
                routeBuilder.component(componentInfo.componentKey());
                routeBuilder.componentFullName(menu.getComponent());
                usedComponent.add(componentInfo);
            }
        } else if (menu.getMenus() != null) {
            List<Route> subRoutes = menu.getMenus().entrySet().stream()
                    .map(subMenuEntry -> buildMenu(menuPath + "/" + subMenuEntry.getKey(), subMenuEntry.getValue(), componentInfoMap, usedComponent))
                    .collect(Collectors.toList());
            routeBuilder.routes(subRoutes);
        }
        return routeBuilder.build();
    }
}
