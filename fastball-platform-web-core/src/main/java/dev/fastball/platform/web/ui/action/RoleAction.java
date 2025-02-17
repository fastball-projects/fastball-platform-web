package dev.fastball.platform.web.ui.action;

import dev.fastball.core.annotation.UIComponent;
import dev.fastball.core.component.LookupAction;
import dev.fastball.core.component.LookupActionParam;
import dev.fastball.platform.core.model.context.Role;
import dev.fastball.platform.web.service.WebPortalRoleService;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@UIComponent
@RequiredArgsConstructor
public class RoleAction implements LookupAction<Role, Object> {
    private final WebPortalRoleService roleService;

    @Override
    public Collection<Role> loadLookupItems(LookupActionParam<Object> param) {
        return roleService.getAllRole();
    }
}
