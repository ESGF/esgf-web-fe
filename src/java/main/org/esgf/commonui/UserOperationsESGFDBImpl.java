package org.esgf.commonui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.esgf.adminui.User;

import esg.common.util.ESGFProperties;
import esg.node.security.UserInfo;
import esg.node.security.UserInfoCredentialedDAO;
import esg.node.security.UserInfoDAO;

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

    /*private static String passwd = "mattryan12!";
    
    private UserInfoCredentialedDAO myUserInfoDAO;
    
   
    
    
    public UserOperationsESGFDBImpl() throws FileNotFoundException, IOException {
        ESGFProperties myESGFProperties = new ESGFProperties();
        
        setMyUserInfoDAO(new UserInfoCredentialedDAO("rootAdmin",passwd,myESGFProperties));
       
    }
    
    
    @Override
    public void addUser(String first, String middle, String last, String email,
            String username, String organization, String city, String state,
            String country) {
        
        UserInfo user = myUserInfoDAO.getUserById(username);
        
        user.setFirstName(first);
        user.setMiddleName(middle);
        user.setLastName(last);
        user.setEmail(last);
        user.setOrganization(organization);
        user.setCity(city);
        user.setState(state);
        user.setCountry(country);

        myUserInfoDAO.addUserInfo(user);      
        
        
    }

    @Override
    public void editUser(String userId, String first, String middle,
            String last, String email, String username, String organization,
            String city, String state, String country) {
        // TODO Auto-generated method stub
        
        UserInfo user = myUserInfoDAO.getUserById(username);
        
        user.setFirstName(first);
        user.setMiddleName(middle);
        user.setLastName(last);
        user.setEmail(email);
        user.setOrganization(organization);
        user.setCity(city);
        user.setState(state);
        user.setCountry(country);

        
        myUserInfoDAO.addUserInfo(user); 
    }

    @Override
    public void deleteUser(String userId) {
        // TODO Auto-generated method stub
System.out.println("\n\n\n\n");
        
        System.out.println("userId: " + userId);
        
        System.out.println("\n\n\n\n");
        
        UserInfo user = null;
        if(userId != null) user = this.myUserInfoDAO.getUserById(userId);


        System.out.println("\n\n\n\n");
        
        System.out.println("USER: " + user);
        
        System.out.println("\n\n\n\n");
        
        
        
        myUserInfoDAO.deleteUserInfo(user);
        

    }

    
    @Override
    public User getUserObjectFromUserName(String username) {
        // TODO Auto-generated method stub
        
        UserInfo userInfo = myUserInfoDAO.getUserById(username);

        User user = new User();

        //required
        String openId = userInfo.getOpenid();
        String userId = Integer.toString(userInfo.getid());
        //String username = userInfo.getUserName();
        String first = userInfo.getFirstName();
        String last = userInfo.getLastName();
        String email = userInfo.getEmail();
        
        //not required
        String middle = userInfo.getMiddleName();
        if(middle == null || middle.equals("")) {
            middle = "N/A";
        }
        String organization = userInfo.getOrganization();
        if(organization == null || organization.equals("")) {
            organization = "N/A";
        }
        String city = userInfo.getCity();
        if(city == null || city.equals("")) {
            city = "N/A";
        }
        String state = userInfo.getState();
        if(state == null || state.equals("")) {
            state = "N/A";
        }
        String country = userInfo.getCountry();
        if(country == null || country.equals("")) {
            country = "N/A";
        }
        String dn = userInfo.getDn();
        if(dn == null || dn.equals("")) {
            dn = "N/A";
        }

        user = new User(userId,last,first,middle,username,email,organization,city,state,country,openId,dn);
        
        
        
        return user;
    }

    @Override
    public User getUserObjectFromUserOpenID(String openId) {
        // TODO Auto-generated method stub
        
        UserInfo userInfo = myUserInfoDAO.getUserByOpenid(openId);

        User user = new User();

        //required
        String userId = Integer.toString(userInfo.getid());
        String username = userInfo.getUserName();
        String first = userInfo.getFirstName();
        String last = userInfo.getLastName();
        String email = userInfo.getEmail();
        
        //not required
        String middle = userInfo.getMiddleName();
        if(middle == null || middle.equals("")) {
            middle = "N/A";
        }
        String organization = userInfo.getOrganization();
        if(organization == null || organization.equals("")) {
            organization = "N/A";
        }
        String city = userInfo.getCity();
        if(city == null || city.equals("")) {
            city = "N/A";
        }
        String state = userInfo.getState();
        if(state == null || state.equals("")) {
            state = "N/A";
        }
        String country = userInfo.getCountry();
        if(country == null || country.equals("")) {
            country = "N/A";
        }
        String dn = userInfo.getDn();
        if(dn == null || dn.equals("")) {
            dn = "N/A";
        }

        user = new User(userId,last,first,middle,username,email,organization,city,state,country,openId,dn);
        
        
        
        return user;
    }

    @Override
    public String getUserNameFromOpenID(String openId) {
        // TODO Auto-generated method stub
        
        UserInfo user = myUserInfoDAO.getUserByOpenid(openId);

        String userName = user.getUserName();
        
        return userName;
    }

    @Override
    public String getUserIdFromUserName(String userName) {
        // TODO Auto-generated method stub
        
        UserInfo user = myUserInfoDAO.getUserById(userName);
        
        String userId = Integer.toString(user.getid());
        
        return userId;
    }

    @Override
    public String getUserIdFromOpenID(String openId) {
        // TODO Auto-generated method stub
        
        UserInfo user = myUserInfoDAO.getUserByOpenid(openId);
        
        String userId = Integer.toString(user.getid());
        
        return userId;
    }

    @Override
    public List<User> getUsersFromGroup(String groupId) {
        // Need to create a query that does this
        
        List<User> users = this.getAllUsers();
        
        return users;
    }

    @Override
    public List<User> getAllUsers() {

        List<User> users = new ArrayList<User>();
        
        try {
            ESGFProperties props = new ESGFProperties();
            
            UserInfoDAO uid = new UserInfoDAO(props);
            
            
            for(int i=0;i<uid.getUserEntries().size();i++) {
                String [] str = uid.getUserEntries().get(i);

                User us = this.getUserObjectFromUserName(str[0]);
                
                users.add(us);
            }
        }catch(Exception e) {
            System.out.println("Error in getAllUsers");
        }
        
        
        return users;
    }
    
    public static void main(String [] args) throws FileNotFoundException, IOException {
         
        
        UserOperationsESGFDBImpl uo = new UserOperationsESGFDBImpl();
        
        String username = "_userName232";
        String first = "_firstName1";
        String middle = "_middleName1";
        String last = "_lastName1";
        String email = "_email1";
        String organization = "_organization1";
        String city = "_city1";
        String state = "_state1";
        String country = "_country1";
  
        
        username = "gavin";
        
        ESGFProperties myESGFProperties = new ESGFProperties();
        
      
        
        
        UserInfoDAO uid = new UserInfoDAO(myESGFProperties);
        
        List<User> users = uo.getAllUsers();
        
        uo.editUser("userId", "first", "middle",
                "last", "email", "gavin", "organization",
                " city", "state", "country");
    }


    public UserInfoCredentialedDAO getMyUserInfoDAO() {
        return myUserInfoDAO;
    }


    public void setMyUserInfoDAO(UserInfoCredentialedDAO myUserInfoDAO) {
        this.myUserInfoDAO = myUserInfoDAO;
    }*/

}
