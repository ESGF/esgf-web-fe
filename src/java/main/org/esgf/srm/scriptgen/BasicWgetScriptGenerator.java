package org.esgf.srm.scriptgen;

import java.util.HashMap;
import java.util.Map;

import org.esgf.filetransformer.SRMFileTransformationUtils;

public class BasicWgetScriptGenerator extends WgetScriptGenerator {
	
    private String template; 
    private String fileStr;
	
    public BasicWgetScriptGenerator() {
		setType("basic");
		
		this.fileStr = "";
		
		this.template = "{{files}}";
	}

    public Map<String, String> populateTemplateTagMap() {
        Map<String,String> templateTagMap = new HashMap<String,String>();
        
        //add the files
        templateTagMap.put("files", fileStr);
        
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
    
    public void setFileStr(String [] files) {
        for(int i=0;i<files.length;i++) {
           //System.out.println("file: " + files[i]);
           
           this.fileStr += "wget " + SRMFileTransformationUtils.gridftp2http(files[i]);
           if(i!=files.length-1) {
               this.fileStr += "\n";
           }
        }
    }

}
