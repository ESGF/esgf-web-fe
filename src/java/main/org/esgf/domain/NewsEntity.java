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
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class NewsEntity implements DomainObject {

    private Long id;
    private Integer version;

    @NotNull
    @Size(min = 1, max = 120)
    private String title;

    private byte[] picture;

    @NotNull
    @Size(min = 1, max = 500)
    private String body;

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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public Long assignId() {
        this.id = idSequence.incrementAndGet();
        return id;
    }

    private static final AtomicLong idSequence = new AtomicLong();

}
