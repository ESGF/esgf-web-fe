package org.esgf.commonui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.esgf.adminui.Group;
import org.esgf.adminui.User;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.springframework.core.io.ClassPathResource;


public class GroupOperationsXMLImpl implements GroupOperationsInterface {

    //private final static String GROUPS_FILE = "C:\\Users\\8xo\\esgProjects\\esgf-6-29\\esgf-web-fe\\esgf-web-fe\\src\\java\\main\\db.groups";
    
    //private final static String PERMISSIONS_FILE = "C:\\Users\\8xo\\esgProjects\\esgf-6-29\\esgf-web-fe\\esgf-web-fe\\src\\java\\main\\db.permissions";

    private File GROUPS_FILE;
    private File PERMISSIONS_FILE;
    
    public GroupOperationsXMLImpl(){
        GROUPS_FILE = new File("");
        try {
            GROUPS_FILE = new ClassPathResource("db.groups").getFile();
        }catch(Exception e) {
            System.out.println("error in db.users");
        }
        
        PERMISSIONS_FILE = new File("");
        try {
            PERMISSIONS_FILE = new ClassPathResource("db.permissions").getFile();
        }catch(Exception e) {
            System.out.println("error in db.permissions");
        }
    }
    
    public List<String> getGroupIdsForUserId(String userId) {
        
        System.out.println("Returning all groups for user " + userId);
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
        System.out.println("Returning all groups for user " + userId);
        
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
    
    
    /* delete group */
    public void deleteGroup(String groupId) {
        
        System.out.println("Deleted group " + groupId);
        
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
    
    /* edit group 
     * 
     * basically copies all the elements with the exception of the element changed
     * 
     * */
    public void editGroup(final String groupId, String groupName, String groupDescription) {
        
        System.out.println("Edited group " + groupId);

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

                Element newElement_groupIdEl = new Element("groupid");
                Element newElement_groupNameEl = new Element("groupname");
                Element newElement_groupDescriptionEl = new Element("groupdescription");

                newElement_groupIdEl.setText(groupIdText);
                newElement_groupNameEl.setText(groupNameText);
                newElement_groupDescriptionEl.setText(groupDescriptionText);

                newElement_group.addContent((Element)newElement_groupIdEl);
                newElement_group.addContent((Element)newElement_groupNameEl);
                newElement_group.addContent((Element)newElement_groupDescriptionEl);

                System.out.println(groupIdText);
                newElement_groups.addContent(newElement_group);
            }
            
            XMLOutputter outputter = new XMLOutputter();
            xmlContent = outputter.outputString(newElement_groups);
            
            Writer output = null;
            output = new BufferedWriter(new FileWriter(GROUPS_FILE));
            System.out.println("WRITING..." + xmlContent);
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
    
    
    /*
    public Element getAllGroups() {
        System.out.println("Returning all groups ");
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        Element returnedEl = null;
        
        try{
            Document document = (Document) builder.build(GROUPS_FILE);
            Element rootNode = document.getRootElement();
            returnedEl = rootNode;
        }catch(Exception e) {
            System.out.println("Error in getAllGroups");
        }
        
        return returnedEl;
    }
    */
    
    private String createGroupId() {
        Random rand = new Random();
        
        int num = rand.nextInt();
        while(idExists(num)) {
            num = rand.nextInt();
        }
        String str = "group" + num + "_id";
        return str;
    }
    
    private boolean idExists(int id) {
        boolean idExists = false;
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        File file = GROUPS_FILE;
        
        try{

            Document document = (Document) builder.build(file);
            
            Element rootNode = document.getRootElement();
        
            List groups = (List)rootNode.getChildren();
            String intStr = Integer.toString(id);
            for(int i=0;i<groups.size();i++)
            {
                Element groupEl = (Element)groups.get(i);
                Element groupIdEl = groupEl.getChild("groupid");
                String groupId = groupIdEl.getTextNormalize();
                if(groupId.contains(intStr)) {
                    idExists = true;
                    System.out.println("Id exists");
                }
            }
        
        
        }catch(Exception e) {
            System.out.println("Problem in idExists");
            
        }
        return idExists;
    }
    
    

    /* addGroup */
    public void addGroup(String groupName,String description) {
        String groupId = createGroupId();
        System.out.println("Added group " + groupId + " " + groupName + " " + description);
        
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        
        
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
            
        }catch(Exception e) {
            System.out.println("Error in group add");
        }
        
    }
    
    
    
    /* */
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
    
    
    
    
    
    
    
    
    
    public static void main(String [] args) {
        String groupId = "group1_id";
        String userId = "user2_id";
        //addGroup("gN","gD");
        //deleteGroup("group1_id");
        //editGroup("group2_id","gn","gd");
        /*
        Element group = getGroupInfoFromGroupId(groupId);
        if(group != null) {
            System.out.println(group.getChild("groupdescription").getTextNormalize());
        }
        */
        //String groupName = getGroupNameFromGroupId(groupId);
        //groupId = getGroupIdFromGroupName(groupName);
        
        //Element groups = getGroupsForUserId(userId);
        //System.out.println(groups.getName() + " " + groups.getChildren("group"));
        
        //List<String> groups = getGroupIdsForUserId(userId);
        //System.out.println(groups);
        
        GroupOperationsInterface goi = new GroupOperationsXMLImpl();
        //List<Group> groups = goi.getAllGroups();
        
    }
}


