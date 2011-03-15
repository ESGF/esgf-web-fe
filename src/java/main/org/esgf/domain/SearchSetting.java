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

    private Boolean annotate;

    private Boolean googleScholar;

    private Boolean mendeley;

    public void setAnnotate(Boolean annotate) {
        this.annotate = annotate;
    }

    public Boolean getAnnotate() {
        return annotate;
    }

    public void setGoogleScholar(Boolean googleScholar) {
        this.googleScholar = googleScholar;
    }

    public Boolean getGoogleScholar() {
        return googleScholar;
    }

    public void setMendeley(Boolean mendeley) {
        this.mendeley = mendeley;
    }

    public Boolean getMendeley() {
        return mendeley;
    }


}
