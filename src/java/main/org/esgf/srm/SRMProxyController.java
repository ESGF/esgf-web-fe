package org.esgf.srm;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.http.HTTPException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.esgf.email.Email;
import org.esgf.email.Attachment;
import org.esgf.scriptgenerator.ScriptGenerator;
import org.esgf.solr.model.Solr;
import org.esgf.solr.model.SolrRecord;
import org.esgf.solr.model.SolrResponse;
import org.esgf.util.XmlFormatter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import esg.common.util.ESGFProperties;
import esg.node.security.UserInfo;
import esg.node.security.UserInfoCredentialedDAO;

// this is a turl -> gsiftp://esg2-sdnl1.ccs.ornl.gov//lustre/esgfs/SRM/shared/V.0.0-505553807/t341f02.FAMIPr.cam2.h0.1978-09.nc

@Controller
public class SRMProxyController {
    
    private static String MAX_LIMIT = "9999";

    private static final String DEFAULT_TYPE = "Dataset";
    
    private static int NUM_FILES_LIMIT = 6;

    private static boolean debugFlag = false;
    
    private static boolean printIDsFlag = false; 
    
    private static boolean isProduction = false;
    
    private static boolean cacheOn = false;
    
    private Email initialEmail;
    private Email confirmationEmail;
    private SRMResponse srm_response;
    
    
    public static void main(String [] args) {
        
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        
       
        
        //what are the inputs?
        //- all inputs required for solr
        //- the "type" (Database/File)
        //- the openid
        //- the return type
        
        //scenario for dataset
        mockRequest.addParameter("dataset_id", SRMProxyControllerConstants.INPUT_DATASET_ID);
        mockRequest.addParameter("constraints", SRMProxyControllerConstants.INPUT_CONSTRAINTS);
        mockRequest.addParameter("file_id", SRMProxyControllerConstants.INPUT_DATASET_FILE_ID);
        mockRequest.addParameter("file_url", SRMProxyControllerConstants.INPUT_DATASET_FILE_URL);
        mockRequest.addParameter("open_id", SRMProxyControllerConstants.INPUT_OPEN_ID);
        mockRequest.addParameter("type", "File");
        
        System.out.println(mockRequest.getParameter("type"));
        //scenario for file
        
        SRMProxyController fc = new SRMProxyController();
        
        String response = fc.doPost(mockRequest, null);
        
        /*
        SRMResponse srm_response = new SRMResponse();
        srm_response.fromXML(response);
        
        System.out.println("resp: " + srm_response.getMessage());
        */
    }
    
