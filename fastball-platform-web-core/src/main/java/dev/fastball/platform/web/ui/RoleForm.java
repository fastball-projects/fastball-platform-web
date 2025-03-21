package dev.fastball.platform.web.ui;

import dev.fastball.components.form.Form;
import dev.fastball.core.annotation.RecordAction;
import dev.fastball.core.annotation.UIComponent;
import dev.fastball.platform.entity.Role;
import dev.fastball.platform.service.PlatformRoleService;
import dev.fastball.platform.web.model.RoleDTO;
import dev.fastball.platform.web.service.WebPortalDataService;
import lombok.RequiredArgsConstructor;

@UIComponent
@RequiredArgsConstructor
public class RoleForm implements Form<RoleDTO> {

    private final WebPortalDataService dataService;

    @RecordAction(name = "提交")
    public void submit(RoleDTO role) {
        dataService.saveRole(role);
    }

}