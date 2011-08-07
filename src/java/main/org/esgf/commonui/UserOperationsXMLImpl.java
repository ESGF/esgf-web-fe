package org.esgf.commonui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.esgf.adminui.CreateGroupsController;
import org.esgf.adminui.Group;
import org.esgf.adminui.User;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.springframework.core.io.ClassPathResource;


public class UserOperationsXMLImpl implements UserOperationsInterface {
    
    private final static Logger LOG = Logger.getLogger(UserOperationsXMLImpl.class);

    private final static String users_file = "C:\\Users\\8xo\\esgProjects\\esgf-6-29\\esgf-web-fe\\esgf-web-fe\\src\\java\\main\\db.users";
    private final static String groups_file = "C:\\Users\\8xo\\esgProjects\\esgf-6-29\\esgf-web-fe\\esgf-web-fe\\src\\java\\main\\db.groups";
    private final static String roles_file = "C:\\Users\\8xo\\esgProjects\\esgf-6-29\\esgf-web-fe\\esgf-web-fe\\src\\java\\main\\db.roles";
    private final static String permissions_file = "C:\\Users\\8xo\\esgProjects\\esgf-6-29\\esgf-web-fe\\esgf-web-fe\\src\\java\\main\\db.permissions";
   
    
    private File USERS_FILE;
    private File GROUPS_FILE;
    private File ROLES_FILE;
    private File PERMISSIONS_FILE;
    
    public UserOperationsXMLImpl(){
        USERS_FILE = new File("");
        GROUPS_FILE = new File("");
        ROLES_FILE = new File("");
        PERMISSIONS_FILE = new File("");
        
        try {
            File dir1 = new File(".");
            USERS_FILE = new File(users_file); //new ClassPathResource("db.users").getFile();
            GROUPS_FILE = new File(groups_file); //new ClassPathResource("db.groups").getFile();
            PERMISSIONS_FILE = new File(permissions_file);//new ClassPathResource("db.permissions").getFile();
            ROLES_FILE = new File(roles_file);
        }catch(Exception e) {
            LOG.debug("error in UserOperationsXMLImpl constructor");
        }
    }

    @Override
    public void addUser(String first, String middle, String last, String email,
            String username, String organization, String city, String state,
            String country) {
        
        String userId = Utils.createUserId(USERS_FILE);
        String openId = Utils.createOpenId(USERS_FILE);
        String userName = Utils.createUserName(USERS_FILE);
        String dn = Utils.createUserDN(USERS_FILE);
        
        
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        
        try{
            
            Document document = (Document) builder.build(USERS_FILE);
            Element rootNode = document.getRootElement();
            Element userElement = new Element("user");
            
            Element userIdEl = new Element("id");
            userIdEl.addContent(userId);
            Element userfirstEl = new Element("first");
            userfirstEl.addContent(first);
            Element userlastEl = new Element("last");
            userlastEl.addContent(last);
            Element usermiddleEl = new Element("middle");
            usermiddleEl.addContent(middle);
            Element userNameEl = new Element("username");
            userNameEl.addContent(userName);
            Element emailEl = new Element("email");
            emailEl.addContent(email);
            Element organizationEl = new Element("organization");
            organizationEl.addContent(organization);
            Element cityEl = new Element("city");
            cityEl.addContent(city);
            Element stateEl = new Element("state");
            stateEl.addContent(state);
            Element countryEl = new Element("country");
            countryEl.addContent(country);
            Element openIdEl = new Element("openid");
            openIdEl.addContent(openId);
            Element dnEl = new Element("dn");
            dnEl.addContent(dn);

            userElement.addContent(userIdEl);
            userElement.addContent(userfirstEl);
            userElement.addContent(userlastEl);
            userElement.addContent(usermiddleEl);
            userElement.addContent(userNameEl);
            userElement.addContent(emailEl);
            userElement.addContent(organizationEl);
            userElement.addContent(cityEl);
            userElement.addContent(stateEl);
            userElement.addContent(countryEl);
            userElement.addContent(openIdEl);
            userElement.addContent(dnEl);

            rootNode.addContent(userElement);
            
            Utils.writeXMLContentToFile(rootNode, USERS_FILE);
            
        }catch(Exception e) {
            LOG.debug("Error in addUser");
            e.printStackTrace();
        }
        
    }

    
    
