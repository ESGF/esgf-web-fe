package org.esgf.srm.scriptgen;

public class ScriptGeneratorFactory {

	public ScriptGenerator makeScriptGenerator(String generatorName,String generatorType) {
		
		if(generatorName.equalsIgnoreCase("WGET")) {
			if(generatorType.equalsIgnoreCase("basic")) {
				return new BasicWgetScriptGenerator();
			} else if (generatorType.equalsIgnoreCase("complex")) {
                return new ComplexWgetScriptGenerator();
            } 
			else {
			    return null;
			}
			/*
			else {
				return new WgetScriptGenerator();
			}
			*/
		} else {
			
			if(generatorType.equalsIgnoreCase("basic")) {
				return new BasicGlobusUrlCopyScriptGenerator();
			} else if (generatorType.equalsIgnoreCase("complex")) {
				return new ComplexGlobusUrlCopyScriptGenerator();
			} else {
				return null;
			}
			
		    
		} 
		
	}
	
}
