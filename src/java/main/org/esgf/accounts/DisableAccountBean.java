package org.esgf.accounts;

/**
 * Web bean to support disabling a user account.
 * 
 * @author Luca Cinquini
 *
 */
public class DisableAccountBean {
    
    private String openid;
    private String token;
    private String message;
    
    public DisableAccountBean() {}
    
    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOpenid() {
        return openid;
    }
    
    public String getToken() {
        return token;
    }
    
    

}
