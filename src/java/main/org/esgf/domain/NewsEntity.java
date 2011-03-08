package org.esgf.domain;

/**
 *
 * @author fwang2
 *
 */


import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
public class NewsEntity implements DomainObject {

    private Long id;
    private Integer version;
    private String imageFileName;
    private byte[] imageFile;
    private String title;
    private String body;

    private static final AtomicLong idSequence = new AtomicLong();
    public static final String BASE_URL = "/images/thumbnail/";

    public NewsEntity() {};


    @Id
    @GeneratedValue
    public final Long getId() {
        return id;
    }


    @Version
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @NotNull
    @Size(min = 1, max = 120)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Lob
    public byte[] getImageFile() {
        return imageFile;
    }


    public void setImageFile(byte[] imageFile) {
        this.imageFile = imageFile;
    }


    @NotNull
    @Size(min = 1, max = 500)
    public String getBody() {
        return body;
    }


    public void setBody(String body) {
        this.body = body;
    }


    public Long assignId() {
        this.id = idSequence.incrementAndGet();
        return id;
    }


    @Transient
    public String getUrl() {
        return BASE_URL + this.getId();
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getImageFileName() {
        return imageFileName;
    }


}
