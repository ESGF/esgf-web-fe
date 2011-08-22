package org.esgf.commonui;

import org.esgf.adminui.Role;

public interface RoleOperationsInterface {

    public Role getRoleForUserInGroup(String userId, String groupId);
    
    public Role getRoleObjectFromRoleId(String roleId);
    
    public String getRoleIdFromUserName(String roleName);
    
}
