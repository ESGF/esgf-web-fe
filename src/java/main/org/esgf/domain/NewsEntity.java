/*****************************************************************************
 * Copyright © 2011 , UT-Battelle, LLC All rights reserved
 *
 * OPEN SOURCE LICENSE
 *
 * Subject to the conditions of this License, UT-Battelle, LLC (the
 * “Licensor”) hereby grants to any person (the “Licensee”) obtaining a copy
 * of this software and associated documentation files (the "Software"), a
 * perpetual, worldwide, non-exclusive, irrevocable copyright license to use,
 * copy, modify, merge, publish, distribute, and/or sublicense copies of the
 * Software.
 *
 * 1. Redistributions of Software must retain the above open source license
 * grant, copyright and license notices, this list of conditions, and the
 * disclaimer listed below.  Changes or modifications to, or derivative works
 * of the Software must be noted with comments and the contributor and
 * organization’s name.  If the Software is protected by a proprietary
 * trademark owned by Licensor or the Department of Energy, then derivative
 * works of the Software may not be distributed using the trademark without
 * the prior written approval of the trademark owner.
 *
 * 2. Neither the names of Licensor nor the Department of Energy may be used
 * to endorse or promote products derived from this Software without their
 * specific prior written permission.
 *
 * 3. The Software, with or without modification, must include the following
 * acknowledgment:
 *
 *    "This product includes software produced by UT-Battelle, LLC under
 *    Contract No. DE-AC05-00OR22725 with the Department of Energy.”
 *
 * 4. Licensee is authorized to commercialize its derivative works of the
 * Software.  All derivative works of the Software must include paragraphs 1,
 * 2, and 3 above, and the DISCLAIMER below.
 *
 *
 * DISCLAIMER
 *
 * UT-Battelle, LLC AND THE GOVERNMENT MAKE NO REPRESENTATIONS AND DISCLAIM
 * ALL WARRANTIES, BOTH EXPRESSED AND IMPLIED.  THERE ARE NO EXPRESS OR
 * IMPLIED WARRANTIES OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE,
 * OR THAT THE USE OF THE SOFTWARE WILL NOT INFRINGE ANY PATENT, COPYRIGHT,
 * TRADEMARK, OR OTHER PROPRIETARY RIGHTS, OR THAT THE SOFTWARE WILL
 * ACCOMPLISH THE INTENDED RESULTS OR THAT THE SOFTWARE OR ITS USE WILL NOT
 * RESULT IN INJURY OR DAMAGE.  The user assumes responsibility for all
 * liabilities, penalties, fines, claims, causes of action, and costs and
 * expenses, caused by, resulting from or arising out of, in whole or in part
 * the use, storage or disposal of the SOFTWARE.
 *
 *
 ******************************************************************************/

package org.esgf.domain;

/**
 *
 *
 */

/**
 * @author Feiyi Wang (fwang2@ornl.gov)
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
