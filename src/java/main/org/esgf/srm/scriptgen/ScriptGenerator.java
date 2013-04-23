package org.esgf.srm.scriptgen;
import java.util.Map;


public abstract class ScriptGenerator {

    private String name;
    private String type;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public abstract String generateScript();
    
    public abstract Map<String,String> populateTemplateTagMap();
    
    
    
}
