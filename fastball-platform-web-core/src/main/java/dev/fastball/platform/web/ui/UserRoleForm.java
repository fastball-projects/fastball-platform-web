package dev.fastball.platform.web.ui;

import dev.fastball.core.annotation.RecordAction;
import dev.fastball.core.annotation.UIComponent;
import dev.fastball.platform.core.model.context.Role;
import dev.fastball.platform.web.model.UserDTO;
import dev.fastball.platform.web.model.UserRoleModel;
import dev.fastball.platform.web.service.WebPortalRoleService;
import dev.fastball.components.form.VariableForm;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

@UIComponent
@RequiredArgsConstructor
public class UserRoleForm implements VariableForm<UserRoleModel, UserDTO> {
    private final WebPortalRoleService roleService;

    @Override
    public UserRoleModel loadData(UserDTO user) {
        UserRoleModel userRole = new UserRoleModel();
        userRole.setUserId(user.getId());
        userRole.setRoles(roleService.getUserRole(user.getId()).stream().map(Role::getId).collect(Collectors.toList()));
        return userRole;
    }

    @RecordAction(name = "提交")
    public void save(UserRoleModel userRoleModel) {
        roleService.saveUserRoles(userRoleModel.getUserId(), userRoleModel.getRoles());
    }
}
