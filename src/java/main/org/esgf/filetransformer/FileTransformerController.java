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
        
        //System.out.println(filetrans.getHttp());
            
        return filetrans.getSRM();
    }
    
    @RequestMapping(method=RequestMethod.GET, value="/gridftpfile")
    public @ResponseBody String getGridFTPFile(HttpServletRequest request) {
       
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
        
        //System.out.println(filetrans.getHttp());
            
        return filetrans.getGridFTP();
    }
    
    @RequestMapping(method=RequestMethod.GET, value="/httpfile")
    public @ResponseBody String getHTTPFile(HttpServletRequest request) {
        
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
        //System.out.println(filetrans.getHttp());
            
        return filetrans.getHttp();
    }
}
