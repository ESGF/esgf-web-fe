package org.esgf.srm.scriptgen;

import java.util.HashMap;
import java.util.Map;

import org.esgf.filetransformer.SRMFileTransformation;

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
           this.fileStr += "wget " + SRMFileTransformation.gridftp2http(files[i]) + "\n";
           
        }
    }

}
