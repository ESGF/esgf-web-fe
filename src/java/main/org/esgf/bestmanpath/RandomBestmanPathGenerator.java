package org.esgf.bestmanpath;

import java.util.Random;

import org.esgf.srmcache.SRMCacheStore;

public class RandomBestmanPathGenerator extends BestmanPathGenerator {

    public String getBestmanPath() {
        Random random = new Random();
        
        int bestmannum = Math.abs(random.nextInt());
        
        String bestmannumStr = "V.0.0-" + Integer.toString(bestmannum);
        /*
        String bestmanPath = "gsiftp://esg.ccs.ornl.gov:2811//lustre/esgfs/shared/" + bestmannum + "/" + this.file_url;
        */
        return bestmannumStr;
    }

    @Override
    public String getBestmanPath(SRMCacheStore srm_cache) {
Random random = new Random();
        
        int bestmannum = Math.abs(random.nextInt());
        
        String bestmannumStr = "V.0.0-" + Integer.toString(bestmannum);
        /*
        String bestmanPath = "gsiftp://esg.ccs.ornl.gov:2811//lustre/esgfs/shared/" + bestmannum + "/" + this.file_url;
        */
        return bestmannumStr;
    }

}
