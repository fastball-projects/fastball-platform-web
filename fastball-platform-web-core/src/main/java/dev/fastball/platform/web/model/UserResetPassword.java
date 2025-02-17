package dev.fastball.platform.web.model;

import dev.fastball.core.annotation.Field;
import dev.fastball.meta.basic.DisplayType;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class UserResetPassword {

    @Field(display = DisplayType.Hidden)
    private Long id;

    @Field(title = "重置的密码", entireRow = true)
    @Size(min = 6, message = "密码长度至少 6 位")
    private String password;
}
