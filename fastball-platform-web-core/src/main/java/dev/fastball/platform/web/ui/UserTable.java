package dev.fastball.platform.web.ui;

import dev.fastball.components.common.query.TableSearchParam;
import dev.fastball.components.table.SearchTable;
import dev.fastball.core.annotation.*;
import dev.fastball.core.component.DataResult;
import dev.fastball.core.component.RecordActionFilter;
import dev.fastball.meta.basic.PopupType;
import dev.fastball.platform.dict.UserStatus;
import dev.fastball.platform.service.PlatformUserService;
import dev.fastball.platform.web.model.UserDTO;
import dev.fastball.platform.web.model.UserQueryModel;
import dev.fastball.platform.web.service.WebPortalDataService;
import lombok.RequiredArgsConstructor;

@UIComponent
@RequiredArgsConstructor
@ViewActions(
        actions = @ViewAction(key = "new", name = "新建", popup = @Popup(@RefComponent(UserForm.class))),
        recordActions = {
//                @ViewAction(key = "edit", name = "编辑", popup = @Popup(@RefComponent(UserForm.class))),
                @ViewAction(key = "role", name = "角色授权", popup = @Popup(value = @RefComponent(UserRoleForm.class), popupType = PopupType.Modal)),
                @ViewAction(key = "resetPassword", name = "重置密码", popup = @Popup(value = @RefComponent(ResetPasswordForm.class), popupType = PopupType.Modal, width = "25%"))
        }
)
public class UserTable implements SearchTable<UserDTO, UserQueryModel> {

    private final WebPortalDataService dataService;
    private final PlatformUserService userService;

    @Override
    public DataResult<UserDTO> loadData(TableSearchParam<UserQueryModel> search) {
        return dataService.pagingUser(search);
    }

    @RecordAction(name = "启用", recordActionFilter = EnabledRecordActionFilter.class)
    public void enable(UserDTO user) {
        userService.enableUser(user.getId());
    }

    @RecordAction(name = "禁用", recordActionFilter = DisabledRecordActionFilter.class)
    public void disable(UserDTO user) {
        userService.disableUser(user.getId());
    }

    public static class EnabledRecordActionFilter implements RecordActionFilter<UserDTO> {
        @Override
        public boolean filter(UserDTO user) {
            return user.getStatus() != UserStatus.Enabled;
        }
    }

    public static class DisabledRecordActionFilter implements RecordActionFilter<UserDTO> {
        @Override
        public boolean filter(UserDTO user) {
            return user.getStatus() == UserStatus.Enabled;
        }
    }
}
