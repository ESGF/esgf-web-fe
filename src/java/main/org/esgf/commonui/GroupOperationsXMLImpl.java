package org.esgf.commonui;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.esgf.adminui.Group;
import org.esgf.adminui.User;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.springframework.core.io.ClassPathResource;


public class GroupOperationsXMLImpl implements GroupOperationsInterface {
    private final static Logger LOG = Logger.getLogger(GroupOperationsXMLImpl.class);

    
    private final static String users_file = "C:\\Users\\8xo\\esgProjects\\esgf-6-29\\esgf-web-fe\\esgf-web-fe\\src\\java\\main\\db.users";
    private final static String groups_file = "C:\\Users\\8xo\\esgProjects\\esgf-6-29\\esgf-web-fe\\esgf-web-fe\\src\\java\\main\\db.groups";
    private final static String roles_file = "C:\\Users\\8xo\\esgProjects\\esgf-6-29\\esgf-web-fe\\esgf-web-fe\\src\\java\\main\\db.roles";
    private final static String permissions_file = "C:\\Users\\8xo\\esgProjects\\esgf-6-29\\esgf-web-fe\\esgf-web-fe\\src\\java\\main\\db.permissions";
    //private final static String password_file = "/usr/local/.esg_pg_pass";
    
    
    private File USERS_FILE;
    private File GROUPS_FILE;
    private File ROLES_FILE;
    private File PERMISSIONS_FILE;
    private File PASSWORD_FILE;
    
    private String passwd;
    
    public GroupOperationsXMLImpl(){
        USERS_FILE = new File("");
        GROUPS_FILE = new File("");
        ROLES_FILE = new File("");
        PERMISSIONS_FILE = new File("");
        PASSWORD_FILE = new File("");
        try {
            File dir1 = new File("/usr/local");
            USERS_FILE = new File(users_file); //new ClassPathResource("db.users").getFile();
            GROUPS_FILE = new File(groups_file); //new ClassPathResource("db.groups").getFile();
            PERMISSIONS_FILE = new File(permissions_file);//new ClassPathResource("db.permissions").getFile();
            ROLES_FILE = new File(roles_file);
            
            //get the password
            //PASSWORD_FILE = new File("/usr/local/.esg_pg_pass");
            //this.passwd = Utils.getPassword(PASSWORD_FILE);
            this.passwd = "mattryan12!";
        }catch(Exception e) {
            LOG.debug("error in GroupOperationsXMLImpl constructor");
        }
    }

    /*
     * (non-Javadoc)
     * @see org.esgf.commonui.GroupOperationsInterface#addGroup(java.lang.String, java.lang.String)
     */
    @Override
    public void addGroup(String groupName, String groupDescription) {
        
        String groupId = Utils.createGroupId(GROUPS_FILE);
        
        SAXBuilder builder = new SAXBuilder();
        
        //use the two interfaces
        GroupOperationsInterface goi = new GroupOperationsXMLImpl();
        UserOperationsInterface uoi = new UserOperationsXMLImpl();
        
        try{
            //build the document from the group file store
            Document document = (Document) builder.build(GROUPS_FILE);
            Element rootNode = document.getRootElement();
            
            //create a fresh group element
            Element groupElement = new Element("group");
            //add groupId to the group element
            Element groupIdEl = new Element("groupid");
            groupIdEl.addContent(groupId);
            //add groupName to the group element
            Element groupNameEl = new Element("groupname");
            groupNameEl.addContent(groupName);
            //add groupDescription to the group element
            Element groupdescriptionEl = new Element("groupdescription");
            groupdescriptionEl.addContent(groupDescription);
            
            groupElement.addContent(groupIdEl);
            groupElement.addContent(groupNameEl);
            groupElement.addContent(groupdescriptionEl);
            
            //attach the group element to the document
            rootNode.addContent(groupElement);
            
            //write the document back to the groups file
            document.setContent(rootNode);

            Utils.writeXMLContentToFile(rootNode,GROUPS_FILE);
            
            document = (Document) builder.build(PERMISSIONS_FILE);
            
            rootNode = document.getRootElement();
            List tuples = (List)rootNode.getChildren();
            
            Element returnedEl = new Element("tuple");
            Element returnedGroupEl = new Element("groupid");
            Element returnedUserEl = new Element("userid");
            Element returnedRoleEl = new Element("roleid");

            returnedGroupEl.addContent(goi.getGroupIdFromGroupName(groupName));
            returnedUserEl.addContent("rootAdmin_id");
            returnedRoleEl.addContent("role1_id");
            returnedEl.addContent(returnedGroupEl);
            returnedEl.addContent(returnedUserEl);
            returnedEl.addContent(returnedRoleEl);
            
            rootNode.addContent(returnedEl);
            
            document.setContent(rootNode);

            Utils.writeXMLContentToFile(rootNode,PERMISSIONS_FILE);
            
        }catch(Exception e) {
            LOG.debug("Error in GroupOperationsXMLImpl addGroup");
            e.printStackTrace();
        }
        
    }
    
    
    @Override
    public void editGroup(String groupId, String newGroupName,
            String groupDescription) {
        
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        
        
        try{
            Document document = (Document) builder.build(GROUPS_FILE);

            Element rootNode = document.getRootElement();
            Element newElement_groups = new Element("groups");
            
            List groups = (List)rootNode.getChildren();
            
            for(int i=0;i<groups.size();i++)
            {
                Element groupEl = (Element)groups.get(i);
                Element newElement_group = new Element("group");
                
                Element groupIdEl = groupEl.getChild("groupid");
                Element groupNameEl = groupEl.getChild("groupname");
                Element groupDescriptionEl = groupEl.getChild("groupdescription");
                
                String groupIdText = groupIdEl.getTextNormalize();
                String groupNameText = groupNameEl.getTextNormalize();
                String groupDescriptionText = groupDescriptionEl.getTextNormalize();
                if(groupIdEl.getTextNormalize().equals(groupId)) {
                    groupIdText = groupId;
                    groupNameText = newGroupName;
                    groupDescriptionText = groupDescription;
                }

                //tuple
                Element newElement_groupIdEl = new Element("groupid");
                Element newElement_groupNameEl = new Element("groupname");
                Element newElement_groupDescriptionEl = new Element("groupdescription");

                newElement_groupIdEl.setText(groupIdText);
                newElement_groupNameEl.setText(groupNameText);
                newElement_groupDescriptionEl.setText(groupDescriptionText);

                newElement_group.addContent((Element)newElement_groupIdEl);
                newElement_group.addContent((Element)newElement_groupNameEl);
                newElement_group.addContent((Element)newElement_groupDescriptionEl);

                newElement_groups.addContent(newElement_group);
            }
            
            Utils.writeXMLContentToFile(newElement_groups, GROUPS_FILE);
            
        }catch(Exception e) {
            LOG.debug("Error in editGroup");
            e.printStackTrace();
            
        }
        
        
        
    }

