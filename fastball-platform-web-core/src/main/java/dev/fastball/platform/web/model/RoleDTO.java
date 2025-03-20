package dev.fastball.platform.web.model;

import dev.fastball.core.annotation.Field;
import dev.fastball.core.annotation.TreeLookup;
import dev.fastball.meta.basic.ValueType;
import dev.fastball.platform.web.ui.action.PermissionTreeAction;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.List;


@Getter
@Setter
@SuperBuilder
public class RoleDTO extends BaseDTO {

    @Field(title = "编码")
    @NotNull(message = "角色编码不可为空")
    private String code;

    @Field(title = "名称")
    @NotNull(message = "角色名称不可为空")
    private String name;

    @Field(title = "授权", entireRow = true)
    @TreeLookup(value = PermissionTreeAction.class, valueField = "id", labelField = "name", childrenField = "subPermissions")
    private List<Long> permissions;

    @Field(title = "备注", type = ValueType.TEXTAREA, entireRow = true)
    private String description;
}