    @RequestMapping(method=RequestMethod.POST, value="/srmproxy")
    //public ModelAndView addEmployee(@RequestBody String body) {
    public @ResponseBody String doPost(HttpServletRequest request,final HttpServletResponse response) {
        
        Enumeration<String> paramEnum = request.getParameterNames();
        
        while(paramEnum.hasMoreElements()) { 
            String postContent = (String) paramEnum.nextElement();
            System.out.println(postContent+"-->"); 
            System.out.println(request.getParameter(postContent));
        }   
        
        System.out.println("In ESGF-WEB-FE SRMProxyController. HTTP POST: doPost");
        
        String responseStr = "";
        
        //type
        String type = request.getParameter("type");
        if(type == null) {
            type = SRMProxyControllerConstants.INPUT_TYPE_FILE;
        }
                
        System.out.println("TYPE: " + type + "\n\n\n") ;
        if(type.equals("Dataset")) {
            
            
            String dataset_id = request.getParameter("dataset_id");
            if(dataset_id == null) {
                dataset_id = "ornl.ultrahighres.CESM1.t341f02.FAMIPr.v1|esg2-sdnl1.ccs.ornl.gov";
            }
            
            
            
            //constraints/query
            String constraints = request.getParameter("constraints");
            String query = "*";
            if(constraints != null) {
                System.out.println("Constraints: " + constraints);
                if(constraints.contains("query=")) {
                    String [] facets = constraints.split(";");
                    for(int i=0;i<facets.length;i++) {
                        if(facets[i].contains("query=")) {
                            String queryValue = facets[i].split("=")[1];
                            query = queryValue;
                        }
                    }
                    
                }
            }
            
            
            
            
            
            
            //query solr and get the responses
            SolrResponse solrResponse = querySolr(query,dataset_id);
            
            
            int numFiles = NUM_FILES_LIMIT;
            
            if(numFiles > solrResponse.getSolrRecords().size()) {
                numFiles = solrResponse.getSolrRecords().size();
            }
            
            if(numFiles != 0) {
                
                String [] file_urls = new String[numFiles];
                
                String [] file_ids = new String[numFiles];
                
                
                
                //for(int i=0;i<solrResponse.getSolrRecords().size();i++) {
                for(int i=0;i<numFiles;i++) {
                    SolrRecord solrRecord = solrResponse.getSolrRecords().get(i);
                    String solr_record_url = solrRecord.getArrField("url").get(0);
                    
                    //System.out.println("srm_url: " + solr_record_url);
                    
                    String url = solr_record_url.split("\\|")[0];
                    file_urls[i] = url;
                 
                    String file_id = solrRecord.getStrField("id");
                    file_ids[i] = file_id;
                }
                
                
                System.out.println("\nSending initial email...\n");
                String emailAddr = SRMProxyControllerConstants.DEFAULT_EMAIL_ADDR;
                writeInitialEmail(file_urls,emailAddr);
                
                if(isProduction) {
                    this.initialEmail.sendEmail();
                } else {
                    System.out.println(this.initialEmail.toString());
                }
                
                
                SRMEntryList srm_list = new SRMEntryList();
                
                if(cacheOn) {
                    System.out.println("Reading in entry list");
                    srm_list.fromFile("/esg/config/srm_entry_list_File.xml");
                    System.out.println("Current cache state for ids");
                    for(int i=0;i<file_ids.length;i++) {
                        System.out.println("file id: " + file_ids[i] + " " + srm_list.isCached(file_ids[i]));
                    }
                }
                
                
                
                //issue the request to the srm and get response
                this.srm_response = new SRMResponse();
                if(file_urls != null) {
                    responseStr = queryESGSRM(file_urls);
                }
                this.srm_response.fromXML(responseStr);

                
                
                
                
                //cache these file entries
                if(cacheOn) {
                    for(int i=0;i<file_ids.length;i++) {
                        srm_list.changeCached(file_ids[i], "true");
                    }
                    //System.out.println(new XmlFormatter().format(srm_list.toXML()));
                    srm_list.toFile("/esg/config/srm_entry_list_File.xml");
                }

                System.out.println(new XmlFormatter().format(srm_response.toXML()) + "\n");
                
                writeConfirmationEmail("http",null,file_urls,null,null,emailAddr);
                        
                System.out.println("\nSending confirmation email...\n");
                
                if(isProduction) {
                    this.confirmationEmail.sendEmail();
                } else {
                    System.out.println(this.confirmationEmail.toString());
                }
            } else {
                //there are no files
                
                
            }
            
            
            
            
        } else {
            
            System.out.println("In type file");
            
            //files workflow
            
            String [] file_urls = new String[1];
            
            String [] file_ids = new String[1];
            
            String file_url = request.getParameter("file_url");
            if(file_url == null) {
                file_url = SRMProxyControllerConstants.INPUT_FILE_FILE_URL;
            }
            
            file_urls[0] = file_url;
            
            String file_id = request.getParameter("file_id");
            if(file_id == null) {
                file_id = SRMProxyControllerConstants.INPUT_FILE_FILE_ID;
            }
            
            file_ids[0] = file_id;
            
            
            System.out.println("\nSending initial email...\n");
            
            SRMEntryList srm_list = new SRMEntryList();
            
            if(cacheOn) {
                System.out.println("Reading in entry list");
                srm_list.fromFile("/esg/config/srm_entry_list_File.xml");
                System.out.println("Current cache state for ids");
                for(int i=0;i<file_ids.length;i++) {
                    System.out.println("file id: " + file_ids[i] + " " + srm_list.isCached(file_ids[i]));
                }
            }
            
            //issue the request to the srm and get response
            this.srm_response = new SRMResponse();
            if(file_urls != null) {
                responseStr = queryESGSRM(file_urls);
            }
            this.srm_response.fromXML(responseStr);

            System.out.println(new XmlFormatter().format(srm_response.toXML()) + "\n");
            
            
            /*
            String emailAddr = SRMProxyControllerConstants.DEFAULT_EMAIL_ADDR;
            writeInitialEmail(file_urls,emailAddr);
            
            if(isProduction) {
                this.initialEmail.sendEma0il();
            } else {
                System.out.println(this.initialEmail.toString());
            }
            
            
            SRMEntryList srm_list = new SRMEntryList();
            
            if(cacheOn) {
                System.out.println("Reading in entry list");
                srm_list.fromFile("/esg/config/srm_entry_list_File.xml");
                System.out.println("Current cache state for ids");
                for(int i=0;i<file_ids.length;i++) {
                    System.out.println("file id: " + file_ids[i] + " " + srm_list.isCached(file_ids[i]));
                }
            }
            
            
            
            //issue the request to the srm and get response
            this.srm_response = new SRMResponse();
            if(file_urls != null) {
                responseStr = queryESGSRM(file_urls);
            }
            this.srm_response.fromXML(responseStr);

            
            
            
            
            //cache these file entries
            if(cacheOn) {
                for(int i=0;i<file_ids.length;i++) {
                    srm_list.changeCached(file_ids[i], "true");
                }
                //System.out.println(new XmlFormatter().format(srm_list.toXML()));
                srm_list.toFile("/esg/config/srm_entry_list_File.xml");
            }

            System.out.println(new XmlFormatter().format(srm_response.toXML()) + "\n");
            
            writeConfirmationEmail("http",null,file_urls,null,null,emailAddr);
                    
            System.out.println("\nSending confirmation email...\n");
            
            if(isProduction) {
                this.confirmationEmail.sendEmail();
            } else {
                System.out.println(this.confirmationEmail.toString());
            }
            */
        } //end if/else dataset or file
        
        

        
        
        
        
       
        //responseStr = "<a>a</a>";
        return "";
    }
    
