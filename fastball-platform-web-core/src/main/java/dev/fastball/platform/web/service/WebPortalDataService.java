package dev.fastball.platform.web.service;

import dev.fastball.components.common.query.TableSearchParam;
import dev.fastball.core.component.DataResult;
import dev.fastball.platform.web.model.*;

import java.util.List;

public interface WebPortalDataService {
    List<ApplicationDTO> getUserApplicationsWithMenu(Long id);

    List<ApplicationDTO> getUserApplications(Long id);

    ApplicationDTO getUserApplicationWithMenu(Long id, String applicationKey);

    DataResult<UserDTO> pagingUser(TableSearchParam<UserQueryModel> search);

    DataResult<RoleDTO> pagingRole(TableSearchParam<RoleQueryModel> search);

    void saveRole(RoleDTO role);
}
