package org.esgf.filedownload2;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.http.HTTPException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.esgf.filedownload.DocElement;
import org.esgf.filedownload.FileDownloadTemplateController;
import org.esgf.filedownload.FileElement;
import org.esgf.filedownload.XmlFormatter;
import org.esgf.metadata.JSONArray;
import org.esgf.metadata.JSONException;
import org.esgf.metadata.JSONObject;
import org.esgf.metadata.XML;


public class DataCartSolrHandler {

    private static String searchAPIURL = "http://localhost:8081/esg-search/search?";
    private static String queryPrefix = "format=application%2Fsolr%2Bjson&type=File";
    private final static Logger LOG = Logger.getLogger(DataCartSolrHandler.class);
    
    private static int initialLimit = 10;
    
    private String peerStr;
    private String showAllStr;
    private String [] fq;
    //private String technoteStr;
    
    
    private String solrQueryString;
    
    public static String getSearchAPIURL() {
        return searchAPIURL;
    }

    public static void setSearchAPIURL(String searchAPIURL) {
        DataCartSolrHandler.searchAPIURL = searchAPIURL;
    }

    public static String getQueryPrefix() {
        return queryPrefix;
    }

    public static void setQueryPrefix(String queryPrefix) {
        DataCartSolrHandler.queryPrefix = queryPrefix;
    }

    public String getPeerStr() {
        return peerStr;
    }

    public void setPeerStr(String peerStr) {
        this.peerStr = peerStr;
    }

    public String getShowAllStr() {
        return showAllStr;
    }

    public void setShowAllStr(String showAllStr) {
        this.showAllStr = showAllStr;
    }


    public String getSolrQueryString() {
        return solrQueryString;
    }

    public void setSolrQueryString(String solrQueryString) {
        this.solrQueryString = solrQueryString;
    }

    public String[] getFq() {
        return fq;
    }

    public void setFq(String[] fq) {
        this.fq = fq;
    }

    
    public DataCartSolrHandler() {
        
    }
    
    public DataCartSolrHandler(String peerStr,String showAllStr,String [] fq) {
        this.peerStr = peerStr;
        this.showAllStr = showAllStr;
        
        this.solrQueryString = null;
        
        if(fq != null) {
            this.fq = new String[fq.length];
            for(int i=0;i<fq.length;i++) {
                this.fq[i] = fq[i];
            }
        }
        this.preassembleQueryString();
    }
    
    private void preassembleQueryString() {
        
        System.out.println("In preassemble query string");
        
        this.solrQueryString = queryPrefix;
        
        if(this.showAllStr == null) {
            System.out.println("NULL");
        }
        
        
      //put any search criteria if the user selected "filter"
        if(this.showAllStr.equals("false")) {
        
            String fullText = "";
            if(this.fq != null) {
                for(int i=0;i<this.fq.length;i++) {
                    String fqParam = this.fq[i];
                    System.out.println("\t" + fqParam);
                    /*
                     * Should ignore the following:
                     * - blanks - ""
                     * - replica - "replica=..."
                     * - offset - "offset=..."
                     */
                    boolean ignore = fqParam.equals("") || 
                                     fqParam.equals(" ") ||
                                     fqParam.contains("offset=") ||
                                     fqParam.contains("replica=");
                    
                    //otherwise add to the query
                    if(!ignore) {
                        if(!fqParam.contains("query")) {
                            this.solrQueryString += "&" + fqParam;
                        } 
                        //full text queries have to be handled differently
                        else {
                            String [] clause = fqParam.split("=");
                            fullText += clause[1] + "%20";
                        }
                    }
                    
                }//end for
                if(!fullText.equals("") && !fullText.equals(" ")) {
                    this.solrQueryString += "&query=" + fullText;
                }
                
            }
            
        }
    }
    
   
    
