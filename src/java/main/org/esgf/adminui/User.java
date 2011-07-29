package org.esgf.adminui;

import java.io.Serializable;

public class User implements Serializable{

    private String lastName;
    private String firstName;
    private String userName;
    private String emailAddress;
    private String status;
    
    
    private String organization;
    private String city;
    private String state;
    private String country;
    
    private String openId;
    private String DN;
    
    private Group [] groups;
    
    public User(String lastName,
                String firstName,
                String userName,
                String emailAddress,
                String status,
                String organization,
                String city,
                String state,
                String country,
                String openId,
                String DN,
                Group [] groups) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.status = status;
        
        this.organization = organization;
        this.city = city;
        this.state = state;
        this.country = country;
        this.openId = openId;
        this.DN = DN;
        
        /*
        this.groups = new Group[groups.length];
        for(int i=0;i<groups.length;i++) {
            this.groups[i] = new Group(groups[i].getName(),groups[i].getRole(),groups[i].getStatus());
        }
        */  
        
        
    }
    
    
    public User() {
        this.lastName = "lastName";
        this.firstName = "firstName";
        this.userName = "userName";
        this.emailAddress = "emailAddress";
        this.status = "status";
        
        
        this.organization = "organization";
        
        this.city = "city";
        this.state = "state";
        this.country = "country";
        this.openId = "openId";
        this.DN = "DN";
        
        /*
        this.groups = new Group[1];
        this.groups[0] = new Group();
        */
    }
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getDN() {
        return DN;
    }

    public void setDN(String dN) {
        DN = dN;
    }
     
    public Group[] getGroups() {
        return groups;
    }

    public void setGroups(Group[] groups) {
        this.groups = groups;
    }

    

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }
}