    @Override
    public void editUser(String userId, String first, String middle,
            String last, String email, String username, String organization,
            String city, String state, String country) {
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        try{
            Document document = (Document) builder.build(USERS_FILE);

            Element rootNode = document.getRootElement();
            Element newElement_users = new Element("users");

            List users = (List)rootNode.getChildren();
            for(int i=0;i<users.size();i++)
            {
                Element userEl = (Element)users.get(i);
                Element newElement_user = new Element("user");

                Element userIdEl = userEl.getChild("id");
                Element userFirstEl = userEl.getChild("first");
                Element userMiddleEl = userEl.getChild("middle");
                Element userLastEl = userEl.getChild("last");
                Element userEmailEl = userEl.getChild("email");
                Element userUserNameEl = userEl.getChild("username");
                Element userDNEl = userEl.getChild("dn");
                Element userOpenIDEl = userEl.getChild("openid");
                Element userOrganizationEl = userEl.getChild("organization");
                Element userCityEl = userEl.getChild("city");
                Element userStateEl = userEl.getChild("state");
                Element userCountryEl = userEl.getChild("country");
                
                String userIdText = userIdEl.getTextNormalize();
                String userFirstText = userFirstEl.getTextNormalize();
                String userMiddleText = userMiddleEl.getTextNormalize();
                String userLastText = userLastEl.getTextNormalize();
                String userEmailText = userEmailEl.getTextNormalize();
                String userUserNameText = userUserNameEl.getTextNormalize();
                String userDNText = userDNEl.getTextNormalize();
                String userOpenIDText = userOpenIDEl.getTextNormalize();
                String userOrganizationText = userOrganizationEl.getTextNormalize();
                String userCityText = userCityEl.getTextNormalize();
                String userStateText = userStateEl.getTextNormalize();
                String userCountryText = userCountryEl.getTextNormalize();
                
                
                if(userIdEl.getTextNormalize().equals(userId)) {
                    userIdText = userId;
                    userFirstText =first;
                    userMiddleText = middle;
                    userLastText = last;
                    userEmailText = email;
                    userOrganizationText = organization;
                    userCityText = city;
                    userStateText = state;
                    userCountryText = country;
                }
                
                Element newElement_userIdEl = new Element("id");
                Element newElement_userFirstEl = new Element("first");
                Element newElement_userMiddleEl = new Element("middle");
                Element newElement_userLastEl = new Element("last");
                Element newElement_userEmailEl = new Element("email");
                Element newElement_userUserNameEl = new Element("username");
                Element newElement_userDNEl = new Element("dn");
                Element newElement_userOpenIDEl = new Element("openid");
                Element newElement_userOrganizationEl = new Element("organization");
                Element newElement_userCityEl = new Element("city");
                Element newElement_userStateEl = new Element("state");
                Element newElement_userCountryEl = new Element("country");

                newElement_userIdEl.setText(userIdText);
                newElement_userFirstEl.setText(userFirstText);
                newElement_userMiddleEl.setText(userMiddleText);
                newElement_userLastEl.setText(userLastText);
                newElement_userEmailEl.setText(userEmailText);
                newElement_userUserNameEl.setText(userUserNameText);
                newElement_userDNEl.setText(userDNText);
                newElement_userOpenIDEl.setText(userOpenIDText);
                newElement_userOrganizationEl.setText(userOrganizationText);
                newElement_userCityEl.setText(userCityText);
                newElement_userStateEl.setText(userStateText);
                newElement_userCountryEl.setText(userCountryText);

                newElement_user.addContent((Element)newElement_userIdEl);
                newElement_user.addContent((Element)newElement_userFirstEl);
                newElement_user.addContent((Element)newElement_userMiddleEl);
                newElement_user.addContent((Element)newElement_userLastEl);
                newElement_user.addContent((Element)newElement_userEmailEl);
                newElement_user.addContent((Element)newElement_userUserNameEl);
                newElement_user.addContent((Element)newElement_userDNEl);
                newElement_user.addContent((Element)newElement_userOpenIDEl);
                newElement_user.addContent((Element)newElement_userOrganizationEl);
                newElement_user.addContent((Element)newElement_userCityEl);
                newElement_user.addContent((Element)newElement_userStateEl);
                newElement_user.addContent((Element)newElement_userCountryEl);
                
                newElement_users.addContent(newElement_user);
            }
            
            Utils.writeXMLContentToFile(newElement_users, USERS_FILE);
            
        }catch(Exception e) {
            LOG.debug("Error in editUser");
            e.printStackTrace();
        }
        
    }

    @Override
    public void deleteUser(String userId) {
        
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        
        try{
            Document document = (Document) builder.build(USERS_FILE);
            
            Element rootNode = document.getRootElement();
            List users = (List)rootNode.getChildren();
            for(int i=0;i<users.size();i++)
            {
                Element userEl = (Element)users.get(i);
                Element userIdEl = userEl.getChild("id");
                if(userIdEl.getTextNormalize().equals(userId)) {
                    rootNode.removeContent(userEl);
                }
                
            }
            Utils.writeXMLContentToFile(rootNode, USERS_FILE);
            
        }catch(Exception e) {
            LOG.debug("Error in deleteUser");
            e.printStackTrace();    
        }
    }

    @Override
    public User getUserObjectFromUserName(String userName) {
        User user = null;

        SAXBuilder builder = new SAXBuilder();
        try{
            Document groups_document = (Document) builder.build(USERS_FILE);
            Element rootNode = groups_document.getRootElement();
            List users = (List)rootNode.getChildren();
            for(int i=0;i<users.size();i++)
            {
                Element userEl = (Element)users.get(i);
                Element userIdEl = userEl.getChild("id");
                Element userFirstEl = userEl.getChild("first");
                Element userMiddleEl = userEl.getChild("middle");
                Element userLastEl = userEl.getChild("last");
                Element userEmailEl = userEl.getChild("email");
                Element userUserNameEl = userEl.getChild("username");
                Element userDNEl = userEl.getChild("dn");
                Element userOpenIDEl = userEl.getChild("openid");
                Element userOrganizationEl = userEl.getChild("organization");
                Element userCityEl = userEl.getChild("city");
                Element userStateEl = userEl.getChild("state");
                Element userCountryEl = userEl.getChild("country");
                
                if(userUserNameEl.getTextNormalize().equals(userName)) {
                    user = new User();
                    user.setUserId(userIdEl.getTextNormalize());
                    user.setUserName(userUserNameEl.getTextNormalize());
                    user.setFirstName(userFirstEl.getTextNormalize());
                    user.setLastName(userLastEl.getTextNormalize());
                    user.setMiddleName(userMiddleEl.getTextNormalize());
                    user.setEmailAddress(userEmailEl.getTextNormalize());
                    user.setOpenId(userOpenIDEl.getTextNormalize());
                    user.setOrganization(userOrganizationEl.getTextNormalize());
                    user.setCity(userCityEl.getTextNormalize());
                    user.setCountry(userCountryEl.getTextNormalize());
                    user.setDN(userDNEl.getTextNormalize());
                    user.setOrganization(userIdEl.getTextNormalize());
                    user.setState(userStateEl.getTextNormalize());
                    
                }
            }
        }catch(Exception e) {
            LOG.debug("Problem in getUserObjectFromUserName");
            e.printStackTrace();
            
        }
        return user;
    }

