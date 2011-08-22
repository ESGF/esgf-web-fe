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
        if(this.firstName != null) {
            return firstName;
        }
        else
            return "N/A";
        
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUserName() {
        if(this.userName != null)
            return userName;
        else
            return "N/A";
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailAddress() {
        if(this.emailAddress != null)
            return emailAddress;
        else
            return "N/A";
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    
    public String getOrganization() {
        if(this.organization != null)
            return organization;
        else
            return "N/A";
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getCity() {
        if(this.city != null)
            return city;
        else
            return "N/A";
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        if(this.state != null)
            return state;
        else
            return "N/A";
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        if(this.country != null)
            return country;
        else
            return "N/A";
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getOpenId() {
        if(this.openId != null)
            return openId;
        else
            return "N/A";
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getDN() {
        if(this.DN != null)
            return DN;
        else
            return "N/A";
    }

    public void setDN(String dN) {
        DN = dN;
    }
    

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        if(this.lastName != null)
            return lastName;
        else
            return "N/A";
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
        if(this.middleName != null)
            return middleName;
        else
            return "N/A";
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getUserId() {
        if(this.userId != null)
            return userId;
        else
            return "N/A";
    }
   
    
    public String toXml() {
        String xmlContent = "";
        
        Element userElement = new Element("user");
        
        System.out.println("x");
        
        Element userIdEl = new Element("id");
        if(this.userId != null) {
            userIdEl.addContent(this.userId);
        }
        else {
            userIdEl.addContent("N/A");
        }

        System.out.println("x");
        
        Element userfirstEl = new Element("first");
        if(this.firstName != null) {
            userfirstEl.addContent(this.firstName);
        }
        else {
            userfirstEl.addContent("N/A");
        }

        System.out.println("x");
        Element userlastEl = new Element("last");
        if(this.lastName != null) {
            userlastEl.addContent(this.lastName);
        }
        else {
            userlastEl.addContent("N/A");
        }
        System.out.println("x");
        
        
        Element usermiddleEl = new Element("middle");
        if(this.middleName != null) {
            usermiddleEl.addContent(this.middleName);
        }
        else {
            usermiddleEl.addContent("N/A");
        }

        System.out.println("x");
        Element userNameEl = new Element("username");
        if(this.userName != null) {
            userNameEl.addContent(this.userName);
        }
        else {
            userNameEl.addContent("N/A");
        }

        System.out.println("x");
        
        Element emailEl = new Element("email");
        if(this.emailAddress != null) {
            emailEl.addContent(this.emailAddress);
        }
        else {
            emailEl.addContent("N/A");
        }

        System.out.println("x");
        
        
        Element organizationEl = new Element("organization");
        if(this.organization != null) {
            organizationEl.addContent(this.organization);
        }
        else {
            organizationEl.addContent("N/A");
        }

        System.out.println("x");
        
        Element cityEl = new Element("city");
        if(this.city != null) {
            cityEl.addContent(this.city);
        }
        else {
            cityEl.addContent("N/A");
        }

        System.out.println("x");
        
        
        Element stateEl = new Element("state");
        if(this.state != null) {
            stateEl.addContent(this.state);
        }
        else {
            stateEl.addContent("N/A");
        }

        System.out.println("x");
        
        Element countryEl = new Element("country");
        if(this.country != null) {
            countryEl.addContent(this.country);
        }
        else {
            countryEl.addContent("N/A");
        }

        System.out.println("x");
        
        Element openIdEl = new Element("openid");
        if(this.openId != null) {
            openIdEl.addContent(this.openId);
        }
        else {
            openIdEl.addContent("N/A");
        }

        System.out.println("x");
        
        Element dnEl = new Element("dn");
        if(this.DN != null) {
            dnEl.addContent(this.DN);
        }
        else {
            dnEl.addContent("N/A");
        }

        System.out.println("x");
        
        
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
