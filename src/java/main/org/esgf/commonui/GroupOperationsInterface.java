package org.esgf.commonui;

import java.util.List;

import org.esgf.adminui.Group;

public interface GroupOperationsInterface {

    void addGroup(String groupName, String groupDescription);

    void editGroup(String groupId, String newGroupName, String groupDescription);

    void deleteGroup(String groupName);

    String getGroupIdFromGroupName(String groupName);

    String getGroupNameFromGroupId(String groupId);
    
    List<Group> getAllGroups();

    Group getGroupObjectFromGroupName(String groupName);

}
