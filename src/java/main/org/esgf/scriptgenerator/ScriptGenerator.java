package org.esgf.scriptgenerator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.esgf.srm.SRMUtils;

public class ScriptGenerator {

	public static String DEFAULT_MESSAGE = "message val: Script created for 14 file(s)\n(The count won't match if you manually edit this file!)";
	
	public static String DEFAULT_FILES = "'huss_day_inmcm4_1pctCO2_r1i1p1_20900101-20991231.nc' 'http://pcmdi9.llnl.gov/thredds/fileServer/cmip5_data/cmip5/output1/INM/inmcm4/1pctCO2/day/atmos/day/r1i1p1/huss/1/huss_day_inmcm4_1pctCO2_r1i1p1_20900101-20991231.nc' 'MD5' '7fcd959a4bb57e4079c8e65a7a5d0499'" +
								  "'huss_day_inmcm4_1pctCO2_r1i1p1_21000101-21091231.nc' 'http://pcmdi9.llnl.gov/thredds/fileServer/cmip5_data/cmip5/output1/INM/inmcm4/1pctCO2/day/atmos/day/r1i1p1/huss/1/huss_day_inmcm4_1pctCO2_r1i1p1_21000101-21091231.nc' 'MD5' '6f805cbee324d7151c95c752f5d8352e'" +
								  "'huss_day_inmcm4_1pctCO2_r1i1p1_21100101-21191231.nc' 'http://pcmdi9.llnl.gov/thredds/fileServer/cmip5_data/cmip5/output1/INM/inmcm4/1pctCO2/day/atmos/day/r1i1p1/huss/1/huss_day_inmcm4_1pctCO2_r1i1p1_21100101-21191231.nc' 'MD5' '1d1c6c867726eea0f5e7167d970895c1'";

	public static String DEFAULT_OPENID = "https://esg.ccs.ornl.gov/esgf-idp/openid/jfharney";
	
	public static String DEFAULT_HOSTNAME = "esg.ccs.ornl.gov";
	
	public static String DEFAULT_SEARCHURL = "http://localhost:8080/esg-search/wget/?shards=localhost:8983/solr&dataset_id=cmip5.output1.INM.inmcm4.1pctCO2.day.atmos.day.r1i1p1.v20110323|pcmdi9.llnl.gov";
	
	public static String DEFAULT_DATE = "2013/02/27 16:15:46";
	
	public static String WGET_TEMPLATE_FILE = "/esg/config/wget-template";
	
	
	private String files;
	private String message;
	private String hostname;
	private String searchURL;
	private String date;
	private String wgetTemplateFile;
	private String openid;
	
	
	public static void main(String [] args) {
		/*
		String template = getTemplate();
		
		Map<String,String> templateTagMap = populateTemplateTagMap();
		
		for(Object key : templateTagMap.keySet()) {
			System.out.println((String) key);
		}
		
		
		String newTemplate = replace(template,templateTagMap);
		
		System.out.println(newTemplate);
		*/
	}
	
	public ScriptGenerator() {
	    this.openid = DEFAULT_OPENID;
	    this.message = DEFAULT_MESSAGE;
	    this.files = DEFAULT_FILES;
	    this.hostname = DEFAULT_HOSTNAME;
	    this.searchURL = DEFAULT_SEARCHURL;
	    this.date = DEFAULT_DATE;
	    this.wgetTemplateFile = WGET_TEMPLATE_FILE;
	    
	}
	
