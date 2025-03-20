package dev.fastball.platform.web.service;

import dev.fastball.components.common.metadata.query.TableSearchParam;
import dev.fastball.core.component.DataResult;
import dev.fastball.platform.core.dict.UserStatus;
import dev.fastball.platform.core.exception.UserNotFoundException;
import dev.fastball.platform.core.model.context.Menu;
import dev.fastball.platform.core.model.context.Permission;
import dev.fastball.platform.web.model.ApplicationDTO;
import dev.fastball.platform.web.model.UserDTO;
import dev.fastball.platform.web.model.UserQueryModel;

import java.util.List;

public interface WebPortalUserService {

    List<Menu> getUserMenu(Long userId);

    List<ApplicationDTO> getUserApplications(Long userId);

    List<ApplicationDTO> getUserApplicationsWithMenu(Long userId);

    ApplicationDTO getUserApplicationWithMenu(Long userId, String applicationKey);

    List<Permission> getUserPermission(Long userId);

    List<Permission> getUserPermission(Long userId, String permissionType);

    Permission getUserPermission(Long userId, String permissionType, String permissionCode);

    DataResult<UserDTO> pagingUser(TableSearchParam<UserQueryModel> search);

    UserStatus getUserStatus(Long userId) throws UserNotFoundException;

    boolean enableUser(Long userId);

    boolean disableUser(Long userId);
}
