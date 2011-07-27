package org.esgf.adminui;

import java.util.List;

public interface GroupOperationsInterface {

    public void addGroup();
    
    public void editGroup();
    
    public void deleteGroup();
    
    public Group getGroupFromGroupId(final String groupId);
    
    public List<Group> getGroupsForUser(final String userId);
    
    public List<Group> getGroupsForAdminUser(final String userId);
    
    public List<Group> getAllGroups();
    
    
}
