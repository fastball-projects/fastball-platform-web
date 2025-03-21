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
import dev.fastball.platform.web.feature.message.WebPortalMessageAccessor;
import dev.fastball.platform.web.filter.BusinessContextFilter;
import dev.fastball.platform.web.service.WebPortalBusinessContextService;
import dev.fastball.platform.web.service.WebPortalDataService;
import dev.fastball.platform.web.service.WebPortalInitService;
import dev.fastball.platform.web.service.support.DefaultWebPortalMessageAccessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration
@EntityScan(basePackageClasses = JpaApplicationEntity.class)
@EnableJpaRepositories(basePackageClasses = ApplicationRepo.class)
@ComponentScan(basePackages = "dev.fastball.platform.web")
public class JpaFastballSecurityConfiguration {

    @Bean
    public WebPortalDataService fastballPortalService(
            PlatformPermissionService<JpaPermissionEntity> permissionService,
            PlatformRoleService<Role> roleService,
            ApplicationRepo applicationRepo, MenuRepo menuRepo, RoleRepo roleRepo, UserRepo userRepo
    ) {
        return new JpaWebPortalDataService(permissionService, roleService, roleRepo, userRepo, applicationRepo, menuRepo);
    }

    @Bean
    public WebPortalInitService fastballPortalInitService(
            PlatformUserService userService, PlatformRoleService<JpaRoleEntity> roleService,
            PlatformPermissionService<JpaPermissionEntity> permissionService,
            ApplicationRepo applicationRepo, MenuRepo menuRepo
    ) {
        return new JpaWebPortalInitService(userService, roleService, permissionService, applicationRepo, menuRepo);
    }

    @Bean
    @ConditionalOnMissingBean(WebPortalMessageAccessor.class)
    public WebPortalMessageAccessor webPortalMessageAccessor() {
        return new DefaultWebPortalMessageAccessor();
    }

    @Bean
    public BusinessContextFilter businessContextFilter(WebPortalBusinessContextService businessContextService) {
        return new BusinessContextFilter(businessContextService);
    }

}
