package org.esgf.bestmanpath;

public class BestmanPathGeneratorFactory {

    public BestmanPathGenerator makeBestmanPathGenerator(String bestmanPathGeneratorName,String dataset_id,String file_id) {
        
        if(bestmanPathGeneratorName.equalsIgnoreCase("Random")) {
            return new RandomBestmanPathGenerator();
        } else {
            return new PostgresBestmanPathGenerator(dataset_id,file_id);
        }
        
    }
}
