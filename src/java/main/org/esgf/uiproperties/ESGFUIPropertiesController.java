package org.esgf.uiproperties;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import org.esgf.metadata.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


//@Controller
//@RequestMapping(value="/uiproperties")
public class ESGFUIPropertiesController {
    
    
    private final static String FACET_PROPERTIES_FILE = "/esg/config/facets.properties";
    private final static String FACET_PRACTICE_FILE = "/esg/config/facets_prac.properties";
    
    
    private final static String FACET_SHORT_NAMES = "Facet_Short_Names";
    private final static String FACET_LONG_NAMES = "Facet_Long_Names";
    private final static String FACET_DESCRIPTIONS = "Facet_Descriptions";
    
    
  
    
   

    
    
    
}