    @Override
    public void deleteGroup(String groupName) {
        // TODO Auto-generated method stub
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        
        try{

            Document document = (Document) builder.build(GROUPS_FILE);
            
            Element rootNode = document.getRootElement();
            List groups = (List)rootNode.getChildren();
            for(int i=0;i<groups.size();i++)
            {
                Element groupEl = (Element)groups.get(i);
                Element groupIdEl = groupEl.getChild("groupid");
                Element groupNameEl = groupEl.getChild("groupname");
                if(groupIdEl.getTextNormalize().equals(groupName)) {
                    rootNode.removeContent(groupEl);
                }
            }

            Utils.writeXMLContentToFile(rootNode, GROUPS_FILE);
            
        } catch(Exception e) {
            LOG.debug("Error in deleteGroup");    
            e.printStackTrace();
        }
        
        
        //note that a group deletion should be cascaded throughout the store
    }

    @Override
    public String getGroupIdFromGroupName(String groupName) {
        
        String groupId = null;
        
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        try{
            Document groups_document = (Document) builder.build(GROUPS_FILE);
            
            Element rootNode = groups_document.getRootElement();
            List groups = (List)rootNode.getChildren();
            for(int i=0;i<groups.size();i++)
            {
                Element groupEl = (Element)groups.get(i);
                Element groupIdEl = groupEl.getChild("groupid");
                Element groupNameEl = groupEl.getChild("groupname");
                if(groupNameEl.getTextNormalize().contains(groupName)) {
                    groupId = groupIdEl.getTextNormalize();
                }
            }
            
            
        } catch(Exception e) {
            LOG.debug("Error in getGroupNameFromGroupId");
        }
        return groupId;
    }

    @Override
    public List<Group> getAllGroups() {
        
        List<Group> groupsList = new ArrayList<Group>();
        
        SAXBuilder builder = new SAXBuilder();
        try{
            Document groups_document = (Document) builder.build(GROUPS_FILE);
            
            Element rootNode = groups_document.getRootElement();
            List groups = (List)rootNode.getChildren();
            for(int i=0;i<groups.size();i++)
            {
                Element groupEl = (Element)groups.get(i);
                Element groupIdEl = groupEl.getChild("groupid");
                Element groupNameEl = groupEl.getChild("groupname");
                Element groupDescriptionEl = groupEl.getChild("groupdescription");

                Group group = new Group();
                group.setid(groupIdEl.getTextNormalize());
                group.setname(groupNameEl.getTextNormalize());
                group.setdescription(groupDescriptionEl.getTextNormalize());

                groupsList.add(group);
            }
        } catch(Exception e) {
            LOG.debug("Problem in getAllGroups");
            e.printStackTrace();
        }
        return groupsList;
       
    }

    @Override
    public Group getGroupObjectFromGroupName(String groupName) {
        
        Group group = null;
        
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        try{
            Document groups_document = (Document) builder.build(GROUPS_FILE);
            Element rootNode = groups_document.getRootElement();
            List groups = (List)rootNode.getChildren();
            for(int i=0;i<groups.size();i++)
            {
                Element groupEl = (Element)groups.get(i);
                Element groupIdEl = groupEl.getChild("groupid");
                Element groupNameEl = groupEl.getChild("groupname");
                Element groupDescriptionEl = groupEl.getChild("groupdescription");
                if(groupNameEl.getTextNormalize().equals(groupName)) {
                    group = new Group();
                    group.setid(groupIdEl.getTextNormalize());
                    group.setname(groupNameEl.getTextNormalize());
                    group.setdescription(groupDescriptionEl.getTextNormalize());
                }
            }
        }catch(Exception e) {
            LOG.debug("Problem in getGroupObjectFromGroupName");
            
        }
        return group;
    }
    
    
    public String getGroupNameFromGroupId(String groupId) {
        String groupName = null;

        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        
        try{
            Document groups_document = (Document) builder.build(GROUPS_FILE);

            Element rootNode = groups_document.getRootElement();
            List groups = (List)rootNode.getChildren();
            for(int i=0;i<groups.size();i++)
            {
                Element groupEl = (Element)groups.get(i);
                Element groupIdEl = groupEl.getChild("groupid");
                Element groupNameEl = groupEl.getChild("groupname");
                if(groupIdEl.getTextNormalize().contains(groupId)) {
                    groupName = groupNameEl.getTextNormalize();
                }
            }
        } catch(Exception e) {
            LOG.debug("Error in getGroupNameFromGroupId");
        }
        return groupName;
    }
    
}


