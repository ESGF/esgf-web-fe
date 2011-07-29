package org.esgf.commonui;

import java.io.File;
import java.util.List;

import org.esgf.adminui.Role;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.core.io.ClassPathResource;

public class RoleOperationsXMLImpl implements RoleOperationsInterface {

    private File GROUPS_FILE;
    private File PERMISSIONS_FILE;
    private File USERS_FILE;
    private File ROLES_FILE;
    
    public RoleOperationsXMLImpl(){
        GROUPS_FILE = new File("");
        try {
            GROUPS_FILE = new ClassPathResource("db.groups").getFile();
        }catch(Exception e) {
            System.out.println("error in db.groups");
        }
        
        PERMISSIONS_FILE = new File("");
        try {
            PERMISSIONS_FILE = new ClassPathResource("db.permissions").getFile();
        }catch(Exception e) {
            System.out.println("error in db.permissions");
        }
        
        ROLES_FILE = new File("");
        try {
            ROLES_FILE = new ClassPathResource("db.roles").getFile();
        }catch(Exception e) {
            System.out.println("error in db.roles");
        }
        
        USERS_FILE = new File("");
        try {
            USERS_FILE = new ClassPathResource("db.users").getFile();
        }catch(Exception e) {
            System.out.println("error in db.users");
        }
    }
    
    
    @Override
    public Role getRoleForUserInGroup(String userId, String groupId) {
        Role role = null;
        
        
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        
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


    @Override
    public Role getRoleObjectFromRoleId(String roleId) {
        
        Role role = new Role();
        
        
        SAXBuilder builder = new SAXBuilder();
        String xmlContent = "";
        
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
