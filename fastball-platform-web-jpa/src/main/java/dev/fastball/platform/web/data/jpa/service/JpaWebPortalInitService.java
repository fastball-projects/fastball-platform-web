package dev.fastball.platform.web.data.jpa.service;

import dev.fastball.platform.data.jpa.entity.JpaPermissionEntity;
import dev.fastball.platform.data.jpa.entity.JpaRoleEntity;
import dev.fastball.platform.dict.UserStatus;
import dev.fastball.platform.model.RegisterUser;
import dev.fastball.platform.entity.Permission;
import dev.fastball.platform.entity.User;
import dev.fastball.platform.service.PlatformPermissionService;
import dev.fastball.platform.service.PlatformRoleService;
import dev.fastball.platform.service.PlatformUserService;
import dev.fastball.platform.web.WebPlatformConstants;
import dev.fastball.platform.web.config.WebApplication;
import dev.fastball.platform.web.config.WebMenu;
import dev.fastball.platform.web.config.WebPlatformConfig;
import dev.fastball.platform.web.config.WebPortalAdmin;
import dev.fastball.platform.web.data.jpa.entity.JpaApplicationEntity;
import dev.fastball.platform.web.data.jpa.entity.JpaMenuEntity;
import dev.fastball.platform.web.data.jpa.repo.ApplicationRepo;
import dev.fastball.platform.web.data.jpa.repo.MenuRepo;
import dev.fastball.platform.web.service.WebPortalInitService;
import dev.fastball.platform.web.utils.ConfigUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static dev.fastball.platform.web.WebPlatformConstants.WEB_PLATFORM;

@RequiredArgsConstructor
public class JpaWebPortalInitService implements WebPortalInitService {

    private final PlatformUserService userService;
    private final PlatformRoleService<JpaRoleEntity> roleService;
    private final PlatformPermissionService<JpaPermissionEntity> permissionService;

    private final ApplicationRepo applicationRepo;
    private final MenuRepo menuRepo;

    @Override
    public void initMenusAndMenuPermissions() {
        WebPlatformConfig fastballConfig = ConfigUtils.getWebPlatformConfig();
        if (fastballConfig == null || fastballConfig.getApplications() == null) {
            return;
        }
        for (Map.Entry<String, WebApplication> app : fastballConfig.getApplications().entrySet()) {
            initApplicationAndPermission(app.getKey(), app.getValue());
        }
        if (fastballConfig.getAdmin() != null && fastballConfig.getAdmin().getInit()) {
            initAdmin(fastballConfig);
        }
    }

    private void initAdmin(WebPlatformConfig fastballConfig) {
        WebPortalAdmin adminProperties = fastballConfig.getAdmin();
        User adminUser = userService.loadByUsername(adminProperties.getDefaultUsername());
        if (adminUser == null) {
            RegisterUser admin = new RegisterUser();
            admin.setNickname(adminProperties.getDefaultNickname());
            admin.setUsername(adminProperties.getDefaultUsername());
            admin.setMobile(adminProperties.getDefaultMobile());
            admin.setPassword(adminProperties.getDefaultPassword());
            admin.setStatus(UserStatus.Enabled);
            userService.registerUser(admin);
            adminUser = userService.loadByUsername(adminProperties.getDefaultUsername());
        }
        JpaRoleEntity adminRole = roleService.loadRoleByCode(adminProperties.getAdminRoleCode());
        if (adminRole == null) {
            adminRole = new JpaRoleEntity();
            adminRole.setCode(adminProperties.getAdminRoleCode());
            adminRole.setName(adminProperties.getAdminRoleName());
            adminRole.setDescription(adminProperties.getAdminRoleDescription());
            adminRole = roleService.save(adminRole);
            roleService.saveUserRoles(adminUser.getId(), Collections.singletonList(adminRole.getId()));
        }
        List<Long> allPermissionId = permissionService.getAllPermissions().stream().map(Permission::getId).collect(Collectors.toList());
        roleService.saveRolePermissions(adminUser.getId(), allPermissionId);
    }

