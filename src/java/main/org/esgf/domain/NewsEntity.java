package org.esgf.domain;

/**
 * 
 * @author fwang2
 *
 */

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Version;
import javax.persistence.GeneratedValue;

@Entity
public class NewsEntity implements DomainObject {

    private Long id;
    private Integer version;
    private String title;
    private String description;
    private NewsData thumbnailPicture;
    
    @Id
    @GeneratedValue
    public final Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    
    
}
