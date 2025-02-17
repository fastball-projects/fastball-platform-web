package dev.fastball.platform.web.data.jpa.service;

import dev.fastball.core.exception.BusinessException;
import dev.fastball.platform.core.dict.PermissionType;
import dev.fastball.platform.core.dict.UserStatus;
import dev.fastball.platform.core.exception.UserNotFoundException;
import dev.fastball.platform.core.exception.RoleNotFoundException;
import dev.fastball.platform.core.model.CurrentUser;
import dev.fastball.platform.core.model.RegisterUser;
import dev.fastball.platform.core.model.context.Menu;
import dev.fastball.platform.core.model.context.Permission;
import dev.fastball.platform.core.model.context.Role;
import dev.fastball.platform.core.model.context.User;
import dev.fastball.platform.core.model.entity.UserEntity;
import dev.fastball.platform.core.service.FastballPortalService;
import dev.fastball.platform.web.data.jpa.entity.JpaRoleEntity;
import dev.fastball.platform.web.data.jpa.entity.JpaUserEntity;
import dev.fastball.platform.web.data.jpa.repo.MenuRepo;
import dev.fastball.platform.web.data.jpa.repo.PermissionRepo;
import dev.fastball.platform.web.data.jpa.repo.RoleRepo;
import dev.fastball.platform.web.data.jpa.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JpaFastballPortalService implements FastballPortalService {

    private final UserRepo userRepo;
    private final PermissionRepo permissionRepo;
    private final RoleRepo roleRepo;
    private final MenuRepo menuRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserEntity loadAccountByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public UserEntity registerUser(RegisterUser user) {
        JpaUserEntity userEntity = new JpaUserEntity();
        userEntity.setUsername(user.getUsername());
        userEntity.setNickname(user.getNickname());
        userEntity.setMobile(user.getMobile());
        userEntity.setStatus(UserStatus.Enabled);
        if (user.getPassword() != null) {
            userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepo.save(userEntity);
        return userEntity;
    }

    @Override
    public Role registerRole(String roleCode, String roleName, String description) {
        JpaRoleEntity role = new JpaRoleEntity();
        role.setCode(roleCode);
        role.setName(roleName);
        role.setDescription(description);
        roleRepo.save(role);
        return role;
    }

    @Override
    public void changePassword(Long userId, String password, String newPassword) {
        Optional<JpaUserEntity> userOptional = userRepo.findById(userId);
        if (!userOptional.isPresent()) {
            return;
        }
        JpaUserEntity user = userOptional.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("密码错误");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
    }

    @Override
    public boolean resetPasswordByUserId(Long userId, String password) {
        Optional<JpaUserEntity> userOptional = userRepo.findById(userId);
        if (!userOptional.isPresent()) {
            return false;
        }
        JpaUserEntity user = userOptional.get();
        user.setPassword(passwordEncoder.encode(password));
        user.setNickname(user.getNickname());
        user.setLastUpdatedAt(new Date());
        userRepo.save(user);
        return true;
    }

    @Override
    public boolean resetPasswordByUserName(String username, String password) {
        JpaUserEntity user = userRepo.findByUsername(username);
        if (user == null) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(password));
        user.setNickname(user.getNickname());
        user.setLastUpdatedAt(new Date());
        userRepo.save(user);
        return true;
    }

    @Override
    public boolean resetPasswordByMobile(String mobile, String password) {
        JpaUserEntity user = userRepo.findByMobile(mobile);
        if (user == null) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(password));
        user.setNickname(user.getNickname());
        user.setLastUpdatedAt(new Date());
        userRepo.save(user);
        return true;
    }

    @Override
    public CurrentUser loadByUsername(String username) {
        UserEntity account = loadAccountByUsername(username);
        if (account == null) {
            return null;
        }
        CurrentUser currentUser = new CurrentUser();
        BeanUtils.copyProperties(account, currentUser);
        return currentUser;
    }

    @Override
    public UserEntity loadByMobile(String mobile) {
        return userRepo.findByMobile(mobile);
    }
}
