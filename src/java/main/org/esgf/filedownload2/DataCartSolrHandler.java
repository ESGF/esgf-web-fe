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
import org.esgf.metadata.JSONArray;
import org.esgf.metadata.JSONException;
import org.esgf.metadata.JSONObject;
import org.esgf.metadata.XML;


public class DataCartSolrHandler {

    private static String searchAPIURL = "http://localhost/esg-search/search?";
    private static String queryPrefix = "format=application%2Fsolr%2Bjson&type=File";
    private final static Logger LOG = Logger.getLogger(DataCartSolrHandler.class);
    
    private static int initialLimit = 10;
    
    private String peerStr;
    private String showAllStr;
    private String initialQuery;
    
    //private JSONObject solrResponse;
    
    private String solrQueryString;
    
    private String [] fq;
    private String [] id;
    //private String technoteStr;
    
    
    public DataCartSolrHandler() {
        
    }
    
    public DataCartSolrHandler(String peerStr,String showAllStr,String initialQuery,String [] fq,String [] id) {
        this.peerStr = peerStr;
        this.showAllStr = showAllStr;
        this.initialQuery = initialQuery;
        
        this.solrQueryString = null;
        
        if(fq != null) {
            for(int i=0;i<fq.length;i++) {
                this.fq[i] = fq[i];
            }
        }
        
        if(id != null) {
            for(int i=0;i<id.length;i++) {
                this.id[i] = id[i];
            }
        }
        
        
    }
    
    public void preassembleQueryString() {
        
        this.solrQueryString = queryPrefix;
        
      //put any search criteria if the user selected "filter"
        if(this.showAllStr.equals("false")) {
        
            String fullText = "";
            if(this.fq != null) {
                for(int i=0;i<this.fq.length;i++) {
                    String fqParam = this.fq[i];
                    
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
        
        docElement.setDatasetId(datasetId);
        
        List<FileElement2> fileElements = getFileElements(datasetId);

        docElement.setFileElements(fileElements);
        
        //create a count element
        docElement.setCount(fileElements.size());
        
        for(int i=0;i<fileElements.size();i++) {
            FileElement2 fileElement = fileElements.get(i);
            
            if(fileElement.getHasHttp().equals("true")){
                docElement.setHasHttp("true");
            }
            if(fileElement.getHasOpenDap().equals("true")) {
                docElement.setHasOpenDap("true");
            }
            if(fileElement.getHasGrid().equals("true")) {
                docElement.setHasGridFTP("true");
            }
            if(fileElement.getHasSRM().equals("true")) {
                docElement.setHasSRM("true");
            }
        }
        
        return docElement;
    }
    
    private List<FileElement2> getFileElements(String dataset_id) {
    
        List<FileElement2> fileElements = new ArrayList<FileElement2>();
        
      //raw response from solr of files matching the query for dataset_id
        String solrResponse = querySolrForFiles(dataset_id);

        //convert to JSON array
        JSONArray files = solrResponseToJSON(solrResponse);
        
        for(int j=0;j<files.length();j++) {
            
            try {
                //grab the JSON object
                JSONObject docJSON = new JSONObject(files.get(j).toString());

                FileElement2 fileElement = new FileElement2();
                fileElement.fromSolr(docJSON);
                
                fileElements.add(fileElement);
                
                //create a new FileElement from the JSONObject
                //FileElement fileElement = new FileElement(docJSON,"solr");
                //System.out.println("\ttracking id" + fileElement.getTrackingId());
                //fileElements.add(fileElement);
                
            } catch(Exception e) {
                System.out.println("Problem assembling files");
            }
        }

        
        //if(getFileElementsDebug) {
        //    System.out.println("---End Get File Elements---");
        //}

        return fileElements;
    }
    
    private String querySolrForFiles(String dataset_id) {
     
        String marker = "\"response\":";
        
        String responseBody = null;
        
        // create an http client
        HttpClient client = new HttpClient();

        //attact the dataset id to the query string
        GetMethod method = new GetMethod(searchAPIURL);
        
        //dataset_id = dataset_id.replace("|", "%7C");
        //if(querySolrForFilesDebug) {
        //    System.out.println("\n\n\tQueryString issued to solr (before encoding) -> " + (this.solrQueryString+"&dataset_id=" + dataset_id));
        //}
        
        //add the dataset to the query string
        try {
            this.solrQueryString += "&dataset_id=" + URLEncoder.encode(dataset_id,"UTF-8").toString();
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        //if(querySolrForFilesDebug) {
        //    System.out.println("\tQueryString issued to solr -> " + queryString + "\n\n");
        //}
        
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
        
        ///if(solrResponseDebug) {
        //    System.out.println("\tResponse:\n\t" + responseString);
        //}
        
        //if(querySolrForFilesDebug) {
        //    System.out.println("---End Query Solr for Files---");
        //}

        return responseString;
        
    }
    
    /**
     * 
     * @param rawString
     * @return
     */
    private static JSONArray solrResponseToJSON(String rawString) {
        //if(solrResponseToJSONDebug) {
        //    System.out.println("---SolrResponseToJSON---");
        //}
        
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
        
        //if(solrResponseToJSONDebug) {
        //    System.out.println("---End SolrResponseToJSON---");
        //}
        return jsonArray;
    }
    
    
    public static void main(String [] args) {

        String peerStr = "";
        String showAllStr = "";
        String initialQuery = "";
        
        
        
        //DataCartSolrHandler handler = new DataCartSolrHandler(peerStr,showAllStr,initialQuery);
        
        
    }
    
}