    @Override
    public User getUserObjectFromUserOpenID(String openId) {
        User user = null;
        
        SAXBuilder builder = new SAXBuilder();
        try{
            Document groups_document = (Document) builder.build(USERS_FILE);
            Element rootNode = groups_document.getRootElement();
            List users = (List)rootNode.getChildren();
            for(int i=0;i<users.size();i++)
            {
                Element userEl = (Element)users.get(i);
                Element userIdEl = userEl.getChild("id");
                Element userFirstEl = userEl.getChild("first");
                Element userMiddleEl = userEl.getChild("middle");
                Element userLastEl = userEl.getChild("last");
                Element userEmailEl = userEl.getChild("email");
                Element userUserNameEl = userEl.getChild("username");
                Element userDNEl = userEl.getChild("dn");
                Element userOpenIDEl = userEl.getChild("openid");
                Element userOrganizationEl = userEl.getChild("organization");
                Element userCityEl = userEl.getChild("city");
                Element userStateEl = userEl.getChild("state");
                Element userCountryEl = userEl.getChild("country");
                
                if(userOpenIDEl.getTextNormalize().equals(openId)) {
                    user = new User();
                    user.setUserId(userIdEl.getTextNormalize());
                    user.setUserName(userUserNameEl.getTextNormalize());
                    user.setFirstName(userFirstEl.getTextNormalize());
                    user.setLastName(userLastEl.getTextNormalize());
                    user.setMiddleName(userMiddleEl.getTextNormalize());
                    user.setEmailAddress(userEmailEl.getTextNormalize());
                    user.setOpenId(userOpenIDEl.getTextNormalize());
                    user.setOrganization(userOrganizationEl.getTextNormalize());
                    user.setCity(userCityEl.getTextNormalize());
                    user.setCountry(userCountryEl.getTextNormalize());
                    user.setDN(userDNEl.getTextNormalize());
                    user.setOrganization(userIdEl.getTextNormalize());
                    user.setState(userStateEl.getTextNormalize());
                    
                }
            }
        }catch(Exception e) {
            LOG.debug("Problem in getUserObjectFromOpenID");
            e.printStackTrace();
            
        }
        return user;
    }

    @Override
    public String getUserNameFromOpenID(String openId) {
        
        String userId = null;
        
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        try{
            Document users_document = (Document) builder.build(USERS_FILE);
            Element rootNode = users_document.getRootElement();
            List users = (List)rootNode.getChildren();
            for(int i=0;i<users.size();i++)
            {
                Element userEl = (Element)users.get(i);
                if(userEl.getChild("openid").getTextNormalize().equals(openId)){
                    userId = userEl.getChildText("username");
                }
            }
            
        }catch(Exception e) {
            e.printStackTrace();
            LOG.debug("Error in getUserNameFromOpenID");
        }
        return userId;
    }

    
    
    @Override
    public String getUserIdFromUserName(String userName) {

        String userId = null;
        
        SAXBuilder builder = new SAXBuilder();
        try{
            Document users_document = (Document) builder.build(USERS_FILE);
            Element rootNode = users_document.getRootElement();
            List users = (List)rootNode.getChildren();
            for(int i=0;i<users.size();i++)
            {
                Element userEl = (Element)users.get(i);
                if(userEl.getChild("username").getTextNormalize().equals(userName)){
                    userId = userEl.getChildText("id");
                }
            }
            
        }catch(Exception e) {
            e.printStackTrace();
            LOG.debug("Error in getUserIdFromUserName");
        }
        
        return userId;
    }

    @Override
    public String getUserIdFromOpenID(String openId) {
        String userId = null;
        
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        try{
            Document users_document = (Document) builder.build(USERS_FILE);
            Element rootNode = users_document.getRootElement();
            List users = (List)rootNode.getChildren();
            for(int i=0;i<users.size();i++)
            {
                Element userEl = (Element)users.get(i);
                if(userEl.getChild("openid").getTextNormalize().equals(openId)){
                    userId = userEl.getChildText("id");
                }
            }
            
        }catch(Exception e) {
            e.printStackTrace();
            LOG.debug("Error in getUserIdFromOpenID");
        }
        
        return userId;
    }

    @Override
    public List<User> getUsersFromGroup(String groupName) {

        List<User> users = new ArrayList<User>();
        
        GroupOperationsInterface goi = new GroupOperationsXMLImpl();
        String groupId = goi.getGroupIdFromGroupName(groupName);
        
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        try{
            Document permissions_document = (Document) builder.build(PERMISSIONS_FILE);
            
            Element rootNode = permissions_document.getRootElement();
            List permissions = (List)rootNode.getChildren();
            
            for(int i=0;i<permissions.size();i++)
            {
                Element tupleEl = (Element)permissions.get(i);
                Element userIdEl = tupleEl.getChild("userid");
                Element groupIdEl = tupleEl.getChild("groupid");
                if(groupIdEl.getTextNormalize().equals(groupId)) {
                    String userId = userIdEl.getTextNormalize();
                    User user = getUserObjectFromUserId(userId);
                    users.add(user);
                }
            }
            
        }catch(Exception e) {
            LOG.debug("Error in getUsersFromGroup");
        }
        
        return users;
    }

