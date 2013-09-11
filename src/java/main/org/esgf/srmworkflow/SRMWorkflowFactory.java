package org.esgf.srmworkflow;


public class SRMWorkflowFactory {

    public SRMWorkflow makeSRMWorkflow(String type) {
        
        if(type.equalsIgnoreCase("Simulation")) {
            
            return new SimulationSRMWorkflow(type);
            
        } else if(type.equalsIgnoreCase("Production")){
            
            return new ProductionSRMWorkflow(type);
            
        } else {
            
            return null;
        
        }
        
    }
    
}
