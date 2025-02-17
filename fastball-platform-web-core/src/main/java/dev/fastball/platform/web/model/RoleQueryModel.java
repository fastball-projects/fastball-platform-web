package dev.fastball.platform.web.model;

import dev.fastball.core.annotation.Field;
import lombok.Data;


@Data
public class RoleQueryModel {

    @Field(title = "名称")
    private String name;

}
