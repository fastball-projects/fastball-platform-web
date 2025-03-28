package dev.fastball.platform.web.ui;

import dev.fastball.components.form.Form;
import dev.fastball.core.annotation.RecordAction;
import dev.fastball.core.annotation.UIComponent;
import dev.fastball.core.exception.BusinessException;
import dev.fastball.platform.service.PlatformUserService;
import dev.fastball.platform.web.model.UserResetPassword;
import lombok.RequiredArgsConstructor;

@UIComponent
@RequiredArgsConstructor
public class ResetPasswordForm implements Form<UserResetPassword> {

    private final PlatformUserService userService;

    @RecordAction(name = "提交")
    public void submit(UserResetPassword userResetPassword) {
        if (!userService.resetPasswordByUserId(userResetPassword.getId(), userResetPassword.getPassword())) {
            throw new BusinessException("重置密码失败");
        }
    }

}
