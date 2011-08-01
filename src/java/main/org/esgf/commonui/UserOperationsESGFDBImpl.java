package org.esgf.commonui;

import java.util.List;

import org.esgf.adminui.User;

public class UserOperationsESGFDBImpl implements UserOperationsInterface {

    @Override
    public void addUser(String first, String middle, String last, String email,
            String username, String organization, String city, String state,
            String country) {
        // TODO Auto-generated method stub

    }

    @Override
    public void editUser(String userId, String first, String middle,
            String last, String email, String username, String organization,
            String city, String state, String country) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteUser(String userId) {
        // TODO Auto-generated method stub

    }

    @Override
    public User getUserObjectFromUserName(String userName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public User getUserObjectFromUserId(String userId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public User getUserObjectFromUserOpenID(String openId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getUserNameFromOpenID(String openId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getUserIdFromUserName(String userName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getUserIdFromOpenID(String openId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<User> getUsersFromGroup(String groupId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        // TODO Auto-generated method stub
        return null;
    }

}
