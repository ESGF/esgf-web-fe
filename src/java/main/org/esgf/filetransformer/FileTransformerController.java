package org.esgf.filetransformer;

import javax.servlet.http.HttpServletRequest;

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

    private static String FAILURE_MESSAGE = "failure";
    private static String SUCCESS_MESSAGE = "success";
    private static String DEFAULT_file_url = "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-10.nc";
    
    /**
     * 
     */
    @RequestMapping(method=RequestMethod.GET, value="/srmfile")
    public @ResponseBody String getSRMFile(HttpServletRequest request) {
        System.out.println("---In srmfile---");
        
        
        
        String file_url = request.getParameter("file_url");
        if(file_url == null) {
            return FAILURE_MESSAGE;
        }
        
        String inputType = request.getParameter("input_type");
        
        FileTransformer filetrans = null;
        
        
        if(inputType == null) {
            FileTransformerFactory factory = new FileTransformerFactory();
            
            filetrans = factory.makeFileTransformer("SRM",file_url);
        } else if(inputType.equalsIgnoreCase("HTTP")) {
            FileTransformerFactory factory = new FileTransformerFactory();
            
            filetrans = factory.makeFileTransformer("HTTP",file_url);
            
        } else if (inputType.equalsIgnoreCase("GridFTP")){
            FileTransformerFactory factory = new FileTransformerFactory();
            
            filetrans = factory.makeFileTransformer("GridFTP",file_url);
        } else {

            FileTransformerFactory factory = new FileTransformerFactory();
            
            filetrans = factory.makeFileTransformer("SRM",file_url);
        }
        
        System.out.println("---End In srmfile---");
            
        return filetrans.getSRM();
    }
    
    @RequestMapping(method=RequestMethod.GET, value="/gridftpfile")
    public @ResponseBody String getGridFTPFile(HttpServletRequest request) {
       
        System.out.println("---In gridftpfile---");
        
        
        /*
        String file_url = request.getParameter("file_url");
        if(file_url == null) {
            return FAILURE_MESSAGE;
        }
        
        String inputType = request.getParameter("input_type");
        
        FileTransformer filetrans = null;
                
        if(inputType == null) {
            FileTransformerFactory factory = new FileTransformerFactory();
            
            filetrans = factory.makeFileTransformer("SRM",file_url);
        } else if(inputType.equalsIgnoreCase("HTTP")) {
            FileTransformerFactory factory = new FileTransformerFactory();
            
            filetrans = factory.makeFileTransformer("HTTP",file_url);
            
        } else if (inputType.equalsIgnoreCase("GridFTP")){
            FileTransformerFactory factory = new FileTransformerFactory();
            
            filetrans = factory.makeFileTransformer("GridFTP",file_url);
        } else {

            FileTransformerFactory factory = new FileTransformerFactory();
            
            filetrans = factory.makeFileTransformer("SRM",file_url);
        }
        
        System.out.println("gridftp: " + filetrans.getGridFTP());
        System.out.println("---End In gridftpfile---");
        */
        
        String dataset_id = request.getParameter("dataset_id");
        if(dataset_id == null) {
            return FAILURE_MESSAGE;
        }
        

        String file_id = request.getParameter("file_id");
        if(file_id == null) {
            return FAILURE_MESSAGE;
        }
        
        String file_url = request.getParameter("file_url");
        if(file_url == null) {
            return FAILURE_MESSAGE;
        }

        System.out.println("\tDatasetId: " + dataset_id);
        System.out.println("\tFileId: " + file_id);
        System.out.println("\tFileUrl: " + file_url);
        
        String inputType = request.getParameter("input_type");
        
        FileTransformer filetrans = null;
              
        FileTransformerFactory factory = new FileTransformerFactory();
        
        filetrans = factory.makeFileTransformer("General",file_url,dataset_id,file_id);
        
        return filetrans.getGridFTP();
    }
    
    @RequestMapping(method=RequestMethod.GET, value="/httpfile")
    public @ResponseBody String getHTTPFile(HttpServletRequest request) {
        
        System.out.println("---In httpfile---");
        
        String dataset_id = request.getParameter("dataset_id");
        if(dataset_id == null) {
            return FAILURE_MESSAGE;
        }
        

        String file_id = request.getParameter("file_id");
        if(file_id == null) {
            return FAILURE_MESSAGE;
        }
        
        String file_url = request.getParameter("file_url");
        if(file_url == null) {
            return FAILURE_MESSAGE;
        }

        System.out.println("\tDatasetId: " + dataset_id);
        System.out.println("\tFileId: " + file_id);
        System.out.println("\tFileUrl: " + file_url);
        
        String inputType = request.getParameter("input_type");
        
        FileTransformer filetrans = null;
              
        FileTransformerFactory factory = new FileTransformerFactory();
        
        filetrans = factory.makeFileTransformer("General",file_url,dataset_id,file_id);
        
        /*
        if(inputType == null) {
            FileTransformerFactory factory = new FileTransformerFactory();
            
            filetrans = factory.makeFileTransformer("SRM",file_url);
        } else if(inputType.equalsIgnoreCase("HTTP")) {
            FileTransformerFactory factory = new FileTransformerFactory();
            
            filetrans = factory.makeFileTransformer("HTTP",file_url);
            
        } else if (inputType.equalsIgnoreCase("GridFTP")){
            FileTransformerFactory factory = new FileTransformerFactory();
            
            filetrans = factory.makeFileTransformer("GridFTP",file_url);
        } else {

            FileTransformerFactory factory = new FileTransformerFactory();
            
            filetrans = factory.makeFileTransformer("SRM",file_url);
        }
        */
        
        System.out.println("http: " + filetrans.getHttp());
        System.out.println("---End In httpfile---");
            
        return filetrans.getHttp();
    }
}
