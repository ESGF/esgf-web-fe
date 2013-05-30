package org.esgf.filetransformer;

import java.io.File;
import java.util.List;
import java.util.Random;

import org.esgf.bestmanpath.BestmanPathGenerator;
import org.esgf.bestmanpath.BestmanPathGeneratorFactory;

/**
 * 
gsiftp://esg.ccs.ornl.gov//lustre/esgfs/SRMTemp/shared/V.0.0-1034476012/t341f02.FAMIPr.cam2.h0.1979-06.nc

wget http://esg.ccs.ornl.gov/thredds/fileServer/esg_srm_dataroot/SRMTemp/shared/V.0.0-10999984/t341f02.FAMIPr.cam2.h0.1978-10.nc

 * 
 * @author 8xo
 *
 */

public class SRMFileTransformationUtils {

    public static String THREDDS_DATAROOT = "/thredds/fileServer/esg_srm_dataroot";
    
    public static String BESTMAN_PATH_GENERATOR_TYPE = "Random";
    
    public static void main(String [] args) {
        String url = "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?" +
                "SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/" +
                "t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-09.nc";
   
/*
        Given: gsiftp://esg.ccs.ornl.gov//lustre/esgfs/SRMTemp/shared/V.0.0-115256497/t85f09.B1850.cice.h.0022-01.nc
    Double...V.0.0-1152564977
  */
        
        String givenUrl = "gsiftp://esg.ccs.ornl.gov//lustre/esgfs/SRMTemp/shared/V.0.0-115256497/t85f09.B1850.cice.h.0022-01.nc";
        
        String bestmanNum = extractBestmanNumFromUrl(givenUrl);
        
        System.out.println(bestmanNum);
        
        /*
        String path = extractFilePathNameFromUrl(url);
        
        String file = extractFileNameFromUrl(url);
        
        String revisedUrl = replaceHostName(url);
        
        String [] urls = new String [1];
        urls[0] = url;
        String [] outputFiles = simulateSRM(urls);
        
        for(int i=0;i<outputFiles.length;i++) {
            System.out.println("i: " + outputFiles[i] + " " + gridftp2http(outputFiles[i]));
        }
        */
        
        
        
        
        /*
        FileTransformerFactory factory = new FileTransformerFactory();
        
        FileTransformer filetrans = null;
        
        filetrans = factory.makeFileTransformer("SRM",url);
        
        
        System.out.println(filetrans.getGridFTP());
        
        System.out.println(filetrans.getHttp());
        */
    }
    
    
    //gridftp -> http
    public static String gridftp2http(String gsiftp) {
        String http = "";
        
        http = replaceHostName(gsiftp);
        
        http = http.replace("gsiftp", "http");
        
        http = http.replace(":2811","");
        
        http = http.replace("//lustre/esgfs", THREDDS_DATAROOT);
        
        
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
    
    public static String extractFilePathNameFromUrl(String url) {
        
        System.out.println("Extract File Path Original URL: " + url);
        
        String newFileName = "";
        String tempStr = "";
        
        int counter = url.length()-1;
        char ch = url.charAt(counter);
        while(ch != '/') {
            tempStr += ch;
            ch = url.charAt(counter);
            counter--;
        }   
        
        newFileName = url.substring(0, (counter+1));
        //System.out.println("new File name: " + newFileName + "\n");
        
        return newFileName;
    }
    
    
    public static String extractBestmanNumFromUrl(String url) {
        //System.out.println("Double..." + extractFileNameFromUrl(extractFilePathNameFromUrl(url)) + "\n");
        
        String newFileName = "";
        String tempStr = "";
        
        int counter = url.length()-1;
        char ch = url.charAt(counter);
        while(ch != '/') {
            tempStr += ch;
            ch = url.charAt(counter);
            counter--;
        }   
        
        newFileName = url.substring(0, (counter+1));
        
        
        
        tempStr = "";
        
        counter = newFileName.length()-1;
        while(url.charAt(counter) != '/') {
            ch = url.charAt(counter);
            tempStr += ch;
            counter--;
        }   
        
        newFileName = "";
        
        for(int i=tempStr.length()-1;i>=0;i--) {
            newFileName += tempStr.charAt(i);
        }
        
        
        
        return newFileName;
        //return extractFileNameFromUrl(extractFilePathNameFromUrl(url));
    }
   
    
    //simulate
    //input
    //srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-09.nc
    //output
    //gsiftp://esg2-sdnl1.ccs.ornl.gov//lustre/esgfs/SRM/shared/V.0.0-505553807/t341f02.FAMIPr.cam2.h0.1978-09.nc
    public static String [] simulateSRM(String [] inputFiles) {
    
        String [] outputFiles = new String [inputFiles.length];
        
        System.out.println("file urls length: " + inputFiles.length);
        
        
        
        
        for(int i=0;i<inputFiles.length;i++) {
            //System.out.println("\tinput file: " + i + " " + inputFiles[i]);
            String tempFile = inputFiles[i].replace("srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?SFN=mss://", "file:///");
            //tempFile = transformServerName(tempFile);
            
            File f = new File(tempFile);
            String fileName = f.getName();
            
            String file_id = "";
            String dataset_id = "";
            
            BestmanPathGeneratorFactory bestmanPathGeneratorFactory = new BestmanPathGeneratorFactory();
            BestmanPathGenerator bestmanNumGenerator = 
                    bestmanPathGeneratorFactory.makeBestmanPathGenerator(BESTMAN_PATH_GENERATOR_TYPE,dataset_id,file_id);
            
            String bestmannum = bestmanNumGenerator.getBestmanPath();
            
            
            String outputFile = "gsiftp://esg.ccs.ornl.gov:2811//lustre/esgfs/shared/" + bestmannum + "/" + fileName;
            System.out.println("\toutput file: " + i + " " + outputFile);
            
            outputFiles[i] = outputFile;
        }
        
        
        
        return outputFiles;
        
    }
    
    
}
