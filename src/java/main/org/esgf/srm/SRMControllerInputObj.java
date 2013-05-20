package org.esgf.srm;

import org.esgf.srm.utils.SRMUtils;

public class SRMControllerInputObj {

    private String file_id;
    private String file_url;
    private String dataset_id;
    private String filtered;
    private String type;
    private String constraints;
    private String scriptType;
    private String openid;
    
    public SRMControllerInputObj() {
        this.setFile_id(SRMUtils.INPUT_FILE_FILE_ID);
        this.setFile_url(SRMUtils.INPUT_FILE_FILE_URL);
        this.setDataset_id(SRMUtils.INPUT_DATASET_ID);
        this.setFiltered(SRMUtils.INPUT_FILTERED);
        this.setType(SRMUtils.INPUT_TYPE_FILE);
        this.constraints = SRMUtils.INPUT_CONSTRAINTS;
        this.setScriptType(SRMUtils.INPUT_SCRIPT_TYPE);
        this.openid = "https://esg.ccs.ornl.gov/esgf-idp/openid/jfharney";
    }
    
    public SRMControllerInputObj(String file_id,String file_url,String dataset_id,String filtered,String type,String constraints,String scriptType) {
        this.setFile_id(file_id);
        this.setFile_url(file_url);
        this.setDataset_id(dataset_id);
        this.setFiltered(filtered);
        this.setType(type);
        this.setConstraints(constraints);
        this.setScriptType(scriptType);
    }

    /**
     * @return the file_id
     */
    public String getFile_id() {
        return file_id;
    }

    /**
     * @param file_id the file_id to set
     */
    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    /**
     * @return the file_url
     */
    public String getFile_url() {
        return file_url;
    }

    /**
     * @param file_url the file_url to set
     */
    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    /**
     * @return the dataset_id
     */
    public String getDataset_id() {
        return dataset_id;
    }

    /**
     * @param dataset_id the dataset_id to set
     */
    public void setDataset_id(String dataset_id) {
        this.dataset_id = dataset_id;
    }

    /**
     * @return the filtered
     */
    public String getFiltered() {
        return filtered;
    }

    /**
     * @param filtered the filtered to set
     */
    public void setFiltered(String filtered) {
        this.filtered = filtered;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the constraints
     */
    public String getConstraints() {
        return constraints;
    }

    /**
     * @param constraints the constraints to set
     */
    public void setConstraints(String constraints) {
        this.constraints = constraints;
    }

    /**
     * @return the scriptType
     */
    public String getScriptType() {
        return scriptType;
    }

    /**
     * @param scriptType the scriptType to set
     */
    public void setScriptType(String scriptType) {
        this.scriptType = scriptType;
    }
    
    
}
