package dev.fastball.platform.web.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Getter
@Setter
@SuperBuilder
public class PermissionDTO extends BaseDTO {
    private String code;
    private String name;
    private String target;
    private String permissionType;
    private String description;
    private List<PermissionDTO> subPermissions;
}