    //requires 2 calls to solr
    //1 - Dataset core for the total count (optional)
    //2 - File core for the files
    private SolrResponse querySolr(String query,String dataset_id) {
    
        System.out.println("Querying solr...");
        
        Solr solr = new Solr();
        
        
        //get the total count first
        solr.addConstraint("query", query);
        solr.addConstraint("dataset_id",dataset_id);
        solr.addConstraint("type", "File");
        solr.addConstraint("limit", MAX_LIMIT);
        //to take out later
        solr.addConstraint("distrib", "false");
        
        solr.executeQuery();
        
        SolrResponse solrResponse = solr.getSolrResponse();
        
        System.out.println("Done Querying solr...");
        
        return solrResponse;
        
    }
    
    private static String getEmailAddrFromOpenId(String openid) {
        String emailAddr = null;
        
        
        UserInfoCredentialedDAO myUserInfoDAO;

        try{
            ESGFProperties myESGFProperties = new ESGFProperties();
            String passwd = myESGFProperties.getAdminPassword();   
            
            myUserInfoDAO = new UserInfoCredentialedDAO("rootAdmin",passwd,myESGFProperties);
            UserInfo userInfo = myUserInfoDAO.getUserById(openid);
            emailAddr = userInfo.getEmail();

            
        } catch(Exception e) {
            //e.printStackTrace();
            emailAddr = "jfharney@gmail.com";
        }
        
        
        return emailAddr;
    }
    
