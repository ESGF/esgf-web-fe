package org.esgf.commonui;

import java.util.List;

import org.esgf.adminui.Group;

public interface GroupOperationsInterface {

    public void addGroup(String groupName,String groupDescription);
    
    public void editGroup(final String groupId,String groupName,String groupDescription);
    
    public void deleteGroup(final String groupId);
    
    public Group getGroupObjectFromGroupName(final String groupName);
    
    public String getGroupIdFromGroupName(String groupName);
    
    public List<Group> getGroupsFromUser(String userId);
    
    /*
    
    public List<Group> getGroupsForAdminUser(final String userId);
    */
    
    public List<Group> getAllGroups();
    
}
