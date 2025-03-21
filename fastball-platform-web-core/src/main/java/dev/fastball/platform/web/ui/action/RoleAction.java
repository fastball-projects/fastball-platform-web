package dev.fastball.platform.web.ui.action;

import dev.fastball.core.annotation.UIComponent;
import dev.fastball.core.component.LookupAction;
import dev.fastball.core.component.LookupActionParam;
import dev.fastball.platform.entity.Role;
import dev.fastball.platform.service.PlatformRoleService;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@UIComponent
@RequiredArgsConstructor
public class RoleAction implements LookupAction<Role, Object> {
    private final PlatformRoleService<Role> roleService;

    @Override
    public Collection<Role> loadLookupItems(LookupActionParam<Object> param) {
        return roleService.getAllRole();
    }
}
