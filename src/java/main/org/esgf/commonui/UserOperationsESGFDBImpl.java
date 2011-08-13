package org.esgf.commonui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.esgf.adminui.Group;
import org.esgf.adminui.User;

import esg.common.util.ESGFProperties;
import esg.node.security.UserInfo;
import esg.node.security.UserInfoCredentialedDAO;
import esg.node.security.UserInfoDAO;

public class UserOperationsESGFDBImpl implements UserOperationsInterface {

    private final static Logger LOG = Logger.getLogger(UserOperationsESGFDBImpl.class);
    
    private File PASSWORD_FILE;
    
    private String passwd;
    private String root = "rootAdmin"; 
    
    public UserInfoCredentialedDAO myUserInfoDAO;
    
    public UserOperationsESGFDBImpl() throws FileNotFoundException, IOException {

        //get the password

        ESGFProperties myESGFProperties = new ESGFProperties();
        this.passwd = myESGFProperties.getAdminPassword();
        
        setMyUserInfoDAO(new UserInfoCredentialedDAO(root,passwd,myESGFProperties));
    }
    
    public void setMyUserInfoDAO(UserInfoCredentialedDAO myUserInfoDAO) {
        this.myUserInfoDAO = myUserInfoDAO;
    }
    
    /*
     * (non-Javadoc)
     * @see org.esgf.commonui.UserOperationsInterface#addUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void addUser(String first,String middle, String last, String email,
                        String username,String organization,String city,String state, String country) {
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
    public void editUser(String userId,String first,String middle, String last, String email,
            String username,String organization,String city,String state, String country) {
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
    
    
    /*
     * 
     */
    @Override
    public void deleteUser(String userName) {
        //System.out.println("<><><><><>REMOVE USER: " + userName);
        UserInfo user = this.myUserInfoDAO.getUserById(userName);
        this.myUserInfoDAO.deleteUser(user);
    }
    
    @Override
    public User getUserObjectFromUserName(String username) {
        //LOG.debug("In getUserObjectByUserName: " + username);
        User user = null;
        
        
        UserInfo userInfo = this.myUserInfoDAO.getUserById(username);
        
        /*
        if(userInfo == null) {
            System.out.println("UserINFO is NULL!");
        }
        */
        
        
        if(userInfo != null && userInfo.isValid()) {
            String openId = userInfo.getOpenid();
            
            String userId = Integer.toString(userInfo.getid());
            String first = userInfo.getFirstName();
            String last = userInfo.getLastName();
            String email = userInfo.getEmail();
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
            
        }
        
        
        return user;
    }
    
    /*
     * (non-Javadoc)
     * @see org.esgf.commonui.UserOperationsInterface#getUserObjectFromUserOpenID(java.lang.String)
     */
    @Override
    public User getUserObjectFromUserOpenID(String openId) {
        User user = null;
        
       UserInfo userInfo = myUserInfoDAO.getUserByOpenid(openId);
       
       if(userInfo != null && userInfo.isValid()) {
           String userId = Integer.toString(userInfo.getid());
           String username = userInfo.getUserName();
           String first = userInfo.getFirstName();
           String last = userInfo.getLastName();
           String email = userInfo.getEmail();
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
           
       }
        return user;
    }
    
    /*
     * (non-Javadoc)
     * @see org.esgf.commonui.UserOperationsInterface#getUserNameFromOpenID(java.lang.String)
     */
    @Override
    public String getUserNameFromOpenID(String openId) {
        String userName = null;
        UserInfo user = this.myUserInfoDAO.getUserByOpenid(openId);
        if(user != null & user.isValid()) {
            userName = user.getUserName();
        }
        
        return userName;
    }
    
    public String getUserIdFromOpenID(String openId) {
        String userId = null;
        
        UserInfo user = this.myUserInfoDAO.getUserByOpenid(openId);
        if(user != null && user.isValid()) {
            userId = Integer.toString(user.getid());
        }
        
        return userId;
    }
    
    /*
     * 
     */
    @Override
    public List<Group> getGroupsFromUser(String userName) {
        List<Group> groups = new ArrayList<Group>();
        
        UserInfo user = this.myUserInfoDAO.getUserById(userName);
        try {
            GroupOperationsInterface go = new GroupOperationsESGFDBImpl();
            Map<String,Set<String>> permissions = user.getPermissions();
            
            if(permissions != null) {
                for(final String group : permissions.keySet()) {
                    groups.add(go.getGroupObjectFromGroupName(group));
                }
            }
        } catch(Exception e) {
            LOG.debug("Error in getGroupsFromUser");
        }
        
        
        return groups;
    }
    
