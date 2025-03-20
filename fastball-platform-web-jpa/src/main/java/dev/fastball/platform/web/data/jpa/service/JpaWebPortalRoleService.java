package dev.fastball.platform.web.data.jpa.service;

import dev.fastball.components.common.metadata.query.TableSearchParam;
import dev.fastball.core.component.DataResult;
import dev.fastball.platform.core.exception.RoleNotFoundException;
import dev.fastball.platform.core.exception.UserNotFoundException;
import dev.fastball.platform.core.model.context.Permission;
import dev.fastball.platform.core.model.context.Role;
import dev.fastball.platform.web.data.jpa.entity.JpaRoleEntity;
import dev.fastball.platform.web.data.jpa.entity.JpaUserEntity;
import dev.fastball.platform.web.data.jpa.repo.PermissionRepo;
import dev.fastball.platform.web.data.jpa.repo.RoleRepo;
import dev.fastball.platform.web.data.jpa.repo.UserRepo;
import dev.fastball.platform.web.model.RoleDTO;
import dev.fastball.platform.web.model.RoleQueryModel;
import dev.fastball.platform.web.service.WebPortalRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JpaWebPortalRoleService implements WebPortalRoleService {

    private final RoleRepo roleRepo;
    private final UserRepo userRepo;
    private final PermissionRepo permissionRepo;


    @Override
    public void saveRole(RoleDTO role) {
        roleRepo.save(convertEntity(role));
    }

    @Override
    public void saveUserRoles(Long userId, Collection<Long> roleIds) {
        JpaUserEntity user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        user.setRoles(roleRepo.findAllById(roleIds));
        user.setNickname(user.getNickname());
        user.setLastUpdatedAt(new Date());
        userRepo.save(user);
    }

    @Override
    public void saveRolePermissions(Long roleId, Collection<Long> permissionIds) {
        JpaRoleEntity role = roleRepo.findById(roleId).orElseThrow(RoleNotFoundException::new);
        role.setPermissions(permissionRepo.findAllById(permissionIds));
        roleRepo.save(role);
    }

    @Override
    public List<Role> getUserRole(Long userId) {
        JpaUserEntity user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        return user.getRoles().stream().map(role -> (Role) role).collect(Collectors.toList());
    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepo.findAll().stream().map(p -> (Permission) p).collect(Collectors.toList());
    }

    @Override
    public List<Role> getAllRole() {
        return roleRepo.findAll().stream().map(p -> (Role) p).collect(Collectors.toList());
    }

    @Override
    public Role loadRoleByCode(String roleCode) {
        return roleRepo.findByCode(roleCode);
    }

    @Override
    public DataResult<RoleDTO> pagingRole(TableSearchParam<RoleQueryModel> search) {
        PageRequest pageRequest = PageRequest.of(search.getCurrent().intValue() - 1, search.getPageSize().intValue());
        Page<JpaRoleEntity> result;
        if (search.getSearch() == null) {
            result = roleRepo.findAll(pageRequest);
        } else {
            Example<JpaRoleEntity> example = Example.of(
                    JpaRoleEntity.builder().name(search.getSearch().getName()).build()
            );
            result = roleRepo.findAll(example, pageRequest);
        }
        List<RoleDTO> roleDTOList = result.getContent().stream()
                .map(roleEntity -> RoleDTO.builder()
                        .id(roleEntity.getId())
                        .code(roleEntity.getCode())
                        .name(roleEntity.getName())
                        .description(roleEntity.getDescription())
                        .permissions(roleEntity.getPermissions().stream().map(Permission::getId).collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
        return DataResult.build(result.getTotalElements(), roleDTOList);
    }

    private JpaRoleEntity convertEntity(RoleDTO role) {
        JpaRoleEntity roleEntity = new JpaRoleEntity();
        roleEntity.setId(role.getId());
        roleEntity.setCode(role.getCode());
        roleEntity.setName(role.getName());
        roleEntity.setDescription(role.getDescription());
        if (role.getPermissions() != null && !role.getPermissions().isEmpty()) {
            roleEntity.setPermissions(permissionRepo.findAllById(role.getPermissions()));
        } else {
            roleEntity.setPermissions(Collections.emptyList());
        }
        return roleEntity;
    }
}
