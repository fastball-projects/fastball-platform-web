package dev.fastball.platform.web.data.jpa.configuration;

import dev.fastball.platform.data.jpa.entity.JpaPermissionEntity;
import dev.fastball.platform.data.jpa.entity.JpaRoleEntity;
import dev.fastball.platform.data.jpa.repo.RoleRepo;
import dev.fastball.platform.data.jpa.repo.UserRepo;
import dev.fastball.platform.entity.Role;
import dev.fastball.platform.service.PlatformPermissionService;
import dev.fastball.platform.service.PlatformRoleService;
import dev.fastball.platform.service.PlatformUserService;
import dev.fastball.platform.web.data.jpa.entity.JpaApplicationEntity;
import dev.fastball.platform.web.data.jpa.repo.*;
import dev.fastball.platform.web.data.jpa.service.JpaWebPortalDataService;
import dev.fastball.platform.web.data.jpa.service.JpaWebPortalInitService;
import dev.fastball.platform.web.service.WebPortalDataService;
import dev.fastball.platform.web.service.WebPortalInitService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration
@EntityScan(basePackageClasses = JpaApplicationEntity.class)
@EnableJpaRepositories(basePackageClasses = ApplicationRepo.class)
@ComponentScan(basePackages = "dev.fastball.platform.web")
public class JpaWebPlatformConfiguration {

    @Bean
    public WebPortalDataService webPortalDataService(
            PlatformPermissionService<JpaPermissionEntity> permissionService,
            PlatformRoleService<Role> roleService,
            ApplicationRepo applicationRepo, MenuRepo menuRepo, RoleRepo roleRepo, UserRepo userRepo
    ) {
        return new JpaWebPortalDataService(permissionService, roleService, roleRepo, userRepo, applicationRepo, menuRepo);
    }

    @Bean
    public WebPortalInitService webPortalInitService(
            PlatformUserService userService, PlatformRoleService<JpaRoleEntity> roleService,
            PlatformPermissionService<JpaPermissionEntity> permissionService,
            ApplicationRepo applicationRepo, MenuRepo menuRepo
    ) {
        return new JpaWebPortalInitService(userService, roleService, permissionService, applicationRepo, menuRepo);
    }

}
