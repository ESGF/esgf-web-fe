package org.esgf.srm.scriptgen;

import java.util.Map;
import java.util.Map.Entry;

public class Utils {

    /**
     * This is almost a dummy method, although it works as desired. The
     * replacement should be in O(N), this method uses O(N*M) where M >=
     * #replacing tags
     * 
     * @param temp
     *            the template to use
     * @param tags
     *      a map<tag, value> that will be used for replacing all "{{tag}}" with "value"
     * @return the resulting script as a string
     */
    public static String replace(String temp, Map<String, String> tags) {
        
        //System.out.println("temp: " + temp);
        // incredibly slow but working O(tags.size()*temp.length())
        // potential speed up O(temp.length())
        for (Entry<String, String> e : tags.entrySet()) {
            temp = temp.replaceAll("\\{\\{" + e.getKey() + "\\}\\}",
                    e.getValue());
        }

        return temp;
    }
    
}
