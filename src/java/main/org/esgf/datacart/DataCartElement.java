package org.esgf.datacart;

import org.esgf.metadata.JSONObject;
import org.jdom.Element;

public interface DataCartElement {

    public Element toElement();
    

    /** Description of toXML()
     * 
     * @return
     */
    public String toXML();

    /** Description of toJSONObject()
     * 
     * @return
     */
    public JSONObject toJSONObject();
    
    
    public String toJSON();
    
    
    
    /** Description of toFile()
     * 
     * @param file Filename of the output
     */
    public void toFile(String file);
    
    
    public void fromFile(String file);
    
}
