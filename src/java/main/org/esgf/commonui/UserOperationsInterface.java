package org.esgf.commonui;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.esgf.adminui.Group;
import org.esgf.adminui.User;

public interface UserOperationsInterface {

    public void addUser(String first,String middle,String last,String email,String username,String organization,String city,String state,String country);
    //could be better to do this:
    //public void addUser(User user);
    
    public void editUser(String userId,String first,String middle,String last,String email,String username,String organization,String city,String state,String country);
    //could be better to do this:
    //public void editUser(User user);
    
    public void deleteUser(final String userId);
    //could be better to do this:
    //public void deleteUser(User user)
    
    public Map<String,Set<String>> getUserPermissionsFromOpenID(String openId);

    public User getUserObjectFromUserName(final String userName);

    //public User getUserObjectFromUserId(final String userId);
    
    public User getUserObjectFromUserOpenID(final String openId);
    
    public String getUserNameFromOpenID(final String openId);
    
    public String getUserIdFromUserName(final String userName);
    
    public String getUserIdFromOpenID(final String openId);
    
    public List<User> getUsersFromGroup(final String groupName);
    
    public List<User> getAllUsers();

    void addUserToGroup(String userName, String groupName);

    void deleteUserFromGroup(String userName, String groupName);

    List<Group> getGroupsFromUser(String userName);

    List<User> getSomeUsers(final int start, final int end);
    
    
}
