package dev.fastball.platform.web.ui;

import dev.fastball.core.annotation.RecordAction;
import dev.fastball.core.annotation.UIComponent;
import dev.fastball.platform.core.model.context.Role;
import dev.fastball.platform.web.model.UserDTO;
import dev.fastball.platform.web.model.UserRoleModel;
import dev.fastball.platform.web.service.WebPortalRoleService;
import dev.fastball.ui.components.form.VariableForm;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

@UIComponent
@RequiredArgsConstructor
public class UserRoleForm implements VariableForm<UserRoleModel, UserDTO> {
    private final WebPortalRoleService roleService;

    @Override
    public UserRoleModel loadData(UserDTO user) {
        UserRoleModel staffRole = new UserRoleModel();
        staffRole.setUserId(user.getId());
        staffRole.setRoles(roleService.getUserRole(user.getId()).stream().map(Role::getId).collect(Collectors.toList()));
        return staffRole;
    }

    @RecordAction(name = "提交")
    public void save(UserRoleModel staffRoleModel) {
        roleService.saveUserRoles(staffRoleModel.getUserId(), staffRoleModel.getRoles());
    }
}
