package org.esgf.domain;

import javax.persistence.*;

@Entity
public class NewsData implements DomainObject {
    private Long id;
    private byte[] picture;
    private Integer version;
    
    public NewsData() {        
    }
    
    public NewsData(byte[] picture) {
        this.picture = picture;
    }
    
    @Id
    @GeneratedValue
    public final Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
}
