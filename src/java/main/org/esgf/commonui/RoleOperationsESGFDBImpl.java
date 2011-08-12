package org.esgf.commonui;

import org.esgf.adminui.Role;

public class RoleOperationsESGFDBImpl implements RoleOperationsInterface {


    @Override
    public Role getRoleForUserInGroup(String userId, String groupId) {
        // TODO Auto-generated method stub
        
        Role role = new Role();
        
        return role;
    }

    @Override
    public Role getRoleObjectFromRoleId(String roleId) {
        // TODO Auto-generated method stub
        Role role = new Role();
        
        
        return role;
    }

    @Override
    public String getRoleIdFromUserName(String roleName) {
        // TODO Auto-generated method stub
        return roleName;
    }

}
