package org.esgf.adminui;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.esgf.metadata.JSONArray;
import org.esgf.metadata.JSONException;
import org.esgf.metadata.JSONObject;
import org.esgf.metadata.XML;
import org.esgf.web.MetadataExtractorController;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * Implementation of metadata extraction controller responsible for extracting metadata that ARE NOT contained in the solr records.
 * The controller searches through the metdata file to find the proper record.  Currently parsing of TDS, OAI, CAS, and FGDC files are supported.
 *
 * @author john.harney
 *
 */
@Controller
@RequestMapping("/extractgroupdataproxy")
public class ExtractGroupInfoController {
    
    private final static Logger LOG = Logger.getLogger(ExtractGroupInfoController.class);
    
    //change me later
    private final static String USERS_FILE = "C:\\Users\\8xo\\esgProjects\\esgf-6-29\\esgf-web-fe\\esgf-web-fe\\src\\java\\main\\users.file";
    private final static String GROUPS_FILE = "C:\\Users\\8xo\\esgProjects\\esgf-6-29\\esgf-web-fe\\esgf-web-fe\\src\\java\\main\\groups.file";


    private final static boolean debugFlag = true;
    /**
     * Note: GET and POST contain the same functionality.
     *
     * @param  request  HttpServletRequest object containing the query string
     * @param  response  HttpServletResponse object containing the metadata in json format
     * @throws JDOMException 
     *
     */
    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody String doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, ParserConfigurationException, JDOMException {
        LOG.debug("ExtractGroupInfoController doGet");

      
        String type = request.getParameter("type");
        if(debugFlag)
            LOG.debug("Type: " + type);
        
        if(type.equalsIgnoreCase("edit")) {
            return processEditType(request, response);
        }
        else {
            return null;
        }
    }
    
    /**
     * Note: GET and POST contain the same functionality.
     *
     * @param  request  HttpServletRequest object containing the query string
     * @param  response  HttpServletResponse object containing the metadata in json format
     * @throws JDOMException 
     *
     */
    @RequestMapping(method=RequestMethod.POST)
    public @ResponseBody String doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, ParserConfigurationException, JDOMException {
        LOG.debug("ExtractUserInfoController doPost");

        String type = request.getParameter("type");
        if(debugFlag)
            LOG.debug("Type: " + type);
        
        if(type.equalsIgnoreCase("edit")) {
            return processEditType(request, response);
        }
        else {
            return null;
        }
    }
    
    /**
     * @param  request  HttpServletRequest object containing the query string
     * @param  response  HttpServletResponse object containing the metadata in json format
     * @throws JDOMException 
     *
     */
    private String processEditType(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, ParserConfigurationException, JDOMException {
        String jsonContent = "jsonContent";

        String userName = request.getParameter("id");
        
        if(debugFlag)
            queryStringInfo(request);
        
        /* Search through the data store to see if the id (username or openid) is there 
         * If it is there, then make the appropriate updates,
         * otherwise, just ignore
         * 
         * As of now, the returned data is in JSON format, so there needs to be some conversion between xml/db store to key/value json pairs
         */
        try {
            //xml store version
            String xmlOutput = getXMLTupleOutputFromEdit(userName);
            
            //db version
            
            
            JSONObject jo = XML.toJSONObject(xmlOutput);

            jsonContent = jo.toString();
            
        }catch(Exception e) {
            LOG.debug("Problem with conversion to json content in processEditType");
        }
        
        if(debugFlag)
            LOG.debug("JsonContent: " + jsonContent);
        
        return jsonContent;
    }
    
    
    
    /* get the user info for the give userName from the xml store */
    private String getXMLTupleOutputFromEdit(String userName) throws JDOMException, IOException {
        
        String xmlOutput = "";
        
        final File file = new File(USERS_FILE);
        SAXBuilder builder = new SAXBuilder();
        
        Document document = (Document) builder.build(file);
        
        Element rootNode = document.getRootElement();
        
        if(debugFlag)   
            LOG.debug("root name: " + rootNode.getName());
        
        Element returnedEl = null;
        
        List users = (List)rootNode.getChildren();
        for(int i=0;i<users.size();i++)
        {
            Element userEl = (Element) users.get(i);
            Element userNameEl = userEl.getChild("userName");
            if(debugFlag)
                LOG.debug("USERNAME: " + userNameEl.getTextNormalize());
            if(userNameEl.getTextNormalize().contains(userName)) {
                if(debugFlag)
                    LOG.debug("found--->: " + userName);
                returnedEl = getGroupReturnedEl(userEl);
                //returnedEl = userEl;
                
            }
        }
       
        XMLOutputter outputter = new XMLOutputter();
        xmlOutput = outputter.outputString(returnedEl);
        
        return xmlOutput;
    }
    
    /*
     * Helper funciton that queries the group data base given the user name
     */
    private Element getGroupReturnedEl(Element userEl) {
        
        Element returnedEl = new Element("groups");
        Element userNameEl = new Element("userName");
        String userName = userEl.getChildText("userName");
        userNameEl.setText(userName);
        returnedEl.addContent(userNameEl);
        
        Element usersfile_groupsEl = userEl.getChild("groups");
        
        List usersfile_groups = usersfile_groupsEl.getChildren("group");
        
        for(int i=0;i<usersfile_groups.size();i++) {
            Element usersfile_groupEl = (Element)usersfile_groups.get(i);
            
            String groupName = usersfile_groupEl.getChild("name").getTextNormalize();
            
            try {
                
                final File file = new File(GROUPS_FILE);
                SAXBuilder builder = new SAXBuilder();
            
                
                Document document = (Document) builder.build(file);
            
                Element rootNode = document.getRootElement();
                
                //go through all the groups in the group "table"
                List groupsfile_groups = (List)rootNode.getChildren();
                for(int j=0;j<groupsfile_groups.size();j++)
                {
                    
                    Element groupsfile_groupEl = (Element)groupsfile_groups.get(j);
                    LOG.debug("userfile_GroupName: " + groupName + " groupfile_GroupName: " + groupsfile_groupEl.getChild("name").getTextNormalize());
                    //if the groupName == the groupsfile name
                    if(groupsfile_groupEl.getChild("name").getTextNormalize().equals(groupName)) {
                        LOG.debug("\tAdd this");
                        Element name = new Element("name");
                        name.addContent(groupName);
                        LOG.debug("\t\tName-> " + name.getTextNormalize());
                        returnedEl.addContent(name);
                        //add to returned result
                    }
                }
                
            }catch(Exception e) {
                LOG.debug("Problem with Groups file");
            }
        }
        
        LOG.debug("RETURNED: " + returnedEl.getContent());
        
        return returnedEl;
    }
    
   
    
    /**
     * queryStringInfo(HttpServletRequest request)
     * Private method that prints out the contents of the request.  Used mainly for debugging.
     * 
     * @param request
     */
    private void queryStringInfo(HttpServletRequest request) {
        LOG.debug("--------Query String Info--------");
        Enumeration<String> paramEnum = request.getParameterNames();
        
        while(paramEnum.hasMoreElements()) { 
            String postContent = (String) paramEnum.nextElement();
            LOG.debug(postContent+"-->"); 
            LOG.debug(request.getParameter(postContent));
        }
        LOG.debug("--------End Query String Info--------");
    }
}