    private void initMenuAndPermission(List<String> menuKeys, WebMenu menuInfo, JpaApplicationEntity application, JpaMenuEntity parentMenu, JpaPermissionEntity parentPermission) {
        String menuCode = String.join("-", menuKeys);
        JpaMenuEntity menuEntity = menuRepo.findByCode(menuCode);
        int hash = -1;
        if (menuEntity == null) {
            String menuPath = "/" + String.join("/", menuKeys);
            menuEntity = new JpaMenuEntity();
            menuEntity.setCode(menuCode);
            menuEntity.setPath(menuPath);
        } else {
            hash = menuEntity.hashCode();
        }
        if (parentMenu != null) {
            menuEntity.setParentId(parentMenu.getId());
        }
        menuEntity.setApplicationId(application.getId());
        menuEntity.setName(menuInfo.getTitle());
        menuEntity.setIcon(menuInfo.getIcon());
        menuEntity.setDescription(menuInfo.getDescription());
        menuEntity.setParams(menuInfo.getParams());
        if (hash != menuEntity.hashCode()) {
            menuRepo.save(menuEntity);
        }
        JpaPermissionEntity permission = initMenuPermission(menuEntity, parentPermission);
        if (menuInfo.getMenus() == null) {
            return;
        }
        for (Map.Entry<String, WebMenu> subMenu : menuInfo.getMenus().entrySet()) {
            List<String> parentMenuKeys = new ArrayList<>(menuKeys);
            parentMenuKeys.add(subMenu.getKey());
            initMenuAndPermission(parentMenuKeys, subMenu.getValue(), application, menuEntity, permission);
        }
    }

    private JpaPermissionEntity initMenuPermission(JpaMenuEntity menu, JpaPermissionEntity parentPermission) {
        JpaPermissionEntity permission = permissionService.getPermission(WEB_PLATFORM, WebPlatformConstants.PermissionType.MENU, menu.getCode());
        int hash = 0;
        if (permission == null) {
            permission = new JpaPermissionEntity();
        } else {
            hash = permission.hashCode();
        }
        permission.setPlatform(WEB_PLATFORM);
        permission.setPermissionType(WebPlatformConstants.PermissionType.MENU);
        permission.setName(menu.getName());
        permission.setCode(menu.getCode());
        permission.setTarget(menu.getId().toString());
        if (parentPermission != null) {
            permission.setParentId(parentPermission.getId());
        }
        if (hash != permission.hashCode()) {
            permissionService.save(permission);
        }
        return permission;
    }

    private void initApplicationAndPermission(String applicationKey, WebApplication app) {
        JpaApplicationEntity applicationEntity = applicationRepo.findByCode(applicationKey);
        int hash = 0;
        if (applicationEntity == null) {
            applicationEntity = new JpaApplicationEntity();
        } else {
            hash = applicationEntity.hashCode();
        }

        applicationEntity.setCode(applicationKey);
        applicationEntity.setName(app.getName());
        applicationEntity.setIcon(app.getIcon());
        applicationEntity.setDescription(app.getDescription());
        applicationEntity.setBusinessContext(app.getBusinessContext());

        if (hash != applicationEntity.hashCode()) {
            applicationEntity = applicationRepo.save(applicationEntity);
        }

        JpaPermissionEntity permission = permissionService.getPermission(WEB_PLATFORM, WebPlatformConstants.PermissionType.APPLICATION, applicationKey);
        if (permission == null) {
            permission = new JpaPermissionEntity();
            hash = 0;
        } else {
            hash = applicationEntity.hashCode();
        }
        permission.setPlatform(WEB_PLATFORM);
        permission.setPermissionType(WebPlatformConstants.PermissionType.APPLICATION);
        permission.setName(app.getName());
        permission.setCode(applicationKey);
        permission.setTarget(applicationEntity.getId().toString());

        if (hash != permission.hashCode()) {
            permission = permissionService.save(permission);
        }

        if (!CollectionUtils.isEmpty(app.getMenus())) {
            for (Map.Entry<String, WebMenu> menu : app.getMenus().entrySet()) {
                initMenuAndPermission(Collections.singletonList(menu.getKey()), menu.getValue(), applicationEntity, null, permission);
            }
        }
    }

    @Override
    public void initPermissions() {

    }
}
