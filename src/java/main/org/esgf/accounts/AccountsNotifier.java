package org.esgf.accounts;

import java.net.URL;

import esg.node.security.UserInfo;

/**
 * API for notifications about user accounts.
 * 
 * @author Luca Cinquini
 *
 */
public interface AccountsNotifier {

    /**
     * Notifies a user that his account has been created.
     * @param user : the user object
     * @param verificationToken : the verification token associated with that user
     */
    public void accountCreated(final String serverUrl, final UserInfo user, final String verificationToken);
    
}
