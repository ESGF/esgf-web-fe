package org.esgf.srm.scriptgen;

import java.util.HashMap;
import java.util.Map;

import org.esgf.filetransformer.SRMFileTransformationUtils;

public class BasicGlobusUrlCopyScriptGenerator extends GlobusUrlCopyScriptGenerator{

    private String template; 
    private String fileStr;
    
    public BasicGlobusUrlCopyScriptGenerator() {
        setType("basic");
        
        this.fileStr = "";
        
        this.template = "{{files}}";
    }
    
    public String generateScript() {
        String template = Utils.replace(this.template, populateTemplateTagMap());
        return template;
    }

    public Map<String, String> populateTemplateTagMap() {
        Map<String,String> templateTagMap = new HashMap<String,String>();
        
        //add the files
        templateTagMap.put("files", fileStr);
        
        return templateTagMap;
    }
    
    public String getFileStr() {
        return fileStr;
    }

    public void setFileStr(String fileStr) {
        this.fileStr = fileStr;
    }
    
    public void setFileStr(String [] files) {
        
        for(int i=0;i<files.length;i++) {
            String fileRow = "globus-url-copy " +  
                            files[i] + " file:///tmp/" + SRMFileTransformationUtils.extractFileNameFromUrl(files[i]) + "\n";
            this.fileStr += fileRow;
        }
        
    }

}
