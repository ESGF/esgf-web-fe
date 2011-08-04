package org.esgf.commonui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.esgf.adminui.Group;

import esg.common.util.ESGFProperties;
import esg.node.security.GroupRoleDAO;

public class GroupOperationsESGFDBImpl implements GroupOperationsInterface {

    GroupRoleDAO groupRoleDAO;
    
    public GroupOperationsESGFDBImpl() throws FileNotFoundException, IOException {
        groupRoleDAO = new GroupRoleDAO(new ESGFProperties());
        
    }
    
    @Override
    public void addGroup(String groupName, String groupDescription) {
        // TODO Auto-generated method stub
        groupRoleDAO.addGroup(groupName,groupDescription);
        
    }

    @Override
    public void editGroup(String groupId, String newGroupName,
            String groupDescription) {
        // TODO Auto-generated method stub
        String oldGroupName = null;
        
        for(int i=0;i<groupRoleDAO.getGroupEntries().size();i++) {
            
            //column1 is the group name
            String groupCol = groupRoleDAO.getGroupEntries().get(i)[0];
            if(groupCol.equalsIgnoreCase(groupId)) {
                oldGroupName = groupRoleDAO.getGroupEntries().get(i)[1];
            }
            
        }
        if(oldGroupName != null) {
            groupRoleDAO.renameGroup(oldGroupName, newGroupName);
        }
        
    }

    @Override
    public void deleteGroup(String groupId) {
        // TODO Auto-generated method stub
        
        groupRoleDAO.deleteGroup(groupId);

    }

    /*
    @Override
    public Group getGroupObjectFromGroupId(String groupId) {
        // TODO Auto-generated method stub
        return null;
    }
    */
    
    @Override
    public String getGroupIdFromGroupName(String groupName) {
        String groupId = null;
        
        for(int i=0;i<groupRoleDAO.getGroupEntries().size();i++) {
            
            //column1 is the group name
            String groupCol = groupRoleDAO.getGroupEntries().get(i)[1];
            if(groupCol.equalsIgnoreCase(groupName)) {
                groupId = groupRoleDAO.getGroupEntries().get(i)[0];
            }
            
        }
        
        
        return groupId;
    }

    @Override
    public List<Group> getGroupsFromUser(String userId) {
        // TODO Auto-generated method stub
        List<Group> groups = this.getAllGroups();
        
        return groups;
    }

    @Override
    public List<Group> getAllGroups() {
        
        List<Group> groups = new ArrayList<Group>();
        
        for(int i=0;i<groupRoleDAO.getGroupEntries().size();i++) {
            Group group = new Group();
            
            //column1 is the group name
            String groupCol = groupRoleDAO.getGroupEntries().get(i)[1];
            //if(groupCol.equalsIgnoreCase(groupName)) {
                String groupDescription = groupRoleDAO.getGroupEntries().get(i)[2];
                String groupName = groupRoleDAO.getGroupEntries().get(i)[1];
                String groupId = groupRoleDAO.getGroupEntries().get(i)[0];
                group = new Group(groupId,groupName,groupDescription);
                
            groups.add(group);
            
        }
        return groups;
    }
    
    @Override
    public Group getGroupObjectFromGroupName(String groupName) {
        // TODO Auto-generated method stub
        
        Group group = new Group();
        
        for(int i=0;i<groupRoleDAO.getGroupEntries().size();i++) {
            
            //column1 is the group name
            String groupCol = groupRoleDAO.getGroupEntries().get(i)[1];
            if(groupCol.equalsIgnoreCase(groupName)) {
                String groupDescription = groupRoleDAO.getGroupEntries().get(i)[2];
                String groupId = groupRoleDAO.getGroupEntries().get(i)[0];
                group = new Group(groupId,groupName,groupDescription);
                
            }
            
        }
        
        
        return group;
    }
    
    public static void main(String [] args) throws FileNotFoundException, IOException {
        System.out.println("Groups");
        
        
        GroupOperationsESGFDBImpl go = new GroupOperationsESGFDBImpl();
        
        String groupId = "1";
        String groupName = "rootAdmin";
        String groupDescription = "groupdescription22";
        
        
        go.editGroup(groupId, groupName, groupDescription);
        //go.deleteGroup(groupName);
        System.out.println(go.getGroupIdFromGroupName(groupName));
    }

    

}
