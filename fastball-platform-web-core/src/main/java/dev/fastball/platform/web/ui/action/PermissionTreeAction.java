package dev.fastball.platform.web.ui.action;

import dev.fastball.core.annotation.UIComponent;
import dev.fastball.core.component.LookupActionParam;
import dev.fastball.core.component.TreeLookupAction;
import dev.fastball.platform.entity.Permission;
import dev.fastball.platform.service.PlatformPermissionService;
import dev.fastball.platform.web.model.PermissionDTO;
import lombok.RequiredArgsConstructor;

import java.util.*;

@UIComponent
@RequiredArgsConstructor
public class PermissionTreeAction implements TreeLookupAction<PermissionDTO, Object> {
    private final PlatformPermissionService<Permission> permissionService;

    @Override
    public Collection<PermissionDTO> loadLookupItems(LookupActionParam<Object> param) {
        Map<Long, List<PermissionDTO>> subPermissionMap = new HashMap<>();
        List<PermissionDTO> permissionList = new ArrayList<>();
        permissionService.getAllPermissions().forEach(permission -> {
            PermissionDTO permissionDTO = this.convert(permission);
            if (permission.getParentId() != null) {
                List<PermissionDTO> subPermissions = subPermissionMap.computeIfAbsent(permission.getParentId(), pId -> new ArrayList<>());
                subPermissions.add(permissionDTO);
            } else {
                permissionList.add(permissionDTO);
            }
            permissionDTO.setSubPermissions(subPermissionMap.computeIfAbsent(permission.getId(), pId -> new ArrayList<>()));
        });
        return permissionList;
    }

    private PermissionDTO convert(Permission permission) {
        return PermissionDTO.builder()
                .id(permission.getId())
                .code(permission.getCode())
                .name(permission.getName())
                .permissionType(permission.getPermissionType())
                .target(permission.getTarget())
                .description(permission.getDescription())
                .build();
    }
}
