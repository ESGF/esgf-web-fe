package org.esgf.propertiesreader;

import java.util.Properties;

public abstract class PropertiesReader {

    protected String name;
    protected Properties prop;
    
    abstract public String getValue(String key);
    
    public abstract void printProps();
    
    public abstract void setProp(Properties prop);
    
    public abstract Properties getProp();
    
    
}
