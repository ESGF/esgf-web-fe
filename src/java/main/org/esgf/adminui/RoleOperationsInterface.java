package org.esgf.adminui;

import java.util.List;

public interface RoleOperationsInterface {

    public List<Role> getRolesUserForGroup(final String userId,final String groupId);
    
}
