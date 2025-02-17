package dev.fastball.platform.web.data.jpa.service;

import dev.fastball.core.component.DataResult;
import dev.fastball.platform.core.dict.UserStatus;
import dev.fastball.platform.core.exception.UserNotFoundException;
import dev.fastball.platform.core.model.context.Menu;
import dev.fastball.platform.core.model.context.Permission;
import dev.fastball.platform.web.WebPlatformConstants;
import dev.fastball.platform.web.data.jpa.entity.JpaApplicationEntity;
import dev.fastball.platform.web.data.jpa.entity.JpaMenuEntity;
import dev.fastball.platform.web.data.jpa.entity.JpaUserEntity;
import dev.fastball.platform.web.data.jpa.repo.ApplicationRepo;
import dev.fastball.platform.web.data.jpa.repo.MenuRepo;
import dev.fastball.platform.web.data.jpa.repo.PermissionRepo;
import dev.fastball.platform.web.data.jpa.repo.UserRepo;
import dev.fastball.platform.web.model.ApplicationDTO;
import dev.fastball.platform.web.model.UserDTO;
import dev.fastball.platform.web.model.UserQueryModel;
import dev.fastball.platform.web.service.WebPortalUserService;
import dev.fastball.ui.components.table.param.TableSearchParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static dev.fastball.platform.web.WebPlatformConstants.PLATFORM;


@Service
@RequiredArgsConstructor
public class JpaWebPortalUserService implements WebPortalUserService {

    private final UserRepo userRepo;
    private final ApplicationRepo applicationRepo;
    private final PermissionRepo permissionRepo;
    private final MenuRepo menuRepo;

    @Override
    public List<Menu> getUserMenu(Long userId) {
        List<Long> menuIdList = findUserPermissionTargetIdList(userId, WebPlatformConstants.PermissionType.MENU);
        return menuRepo.findAllById(menuIdList).stream().map(menu -> (Menu) menu).toList();
    }

    @Override
    public List<ApplicationDTO> getUserApplications(Long userId) {
        List<Long> applicationIdList = findUserPermissionTargetIdList(userId, WebPlatformConstants.PermissionType.APPLICATION);
        return applicationRepo.findAllById(applicationIdList).stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public List<ApplicationDTO> getUserApplicationsWithMenu(Long userId) {
        List<Long> applicationIdList = findUserPermissionTargetIdList(userId, WebPlatformConstants.PermissionType.APPLICATION);
        List<Long> menuIdList = findUserPermissionTargetIdList(userId, WebPlatformConstants.PermissionType.MENU);
        Map<Long, List<Menu>> applicationMenuMap = menuRepo.findAllById(menuIdList)
                .stream()
                .collect(Collectors.groupingBy(JpaMenuEntity::getApplicationId, Collectors.mapping(menu -> (Menu) menu, Collectors.toList())));

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
        Permission permission = getUserPermission(userId, WebPlatformConstants.PermissionType.APPLICATION, applicationKey);
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
        application.setMenus(menus.stream().map(menu -> (Menu) menu).toList());
        return application;
    }

    @Override
    public List<Permission> getUserPermission(Long userId) {
        return permissionRepo.findByUserId(userId).stream().map(p -> (Permission) p).collect(Collectors.toList());
    }

    @Override
    public List<Permission> getUserPermission(Long userId, String permissionType) {
        return permissionRepo.findByUserIdAndPlatformAndType(userId, PLATFORM, permissionType).stream().map(p -> (Permission) p).collect(Collectors.toList());
    }

    @Override
    public Permission getUserPermission(Long userId, String permissionType, String permissionCode) {
        return permissionRepo.findByUserIdAndPlatformAndTypeAndCode(userId, PLATFORM, permissionType, permissionCode);
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
    public UserStatus getUserStatus(Long userId) throws UserNotFoundException {
        Optional<JpaUserEntity> userOptional = userRepo.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        return userOptional.get().getStatus();
    }

    @Override
    public boolean enableUser(Long userId) {
        Optional<JpaUserEntity> userOptional = userRepo.findById(userId);
        if (userOptional.isEmpty()) {
            return false;
        }
        JpaUserEntity user = userOptional.get();
        user.setStatus(UserStatus.Enabled);
        user.setNickname(user.getNickname());
        user.setLastUpdatedAt(new Date());
        userRepo.save(user);
        return true;
    }

    @Override
    public boolean disableUser(Long userId) {
        Optional<JpaUserEntity> userOptional = userRepo.findById(userId);
        if (userOptional.isEmpty()) {
            return false;
        }
        JpaUserEntity user = userOptional.get();
        user.setStatus(UserStatus.Disabled);
        user.setNickname(user.getNickname());
        user.setLastUpdatedAt(new Date());
        userRepo.save(user);
        return true;
    }

    private List<Long> findUserPermissionTargetIdList(Long userId, String permissionType) {
        return getUserPermission(userId, permissionType)
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
}
