package org.esgf.srm.scriptgen;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.esgf.filetransformer.SRMFileTransformationUtils;
import org.esgf.srm.utils.SRMUtils;

public class ComplexWgetScriptGenerator extends WgetScriptGenerator {

    public static String WGET_TEMPLATE_FILE = "/esg/config/wget-template";
    
    private String template; 
    
    //params in the wget script template
    private String fileStr;
    
    private String message;
    private String userOpenId;
    private String hostName;
    private String searchUrl;
    private String date;
    
    public ComplexWgetScriptGenerator() {
        setType("complex");
        
        //this.fileStr = "";
        FileInputStream fis = null;
        InputStreamReader reader = null;
        
        try {
            
            fis = new FileInputStream(WGET_TEMPLATE_FILE);
            reader = new InputStreamReader(fis, "UTF-8");
            
            StringBuilder sb = new StringBuilder();
            char[] buff = new char[1024];
            int read;
            
            while ((read = reader.read(buff)) == buff.length) {
                sb.append(buff);
            }
            sb.append(buff, 0, read);
            
            this.template = sb.toString();
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public Map<String, String> populateTemplateTagMap() {
        Map<String,String> templateTagMap = new HashMap<String,String>();

        //add the message
        templateTagMap.put("message", message);
        
        //add the files
        templateTagMap.put("files", fileStr);
        
        //add the openid
        templateTagMap.put("userOpenId", userOpenId);
        
        //add the hostname
        templateTagMap.put("hostName", hostName);
        
        //add the searchurl
        templateTagMap.put("searchUrl", searchUrl);
        
        //add the date
        templateTagMap.put("date", date);
        
        
        return templateTagMap;
    }
    
    public String generateScript() {
        String template = Utils.replace(this.template, populateTemplateTagMap());
        return template;
    }
   

    
    public String getFileStr() {
        return fileStr;
    }

    public void setFileStr(String fileStr) {
        this.fileStr = fileStr;
    }
    
    public void setFileStr(String [] files,String [] checksums,String [] checksumTypes) {
      //create fileStr here
        String [] fileNames = SRMUtils.extractFileNames(files);
        
        String fileStr = "";
        for(int i=0;i<files.length;i++) {
            String fileRow = "'" + SRMFileTransformationUtils.gridftp2http(files[i]) + "' " +  
                             "'" + fileNames[i] + "' " +
                             "'" + checksums[i] + "' " +
                             "'" + checksumTypes[i] + "'" + "\n";
            fileStr += fileRow;
        }
        this.fileStr = fileStr;
    }
    
    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserOpenId() {
        return userOpenId;
    }

    public void setUserOpenId(String userOpenId) {
        this.userOpenId = userOpenId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getSearchUrl() {
        return searchUrl;
    }

    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    
}
