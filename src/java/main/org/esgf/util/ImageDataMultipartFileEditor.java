/**
 * @author fwang2
 */
package org.esgf.util;

import java.io.IOException;
import org.apache.log4j.Logger;
import org.esgf.domain.NewsEntity;
import org.springframework.beans.propertyeditors.ByteArrayPropertyEditor;
import org.springframework.web.multipart.MultipartFile;


public class ImageDataMultipartFileEditor extends ByteArrayPropertyEditor {

    protected final static Logger LOG = Logger.getLogger(ImageDataMultipartFileEditor.class);

    public void setValue(Object value) {

        byte[] imageData = null;
        if (value instanceof MultipartFile) {
            MultipartFile multipartFile = (MultipartFile) value;

            try {
                imageData = multipartFile.getBytes();
            } catch (IOException ex) {
                LOG.error("Cannot read contents of multipart file", ex);
                throw new IllegalArgumentException("Cannot read contents of multipart file: " + ex.getMessage());
            }
            LOG.debug("Original File Name:" + multipartFile.getOriginalFilename() );
        } else if (value instanceof byte[]) {
            imageData = (byte[]) value;
        } else {
            imageData = (value != null ? value.toString().getBytes() : null);
        }

        super.setValue(imageData);
    }

    public String getAsText() {
        byte[] value = ((NewsEntity) getValue()).getImageFile();
        return (value != null ? new String(value) : "");
    }

}
