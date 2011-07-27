package org.esgf.adminui;

import java.util.List;

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
    
    public User getUserObjectFromUserId(final String userId);
    
    public String getUserNameFromOpenID(final String openId);
    
    public String getUserIdFromOpenID(final String openId);
    
    public List<User> getAllUsersInGroup(final String groupId);
    
    public List<User> getAllUsers();
    
    
}
