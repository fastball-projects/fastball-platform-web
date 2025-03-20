package dev.fastball.platform.web.ui;

import dev.fastball.components.tree.Tree;
import dev.fastball.components.tree.config.TreeConfig;
import dev.fastball.core.annotation.UIComponent;
import dev.fastball.core.component.DataResult;
import dev.fastball.platform.core.model.context.Permission;
import dev.fastball.platform.web.model.PermissionDTO;
import dev.fastball.platform.web.service.WebPortalRoleService;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UIComponent
@TreeConfig(titleField = "name", childrenField = "subPermissions")
@RequiredArgsConstructor
public class PermissionTree implements Tree<PermissionDTO> {

    private final WebPortalRoleService roleService;

    @Override
    public DataResult<PermissionDTO> loadData() {
        Map<Long, List<PermissionDTO>> subPermissionMap = new HashMap<>();
        List<PermissionDTO> permissionList = new ArrayList<>();
        roleService.getAllPermissions().forEach(permission -> {
            PermissionDTO permissionDTO = this.convert(permission);
            if (permission.getParentId() != null) {
                List<PermissionDTO> subPermissions = subPermissionMap.computeIfAbsent(permission.getParentId(), pId -> new ArrayList<>());
                subPermissions.add(permissionDTO);
            } else {
                permissionList.add(permissionDTO);
            }
            permissionDTO.setSubPermissions(subPermissionMap.computeIfAbsent(permission.getId(), pId -> new ArrayList<>()));
        });
        return DataResult.build(permissionList);
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
