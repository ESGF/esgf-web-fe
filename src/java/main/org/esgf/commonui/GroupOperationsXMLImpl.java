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

    private static String xmlDir = "C:\\Users\\8xo\\esgProjects\\esgf-6-29\\esgf-web-fe\\esgf-web-fe-8-16\\esgf-web-fe\\src\\java\\main\\";
    
    private final static String users_file = xmlDir + "db.users";
    private final static String groups_file = xmlDir + "db.groups";
    private final static String roles_file = xmlDir + "db.roles";
    private final static String permissions_file = xmlDir + "db.permissions";
   
    
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















/* Keep until full commit is ready */
/*
public List<String> getGroupIdsForUserId(String userId) {
    
    ArrayList<String> returnedGroupsList = new ArrayList<String>();
    SAXBuilder builder = new SAXBuilder();
    String xmlContent = "";
    Element returnedGroupsEl = new Element("groups");
    try{
        Document permissions_document = (Document) builder.build(PERMISSIONS_FILE);
        Element permissions_rootNode = permissions_document.getRootElement();
        List tuples = (List)permissions_rootNode.getChildren();
        for(int i=0;i<tuples.size();i++)
        {
            Element tupleEl = (Element)tuples.get(i);
            Element userIdEl = tupleEl.getChild("userid");
            if(userIdEl.getTextNormalize().equals(userId)) {
                //get the groupid
                String groupId = tupleEl.getChild("groupid").getTextNormalize();
                
                //get the group matching that id and attach it to the returnedGroups Element
                Element returnedGroupEl = (Element)getGroupInfoFromGroupId(groupId);

                String returnedGroupId = returnedGroupEl.getChild("groupid").getTextNormalize();
                returnedGroupsList.add(returnedGroupId);
            }
        }
    }catch(Exception e) {
        e.printStackTrace();
        System.out.println("Error in getGroupsForUser");
    }
    return returnedGroupsList;
}

public Element getGroupsForUserId(String userId) {
    
    SAXBuilder builder = new SAXBuilder();
    String xmlContent = "";
    
    Element returnedGroupsEl = new Element("groups");
    try{
        Document permissions_document = (Document) builder.build(PERMISSIONS_FILE);
        Element permissions_rootNode = permissions_document.getRootElement();
        List tuples = (List)permissions_rootNode.getChildren();
        for(int i=0;i<tuples.size();i++)
        {
            Element tupleEl = (Element)tuples.get(i);
            Element userIdEl = tupleEl.getChild("userid");
            
            if(userIdEl.getTextNormalize().equals(userId)) {
                //get the groupid
                String groupId = tupleEl.getChild("groupid").getTextNormalize();
                
                //get the group matching that id and attach it to the returnedGroups Element
                Element returnedGroupEl = (Element)getGroupInfoFromGroupId(groupId);

                returnedGroupsEl.addContent(returnedGroupEl);
                
            }
        }
    }catch(Exception e) {
        e.printStackTrace();
        System.out.println("Error in getGroupsForUser");
    }
    
    return returnedGroupsEl;
}



public void deleteGroup(String groupId) {
    
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
            if(groupIdEl.getTextNormalize().equals(groupId)) {
                rootNode.removeContent(groupEl);
            }
        }

        XMLOutputter outputter = new XMLOutputter();
        xmlContent = outputter.outputString(rootNode);
        
        Writer output = null;
        output = new BufferedWriter(new FileWriter(GROUPS_FILE));
        output.write(xmlContent);
        output.close();
        
    } catch(Exception e) {
        System.out.println("Error in group delete");    
    }
    
    
    //note that a group deletion should be cascaded throughout the store
}


public void editGroup(final String groupId, String groupName, String groupDescription) {
    
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
                groupNameText = groupName;
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
        
        XMLOutputter outputter = new XMLOutputter();
        xmlContent = outputter.outputString(newElement_groups);
        
        Writer output = null;
        output = new BufferedWriter(new FileWriter(GROUPS_FILE));
        output.write(xmlContent);
        output.close();
    }catch(Exception e) {
        e.printStackTrace();
        System.out.println("Error in group edit");
        
    }
    
    
    
}

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
        System.out.println("Problem in getAllGroups");
        e.printStackTrace();
    }
    
    
    return groupsList;
   
    
}









public void addGroup(String groupName,String description) {
    String groupId = createGroupId();
    
    SAXBuilder builder = new SAXBuilder();
    String xmlContent = "";
    

    GroupOperationsInterface goi = new GroupOperationsXMLImpl();
    UserOperationsInterface uoi = new UserOperationsXMLImpl();
    
    try{

        Document document = (Document) builder.build(GROUPS_FILE);
        
        Element rootNode = document.getRootElement();
        
        Element groupElement = new Element("group");
        
        Element groupIdEl = new Element("groupid");
        groupIdEl.addContent(groupId);
        Element groupNameEl = new Element("groupname");
        groupNameEl.addContent(groupName);
        Element groupdescriptionEl = new Element("groupdescription");
        groupdescriptionEl.addContent(description);
        
        groupElement.addContent(groupIdEl);
        groupElement.addContent(groupNameEl);
        groupElement.addContent(groupdescriptionEl);
        
        rootNode.addContent(groupElement);
        

        document.setContent(rootNode);

        XMLOutputter outputter = new XMLOutputter();
        xmlContent = outputter.outputString(rootNode);
        
        Writer output = null;
        output = new BufferedWriter(new FileWriter(GROUPS_FILE));
        output.write(xmlContent);
        
        output.close();
        
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

        outputter = new XMLOutputter();
        xmlContent = outputter.outputString(rootNode);
        System.out.println(xmlContent);
        output = null;
        output = new BufferedWriter(new FileWriter(PERMISSIONS_FILE));
        output.write(xmlContent);
        
        output.close();
        
        
        
        
        
        
    }catch(Exception e) {
        System.out.println("Error in group add");
    }
    
}



public Group getGroupObjectFromGroupId(String groupId) {
    
    Group group = new Group();
    
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
            
            if(groupIdEl.getTextNormalize().equals(groupId)) {
                group.setid(groupIdEl.getTextNormalize());
                group.setname(groupNameEl.getTextNormalize());
                group.setdescription(groupDescriptionEl.getTextNormalize());
            }
        }
        
        
        
    }catch(Exception e) {
        System.out.println("Problem in getGroupObjectFromGroupId");
        
    }
    
    return group;
}




public Element getGroupInfoFromGroupId(String groupId) {
    Element returnedEl = new Element("group");
    
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
            
            if(groupIdEl.getTextNormalize().contains(groupId)) {
                Element returnedGroupIdEl = new Element("groupid");
                returnedGroupIdEl.setText(groupIdEl.getTextNormalize());
                Element returnedGroupNameEl = new Element("groupname");
                returnedGroupNameEl.setText(groupNameEl.getTextNormalize());
                Element returnedGroupDescriptionEl = new Element("groupdescription");
                returnedGroupDescriptionEl.setText(groupDescriptionEl.getTextNormalize());
                
                returnedEl.addContent(returnedGroupIdEl);
                returnedEl.addContent(returnedGroupNameEl);
                returnedEl.addContent(returnedGroupDescriptionEl);
                
            }
        }
        
    }catch(Exception e) {
        System.out.println("Problem in getGroupInfoFromGroupId");
    }
    return returnedEl;
}



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
        System.out.println("Error in getGroupNameFromGroupId");
    }
    return groupId;
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
        System.out.println("Error in getGroupNameFromGroupId");
    }
    return groupName;
}


public List<Group> getGroupsFromUser(String userId) {
    List<Group> groups = new ArrayList<Group>();
    
    
    SAXBuilder builder = new SAXBuilder();
    String xmlContent = "";
    try{
        Document permissions_document = (Document) builder.build(PERMISSIONS_FILE);
        
        Element rootNode = permissions_document.getRootElement();
        List permissions = (List)rootNode.getChildren();
        
        for(int i=0;i<permissions.size();i++) {
            Element tupleEl = (Element)permissions.get(i);
            Element userIdEl = tupleEl.getChild("userid");
            Element groupIdEl = tupleEl.getChild("groupid");
            if(userIdEl.getTextNormalize().equals(userId)) {
                String groupId = groupIdEl.getTextNormalize();
                Group group = this.getGroupObjectFromGroupId(groupId);
                groups.add(group);
            }
            
        }
        
    }catch(Exception e) {
        System.out.println("Error in getGroupsFromUser");
        e.printStackTrace();
    }
    
    return groups;
}







public static void main(String [] args) {
    String groupId = "group1_id";
    String userId = "user2_id";
    //addGroup("gN","gD");
    //deleteGroup("group1_id");
    //editGroup("group2_id","gn","gd");
    
    //String groupName = getGroupNameFromGroupId(groupId);
    //groupId = getGroupIdFromGroupName(groupName);
    
    //Element groups = getGroupsForUserId(userId);
    //System.out.println(groups.getName() + " " + groups.getChildren("group"));
    
    //List<String> groups = getGroupIdsForUserId(userId);
    //System.out.println(groups);
    
    GroupOperationsInterface goi = new GroupOperationsXMLImpl();
    //List<Group> groups = goi.getAllGroups();
    
    UserOperationsXMLImpl uoi = new UserOperationsXMLImpl();
    List<Group> groups = goi.getGroupsFromUser("user2_id");
    
    System.out.println(groups);
    
}



@Override
public Group getGroupObjectFromGroupName(String groupName) {
    Group group = new Group();
    
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
            System.out.println("\t\t" + groupNameEl.getTextNormalize() + " " + groupName);
            if(groupNameEl.getTextNormalize().equals(groupName)) {
                System.out.println("MATCH");
                group.setid(groupIdEl.getTextNormalize());
                group.setname(groupNameEl.getTextNormalize());
                group.setdescription(groupDescriptionEl.getTextNormalize());
            }
        }
        
        
        
    }catch(Exception e) {
        System.out.println("Problem in getGroupObjectFromGroupId");
        
    }
    
    return group;
}



@Override
public void editUsersInGroup(String userName, String groupName) {
    
    
    System.out.println("\n\n\n\n");

    
    SAXBuilder builder = new SAXBuilder();
    String xmlContent = "";
    try{
        
        
        System.out.println("\tAdding User: " + userName + " to group: " + groupName);
        
        Document document = (Document) builder.build(PERMISSIONS_FILE);
       
        Element rootNode = document.getRootElement();
        List tuples = (List)rootNode.getChildren();
        

        boolean exists = false;

        GroupOperationsInterface goi = new GroupOperationsXMLImpl();
        UserOperationsInterface uoi = new UserOperationsXMLImpl();
        
        for(int i=0;i<tuples.size();i++)
        {
            
            
            
            Element tupleEl = (Element)tuples.get(i);
            Element groupIdEl = tupleEl.getChild("groupid");
            Element userIdEl = tupleEl.getChild("userid");
            Element roleIdEl = tupleEl.getChild("roleid");
            
            String groupId = goi.getGroupIdFromGroupName(groupName);
            String userId = uoi.getUserIdFromUserName(userName);
            //System.out.println("\t\t" + groupId + " " + groupIdEl.getTextNormalize());
            if(groupIdEl.getTextNormalize().equals(groupId) &&
               userIdEl.getTextNormalize().equals(userId)     ) {
                exists = true;
            }
            
            
        }
        
        System.out.println(exists);
        if(!exists) {
            Element returnedEl = new Element("tuple");
            Element returnedGroupEl = new Element("groupid");
            Element returnedUserEl = new Element("userid");
            Element returnedRoleEl = new Element("roleid");

            returnedGroupEl.addContent(goi.getGroupIdFromGroupName(groupName));
            returnedUserEl.addContent(uoi.getUserIdFromUserName(userName));
            returnedRoleEl.addContent("role4_id");
            returnedEl.addContent(returnedGroupEl);
            returnedEl.addContent(returnedUserEl);
            returnedEl.addContent(returnedRoleEl);
            
            rootNode.addContent(returnedEl);
            
            document.setContent(rootNode);

            
            XMLOutputter outputter = new XMLOutputter();
            xmlContent = outputter.outputString(rootNode);
            System.out.println(xmlContent);
            Writer output = null;
            output = new BufferedWriter(new FileWriter(PERMISSIONS_FILE));
            output.write(xmlContent);
            
            
            output.close();
            
        }
        
        
    } catch(Exception e) {
        System.out.println("Error in add user to group");
    }
    
    
    System.out.println("\n\n\n\n");
    // TODO Auto-generated method stub
    
}

public void deleteUserFromGroup(String userName, String groupName) {

    System.out.println("\n\n\n\n");

    
    SAXBuilder builder = new SAXBuilder();
    String xmlContent = "";
    try{
        
        
        System.out.println("\tAdding User: " + userName + " to group: " + groupName);
        
        Document document = (Document) builder.build(PERMISSIONS_FILE);
       
        Element rootNode = document.getRootElement();
        List tuples = (List)rootNode.getChildren();
        

        boolean exists = false;

        GroupOperationsInterface goi = new GroupOperationsXMLImpl();
        UserOperationsInterface uoi = new UserOperationsXMLImpl();
        
        for(int i=0;i<tuples.size();i++)
        {
            
            
            
            Element tupleEl = (Element)tuples.get(i);
            Element groupIdEl = tupleEl.getChild("groupid");
            Element userIdEl = tupleEl.getChild("userid");
            Element roleIdEl = tupleEl.getChild("roleid");
            
            String groupId = goi.getGroupIdFromGroupName(groupName);
            String userId = uoi.getUserIdFromUserName(userName);
            //System.out.println("\t\t" + groupId + " " + groupIdEl.getTextNormalize());
            if(groupIdEl.getTextNormalize().equals(groupId) &&
               userIdEl.getTextNormalize().equals(userId)     ) {
                exists = true;
            } else {
                rootNode.removeContent(tupleEl);
            }
            
            
            
        }
        
        
        
        if(!exists) {
            Element returnedEl = new Element("tuple");
            Element returnedGroupEl = new Element("groupid");
            Element returnedUserEl = new Element("userid");
            Element returnedRoleEl = new Element("roleid");

            returnedGroupEl.addContent(goi.getGroupIdFromGroupName(groupName));
            returnedUserEl.addContent(uoi.getUserIdFromUserName(userName));
            returnedRoleEl.addContent("role4_id");
            returnedEl.addContent(returnedGroupEl);
            returnedEl.addContent(returnedUserEl);
            returnedEl.addContent(returnedRoleEl);
            
            rootNode.addContent(returnedEl);
            
            document.setContent(rootNode);

            
            XMLOutputter outputter = new XMLOutputter();
            xmlContent = outputter.outputString(rootNode);
            
            Writer output = null;
            output = new BufferedWriter(new FileWriter(PERMISSIONS_FILE));
            output.write(xmlContent);
            
            
            output.close();
            
        }
        
        
    } catch(Exception e) {
        System.out.println("Error in add user to group");
    }
    
    
    System.out.println("\n\n\n\n");
    // TODO Auto-generated method stub
    
=======
>>>>>>> ORNL/adminui-noSpringSecurity
}


*/