    private static String queryESGSRM(String [] file_urls) {
        
        System.out.println("\nIn queryESGSRM for size: " + file_urls.length + "\n");
        
        String response = null;
        String responseBody = null;
        
        // create an http client
        HttpClient client = new HttpClient();

        //attact the dataset id to the query string
        PostMethod method = new PostMethod(SRMUtils.srmAPIURL);
        String queryString = "";
        String unencodedQueryString = "";

        //add the urls
        for(int i=0;i<file_urls.length;i++) {

            System.out.println("file: " + file_urls[i]);
            if(i == 0 && file_urls.length == 1) {
                queryString += "url=";
                unencodedQueryString += "url=";
                queryString += encode(file_urls[i]);
                
            } 
            else if(i == 0 && file_urls.length != 1) {
                queryString += "url=";
                unencodedQueryString += "url=" + (file_urls[i]) + "&";
                queryString += encode(file_urls[i]) + "&";
            }
            else if(i == file_urls.length - 1) {
                queryString += "url=";
                unencodedQueryString += "url=" + (file_urls[i]);
                queryString += encode(file_urls[i]);
            } 
            else {
                queryString += "url=";
                unencodedQueryString += "url=" + (file_urls[i]) + "&";
                queryString += encode(file_urls[i]) + "&";
            }
        }

        queryString += "&length=" + file_urls.length;
        unencodedQueryString += "&length=" + file_urls.length;

        queryString += "&file_request_type=" + "http";
        
        
        if(debugFlag) {
            System.out.println("\tQuerystring-> " + queryString);
            System.out.println("\n");
            System.out.println("\tUnencodedQuerystring-> " + unencodedQueryString);
            System.out.println("\n");
        }
        
        method.setQueryString(queryString);
        
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));
        
        try {
            // execute the method
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
            }

            // read the response
            responseBody = method.getResponseBodyAsString();
        } catch (HTTPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
    
        
        
        return responseBody;
    }
    
    /*
    public static String [] getSolrParams(DocElement doc, String which) {
        String [] file_ids = new String[doc.getFileElements().size()];
        String [] file_urls = new String[doc.getFileElements().size()];
        String [] checksums = new String[doc.getFileElements().size()];
        String [] checksumTypes = new String[doc.getFileElements().size()];
        
        for(int i=0;i<doc.getFileElements().size();i++) {
            
            file_ids[i] = doc.getFileElements().get(i).getFileId();
            checksums[i] = doc.getFileElements().get(i).getChecksum();
            checksumTypes[i] = doc.getFileElements().get(i).getChecksum_type();
            URLSElement urlsElement = doc.getFileElements().get(i).getURLSElement();
            for(int j=0;j<urlsElement.getUrls().size();j++) {
                if(urlsElement.getUrls().get(j).contains("srm://")) { 
                    file_urls[i] = urlsElement.getUrls().get(j);
                    if(printIDsFlag) {
                       System.out.println("\t" + file_urls[i]);
                    } 
                    
                }
            }
        }
        
        if(which.equals("ids")) {
            return file_ids;
        } else if(which.equals("checksums")) {
            return checksums;
        } else if(which.equals("checksumTypes")) {
            return checksumTypes;
        } else {
            return file_urls;
        }
        
    }
    */
    
    
    
    public static String encode(String queryString) {
        
        try {
            queryString = URLEncoder.encode(queryString,"UTF-8").toString();
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        return queryString;
    }
    
    
    public void writeInitialEmail(String [] file_urls,String emailAddr) {
        this.initialEmail = new Email();
        this.initialEmail.setTo(emailAddr);
        Attachment attachment1 = new Attachment();
        //attachment1.setAttachmentName("wget.sh");
        //attachment1.setAttachmentContent("New wget content");
        this.initialEmail.setAttachment(attachment1);
        this.initialEmail.setHeaderText("Your request for data has been submitted.");
        
        String bodyStr = "";
        bodyStr += "Your data request for the following files:\n";
        for(int i=0;i<file_urls.length;i++) {
            bodyStr += "\t" + file_urls[i] + "\n";
        }
        bodyStr += "\nHas been submitted.  Please note that it may take some time to extract the data off tertiary storage and onto the local filesystem.  A confirmation email will be sent to this address soon with instructions on how to access this data.";
        this.initialEmail.setBodyText(bodyStr);
    }
    
    
    
    
    
    
    
    
    public void writeConfirmationEmail(String returnType,
                                       String openid,
                                       String [] file_urls,
                                       String [] checksums,
                                       String [] checksum,
                                       String emailAddr) {
        
        
        this.confirmationEmail = new Email();
        this.initialEmail.setTo(emailAddr);
        
        //assemble the header text here
        this.confirmationEmail.setHeaderText("Your request for data has been successfully staged!");
        
      //assemble the body text here
        String bodyStr = "";
        bodyStr += "Your request for data has been successfully staged!\n";
        bodyStr += "The data you requested was the following:\n";
        for(int i=0;i<file_urls.length;i++) {
            bodyStr += "\t" + file_urls[i] + "\n";
        }
        
        
        Attachment attachment = new Attachment();
        
        
        if(returnType.equals("http")) {
            bodyStr += "\nAttached is a wget get script that may be run on any shell.\n";
            
            attachment.setAttachmentName("wgetscript.sh");
            /*
            String wgetContent = "";
            for(int i=0;i<srm_response.getResponse_urls().length;i++) {
                wgetContent += "wget " + srm_response.getResponse_urls()[i] + "\n";
            }
            
            //change to http urls
            file_urls = SRMUtils.gridftp2httpArr(file_urls);
            
            //replace the cache names here
            file_urls = SRMUtils.replaceCacheNames(file_urls);
            
            
            ScriptGenerator scriptgenerator = new ScriptGenerator(file_urls,checksums,checksum);
            
            wgetContent = scriptgenerator.getRevisedTemplate();
            */
            String wgetContent = "";
            for(int i=0;i<srm_response.getResponse_urls().length;i++) {
                String revised_urls = SRMUtils.gridftp2http(srm_response.getResponse_urls()[i]);
                //wgetContent += "wget " + srm_response.getResponse_urls()[i] + "\n";
                wgetContent += "wget " + revised_urls + " ; \n";
                
            }
            attachment.setAttachmentContent(wgetContent);
            
        } else if(returnType.equals("gridftp")){
            
            
            
            
            
            
            
            
            
        } else if(returnType.equals("globusonline")){
            
            
            
            
            
            
            
        } else {
            bodyStr += "\nAttached is a wget get script that may be run on any shell.\n";
            
            attachment.setAttachmentName("wget.sh");
            String wgetContent = "";
            for(int i=0;i<srm_response.getResponse_urls().length;i++) {
                wgetContent += "wget " + srm_response.getResponse_urls()[i] + "\n";
            }
            
            ScriptGenerator scriptgenerator = new ScriptGenerator(file_urls,checksums,checksum);
            
            wgetContent = scriptgenerator.getRevisedTemplate();
            
            attachment.setAttachmentContent(wgetContent);
            
        }
        
        this.confirmationEmail.setBodyText(bodyStr);
        this.confirmationEmail.setAttachment(attachment);
        
       
    }
    
    
}



