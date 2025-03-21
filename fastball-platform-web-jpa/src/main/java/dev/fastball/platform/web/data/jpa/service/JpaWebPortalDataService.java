package dev.fastball.platform.web.data.jpa.service;

import dev.fastball.components.common.query.TableSearchParam;
import dev.fastball.core.component.DataResult;
import dev.fastball.platform.data.jpa.entity.JpaPermissionEntity;
import dev.fastball.platform.data.jpa.entity.JpaRoleEntity;
import dev.fastball.platform.data.jpa.entity.JpaUserEntity;
import dev.fastball.platform.data.jpa.repo.RoleRepo;
import dev.fastball.platform.data.jpa.repo.UserRepo;
import dev.fastball.platform.entity.Permission;
import dev.fastball.platform.entity.Role;
import dev.fastball.platform.service.PlatformPermissionService;
import dev.fastball.platform.service.PlatformRoleService;
import dev.fastball.platform.web.WebPlatformConstants;
import dev.fastball.platform.web.data.jpa.entity.JpaApplicationEntity;
import dev.fastball.platform.web.data.jpa.entity.JpaMenuEntity;
import dev.fastball.platform.web.data.jpa.repo.ApplicationRepo;
import dev.fastball.platform.web.data.jpa.repo.MenuRepo;
import dev.fastball.platform.web.model.*;
import dev.fastball.platform.web.service.WebPortalDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static dev.fastball.platform.web.WebPlatformConstants.PLATFORM;


@RequiredArgsConstructor
public class JpaWebPortalDataService implements WebPortalDataService {

    private final PlatformPermissionService<JpaPermissionEntity> permissionService;
    private final PlatformRoleService<Role> roleService;
    private final RoleRepo roleRepo;
    private final UserRepo userRepo;
    private final ApplicationRepo applicationRepo;
    private final MenuRepo menuRepo;

    @Override
    public List<ApplicationDTO> getUserApplications(Long userId) {
        List<Long> applicationIdList = findUserPermissionTargetIdList(userId, WebPlatformConstants.PermissionType.APPLICATION);
        return applicationRepo.findAllById(applicationIdList).stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public List<ApplicationDTO> getUserApplicationsWithMenu(Long userId) {
        List<Long> applicationIdList = findUserPermissionTargetIdList(userId, WebPlatformConstants.PermissionType.APPLICATION);
        List<Long> menuIdList = findUserPermissionTargetIdList(userId, WebPlatformConstants.PermissionType.MENU);
        Map<Long, List<MenuDTO>> applicationMenuMap = menuRepo.findAllById(menuIdList)
                .stream()
                .collect(Collectors.groupingBy(JpaMenuEntity::getApplicationId, Collectors.mapping(this::convert, Collectors.toList())));

        return applicationRepo.findAllById(applicationIdList)
                .stream()
                .map(appEntity -> {
                    ApplicationDTO app = convert(appEntity);
                    app.setMenus(applicationMenuMap.get(appEntity.getId()));
                    return app;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ApplicationDTO getUserApplicationWithMenu(Long userId, String applicationKey) {
        Permission permission = permissionService.getUserPermission(userId, PLATFORM, WebPlatformConstants.PermissionType.APPLICATION, applicationKey);
        if (permission == null) {
            return null;
        }
        JpaApplicationEntity applicationEntity = applicationRepo.findByCode(applicationKey);
        if (applicationEntity == null) {
            return null;
        }
        ApplicationDTO application = convert(applicationEntity);
        List<Long> menuIdList = findUserPermissionTargetIdList(userId, WebPlatformConstants.PermissionType.MENU);
        List<JpaMenuEntity> menus = menuRepo.findByApplicationIdAndIdIn(applicationEntity.getId(), menuIdList);
        application.setMenus(menus.stream().map(this::convert).toList());
        return application;
    }

    @Override
    public DataResult<UserDTO> pagingUser(TableSearchParam<UserQueryModel> search) {
        PageRequest pageRequest = PageRequest.of(search.getCurrent().intValue() - 1, search.getPageSize().intValue());
        Page<JpaUserEntity> result;
        if (search.getSearch() == null) {
            result = userRepo.findAll(pageRequest);
        } else {
            Example<JpaUserEntity> example = Example.of(
                    JpaUserEntity.builder()
                            .username(search.getSearch().getUsername())
                            .nickname(search.getSearch().getNickname())
                            .build()
            );
            result = userRepo.findAll(example, pageRequest);
        }
        return DataResult.build(result.getTotalElements(), result.map(this::convert).toList());
    }


    @Override
    public DataResult<RoleDTO> pagingRole(TableSearchParam<RoleQueryModel> search) {
        PageRequest pageRequest = PageRequest.of(search.getCurrent().intValue() - 1, search.getPageSize().intValue());
        Page<JpaRoleEntity> result;
        if (search.getSearch() == null) {
            result = roleRepo.findAll(pageRequest);
        } else {
            Example<JpaRoleEntity> example = Example.of(
                    JpaRoleEntity.builder().name(search.getSearch().getName()).build()
            );
            result = roleRepo.findAll(example, pageRequest);
        }
        List<RoleDTO> roleDTOList = result.getContent().stream()
                .map(roleEntity -> RoleDTO.builder()
                        .id(roleEntity.getId())
                        .code(roleEntity.getCode())
                        .name(roleEntity.getName())
                        .description(roleEntity.getDescription())
                        .permissions(roleEntity.getPermissions().stream().map(Permission::getId).collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
        return DataResult.build(result.getTotalElements(), roleDTOList);
    }

    @Override
    public void saveRole(RoleDTO role) {
        roleService.save(convertEntity(role));
    }

    private List<Long> findUserPermissionTargetIdList(Long userId, String permissionType) {
        return permissionService.getUserPermissions(userId, PLATFORM, permissionType)
                .stream().map(Permission::getTarget)
                .filter(Objects::nonNull)
                .mapToLong(Long::valueOf)
                .boxed()
                .toList();
    }

    private ApplicationDTO convert(JpaApplicationEntity entity) {
        return ApplicationDTO.builder()
                .title(entity.getName())
                .code(entity.getCode())
                .icon(entity.getIcon())
                .description(entity.getDescription())
                .businessContext(entity.getBusinessContext())
                .build();
    }

    private MenuDTO convert(JpaMenuEntity menu) {
        return MenuDTO.builder()
                .id(menu.getId())
                .parentId(menu.getParentId())
                .name(menu.getName())
                .path(menu.getPath())
                .description(menu.getDescription())
                .params(menu.getParams())
                .build();
    }

    private UserDTO convert(JpaUserEntity user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .mobile(user.getMobile())
                .nickname(user.getNickname())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .createdBy(user.getCreatedBy())
                .lastUpdatedAt(user.getLastUpdatedAt())
                .lastUpdatedBy(user.getLastUpdatedBy())
                .build();
    }

    private JpaRoleEntity convertEntity(RoleDTO role) {
        JpaRoleEntity roleEntity = new JpaRoleEntity();
        roleEntity.setId(role.getId());
        roleEntity.setCode(role.getCode());
        roleEntity.setName(role.getName());
        roleEntity.setDescription(role.getDescription());
        if (role.getPermissions() != null && !role.getPermissions().isEmpty()) {
            roleEntity.setPermissions(permissionService.getPermissionsById(role.getPermissions()));
        } else {
            roleEntity.setPermissions(Collections.emptyList());
        }
        return roleEntity;
    }
}
