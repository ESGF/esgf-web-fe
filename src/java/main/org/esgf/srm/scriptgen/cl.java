package org.esgf.srm.scriptgen;

import java.util.Scanner;

import org.esgf.filetransformer.SRMFileTransformation;

public class cl {

    private static String SCRIPT_NAME = "guc";
    private static String SCRIPT_TYPE = "complex";
    
	public static void main(String [] args) {
	    
	    String url1 = "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?" +
                "SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/" +
                "t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1978-09.nc";
	    String url2 = "srm://esg2-sdnl1.ccs.ornl.gov:46790/srm/v2/server?" +
                "SFN=mss://esg2-sdnl1.ccs.ornl.gov//proj/cli049/UHRGCS/ORNL/CESM1/" +
                "t341f02.FAMIPr/atm/hist/t341f02.FAMIPr.cam2.h0.1979-09.nc";
   
	    
	    String [] urls = new String [2];
        urls[0] = url1;
        urls[1] = url2;
        String [] outputFiles = SRMFileTransformation.simulateSRM(urls);
        
        String checksum = "dsgasg";
        String checksumType = "MD5";
        
        String [] checksums = new String[2];
        String [] checksumTypes = new String [2];

        checksums[0] = checksum;
        checksums[1] = checksum;
        checksumTypes[0] = checksumType;
        checksumTypes[1] = checksumType;
        
		ScriptGenerator scriptGenerator = null;
		
		ScriptGeneratorFactory scriptGeneratorFactory = new ScriptGeneratorFactory();
		
		scriptGenerator = scriptGeneratorFactory.makeScriptGenerator(SCRIPT_NAME,SCRIPT_TYPE);

		//for basic wget
		//((BasicWgetScriptGenerator) scriptGenerator).setFileStr(outputFiles);
		

        //for complex wget
		/*
		String message = "message";
		String userOpenId = "userOpenId";
		String hostName = "hostName";
		String searchUrl = "searchUrl";
		String date = "date";
        ((ComplexWgetScriptGenerator) scriptGenerator).setFileStr(outputFiles,checksums,checksumTypes);
        ((ComplexWgetScriptGenerator) scriptGenerator).setMessage(message);
        ((ComplexWgetScriptGenerator) scriptGenerator).setUserOpenId(userOpenId);
        ((ComplexWgetScriptGenerator) scriptGenerator).setHostName(hostName);
        ((ComplexWgetScriptGenerator) scriptGenerator).setSearchUrl(searchUrl);
        ((ComplexWgetScriptGenerator) scriptGenerator).setDate(date);
        */
		
		
		//for basic guc
        //((BasicGlobusUrlCopyScriptGenerator) scriptGenerator).setFileStr(outputFiles);
        
        //for complex wget
        String message = "message";
        String userOpenId = "userOpenId";
        String hostName = "hostName";
        String searchUrl = "searchUrl";
        String date = "date";
        ((ComplexGlobusUrlCopyScriptGenerator) scriptGenerator).setFileStr(outputFiles,checksums,checksumTypes);
        ((ComplexGlobusUrlCopyScriptGenerator) scriptGenerator).setMessage(message);
        ((ComplexGlobusUrlCopyScriptGenerator) scriptGenerator).setUserOpenId(userOpenId);
        ((ComplexGlobusUrlCopyScriptGenerator) scriptGenerator).setHostName(hostName);
        ((ComplexGlobusUrlCopyScriptGenerator) scriptGenerator).setSearchUrl(searchUrl);
        ((ComplexGlobusUrlCopyScriptGenerator) scriptGenerator).setDate(date);
        
		doStuff(scriptGenerator);
		
	}
	
	public static void doStuff(ScriptGenerator scriptGenerator)
	{
		System.out.println(scriptGenerator.generateScript());
	}
}
