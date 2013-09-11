/*****************************************************************************
 * Copyright � 2011 , UT-Battelle, LLC All rights reserved
 *
 * OPEN SOURCE LICENSE
 *
 * Subject to the conditions of this License, UT-Battelle, LLC (the
 * �Licensor�) hereby grants to any person (the �Licensee�) obtaining a copy
 * of this software and associated documentation files (the "Software"), a
 * perpetual, worldwide, non-exclusive, irrevocable copyright license to use,
 * copy, modify, merge, publish, distribute, and/or sublicense copies of the
 * Software.
 *
 * 1. Redistributions of Software must retain the above open source license
 * grant, copyright and license notices, this list of conditions, and the
 * disclaimer listed below.  Changes or modifications to, or derivative works
 * of the Software must be noted with comments and the contributor and
 * organization�s name.  If the Software is protected by a proprietary
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
 *    Contract No. DE-AC05-00OR22725 with the Department of Energy.�
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
package org.esgf.web;

/**
*
* @author John Harney (harneyjf@ornl.gov)
*
*/
import javax.servlet.http.HttpServletRequest;

import org.esgf.filetransformer.FileTransformer;
import org.esgf.filetransformer.FileTransformerFactory;
import org.esgf.srm.utils.SRMUtils;
import org.esgf.srmcache.SRMCacheStore;
import org.esgf.srmcache.SRMCacheStoreFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * FileTransformerController is responsible for translating equivalent file names:
 * - srm file name -> http file name
 * - srm file name -> gridftp file name
 * - http file name -> srm file name
 * - gridftp file name -> srm file name
 * - http file name -> gridftp file name
 * - gridftp file name -> http file name
 */
@Controller
public class FileTransformerController {

    //private static String FAILURE_MESSAGE = "failure";
    
    private SRMCacheStore srm_cache;
 
    /**
     * Constructor FileTransformerController
     * 
     * Creates a new srm_cache object which abstracts the table of srm cache values
     */
    public FileTransformerController() {


        if(!SRMUtils.srm_disabled) {
            SRMCacheStoreFactory srmCacheStore = new SRMCacheStoreFactory();
            this.srm_cache = srmCacheStore.makeSRMCacheStore(SRMCacheStoreController.DB_TYPE); 
        }
    }
    
    /**
     * Service srmfile
     * 
     */
    @RequestMapping(method=RequestMethod.GET, value="/srmfile")
    public @ResponseBody String getSRMFile(HttpServletRequest request) {
       
        String file_url = request.getParameter("file_url");
        if(file_url == null) {
            return SRMUtils.failure_message;
        }
        
        String inputType = request.getParameter("input_type");
        
        FileTransformer filetrans = null;
        
        
        if(inputType == null) {
            FileTransformerFactory factory = new FileTransformerFactory();
            
            filetrans = factory.makeFileTransformer("SRM",srm_cache,file_url);
        } else if(inputType.equalsIgnoreCase("HTTP")) {
            FileTransformerFactory factory = new FileTransformerFactory();
            
            filetrans = factory.makeFileTransformer("HTTP",srm_cache,file_url);
            
        } else if (inputType.equalsIgnoreCase("GridFTP")){
            FileTransformerFactory factory = new FileTransformerFactory();
            
            filetrans = factory.makeFileTransformer("GridFTP",srm_cache,file_url);
        } else {

            FileTransformerFactory factory = new FileTransformerFactory();
            
            filetrans = factory.makeFileTransformer("SRM",srm_cache,file_url);
        }
        
        //System.out.println("---End In srmfile---");
            
        return filetrans.getSRM();
    }
    
    @RequestMapping(method=RequestMethod.GET, value="/gridftpfile")
    public @ResponseBody String getGridFTPFile(HttpServletRequest request) {
       
        //System.out.println("---In gridftpfile---");
        
        String dataset_id = request.getParameter("dataset_id");
        if(dataset_id == null) {
            return SRMUtils.failure_message;
        }
        

        String file_id = request.getParameter("file_id");
        if(file_id == null) {
            return SRMUtils.failure_message;
        }
        
        String file_url = request.getParameter("file_url");
        if(file_url == null) {
            return SRMUtils.failure_message;
        }

        
        
        String inputType = request.getParameter("input_type");
        
        FileTransformer filetrans = null;
              
        FileTransformerFactory factory = new FileTransformerFactory();
        
        filetrans = factory.makeFileTransformer("General",srm_cache,file_url,dataset_id,file_id);
        
        return filetrans.getGridFTP();
    }
    
    @RequestMapping(method=RequestMethod.GET, value="/httpfile")
    public @ResponseBody String getHTTPFile(HttpServletRequest request) {
        
        if(SRMUtils.httpFileFlag) {
            System.out.println("---In httpfile---");
        }
        
        String dataset_id = request.getParameter("dataset_id");
        if(dataset_id == null) {
            return SRMUtils.failure_message;
        }
        

        String file_id = request.getParameter("file_id");
        if(file_id == null) {
            return SRMUtils.failure_message;
        }
        
        String file_url = request.getParameter("file_url");
        if(file_url == null) {
            return SRMUtils.failure_message;
        }

        if(SRMUtils.httpFileFlag) {
            System.out.println("\tDatasetId: " + dataset_id);
            System.out.println("\tFileId: " + file_id);
            System.out.println("\tFileUrl: " + file_url);
        }
        
        String inputType = request.getParameter("input_type");
        
        FileTransformer filetrans = null;
              
        FileTransformerFactory factory = new FileTransformerFactory();
        
        
        filetrans = factory.makeFileTransformer("General",this.srm_cache,file_url,dataset_id,file_id);
        
        if(SRMUtils.httpFileFlag) {
            System.out.println("http: " + filetrans.getHttp());
            System.out.println("---End In httpfile---");
        }
        
        return filetrans.getHttp();
    }
}
