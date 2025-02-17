package dev.fastball.platform.web.data.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.fastball.orm.jpa.JpaBaseEntity;
import dev.fastball.platform.core.dict.PermissionType;
import dev.fastball.platform.core.model.entity.PermissionEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(
        name = "fb_permission",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"type", "code"})
        }
)
@EqualsAndHashCode(callSuper = true)
public class JpaPermissionEntity extends JpaBaseEntity implements PermissionEntity {

    private Long parentId;
    private String code;
    private String name;
    private String target;
    private String platform;
    private String permissionType;
    private String description;

    @JsonIgnore
    @ManyToMany(mappedBy = "permissions")
    private List<JpaRoleEntity> roles;
}