    @Override
    public List<User> getAllUsers() {
        
        List<User> usersList = new ArrayList<User>();
        
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        try{
            Document groups_document = (Document) builder.build(USERS_FILE);
            Element rootNode = groups_document.getRootElement();
            List users = (List)rootNode.getChildren();
            for(int i=0;i<users.size();i++)
            {
                Element userEl = (Element)users.get(i);
                Element userIdEl = userEl.getChild("id");
                Element userFirstEl = userEl.getChild("first");
                Element userMiddleEl = userEl.getChild("middle");
                Element userLastEl = userEl.getChild("last");
                Element userEmailEl = userEl.getChild("email");
                Element userUserNameEl = userEl.getChild("username");
                Element userDNEl = userEl.getChild("dn");
                Element userOpenIDEl = userEl.getChild("openid");
                Element userOrganizationEl = userEl.getChild("organization");
                Element userCityEl = userEl.getChild("city");
                Element userStateEl = userEl.getChild("state");
                Element userCountryEl = userEl.getChild("country");
                
                //if(userUserNameEl.getTextNormalize().equals(userName)) {
                User user = new User();
                user.setUserId(userIdEl.getTextNormalize());
                user.setUserName(userUserNameEl.getTextNormalize());
                user.setFirstName(userFirstEl.getTextNormalize());
                user.setLastName(userLastEl.getTextNormalize());
                user.setMiddleName(userMiddleEl.getTextNormalize());
                user.setEmailAddress(userEmailEl.getTextNormalize());
                user.setOpenId(userOpenIDEl.getTextNormalize());
                user.setCity(userCityEl.getTextNormalize());
                user.setCountry(userCountryEl.getTextNormalize());
                user.setDN(userDNEl.getTextNormalize());
                user.setOrganization(userIdEl.getTextNormalize());
                user.setState(userStateEl.getTextNormalize());
                    
                usersList.add(user);
            }
        }catch(Exception e) {
            LOG.debug("Problem in getAllUsers");
            e.printStackTrace();
            
        }
        return usersList;
    }

    
    @Override
    public void addUserToGroup(String userName, String groupName) {
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";

        GroupOperationsInterface go = new GroupOperationsXMLImpl();
        UserOperationsInterface uo = new UserOperationsXMLImpl();
        
        
        //add a tuple to permissions table
        try{
            Document permissions_document = (Document) builder.build(PERMISSIONS_FILE);
            
            Element rootNode = permissions_document.getRootElement();
            
            if(!permissionElementExists(rootNode,userName,groupName)) {
                List permissions = (List)rootNode.getChildren();
                
                Element newTupleEl = new Element("tuple");
                Element newUserIdEl = new Element("userid");
                Element newGroupIdEl = new Element("groupid");
                Element newRoleIdEl = new Element("roleid");
                newUserIdEl.addContent(uo.getUserIdFromUserName(userName));
                newGroupIdEl.addContent(go.getGroupIdFromGroupName(groupName));
                if(userName.equals("rootAdmin")) {
                    newRoleIdEl.addContent("role4_id");
                } else {
                    newRoleIdEl.addContent("role1_id");
                }
                
                newTupleEl.addContent(newUserIdEl);
                newTupleEl.addContent(newGroupIdEl);
                newTupleEl.addContent(newRoleIdEl);
                

                rootNode.addContent(newTupleEl);
                
                Utils.writeXMLContentToFile(rootNode, PERMISSIONS_FILE);
            }
            
        } catch(Exception e) {
            System.out.println("Error in addUserToGroup");
            e.printStackTrace();
        }
    }
    
    private boolean permissionElementExists(Element rootNode,String userName,String groupName) {
        boolean permissionElementExists = false;
        
        GroupOperationsInterface go = new GroupOperationsXMLImpl();
        UserOperationsInterface uo = new UserOperationsXMLImpl();
        
        List permissions = (List)rootNode.getChildren();
        
        for(int i=0;i<permissions.size();i++) {
            Element tuple = (Element)permissions.get(i);
            String groupid = tuple.getChild("groupid").getTextNormalize();
            String userid = tuple.getChild("userid").getTextNormalize();
            if(groupid.equalsIgnoreCase(go.getGroupIdFromGroupName(groupName)) &&
                    userid.equalsIgnoreCase(uo.getUserIdFromUserName(userName))) {
                permissionElementExists = true;
            }
        }
        return permissionElementExists;
    }

    @Override
    public void deleteUserFromGroup(String userName, String groupName) {
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";

        GroupOperationsInterface go = new GroupOperationsXMLImpl();
        UserOperationsInterface uo = new UserOperationsXMLImpl();
        
        //add a tuple to permissions table
        try{
            Document permissions_document = (Document) builder.build(PERMISSIONS_FILE);
            
            Element rootNode = permissions_document.getRootElement();
            if(permissionElementExists(rootNode,userName,groupName)) {
                List permissions = (List)rootNode.getChildren();
            
                for(int i=0;i<permissions.size();i++) {
                    Element tupleEl = (Element)permissions.get(i);
                    Element userIdEl = tupleEl.getChild("userid");
                    Element groupIdEl = tupleEl.getChild("groupid");
                    
                    if(groupIdEl.getTextNormalize().equalsIgnoreCase(go.getGroupIdFromGroupName(groupName)) &&
                       userIdEl.getTextNormalize().equalsIgnoreCase(uo.getUserIdFromUserName(userName)) ) {
                        rootNode.removeContent(tupleEl);
                    }
                }
                Utils.writeXMLContentToFile(rootNode, PERMISSIONS_FILE);
            }
            
        }catch(Exception e) {
            LOG.debug("Error in deleteUserFromGroup");
            e.printStackTrace();
        }
        
    }
    
    
    public static void main(String [] args) {
        UserOperationsInterface uo = new UserOperationsXMLImpl();
        
        String userName = "user8_userName";
        String groupName = "group2_name";
        
        
        
        uo.addUserToGroup(userName, groupName);
        
        
        
        
        //uo.deleteUserFromGroup(userName, groupName);
        
        List<Group> groups = uo.getGroupsFromUser(userName);
        
        System.out.println(groups);
    }
    