/*
public static DocElement querySolr(HttpServletRequest request) {
  //query solr first
    String filtered = request.getParameter("filtered");
    filtered = "false";

    
    String idStr = request.getParameter("dataset_id");
    if(idStr == null) {
        idStr = "null";
    }
    
   
    String peerStr = request.getParameter("peerStr");

    if(peerStr == null) {
        peerStr = "null";
    }
    
    //get the fq string and convert to an array of peers
    String fqStr = request.getParameter("fqParamStr");
    
    if(fqStr == null) {
        fqStr = "null";
    }
    String [] fq = fqStr.split(";");
    

    //get the search constraints togggle parameter
    String showAllStr = "true";
    if(showAllStr == null) {
        showAllStr = "null";
    }
    
    
    //get the flag denoting whether or not this is an initial Query
    String initialQuery = request.getParameter("initialQuery");
    if(initialQuery == null) {
        initialQuery = "null";
    }

    //get the fileCounter
    String fileCounter = request.getParameter("fileCounter");
    if(fileCounter == null) {
        fileCounter = "null";
    }
    
    System.out.println("--------");
    System.out.println("\tidStr\t" + idStr);
    System.out.println("\tfiltered\t" + filtered);
    System.out.println("\tpeerStr\t" + peerStr);
    System.out.println("\tfqStr\t" + fqStr);
    //System.out.println("\ttechnotesStr\t" + technotes);
    System.out.println("\tshowAllStr\t" + showAllStr);
    System.out.println("\tinitialQuery\t" + initialQuery);
    System.out.println("\tfileCount\t " + fileCounter + "\n");
    
    
    DocElement doc = FileDownloadTemplateController.getDocElement(idStr,peerStr,initialQuery,fq,showAllStr,fileCounter);
    
    return doc;
}
*/


/*
String [] file_ids = null;
String [] file_urls = null;
String [] checksums = null;
String [] checksumTypes = null;

String type = request.getParameter("type");
if(type == null) {
    type = "Dataset";
}

String openid = request.getParameter("openid");
if(openid == null) {
    openid = "https://esg.ccs.ornl.gov/esgf-idp/openid/jfharney";
}

String returnType = request.getParameter("returnType");
if(returnType == null) {
    returnType = "http";
} 


//get the file ids and file urls (either from solr or individual file)
if(type.equals("Dataset")) {
    
    DocElement doc = querySolr(request);
    file_ids = getSolrParams(doc,"ids");
    file_urls = getSolrParams(doc,"urls");
    checksums = getSolrParams(doc,"checksums");
    checksumTypes = getSolrParams(doc,"checksumTypes");
    
} else {

    String file_id = request.getParameter("file_id");
    String file_url = request.getParameter("file_url");
    
    file_ids = new String [1];
    file_urls = new String [1];
    
    file_ids[0] = file_id;
    file_urls[0] = file_url;
    
}


String toEmailAddr = getEmailAddrFromOpenId(openid);


//write initial email

//this.writeInitialEmail(file_urls,toEmailAddr);
//if(isProduction) {
//    this.initialEmail.sendEmail();
//} else {
//    //System.out.println(this.initialEmail);
//}



this.srm_response = new SRMResponse();

responseStr = queryESGSRM(file_urls);

this.srm_response.fromXML(responseStr);

String [] response_urls = this.srm_response.getResponse_urls();

//write confirmation email
this.writeConfirmationEmail(returnType, openid, response_urls, checksums, checksumTypes, toEmailAddr);

if(isProduction) {
    System.out.println(this.confirmationEmail);
    this.confirmationEmail.sendEmail();
} else {
    System.out.println(this.confirmationEmail);
}
System.out.println("End Sending email...\n");
responseStr = this.srm_response.toXML();
*/

