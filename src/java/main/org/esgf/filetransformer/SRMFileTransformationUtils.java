package org.esgf.filetransformer;

import java.io.File;
import java.util.List;


public class SRMFileTransformationUtils {

    public static String THREDDS_DATAROOT = "/thredds/fileServer/esg_srm_dataroot/";
    
    public static void main(String [] args) {
        String url = "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?" +
                "SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/" +
                "t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-09.nc";
   
        String file = extractFileNameFromUrl(url);
        //System.out.println(file);
        
        String revisedUrl = replaceHostName(url);
        System.out.println(revisedUrl);
        
        String [] urls = new String [1];
        urls[0] = url;
        String [] outputFiles = simulateSRM(urls);
        
        for(int i=0;i<outputFiles.length;i++) {
            System.out.println("i: " + outputFiles[i] + " " + gridftp2http(outputFiles[i]));
        }
    }
    
    
    //gridftp -> http
    public static String gridftp2http(String gsiftp) {
        String http = "";
        
        http = replaceHostName(gsiftp);
        
        http = http.replace("gsiftp", "http");
        
        http = http.replace(":2811","");
        
        http = http.replace("//lustre/esgfs/SRMTemp", THREDDS_DATAROOT);
        
        
        return http;
    }
    
    //only for older thredds catalogs
    //esg2-sdn1.ccs.ornl.gov -> esg.ccs.ornl.gov
    public static String replaceHostName(String url) {
        String newUrl = url.replaceAll("esg2-sdnl1.ccs.ornl.gov", "esg.ccs.ornl.gov");
        return newUrl;
    }
    
    //cache name replacement
    //SRM -> SRMTemp
   
    
    //get the fileName from the url (i.e. everything after the last "/" of the url)
    public static String extractFileNameFromUrl(String url) {
        String newFileName = "";
        String tempStr = "";
        
        int counter = url.length()-1;
        char ch = url.charAt(counter);
        while(ch != '/') {
            tempStr += ch;
            ch = url.charAt(counter);
            counter--;
        }   
        
        for(int i=tempStr.length()-1;i>=0;i--) {
            newFileName += tempStr.charAt(i);
        }
        
        return newFileName;
    }
    
    
    //simulate
    //input
    //srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-09.nc
    //output
    //gsiftp://esg2-sdnl1.ccs.ornl.gov//lustre/esgfs/SRM/shared/V.0.0-505553807/t341f02.FAMIPr.cam2.h0.1978-09.nc
    public static String [] simulateSRM(String [] inputFiles) {
    
        String [] outputFiles = new String [inputFiles.length];
        
        for(int i=0;i<inputFiles.length;i++) {
            String tempFile = inputFiles[i].replace("srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://", "file:///");
            //tempFile = transformServerName(tempFile);
            
            File f = new File(tempFile);
            String fileName = f.getName();
            
            String outputFile = "gsiftp://esg.ccs.ornl.gov:2811//lustre/esgfs/SRM/" + fileName;
            
            outputFiles[i] = outputFile;
        }
        
        
        return outputFiles;
        
    }
    
    
}