    /*
     * (non-Javadoc)
     * @see org.esgf.commonui.UserOperationsInterface#getUsersFromGroup(java.lang.String)
     */
    @Override
    public List<User> getUsersFromGroup(String groupName) {
        List<User> users = new ArrayList<User>();
        
        List<String> usernames = new ArrayList<String>();
        
        try {
            ESGFProperties props = new ESGFProperties();
            
            UserInfoDAO uid = new UserInfoDAO(props);
            
            for(int i=0;i<uid.getUserEntries().size();i++) {
                String [] str = uid.getUserEntries().get(i);
                
                String userName = str[0];
                
                UserInfo user = this.myUserInfoDAO.getUserById(userName);
                Map<String,Set<String>> permissions = user.getPermissions();
                if(permissions != null) {
                    for(final String group : permissions.keySet()) {
                        if(group.equals(groupName)) {
                            usernames.add(userName);
                        }
                    }
                }
                
            }
            
        } catch (Exception e) {
            LOG.debug("Error in getUsersFromGroup");
            e.printStackTrace();
        }
        
        
        for(int i=0;i<usernames.size();i++) {
            String userName = usernames.get(i);
            
            UserInfo userInfo = this.myUserInfoDAO.getUserById(userName);
            
            String userid = Integer.toString(userInfo.getid());
            String username = userInfo.getUserName();
            String first = userInfo.getFirstName();
            String middle = userInfo.getMiddleName();
            String last = userInfo.getLastName();
            String email = userInfo.getEmail();
            String organization = userInfo.getOrganization();
            String city = userInfo.getCity();
            String state = userInfo.getState();
            String country = userInfo.getCountry();
            String openid = userInfo.getOpenid();
            String dn = userInfo.getDn();
            
            User user = new User(userid,last,first,middle,username,email,organization,city,state,country,openid,dn);
            
            users.add(user);
        }
        
        return users;
    }
    
    
    
    /*
     * (non-Javadoc)
     * @see org.esgf.commonui.UserOperationsInterface#getAllUsers()
     */
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();
        
        try {
            ESGFProperties props = new ESGFProperties();
            UserInfoDAO uid = new UserInfoDAO(props);
            
            for(int i=1;i<uid.getUserEntries().size();i++) {
                String [] str = uid.getUserEntries().get(i);
                //LOG.debug("ADDING USER: |-> " + str[0]);
                User user = this.getUserObjectFromUserName(str[0]);
                users.add(user);
            }
            
        }catch(Exception e) {
            LOG.debug("Error in getAllUsers");
            e.printStackTrace();
        }
        
        return users;
        
    }
    
    /*
     * Note the hardcoded roles
     */
    @Override
    public void addUserToGroup(String userName, String groupName) {
        UserInfo user = this.myUserInfoDAO.getUserById(userName);
        
        if(user.isValid()) {
            if(user.getUserName().equals("rootAdmin")) {
                //System.out.println("adding user>>>" + userName);
                this.myUserInfoDAO.addPermission(user, groupName, "super");
            }
            else {
                //System.out.println("adding user>>>" + userName);
                this.myUserInfoDAO.addPermission(user, groupName, "default");
            }
        }
    }
    
    
    public UserInfoCredentialedDAO getMyUserInfoDAO() {
        return this.myUserInfoDAO;
    }
    
    public void setMyUserInfoCredentialedDAO(UserInfoCredentialedDAO myUserInfoDAO) {
        this.myUserInfoDAO = myUserInfoDAO;
    }
    
    /*
     * 
     */
    @Override
    public void deleteUserFromGroup(String userName,String groupName) {
        UserInfo user = this.myUserInfoDAO.getUserById(userName);
        
        if(this.isInGroup(userName, groupName)) {
            //this.myUserInfoDAO.deleteGroupFromUserPermissions(user,groupName);
            
            //LOG.debug("DELETING USER: " + userName + " from group[->" + groupName + " isValud: " + user.isValid());
            if(user.isValid()) {
                if(user.getUserName().equals("rootAdmin")) {
                    //System.out.println("rootadmin[->");
                    this.myUserInfoDAO.deleteGroupFromUserPermissions(user, groupName);
                    //this.myUserInfoDAO.deletePermission(user, groupName, "super");
                }
                else {
                    //System.out.println("Standard[->");
                    this.myUserInfoDAO.deleteGroupFromUserPermissions(user, groupName);
                    //this.myUserInfoDAO.deletePermission(user, groupName, "default");
                }
            }
            
        }
        
        
    }
    
    public boolean isInGroup(String userName,String groupName) {
        boolean isInGroup = false;
        
        UserInfo user = this.myUserInfoDAO.getUserById(userName);
        
        Map<String,Set<String>> perms = user.getPermissions();
        if(perms != null) {
            //System.out.println("size: " + perms.size());
            for(int i=0;i<perms.size();i++)
            {
                for(String key : perms.keySet()) {
                    //System.out.println("key " + key);
                    if(key.equals(groupName)) {
                        isInGroup = true;
                    }
                }
            }
        }
        
        
        
        return isInGroup;
    }
    
    public static void main(String [] args) throws FileNotFoundException, IOException {
        //UserOperationsInterface u = new UserOperationsESGFDBImpl();
        UserOperationsESGFDBImpl u = new UserOperationsESGFDBImpl();
        
        //ESGFProperties props = new ESGFProperties();
        //System.out.println(props.getAdminPassword());
        boolean isInGroup = u.isInGroup("jfh","1");
        //System.out.println(isInGroup);
    }
    
    
    
    /*
     * (non-Javadoc)
     * @see org.esgf.commonui.UserOperationsInterface#getUserIdFromUserName(java.lang.String)
     */
    @Override
    public String getUserIdFromUserName(String userName) {
        String userId = null;
        
        UserInfo user = this.myUserInfoDAO.getUserById(userName);
        
        if(user != null && user.isValid()) {
            userId = Integer.toString(user.getid());
        }
        
        
        // TODO Auto-generated method stub
        return userId;
    }
    

}
