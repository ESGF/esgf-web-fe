package org.esgf.commonui;

import java.io.File;
import java.util.List;

import org.esgf.adminui.Role;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.core.io.ClassPathResource;

public class RoleOperationsXMLImpl implements RoleOperationsInterface {

    private static String xmlDir = "C:\\Users\\8xo\\esgProjects\\esgf-6-29\\esgf-web-fe\\esgf-web-fe-8-16\\esgf-web-fe\\src\\java\\main\\";
    
    private final static String users_file = xmlDir + "db.users";
    private final static String groups_file = xmlDir + "db.groups";
    private final static String roles_file = xmlDir + "db.roles";
    private final static String permissions_file = xmlDir + "db.permissions";
    
    
    private File USERS_FILE;
    private File GROUPS_FILE;
    private File ROLES_FILE;
    private File PERMISSIONS_FILE;
    
    public RoleOperationsXMLImpl(){
        USERS_FILE = new File("");
        GROUPS_FILE = new File("");
        ROLES_FILE = new File("");
        PERMISSIONS_FILE = new File("");
        
        try {
            File dir1 = new File(".");
            System.out.println("Current dir->" + dir1.getCanonicalPath());
            USERS_FILE = new File(users_file); //new ClassPathResource("db.users").getFile();
            GROUPS_FILE = new File(groups_file); //new ClassPathResource("db.groups").getFile();
            PERMISSIONS_FILE = new File(permissions_file);//new ClassPathResource("db.permissions").getFile();
            ROLES_FILE = new File(roles_file);
        }catch(Exception e) {
            System.out.println("error in RoleOperationsXMLImpl constructor");
        }
    }
    
    
    
    @Override
    public Role getRoleForUserInGroup(String userId, String groupId) {
        Role role = null;
        
        
        SAXBuilder builder = new SAXBuilder();
        
        try{
            Document permissions_document = (Document) builder.build(PERMISSIONS_FILE);
            Element permissions_rootNode = permissions_document.getRootElement();
        
            List tuples = (List)permissions_rootNode.getChildren();
            for(int i=0;i<tuples.size();i++)
            {
                Element tupleEl = (Element)tuples.get(i);
                Element userIdEl = tupleEl.getChild("userid");
                Element groupIdEl = tupleEl.getChild("groupid");
                Element roleIdEl = tupleEl.getChild("roleid");
                if(userIdEl.getTextNormalize().equals(userId) && userIdEl.getTextNormalize().equals(userId)) {
                    String roleId = roleIdEl.getTextNormalize();
                    role = getRoleObjectFromRoleId(roleId);
                }
            }
        
        
        } catch(Exception e) {
            System.out.println("Error in getRoleForUserInGroup");
        }
        
        
        return role;
    }


    public String getRoleIdFromUserName(String roleName) {
        String roleId = null;
        
        SAXBuilder builder = new SAXBuilder();
        try{
            Document roles_document = (Document) builder.build(ROLES_FILE);
            Element rootNode = roles_document.getRootElement();
            List roles = (List)rootNode.getChildren();
            for(int i=0;i<roles.size();i++)
            {
                Element roleEl = (Element)roles.get(i);
                if(roleEl.getChild("rolename").getTextNormalize().equals(roleName)){
                    roleId = roleEl.getChildText("id");
                }
            }
            
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println("Error in getRoleIdFromRoleName");
        }
        
        return roleId;
    }
    
    @Override
    public Role getRoleObjectFromRoleId(String roleId) {
        
        Role role = new Role();
        
        
        SAXBuilder builder = new SAXBuilder();
        
        try{
            Document roles_document = (Document) builder.build(ROLES_FILE);
            Element roles_rootNode = roles_document.getRootElement();
            List roles = (List)roles_rootNode.getChildren();
            for(int i=0;i<roles.size();i++)
            {
                Element roleEl = (Element)roles.get(i);
                Element roleIdEl = roleEl.getChild("roleid");
                Element roleNameEl = roleEl.getChild("rolename");
                Element roleDescriptionEl = roleEl.getChild("roledescription");
                if(roleIdEl.getTextNormalize().equals(roleId)) {
                    role.setRoleId(roleIdEl.getTextNormalize());
                    role.setRoleName(roleNameEl.getTextNormalize());
                    role.setRoleDescription(roleDescriptionEl.getTextNormalize());
                }
            }
            
            
        } catch(Exception e) {
            System.out.println("Error in getRoleObjectFromRoleId");
        }
            
        return role;
    }
    
    public static void main(String [] args) {
        RoleOperationsInterface roi = new RoleOperationsXMLImpl();

        String groupId = "group1_id";
        String userId = "user1_id";
        
        Role role = roi.getRoleForUserInGroup(userId, groupId);
        System.out.println(role);
    }

}
