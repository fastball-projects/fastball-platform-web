package dev.fastball.platform.web.model;

import dev.fastball.platform.dict.UserStatus;
import dev.fastball.platform.entity.User;
import lombok.Data;

import java.util.List;


@Data
public class CurrentUser implements User {

    private Long id;
    private String username;
    private String nickname;
    private String mobile;
    private String avatar;
    private UserStatus status;
    private List<ApplicationDTO> applications;
}
