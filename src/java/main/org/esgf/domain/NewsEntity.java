package org.esgf.domain;

/**
 *
 * @author fwang2
 *
 */


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

    @Id
    @GeneratedValue
    private Long id;

    @Version
    private Integer version;


    private String imageFileName;


    @Lob
    private byte[] imageFile;

    @NotNull
    @Size(min = 1, max = 120)
    private String title;

    @NotNull
    @Size(min = 1, max = 500)
    private String body;

    @Transient
    public static final String BASE_URL = "/images/thumbnail/";

    public NewsEntity() {};


    public final Long getId() {
        return id;
    }



    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public byte[] getImageFile() {
        return imageFile;
    }


    public void setImageFile(byte[] imageFile) {
        this.imageFile = imageFile;
    }




    public String getBody() {
        return body;
    }


    public void setBody(String body) {
        this.body = body;
    }


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
