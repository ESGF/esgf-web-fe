package org.esgf.srm;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
import org.esgf.datacart.DocElement;
import org.esgf.datacart.FileDownloadTemplateController;
import org.esgf.datacart.URLSElement;
import org.esgf.email.Email;
import org.esgf.email.Attachment;
import org.esgf.scriptgenerator.ScriptGenerator;
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

    private static final String DEFAULT_TYPE = "Dataset";

    private static boolean debugFlag = true;
    
    private static boolean printIDsFlag = false; 
    
    private static boolean isProduction = true;
    
    private Email initialEmail;
    private Email confirmationEmail;
    private SRMResponse srm_response;
    
    
    public static void main(String [] args) {
        
        final MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        
        
        /*
        SRMResponse srm_response = new SRMResponse();
        srm_response.fromFile("srm_response.xml");
        
        System.out.println(srm_response.toXML());
        
        String xml = srm_response.toXML();
        
        SRMResponse srm_response2 = new SRMResponse();
        srm_response2.fromXML(xml);
        
        System.out.println("message: " + srm_response2.getMessage());
        for(int i=0;i<srm_response2.getResponse_urls().length;i++) {
            System.out.println(srm_response2.getResponse_urls()[i]);
        }
        */
        
        
        
        mockRequest.addParameter("file_id", SRMProxyControllerConstants.INPUT_FILE_ID);
        mockRequest.addParameter("file_url", SRMProxyControllerConstants.INPUT_FILE_URL);
        mockRequest.addParameter("dataset_id", SRMProxyControllerConstants.INPUT_DATASET_ID);
        mockRequest.addParameter("filtered", SRMProxyControllerConstants.INPUT_FILTERED);
        mockRequest.addParameter("type", SRMProxyControllerConstants.INPUT_TYPE);
        mockRequest.addParameter("peerStr", SRMProxyControllerConstants.INPUT_PEERSTR);
        mockRequest.addParameter("technoteStr", SRMProxyControllerConstants.INPUT_TECHNOTESTR);
        mockRequest.addParameter("fqParamStr", SRMProxyControllerConstants.INPUT_FQPARAMSTR);
        mockRequest.addParameter("initialQuery", SRMProxyControllerConstants.INPUT_INITIALQUERY);
        mockRequest.addParameter("fileCounter", SRMProxyControllerConstants.INPUT_FILE_COUNTER);
        mockRequest.addParameter("openid", SRMProxyControllerConstants.INPUT_OPENID);
        
        
        //what are the inputs?
        //- all inputs required for solr
        //- the "type" (Database/File)
        //- the openid
        //- the return type
        
        
        SRMProxyController fc = new SRMProxyController();
        
        String response = fc.doPost(mockRequest, null);
        
        SRMResponse srm_response = new SRMResponse();
        srm_response.fromXML(response);
        
        System.out.println("resp: " + srm_response.getMessage());
    }
    
    @RequestMapping(method=RequestMethod.POST, value="/srmproxy")
    //public ModelAndView addEmployee(@RequestBody String body) {
    public @ResponseBody String doPost(HttpServletRequest request,final HttpServletResponse response) {
        
        System.out.println("In ESGF-WEB-FE SRMProxyController. HTTP POST: doPost");
        String responseStr = "";
        
        
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
        /*
        this.writeInitialEmail(file_urls,toEmailAddr);
        if(isProduction) {
            this.initialEmail.sendEmail();
        } else {
            //System.out.println(this.initialEmail);
        }
        */
        
        
        this.srm_response = new SRMResponse();
        
        responseStr = queryESGSRM(file_urls);
        
        this.srm_response.fromXML(responseStr);
        
        System.out.println("HERE IS THE RESPONSE STRING\n" + this.srm_response.toXML());
        
        
        String [] response_urls = this.srm_response.getResponse_urls();
        
        //write confirmation email
        this.writeConfirmationEmail(returnType, openid, response_urls, checksums, checksumTypes, toEmailAddr);

        System.out.println("Sending email...\n");
        if(isProduction) {
            System.out.println(this.confirmationEmail);
            this.confirmationEmail.sendEmail();
        } else {
            System.out.println(this.confirmationEmail);
        }
        System.out.println("End Sending email...\n");
        responseStr = this.srm_response.toXML();
        
        System.out.println("HERE IS THE RESPONSE STRING\n" + responseStr);
        
        //responseStr = "<a>a</a>";
        return responseStr;
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
        
        System.out.println("\nIn queryESGSRM\n");
        
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
    
        System.out.println("\nEnd queryESGSRM\n");
        
        
        return responseBody;
    }
    
    
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
        attachment1.setAttachmentName("wget.sh");
        attachment1.setAttachmentContent("New wget content");
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