    @Override
    public List<Group> getGroupsFromUser(String userName) {
        
        List<Group> groups = new ArrayList<Group>();
    
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";

        GroupOperationsInterface go = new GroupOperationsXMLImpl();
        UserOperationsInterface uo = new UserOperationsXMLImpl();
        
        //add a tuple to permissions table
        try{
            Document permissions_document = (Document) builder.build(PERMISSIONS_FILE);
            
            Element rootNode = permissions_document.getRootElement();
            
            String groupid = uo.getUserIdFromUserName(userName);
            
            List permissions = (List)rootNode.getChildren();
            
            for(int i=0;i<permissions.size();i++) {
                Element tupleEl = (Element)permissions.get(i);
                Element groupIdEl = tupleEl.getChild("groupid");
                Element userIdEl = tupleEl.getChild("userid");
                if(userIdEl.getTextNormalize().equalsIgnoreCase(uo.getUserIdFromUserName(userName))) {
                    String groupName = go.getGroupNameFromGroupId(groupIdEl.getTextNormalize());
                    Group group = go.getGroupObjectFromGroupName(groupName);
                    groups.add(group);
                }
            }
            
        } catch(Exception e) {
            LOG.debug("Error in getGroupsFromUser");
            e.printStackTrace();
        }
        
        
        return groups;
    }
    
    
    /*
     * Helper method for getUsersFromGroup method above
     */
    private User getUserObjectFromUserId(String userId) {
        User user = null;

        SAXBuilder builder = new SAXBuilder();
        try{
            Document groups_document = (Document) builder.build(USERS_FILE);
            Element rootNode = groups_document.getRootElement();
            List users = (List)rootNode.getChildren();
            for(int i=0;i<users.size();i++)
            {
                Element userEl = (Element)users.get(i);
                Element userIdEl = userEl.getChild("id");
                Element userFirstEl = userEl.getChild("first");
                Element userMiddleEl = userEl.getChild("middle");
                Element userLastEl = userEl.getChild("last");
                Element userEmailEl = userEl.getChild("email");
                Element userUserNameEl = userEl.getChild("username");
                Element userDNEl = userEl.getChild("dn");
                Element userOpenIDEl = userEl.getChild("openid");
                Element userOrganizationEl = userEl.getChild("organization");
                Element userCityEl = userEl.getChild("city");
                Element userStateEl = userEl.getChild("state");
                Element userCountryEl = userEl.getChild("country");
                
                if(userIdEl.getTextNormalize().equals(userId)) {
                    user = new User();
                    user.setUserId(userIdEl.getTextNormalize());
                    user.setUserName(userUserNameEl.getTextNormalize());
                    user.setFirstName(userFirstEl.getTextNormalize());
                    user.setLastName(userLastEl.getTextNormalize());
                    user.setMiddleName(userMiddleEl.getTextNormalize());
                    user.setEmailAddress(userEmailEl.getTextNormalize());
                    user.setOpenId(userOpenIDEl.getTextNormalize());
                    user.setOrganization(userOrganizationEl.getTextNormalize());
                    user.setCity(userCityEl.getTextNormalize());
                    user.setCountry(userCountryEl.getTextNormalize());
                    user.setDN(userDNEl.getTextNormalize());
                    user.setOrganization(userIdEl.getTextNormalize());
                    user.setState(userStateEl.getTextNormalize());
                    
                }
            }
            
            
        }catch(Exception e) {
            e.printStackTrace();
            LOG.debug("Problem in getUserObjectFromUserName");
            
        }
        
        
        
        return user;
    }
    
