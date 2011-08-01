package org.esgf.adminui;

import java.io.Serializable;

import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class User implements Serializable{

    
    private static final long serialVersionUID = 1L;
    
    private String userId;
    private String lastName;
    private String firstName;
    private String middleName;
    private String userName;
    private String emailAddress;
    
    
    private String organization;
    private String city;
    private String state;
    private String country;
    
    private String openId;
    private String DN;
    
    
    public User(String userId,
                String lastName,
                String firstName,
                String middleName,
                String userName,
                String emailAddress,
                String organization,
                String city,
                String state,
                String country,
                String openId,
                String DN) {
        this.setUserId(userId);
        this.lastName = lastName;
        this.firstName = firstName;
        this.setMiddleName(middleName);
        this.userName = userName;
        this.emailAddress = emailAddress;
        
        this.organization = organization;
        this.city = city;
        this.state = state;
        this.country = country;
        this.openId = openId;
        this.DN = DN;
        
        
    }
    
    
    public User() {
        this.setUserId("userId");
        this.lastName = "lastName";
        this.firstName = "firstName";
        this.userName = "userName";
        this.setMiddleName("middleName");
        this.emailAddress = "emailAddress";
        
        
        this.organization = "organization";
        
        this.city = "city";
        this.state = "state";
        this.country = "country";
        this.openId = "openId";
        this.DN = "DN";
       
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
    

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }
    
    public String toString() {
        String str = "";
        
        str += "User Information" + System.getProperty("line.separator");
        str += "\tUserId: " + this.userId + System.getProperty("line.separator");
        str += "\tUserName: " + this.userName + System.getProperty("line.separator");
        str += "\tLastName: " + this.lastName +  System.getProperty("line.separator");
        str += "\tFirstName: " + this.firstName +  System.getProperty("line.separator");
        str += "\tMiddleName: " + this.middleName +  System.getProperty("line.separator");
        str += "\tEmail: " + this.emailAddress +  System.getProperty("line.separator");
        str += "\tOrganization: " + this.organization +  System.getProperty("line.separator");
        str += "\tCity: " + this.city +  System.getProperty("line.separator");
        str += "\tState: " + this.state +  System.getProperty("line.separator");
        str += "\tCountry: " + this.country +  System.getProperty("line.separator");
        str += "\tOpenId: " + this.openId +  System.getProperty("line.separator");
        str += "\tDN: " + this.DN +  System.getProperty("line.separator");
        
        
        return str;
    }


    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }


    public String getMiddleName() {
        return middleName;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getUserId() {
        return userId;
    }
   
    
    public String toXml() {
        String xmlContent = "";
        
        Element userElement = new Element("user");
        
        Element userIdEl = new Element("id");
        userIdEl.addContent(this.userId);
        Element userfirstEl = new Element("first");
        userfirstEl.addContent(this.firstName);
        Element userlastEl = new Element("last");
        userlastEl.addContent(this.lastName);
        Element usermiddleEl = new Element("middle");
        usermiddleEl.addContent(this.middleName);
        Element userNameEl = new Element("username");
        userNameEl.addContent(this.userName);
        Element emailEl = new Element("email");
        emailEl.addContent(this.emailAddress);
        Element organizationEl = new Element("organization");
        organizationEl.addContent(this.organization);
        Element cityEl = new Element("city");
        cityEl.addContent(this.city);
        Element stateEl = new Element("state");
        stateEl.addContent(this.state);
        Element countryEl = new Element("country");
        countryEl.addContent(this.country);
        Element openIdEl = new Element("openid");
        openIdEl.addContent(this.openId);
        Element dnEl = new Element("dn");
        dnEl.addContent(this.DN);
        
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

        XMLOutputter outputter = new XMLOutputter();
        xmlContent = outputter.outputString(userElement);
        
        
        return xmlContent;
    }
    
}
