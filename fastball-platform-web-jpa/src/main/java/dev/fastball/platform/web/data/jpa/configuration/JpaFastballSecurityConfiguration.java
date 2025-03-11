package dev.fastball.platform.web.data.jpa.configuration;

import dev.fastball.platform.core.service.FastballPortalService;
import dev.fastball.platform.web.data.jpa.entity.JpaUserEntity;
import dev.fastball.platform.web.data.jpa.repo.*;
import dev.fastball.platform.web.data.jpa.service.JpaWebPortalInitService;
import dev.fastball.platform.web.data.jpa.service.JpaFastballPortalService;
import dev.fastball.platform.web.feature.message.WebPortalMessageAccessor;
import dev.fastball.platform.web.filter.BusinessContextFilter;
import dev.fastball.platform.web.service.WebPortalBusinessContextService;
import dev.fastball.platform.web.service.WebPortalInitService;
import dev.fastball.platform.web.service.WebPortalRoleService;
import dev.fastball.platform.web.service.support.DefaultWebPortalMessageAccessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

@AutoConfiguration
@EntityScan(basePackageClasses = JpaUserEntity.class)
@EnableJpaRepositories(basePackageClasses = UserRepo.class)
@ComponentScan(basePackages = "dev.fastball.platform.web")
public class JpaFastballSecurityConfiguration {

    @Bean
    public FastballPortalService fastballPortalService(UserRepo fastballUserRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        return new JpaFastballPortalService(fastballUserRepo, roleRepo, passwordEncoder);
    }

    @Bean
    public WebPortalInitService fastballPortalInitService(FastballPortalService fastballPortalService, WebPortalRoleService roleService, ApplicationRepo applicationRepo, PermissionRepo permissionRepo, MenuRepo menuRepo) {
        return new JpaWebPortalInitService(fastballPortalService, roleService, applicationRepo, permissionRepo, menuRepo);
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
