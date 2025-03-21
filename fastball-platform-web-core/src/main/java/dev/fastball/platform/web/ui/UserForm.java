package dev.fastball.platform.web.ui;

import dev.fastball.components.form.Form;
import dev.fastball.core.annotation.RecordAction;
import dev.fastball.core.annotation.UIComponent;
import dev.fastball.platform.model.RegisterUser;
import dev.fastball.platform.service.PlatformUserService;
import lombok.RequiredArgsConstructor;

@UIComponent
@RequiredArgsConstructor
public class UserForm implements Form<RegisterUser> {

    private final PlatformUserService userService;

    @RecordAction(name = "提交")
    public void submit(RegisterUser user) {
        userService.registerUser(user);
    }
}
