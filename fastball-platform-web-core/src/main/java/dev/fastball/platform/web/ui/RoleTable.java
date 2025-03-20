package dev.fastball.platform.web.ui;


import dev.fastball.components.common.metadata.query.TableSearchParam;
import dev.fastball.components.table.SearchTable;
import dev.fastball.core.annotation.*;
import dev.fastball.core.component.DataResult;
import dev.fastball.platform.web.model.RoleDTO;
import dev.fastball.platform.web.model.RoleQueryModel;
import dev.fastball.platform.web.service.WebPortalRoleService;
import lombok.RequiredArgsConstructor;


@UIComponent
@RequiredArgsConstructor
@ViewActions(
        actions = @ViewAction(key = "new", name = "新建", popup = @Popup(@RefComponent(RoleForm.class))),
        recordActions = @ViewAction(key = "edit", name = "编辑", popup = @Popup(@RefComponent(RoleForm.class)))
)
public class RoleTable implements SearchTable<RoleDTO, RoleQueryModel> {

    private final WebPortalRoleService roleService;

    @Override
    public DataResult<RoleDTO> loadData(TableSearchParam<RoleQueryModel> search) {
        return roleService.pagingRole(search);
    }
}