    /*
    public void deleteUser(String userId) {

        System.out.println("Deleted user " + userId);
        
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        
        try{
            Document document = (Document) builder.build(USERS_FILE);
            
            Element rootNode = document.getRootElement();
            List users = (List)rootNode.getChildren();
            for(int i=0;i<users.size();i++)
            {
                Element userEl = (Element)users.get(i);
                Element userIdEl = userEl.getChild("id");
                if(userIdEl.getTextNormalize().equals(userId)) {
                    rootNode.removeContent(userEl);
                }
                
            }
            
            XMLOutputter outputter = new XMLOutputter();
            xmlContent = outputter.outputString(rootNode);
            
            Writer output = null;
            output = new BufferedWriter(new FileWriter(USERS_FILE));
            output.write(xmlContent);
            output.close();
            
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println("Error in user delete");    
            
        }
        
        //note that a user deletion should be cascaded throughout the store
    
    }
    
    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<User>();
        
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        try{
            Document groups_document = (Document) builder.build(USERS_FILE);
            Element rootNode = groups_document.getRootElement();
            List users = (List)rootNode.getChildren();
            for(int i=0;i<users.size();i++)
            {
                Element userEl = (Element)users.get(i);
                Element userIdEl = userEl.getChild("id");
                Element userFirstEl = userEl.getChild("first");
                Element userMiddleEl = userEl.getChild("middle");
                Element userLastEl = userEl.getChild("last");
                Element userEmailEl = userEl.getChild("email");
                Element userUserNameEl = userEl.getChild("username");
                Element userDNEl = userEl.getChild("dn");
                Element userOpenIDEl = userEl.getChild("openid");
                Element userOrganizationEl = userEl.getChild("organization");
                Element userCityEl = userEl.getChild("city");
                Element userStateEl = userEl.getChild("state");
                Element userCountryEl = userEl.getChild("country");
                
                //if(userUserNameEl.getTextNormalize().equals(userName)) {
                User user = new User();
                user.setUserId(userIdEl.getTextNormalize());
                user.setUserName(userUserNameEl.getTextNormalize());
                user.setFirstName(userFirstEl.getTextNormalize());
                user.setLastName(userLastEl.getTextNormalize());
                user.setMiddleName(userMiddleEl.getTextNormalize());
                user.setEmailAddress(userEmailEl.getTextNormalize());
                user.setOpenId(userOpenIDEl.getTextNormalize());
                user.setCity(userCityEl.getTextNormalize());
                user.setCountry(userCountryEl.getTextNormalize());
                user.setDN(userDNEl.getTextNormalize());
                user.setOrganization(userIdEl.getTextNormalize());
                user.setState(userStateEl.getTextNormalize());
                    
                usersList.add(user);
            }
            
            
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println("Problem in getAllUsers");
            
        }
        
        
        
        return usersList;
    }
    
    
    
    public void editUser(final String userId,
            String first,
            String middle,
            String last,
            String email,
            String username,
            String organization,
            String city,
            String state,
            String country) {

        System.out.println("Edited user " + userId);
        
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        try{
            Document document = (Document) builder.build(USERS_FILE);

            Element rootNode = document.getRootElement();
            Element newElement_users = new Element("users");

            List users = (List)rootNode.getChildren();
            for(int i=0;i<users.size();i++)
            {
                Element userEl = (Element)users.get(i);
                Element newElement_user = new Element("user");

                Element userIdEl = userEl.getChild("id");
                Element userFirstEl = userEl.getChild("first");
                Element userMiddleEl = userEl.getChild("middle");
                Element userLastEl = userEl.getChild("last");
                Element userEmailEl = userEl.getChild("email");
                Element userUserNameEl = userEl.getChild("username");
                Element userDNEl = userEl.getChild("dn");
                Element userOpenIDEl = userEl.getChild("openid");
                Element userOrganizationEl = userEl.getChild("organization");
                Element userCityEl = userEl.getChild("city");
                Element userStateEl = userEl.getChild("state");
                Element userCountryEl = userEl.getChild("country");
                
                String userIdText = userIdEl.getTextNormalize();
                String userFirstText = userFirstEl.getTextNormalize();
                String userMiddleText = userMiddleEl.getTextNormalize();
                String userLastText = userLastEl.getTextNormalize();
                String userEmailText = userEmailEl.getTextNormalize();
                String userUserNameText = userUserNameEl.getTextNormalize();
                String userDNText = userDNEl.getTextNormalize();
                String userOpenIDText = userOpenIDEl.getTextNormalize();
                String userOrganizationText = userOrganizationEl.getTextNormalize();
                String userCityText = userCityEl.getTextNormalize();
                String userStateText = userStateEl.getTextNormalize();
                String userCountryText = userCountryEl.getTextNormalize();
                
                
                if(userIdEl.getTextNormalize().equals(userId)) {
                    userIdText = userId;
                    userFirstText =first;
                    userMiddleText = middle;
                    userLastText = last;
                    userEmailText = email;
                    //userUserNameText = username;
                    //userDNText = userDNText;
                    //userOpenIDText = userOpenIDText;
                    userOrganizationText = organization;
                    userCityText = city;
                    userStateText = state;
                    userCountryText = country;
                }
                
                Element newElement_userIdEl = new Element("id");
                Element newElement_userFirstEl = new Element("first");
                Element newElement_userMiddleEl = new Element("middle");
                Element newElement_userLastEl = new Element("last");
                Element newElement_userEmailEl = new Element("email");
                Element newElement_userUserNameEl = new Element("username");
                Element newElement_userDNEl = new Element("dn");
                Element newElement_userOpenIDEl = new Element("openid");
                Element newElement_userOrganizationEl = new Element("organization");
                Element newElement_userCityEl = new Element("city");
                Element newElement_userStateEl = new Element("state");
                Element newElement_userCountryEl = new Element("country");

                newElement_userIdEl.setText(userIdText);
                newElement_userFirstEl.setText(userFirstText);
                newElement_userMiddleEl.setText(userMiddleText);
                newElement_userLastEl.setText(userLastText);
                newElement_userEmailEl.setText(userEmailText);
                newElement_userUserNameEl.setText(userUserNameText);
                newElement_userDNEl.setText(userDNText);
                newElement_userOpenIDEl.setText(userOpenIDText);
                newElement_userOrganizationEl.setText(userOrganizationText);
                newElement_userCityEl.setText(userCityText);
                newElement_userStateEl.setText(userStateText);
                newElement_userCountryEl.setText(userCountryText);

                newElement_user.addContent((Element)newElement_userIdEl);
                newElement_user.addContent((Element)newElement_userFirstEl);
                newElement_user.addContent((Element)newElement_userMiddleEl);
                newElement_user.addContent((Element)newElement_userLastEl);
                newElement_user.addContent((Element)newElement_userEmailEl);
                newElement_user.addContent((Element)newElement_userUserNameEl);
                newElement_user.addContent((Element)newElement_userDNEl);
                newElement_user.addContent((Element)newElement_userOpenIDEl);
                newElement_user.addContent((Element)newElement_userOrganizationEl);
                newElement_user.addContent((Element)newElement_userCityEl);
                newElement_user.addContent((Element)newElement_userStateEl);
                newElement_user.addContent((Element)newElement_userCountryEl);
                
                newElement_users.addContent(newElement_user);
            }
            
            XMLOutputter outputter = new XMLOutputter();
            xmlContent = outputter.outputString(newElement_users);
            
            Writer output = null;
            output = new BufferedWriter(new FileWriter(USERS_FILE));
            output.write(xmlContent);
            output.close();
            
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println("Error in user edit");
            
        }
        
        
        
        
    }
    
    public void addUser( String first,
                                String middle,
                                String last,
                                String email,
                                String username,
                                String organization,
                                String city,
                                String state,
                                String country) {
        String userId = createUserId();
        String openId = createOpenId();
        String userName = createUserName();
        String dn = createDN();
        
        
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        
        try{
            Document document = (Document) builder.build(USERS_FILE);
            Element rootNode = document.getRootElement();
            Element userElement = new Element("user");
            
            Element userIdEl = new Element("id");
            userIdEl.addContent(userId);
            Element userfirstEl = new Element("first");
            userfirstEl.addContent(first);
            Element userlastEl = new Element("last");
            userlastEl.addContent(last);
            Element usermiddleEl = new Element("middle");
            usermiddleEl.addContent(middle);
            Element userNameEl = new Element("username");
            userNameEl.addContent(userName);
            Element emailEl = new Element("email");
            emailEl.addContent(email);
            Element organizationEl = new Element("organization");
            organizationEl.addContent(organization);
            Element cityEl = new Element("city");
            cityEl.addContent(city);
            Element stateEl = new Element("state");
            stateEl.addContent(state);
            Element countryEl = new Element("country");
            countryEl.addContent(country);
            Element openIdEl = new Element("openid");
            openIdEl.addContent(openId);
            Element dnEl = new Element("dn");
            dnEl.addContent(dn);

            userElement.addContent(userIdEl);
            userElement.addContent(userfirstEl);
            userElement.addContent(userlastEl);
            userElement.addContent(usermiddleEl);
            userElement.addContent(userNameEl);
            userElement.addContent(emailEl);
            userElement.addContent(organizationEl);
            userElement.addContent(cityEl);
            userElement.addContent(stateEl);
            userElement.addContent(countryEl);
            userElement.addContent(openIdEl);
            userElement.addContent(dnEl);

            rootNode.addContent(userElement);
            XMLOutputter outputter = new XMLOutputter();
            xmlContent = outputter.outputString(rootNode);
            
            Writer output = null;
            output = new BufferedWriter(new FileWriter(USERS_FILE));
            output.write(xmlContent);
            
            output.close();
        }catch(Exception e) {
            System.out.println("Error in addUser");
        }
        
        
    }
    
    public User getUserObjectFromUserId(String userId) {
        User user = null;

        SAXBuilder builder = new SAXBuilder();
        try{
            Document groups_document = (Document) builder.build(USERS_FILE);
            Element rootNode = groups_document.getRootElement();
            List users = (List)rootNode.getChildren();
            for(int i=0;i<users.size();i++)
            {
                Element userEl = (Element)users.get(i);
                Element userIdEl = userEl.getChild("id");
                Element userFirstEl = userEl.getChild("first");
                Element userMiddleEl = userEl.getChild("middle");
                Element userLastEl = userEl.getChild("last");
                Element userEmailEl = userEl.getChild("email");
                Element userUserNameEl = userEl.getChild("username");
                Element userDNEl = userEl.getChild("dn");
                Element userOpenIDEl = userEl.getChild("openid");
                Element userOrganizationEl = userEl.getChild("organization");
                Element userCityEl = userEl.getChild("city");
                Element userStateEl = userEl.getChild("state");
                Element userCountryEl = userEl.getChild("country");
                
                if(userIdEl.getTextNormalize().equals(userId)) {
                    user = new User();
                    user.setUserId(userIdEl.getTextNormalize());
                    user.setUserName(userUserNameEl.getTextNormalize());
                    user.setFirstName(userFirstEl.getTextNormalize());
                    user.setLastName(userLastEl.getTextNormalize());
                    user.setMiddleName(userMiddleEl.getTextNormalize());
                    user.setEmailAddress(userEmailEl.getTextNormalize());
                    user.setOpenId(userOpenIDEl.getTextNormalize());
                    user.setOrganization(userOrganizationEl.getTextNormalize());
                    user.setCity(userCityEl.getTextNormalize());
                    user.setCountry(userCountryEl.getTextNormalize());
                    user.setDN(userDNEl.getTextNormalize());
                    user.setOrganization(userIdEl.getTextNormalize());
                    user.setState(userStateEl.getTextNormalize());
                    
                }
            }
            
            
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println("Problem in getUserObjectFromUserName");
            
        }
        
        
        
        return user;
    }
    

    public User getUserObjectFromUserOpenID(final String openId) {
        User user = null;
        
        SAXBuilder builder = new SAXBuilder();
        try{
            Document groups_document = (Document) builder.build(USERS_FILE);
            Element rootNode = groups_document.getRootElement();
            List users = (List)rootNode.getChildren();
            for(int i=0;i<users.size();i++)
            {
                Element userEl = (Element)users.get(i);
                Element userIdEl = userEl.getChild("id");
                Element userFirstEl = userEl.getChild("first");
                Element userMiddleEl = userEl.getChild("middle");
                Element userLastEl = userEl.getChild("last");
                Element userEmailEl = userEl.getChild("email");
                Element userUserNameEl = userEl.getChild("username");
                Element userDNEl = userEl.getChild("dn");
                Element userOpenIDEl = userEl.getChild("openid");
                Element userOrganizationEl = userEl.getChild("organization");
                Element userCityEl = userEl.getChild("city");
                Element userStateEl = userEl.getChild("state");
                Element userCountryEl = userEl.getChild("country");
                
                if(userOpenIDEl.getTextNormalize().equals(openId)) {
                    user = new User();
                    user.setUserId(userIdEl.getTextNormalize());
                    user.setUserName(userUserNameEl.getTextNormalize());
                    user.setFirstName(userFirstEl.getTextNormalize());
                    user.setLastName(userLastEl.getTextNormalize());
                    user.setMiddleName(userMiddleEl.getTextNormalize());
                    user.setEmailAddress(userEmailEl.getTextNormalize());
                    user.setOpenId(userOpenIDEl.getTextNormalize());
                    user.setOrganization(userOrganizationEl.getTextNormalize());
                    user.setCity(userCityEl.getTextNormalize());
                    user.setCountry(userCountryEl.getTextNormalize());
                    user.setDN(userDNEl.getTextNormalize());
                    user.setOrganization(userIdEl.getTextNormalize());
                    user.setState(userStateEl.getTextNormalize());
                    
                }
            }
            
            
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println("Problem in getUserObjectFromUserName");
            
        }
        
        
        
        return user;
    }
    
    
    public User getUserObjectFromUserName(String userName) {
        User user = null;

        
        SAXBuilder builder = new SAXBuilder();
        try{
            Document groups_document = (Document) builder.build(USERS_FILE);
            Element rootNode = groups_document.getRootElement();
            List users = (List)rootNode.getChildren();
            for(int i=0;i<users.size();i++)
            {
                Element userEl = (Element)users.get(i);
                Element userIdEl = userEl.getChild("id");
                Element userFirstEl = userEl.getChild("first");
                Element userMiddleEl = userEl.getChild("middle");
                Element userLastEl = userEl.getChild("last");
                Element userEmailEl = userEl.getChild("email");
                Element userUserNameEl = userEl.getChild("username");
                Element userDNEl = userEl.getChild("dn");
                Element userOpenIDEl = userEl.getChild("openid");
                Element userOrganizationEl = userEl.getChild("organization");
                Element userCityEl = userEl.getChild("city");
                Element userStateEl = userEl.getChild("state");
                Element userCountryEl = userEl.getChild("country");
                
                if(userUserNameEl.getTextNormalize().equals(userName)) {
                    user = new User();
                    user.setUserId(userIdEl.getTextNormalize());
                    user.setUserName(userUserNameEl.getTextNormalize());
                    user.setFirstName(userFirstEl.getTextNormalize());
                    user.setLastName(userLastEl.getTextNormalize());
                    user.setMiddleName(userMiddleEl.getTextNormalize());
                    user.setEmailAddress(userEmailEl.getTextNormalize());
                    user.setOpenId(userOpenIDEl.getTextNormalize());
                    user.setOrganization(userOrganizationEl.getTextNormalize());
                    user.setCity(userCityEl.getTextNormalize());
                    user.setCountry(userCountryEl.getTextNormalize());
                    user.setDN(userDNEl.getTextNormalize());
                    user.setOrganization(userIdEl.getTextNormalize());
                    user.setState(userStateEl.getTextNormalize());
                    
                }
            }
            
            
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println("Problem in getUserObjectFromUserName");
            
        }
        
        
        
        return user;
    }
    
    public String getUserNameFromOpenID(String openId) {
        String userId = null;
        
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        try{
            Document users_document = (Document) builder.build(USERS_FILE);
            Element rootNode = users_document.getRootElement();
            List users = (List)rootNode.getChildren();
            for(int i=0;i<users.size();i++)
            {
                Element userEl = (Element)users.get(i);
                if(userEl.getChild("openid").getTextNormalize().equals(openId)){
                    userId = userEl.getChildText("username");
                }
            }
            
        }catch(Exception e) {
            e.printStackTrace();
            LOG.debug("Error in getUserNameFromOpenID");
        }
        
        return userId;
    }
    
    public String getUserIdFromUserName(String userName) {
        String userId = null;
        
        SAXBuilder builder = new SAXBuilder();
        try{
            Document users_document = (Document) builder.build(USERS_FILE);
            Element rootNode = users_document.getRootElement();
            List users = (List)rootNode.getChildren();
            for(int i=0;i<users.size();i++)
            {
                Element userEl = (Element)users.get(i);
                if(userEl.getChild("username").getTextNormalize().equals(userName)){
                    userId = userEl.getChildText("id");
                }
            }
            
        }catch(Exception e) {
            e.printStackTrace();
            LOG.debug("Error in getUserIdFromUserName");
        }
        
        return userId;
    }
    
    public String getUserIdFromOpenID(String openId) {
        String userId = null;
        
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        try{
            Document users_document = (Document) builder.build(USERS_FILE);
            Element rootNode = users_document.getRootElement();
            List users = (List)rootNode.getChildren();
            for(int i=0;i<users.size();i++)
            {
                Element userEl = (Element)users.get(i);
                if(userEl.getChild("openid").getTextNormalize().equals(openId)){
                    userId = userEl.getChildText("id");
                }
            }
            
        }catch(Exception e) {
            e.printStackTrace();
            LOG.debug("Error in getUserIdFromOpenID");
        }
        
        return userId;
    }
    
    
   
    
    
    
    
    

   
    
    
    
    
    public List<User> getUsersFromGroup(String groupName) {
        List<User> users = new ArrayList<User>();
        
        GroupOperationsInterface goi = new GroupOperationsXMLImpl();
        String groupId = goi.getGroupIdFromGroupName(groupName);
        
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        try{
            Document permissions_document = (Document) builder.build(PERMISSIONS_FILE);
            
            Element rootNode = permissions_document.getRootElement();
            List permissions = (List)rootNode.getChildren();
            
            for(int i=0;i<permissions.size();i++)
            {
                Element tupleEl = (Element)permissions.get(i);
                Element userIdEl = tupleEl.getChild("userid");
                Element groupIdEl = tupleEl.getChild("groupid");
                if(groupIdEl.getTextNormalize().equals(groupId)) {
                    String userId = userIdEl.getTextNormalize();
                    User user = getUserObjectFromUserId(userId);
                    users.add(user);
                }
            }
            
        }catch(Exception e) {
            System.out.println("Error in getUsersFromGroup");
        }
        
        return users;
    }
    
   
    
    public static void main(String [] args) {
        
        
        //UserOperationsInterface uoi = new UserOperationsXMLImpl();
        UserOperationsXMLImpl uoi = new UserOperationsXMLImpl();
        String groupId = "group1_id";
        
        List<User> users = uoi.getUsersFromGroup(groupId);
        System.out.println(users);
        
        
        String openId = "https://pcmdi3.llnl.gov/esgcet/myopenid/jfharney";
        
        String userId = uoi.getUserIdFromOpenID(openId);
        String userName = uoi.getUserNameFromOpenID(openId);
        System.out.println(userId);
        System.out.println(userName);
        
        String first = "first1";
        String middle = "middle1";
        String last = "last1";
        String email = "email1";
        String username = "username1";
        String organization = "organization1";
        String city = "city1";
        String state = "state1";
        String country = "country1";
        
        
        uoi.editUser("user1_id",
                first,
                middle,
                last,
                email,
                username,
                organization,
                city,
                state,
                country);
        
        uoi.deleteUser("user1_id");
        
        
        User user = uoi.getUserObjectFromUserName(userName);
        
        List<User> users = uoi.getAllUsers();
        System.out.println(users);
        
        
        
    }



    */
}
