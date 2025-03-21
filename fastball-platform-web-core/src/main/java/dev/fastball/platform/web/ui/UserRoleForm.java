package dev.fastball.platform.web.ui;

import dev.fastball.components.form.VariableForm;
import dev.fastball.core.annotation.RecordAction;
import dev.fastball.core.annotation.UIComponent;
import dev.fastball.platform.entity.Role;
import dev.fastball.platform.service.PlatformRoleService;
import dev.fastball.platform.web.model.UserDTO;
import dev.fastball.platform.web.model.UserRoleModel;
import lombok.RequiredArgsConstructor;


@UIComponent
@RequiredArgsConstructor
public class UserRoleForm implements VariableForm<UserRoleModel, UserDTO> {
    private final PlatformRoleService<?> roleService;

    @Override
    public UserRoleModel loadData(UserDTO user) {
        UserRoleModel userRole = new UserRoleModel();
        userRole.setUserId(user.getId());
        userRole.setRoles(roleService.getUserRoles(user.getId()).stream().map(Role::getId).toList());
        return userRole;
    }

    @RecordAction(name = "提交")
    public void save(UserRoleModel userRoleModel) {
        roleService.saveUserRoles(userRoleModel.getUserId(), userRoleModel.getRoles());
    }
}
