package org.esgf.datacart;

import java.util.HashMap;
import java.util.Map;

public class DatacartSessionObj {

    private Map<String,String> datacartMap;
    
    public DatacartSessionObj(Map<String,String> datacartMap) {
        this.setDatacartMap(datacartMap);
    }
    
    public DatacartSessionObj() {
        this.setDatacartMap(new HashMap<String,String>());
    }

    /**
     * @return the datacartMap
     */
    public Map<String,String> getDatacartMap() {
        return datacartMap;
    }

    /**
     * @param datacartMap the datacartMap to set
     */
    public void setDatacartMap(Map<String,String> datacartMap) {
        this.datacartMap = datacartMap;
    }
    
}
