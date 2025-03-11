package dev.fastball.platform.web.service;

import dev.fastball.components.common.query.TableSearchParam;
import dev.fastball.core.component.DataResult;
import dev.fastball.platform.core.model.context.Permission;
import dev.fastball.platform.core.model.context.Role;
import dev.fastball.platform.web.model.RoleDTO;
import dev.fastball.platform.web.model.RoleQueryModel;

import java.util.Collection;
import java.util.List;

public interface WebPortalRoleService {

    Role loadRoleByCode(String roleCode);

    void saveRole(RoleDTO role);

    void saveUserRoles(Long userId, Collection<Long> roleIds);

    void saveRolePermissions(Long roleId, Collection<Long> permissionIds);

    List<Role> getUserRole(Long userId);

    DataResult<RoleDTO> pagingRole(TableSearchParam<RoleQueryModel> search);

    List<Role> getAllRole();

    List<Permission> getAllPermissions();
}