    public DocElement2 getDocElement2(String datasetId) {
        
        
        DocElement2 docElement = new DocElement2();
        
        //set the dataset id
        docElement.setDatasetId(datasetId);
        
        //get the file elements and set it
        List<FileElement2> fileElements = getFileElements(datasetId);

        //set the file elements
        docElement.setFileElements(fileElements);
        
        //create a count element
        docElement.setCount(fileElements.size());
        
        //set the booleans
        docElement.setHasGridFTP("false");
        docElement.setHasHttp("false");
        docElement.setHasOpenDap("false");
        docElement.setHasSRM("false");
        for(int i=0;i<docElement.getFileElements().size();i++) {
            FileElement2 fe = docElement.getFileElements().get(i);
            if(fe.getHasGrid().equals("true")) {
                docElement.setHasGridFTP("true");
            }
            if(fe.getHasHttp().equals("true")) {
                docElement.setHasHttp("true");
            }
            if(fe.getHasOpenDap().equals("true")) {
                docElement.setHasOpenDap("true");
            }
            if(fe.getHasSRM().equals("true")) {
                docElement.setHasSRM("true");
            }
        }
        
        
        return docElement;
    }
    
    private List<FileElement2> getFileElements(String dataset_id) {
     
        List<FileElement2> fileElements = new ArrayList<FileElement2>();
        
        JSONArray responseFiles = queryIndex(dataset_id);
        
        for(int i=0;i<responseFiles.length();i++) {
            
            JSONObject file = null;
            try {
                file = responseFiles.getJSONObject(i);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            FileElement2 fileElement = new FileElement2();
            fileElement.fromSolr(file);
            
            
            fileElements.add(fileElement);
            
        }    
        
        
        return fileElements;
    }
    
    
    public JSONArray queryIndex(String dataset_id) {
        
        String rawResponse = this.querySolrForFiles(dataset_id);
        
        
        JSONArray jsonResponse = this.solrResponseToJSON(rawResponse);
        
        return jsonResponse;
        
    }
    
    private String querySolrForFiles(String dataset_id) {
     
        String marker = "\"response\":";
        
        String responseBody = null;
        
        // create an http client
        HttpClient client = new HttpClient();

        //attact the dataset id to the query string
        GetMethod method = new GetMethod(searchAPIURL);
        
        //add the dataset to the query string
        try {
            this.solrQueryString += "&dataset_id=" + URLEncoder.encode(dataset_id,"UTF-8").toString();
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        method.setQueryString(this.solrQueryString);
        
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));
        
        
        
        try {
            // execute the method
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                    LOG.error("Method failed: " + method.getStatusLine());

            }

            // read the response
            responseBody = method.getResponseBodyAsString();
        } catch (HTTPException e) {
            LOG.error("Fatal protocol violation");
            e.printStackTrace();
        } catch (IOException e) {
            LOG.error("Fatal transport error");
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }


        //just get the important part of the response (i.e. leave off the header and the facet info)
        int start = responseBody.lastIndexOf(marker) + marker.length();
        int end = responseBody.length();
        String responseString = responseBody.substring(start,end);
        

        return responseString;
        
    }
    
    /**
     * 
     * @param rawString
     * @return
     */
    private JSONArray solrResponseToJSON(String rawString) {
        
        //convert extracted string into json array
        JSONObject jsonResponse = null;
        JSONArray jsonArray = null;
        try {
            jsonResponse = new JSONObject(rawString);
            jsonArray = jsonResponse.getJSONArray("docs");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            System.out.println("Problem converting Solr response to json string");
            e.printStackTrace();
        }
        
        return jsonArray;
    }
    
    
    public static void main(String [] args) {

        String showAllStr = "false";
        String peerStr = "esg-datanode.jpl.nasa.gov";
        
        String [] id = {"obs4MIPs.NASA-JPL.AIRS.mon.v1|esg-datanode.jpl.nasa.gov"};

        //this is the string that I will get
        String fqStr = ",offset=0,replica=false";
        //need to convert to fq []
        
        String [] fq = fqStr.split(",");

        
        DataCartSolrHandler handler = new DataCartSolrHandler(peerStr,showAllStr,fq);

        //System.out.println("ShowAllStr: " + showAllStr);
        //System.out.println("PeerStr: " + handler.getPeerStr());
        //System.out.println("Initial Query: " + handler.getInitialQuery());
        
        
        
        System.out.println("QueryString: " + handler.getSolrQueryString());
        
        DocElement2 doc = handler.getDocElement2(id[0]);
        

        System.out.println("\tDatasetId: " + doc.getDatasetId());
        System.out.println("\tDatasetCount: " + doc.getCount());
         
        System.out.println(new XmlFormatter().format(doc.toXML()));
        
    }
    
}


