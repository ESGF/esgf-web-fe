package org.esgf.adminui;

import java.io.Serializable;

public class Group implements Serializable{

    private String name;
    private String role;
    private String status;
    
    public Group() {
        this.name = "group_name";
        this.role = "group_role_name";
        this.status = "group_status_name";
    }
    
    public Group(String name, String role, String status) {
        this.name = name;
        this.role = role;
        this.status = status;
    }
    
    
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getRole() {
        return role;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
    
    
    
}