	public ScriptGenerator(String [] files, String [] checksums, String [] checksumTypes) {

        this.openid = DEFAULT_OPENID;
        this.message = DEFAULT_MESSAGE;
        this.hostname = DEFAULT_HOSTNAME;
        this.searchURL = DEFAULT_SEARCHURL;
        this.date = DEFAULT_DATE;
        this.wgetTemplateFile = WGET_TEMPLATE_FILE;
        
        String [] fileNames = SRMUtils.extractFileNames(files);
        
        String fileStr = "";
        for(int i=0;i<files.length;i++) {
            String fileRow = "'" + files[i] + "' " +  
                             "'" + fileNames[i] + "' " +
                             "'" + checksums[i] + "' " +
                             "'" + checksumTypes[i] + "'" + "\n";
            fileStr += fileRow;
        }
        this.files = fileStr;
        
	}
	
	
	//these tags are in the template
	// - message
	// - files
	// - userOpenId
	// - hostName
	// - searchUrl
	// - date
	public Map<String,String> populateTemplateTagMap() {
		Map<String,String> templateTagMap = new HashMap<String,String>();
		
		//add the message
		templateTagMap.put("message", this.message);
		
		//add the files
		templateTagMap.put("files", this.files);
		
		//add the openid
		templateTagMap.put("userOpenId", this.openid);
		
		//add the hostname
		templateTagMap.put("hostName", this.hostname);
		
		//add the searchurl
		templateTagMap.put("searchUrl", this.searchURL);
		
		//add the date
		templateTagMap.put("date", this.date);
		
		return templateTagMap;
	}
	
	
	public static String getTemplate() {
		String template = null;
		
		try {
			FileInputStream fis = null;
			InputStreamReader reader = null;
			
			fis = new FileInputStream(WGET_TEMPLATE_FILE);

			reader = new InputStreamReader(fis, "UTF-8");
			
			StringBuilder sb = new StringBuilder();
			char[] buff = new char[1024];
			int read;
			
			while ((read = reader.read(buff)) == buff.length) {
				sb.append(buff);
			}
			sb.append(buff, 0, read);
			
			template = sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		

		return template;
		
	}
	
	public String getRevisedTemplate() {
	    String revisedTemplate = "";
	    
	    String template = getTemplate();
	    
	    Map<String,String> tagMap = populateTemplateTagMap();
	    
	    revisedTemplate = replace(template,tagMap);
	    
	    return revisedTemplate;
	}
	
	/**
	 * This is almost a dummy method, although it works as desired. The
	 * replacement should be in O(N), this method uses O(N*M) where M >=
	 * #replacing tags
	 * 
	 * @param temp
	 *            the template to use
	 * @param tags
	 * 		a map<tag, value> that will be used for replacing all "{{tag}}" with "value"
	 * @return the resulting script as a string
	 */
	static private String replace(String temp, Map<String, String> tags) {
		// incredibly slow but working O(tags.size()*temp.length())
		// potential speed up O(temp.length())
		for (Entry<String, String> e : tags.entrySet()) {
			System.out.println("\t" + e.getKey());
			temp = temp.replaceAll("\\{\\{" + e.getKey() + "\\}\\}",
					e.getValue());
		}
		return temp;
	}

}

/*
 * key: message val: Script created for 14 file(s)
(The count won't match if you manually edit this file!)


key: files val: 'huss_day_inmcm4_1pctCO2_r1i1p1_20900101-20991231.nc' 'http://pcmdi9.llnl.gov/thredds/fileServer/cmip5_data/cmip5/output1/INM/inmcm4/1pctCO2/day/atmos/day/r1i1p1/huss/1/huss_day_inmcm4_1pctCO2_r1i1p1_20900101-20991231.nc' 'MD5' '7fcd959a4bb57e4079c8e65a7a5d0499'
'huss_day_inmcm4_1pctCO2_r1i1p1_21000101-21091231.nc' 'http://pcmdi9.llnl.gov/thredds/fileServer/cmip5_data/cmip5/output1/INM/inmcm4/1pctCO2/day/atmos/day/r1i1p1/huss/1/huss_day_inmcm4_1pctCO2_r1i1p1_21000101-21091231.nc' 'MD5' '6f805cbee324d7151c95c752f5d8352e'
'huss_day_inmcm4_1pctCO2_r1i1p1_21100101-21191231.nc' 'http://pcmdi9.llnl.gov/thredds/fileServer/cmip5_data/cmip5/output1/INM/inmcm4/1pctCO2/day/atmos/day/r1i1p1/huss/1/huss_day_inmcm4_1pctCO2_r1i1p1_21100101-21191231.nc' 'MD5' '1d1c6c867726eea0f5e7167d970895c1'
'huss_day_inmcm4_1pctCO2_r1i1p1_21200101-21291231.nc' 'http://pcmdi9.llnl.gov/thredds/fileServer/cmip5_data/cmip5/output1/INM/inmcm4/1pctCO2/day/atmos/day/r1i1p1/huss/1/huss_day_inmcm4_1pctCO2_r1i1p1_21200101-21291231.nc' 'MD5' '35583573a291c449d2fb9292e551c042'
'huss_day_inmcm4_1pctCO2_r1i1p1_21300101-21391231.nc' 'http://pcmdi9.llnl.gov/thredds/fileServer/cmip5_data/cmip5/output1/INM/inmcm4/1pctCO2/day/atmos/day/r1i1p1/huss/1/huss_day_inmcm4_1pctCO2_r1i1p1_21300101-21391231.nc' 'MD5' '4faaeeaf8c3f8dabd8aba92faf042e02'
'huss_day_inmcm4_1pctCO2_r1i1p1_21400101-21491231.nc' 'http://pcmdi9.llnl.gov/thredds/fileServer/cmip5_data/cmip5/output1/INM/inmcm4/1pctCO2/day/atmos/day/r1i1p1/huss/1/huss_day_inmcm4_1pctCO2_r1i1p1_21400101-21491231.nc' 'MD5' '5057a2ceb5325d3e787bfcbeb9ab7652'
'huss_day_inmcm4_1pctCO2_r1i1p1_21500101-21591231.nc' 'http://pcmdi9.llnl.gov/thredds/fileServer/cmip5_data/cmip5/output1/INM/inmcm4/1pctCO2/day/atmos/day/r1i1p1/huss/1/huss_day_inmcm4_1pctCO2_r1i1p1_21500101-21591231.nc' 'MD5' '491fa582a465fb6e37d285ffbde4f059'
'huss_day_inmcm4_1pctCO2_r1i1p1_21600101-21691231.nc' 'http://pcmdi9.llnl.gov/thredds/fileServer/cmip5_data/cmip5/output1/INM/inmcm4/1pctCO2/day/atmos/day/r1i1p1/huss/1/huss_day_inmcm4_1pctCO2_r1i1p1_21600101-21691231.nc' 'MD5' '3c5ff3581fb93b4656c2afc21df97bf1'
'huss_day_inmcm4_1pctCO2_r1i1p1_21700101-21791231.nc' 'http://pcmdi9.llnl.gov/thredds/fileServer/cmip5_data/cmip5/output1/INM/inmcm4/1pctCO2/day/atmos/day/r1i1p1/huss/1/huss_day_inmcm4_1pctCO2_r1i1p1_21700101-21791231.nc' 'MD5' 'c731a6549fb05f896e040867e7c98507'
'huss_day_inmcm4_1pctCO2_r1i1p1_21800101-21891231.nc' 'http://pcmdi9.llnl.gov/thredds/fileServer/cmip5_data/cmip5/output1/INM/inmcm4/1pctCO2/day/atmos/day/r1i1p1/huss/1/huss_day_inmcm4_1pctCO2_r1i1p1_21800101-21891231.nc' 'MD5' '92c95d155f13b40cccd0dce23090b6a0'
'huss_day_inmcm4_1pctCO2_r1i1p1_21900101-21991231.nc' 'http://pcmdi9.llnl.gov/thredds/fileServer/cmip5_data/cmip5/output1/INM/inmcm4/1pctCO2/day/atmos/day/r1i1p1/huss/1/huss_day_inmcm4_1pctCO2_r1i1p1_21900101-21991231.nc' 'MD5' 'be30fd41071a162b424fcae331d130f4'
'huss_day_inmcm4_1pctCO2_r1i1p1_22000101-22091231.nc' 'http://pcmdi9.llnl.gov/thredds/fileServer/cmip5_data/cmip5/output1/INM/inmcm4/1pctCO2/day/atmos/day/r1i1p1/huss/1/huss_day_inmcm4_1pctCO2_r1i1p1_22000101-22091231.nc' 'MD5' '51d91c278715677f776ffe7d40b8158c'
'huss_day_inmcm4_1pctCO2_r1i1p1_22100101-22191231.nc' 'http://pcmdi9.llnl.gov/thredds/fileServer/cmip5_data/cmip5/output1/INM/inmcm4/1pctCO2/day/atmos/day/r1i1p1/huss/1/huss_day_inmcm4_1pctCO2_r1i1p1_22100101-22191231.nc' 'MD5' 'b6c7a4b575ac38cfaf9d87dc28cd5330'
'huss_day_inmcm4_1pctCO2_r1i1p1_22200101-22291231.nc' 'http://pcmdi9.llnl.gov/thredds/fileServer/cmip5_data/cmip5/output1/INM/inmcm4/1pctCO2/day/atmos/day/r1i1p1/huss/1/huss_day_inmcm4_1pctCO2_r1i1p1_22200101-22291231.nc' 'MD5' 'ba69d55f017a0709f11f9d31cb282314'
key: userOpenId val: 
key: hostName val: localhost
key: searchUrl val: http://localhost:8080/esg-search/wget/?shards=localhost:8983/solr&dataset_id=cmip5.output1.INM.inmcm4.1pctCO2.day.atmos.day.r1i1p1.v20110323|pcmdi9.llnl.gov
key: date val: 2013/02/27 16:15:46

*/
