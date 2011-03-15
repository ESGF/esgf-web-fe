/**
 *
 * @author fwang2
 *
 * Note: this is not entity that needs to be persisted to the database
 *
 */
package org.esgf.domain;


@SuppressWarnings("serial")
public class SearchSetting implements DomainObject {

    private String annotate;

    private String googleScholar;

    private String mendeley;

    public void setAnnotate(String annotate) {
        this.annotate = annotate;
    }

    public String getAnnotate() {
        return annotate;
    }

    public void setGoogleScholar(String googleScholar) {
        this.googleScholar = googleScholar;
    }

    public String getGoogleScholar() {
        return googleScholar;
    }

    public void setMendeley(String mendeley) {
        this.mendeley = mendeley;
    }

    public String getMendeley() {
        return mendeley;
    }

}
