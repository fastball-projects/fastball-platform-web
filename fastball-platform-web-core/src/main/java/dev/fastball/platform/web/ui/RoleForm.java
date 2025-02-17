package dev.fastball.platform.web.ui;

import dev.fastball.core.annotation.RecordAction;
import dev.fastball.core.annotation.UIComponent;
import dev.fastball.platform.web.model.RoleDTO;
import dev.fastball.platform.web.service.WebPortalRoleService;
import dev.fastball.ui.components.form.Form;
import lombok.RequiredArgsConstructor;

@UIComponent
@RequiredArgsConstructor
public class RoleForm implements Form<RoleDTO> {

    private final WebPortalRoleService roleService;

    @RecordAction(name = "提交")
    public void submit(RoleDTO role) {
        roleService.saveRole(role);
    }

}