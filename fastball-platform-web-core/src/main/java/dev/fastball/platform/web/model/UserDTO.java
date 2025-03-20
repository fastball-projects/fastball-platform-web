package dev.fastball.platform.web.model;

import dev.fastball.core.annotation.Field;
import dev.fastball.platform.core.dict.UserStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class UserDTO extends BaseDTO {

    @Field(title = "手机号")
    private String mobile;

    @Field(title = "用户名")
    private String username;

    @Field(title = "昵称")
    private String nickname;

    @Field(title = "用户状态")
    private UserStatus status;
}
