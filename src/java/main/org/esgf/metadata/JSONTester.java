package org.esgf.metadata;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;


public class JSONTester {

	public static void main(String [] args) throws JSONException, IOException
	{
		
		String xml = "";
		
		String metadataFileName = "ORNL-oai_dif.xml";
		
		metadataFileName = "PartialDif";
		
		//File f = new File("C:\\Users\\8xo\\esgProjects\\esgsearch\\esg-search\\resources\\ORNL-oai_dif.xml");
		File f = new File("C:\\Users\\8xo\\esgProjects\\" + metadataFileName + ".xml");
	    
		//C:\Users\8xo\esgProjects
		FileReader fr = new FileReader(f);
	    BufferedReader br = new BufferedReader(fr);

	    StringBuffer sb = new StringBuffer();
	    String eachLine = br.readLine();

	    while (eachLine != null) {
	      sb.append(eachLine);
	      sb.append("\n");
	      eachLine = br.readLine();
	    }
	    
	    xml = sb.toString();
		
		JSONObject jo = XML.toJSONObject(xml);
		//System.out.println(jo.toString());
		
		
		FileOutputStream out; // declare a file output object
        PrintStream p; // declare a print stream object

        
        
        try
        {
                // Create a new file output stream
                // connected to "myfile.txt"
                out = new FileOutputStream("C:\\Users\\8xo\\esgProjects\\esgsearch\\esg-search\\resources\\" + metadataFileName + ".json");

                // Connect print stream to the output stream
                p = new PrintStream( out );

                p.print(jo.toString());

                p.close();
        }
        catch (Exception e)
        {
                System.err.println ("Error writing to file");
        }
	  }
}
