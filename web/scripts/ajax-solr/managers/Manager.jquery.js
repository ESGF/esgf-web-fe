/*****************************************************************************
 * Copyright � 2011 , UT-Battelle, LLC All rights reserved
 *
 * OPEN SOURCE LICENSE
 *
 * Subject to the conditions of this License, UT-Battelle, LLC (the
 * �Licensor�) hereby grants to any person (the �Licensee�) obtaining a copy
 * of this software and associated documentation files (the "Software"), a
 * perpetual, worldwide, non-exclusive, irrevocable copyright license to use,
 * copy, modify, merge, publish, distribute, and/or sublicense copies of the
 * Software.
 *
 * 1. Redistributions of Software must retain the above open source license
 * grant, copyright and license notices, this list of conditions, and the
 * disclaimer listed below.  Changes or modifications to, or derivative works
 * of the Software must be noted with comments and the contributor and
 * organization�s name.  If the Software is protected by a proprietary
 * trademark owned by Licensor or the Department of Energy, then derivative
 * works of the Software may not be distributed using the trademark without
 * the prior written approval of the trademark owner.
 *
 * 2. Neither the names of Licensor nor the Department of Energy may be used
 * to endorse or promote products derived from this Software without their
 * specific prior written permission.
 *
 * 3. The Software, with or without modification, must include the following
 * acknowledgment:
 *
 *    "This product includes software produced by UT-Battelle, LLC under
 *    Contract No. DE-AC05-00OR22725 with the Department of Energy.�
 *
 * 4. Licensee is authorized to commercialize its derivative works of the
 * Software.  All derivative works of the Software must include paragraphs 1,
 * 2, and 3 above, and the DISCLAIMER below.
 *
 *
 * DISCLAIMER
 *
 * UT-Battelle, LLC AND THE GOVERNMENT MAKE NO REPRESENTATIONS AND DISCLAIM
 * ALL WARRANTIES, BOTH EXPRESSED AND IMPLIED.  THERE ARE NO EXPRESS OR
 * IMPLIED WARRANTIES OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE,
 * OR THAT THE USE OF THE SOFTWARE WILL NOT INFRINGE ANY PATENT, COPYRIGHT,
 * TRADEMARK, OR OTHER PROPRIETARY RIGHTS, OR THAT THE SOFTWARE WILL
 * ACCOMPLISH THE INTENDED RESULTS OR THAT THE SOFTWARE OR ITS USE WILL NOT
 * RESULT IN INJURY OR DAMAGE.  The user assumes responsibility for all
 * liabilities, penalties, fines, claims, causes of action, and costs and
 * expenses, caused by, resulting from or arising out of, in whole or in part
 * the use, storage or disposal of the SOFTWARE.
 *
 *
 ******************************************************************************/

/**
 * Front end "controller" for ESG
 *
 * fwang2@ornl.gov
 * harneyjf@ornl.gov
 */


/**
 * @see http://wiki.apache.org/solr/SolJSON#JSON_specific_parameters
 * @class Manager
 * @augments AjaxSolr.AbstractManager
 */
AjaxSolr.Manager = AjaxSolr.AbstractManager.extend(
  /** @lends AjaxSolr.Manager.prototype */
  {
      executeRequest: function (servlet) {
    	  
    	  
          var self = this;
          
          //loads everything in the html5 'fq' store
          self.loadExistingQueries();
          
          //check to see if a distributed search is needed
          self.appendDistributedRequestHandler();
          
          LOG.debug('store string: ' + this.store.string());
          
          if (this.proxyUrl) {
	          jQuery.ajax({
	              url: this.proxyUrl,
	              data: this.store.string(),
	              type: 'GET',
	              success: function(data) {self.handleResponse(data);},
	              error: function() {
	            	  alert("There was an error in processing your query.  Try your search again.");
	            	  
	            	  //remove the existing parameter store
	            	  Manager.store.remove('fq');
	            	  
	            	  //set the search back to local type
	            	  ESGF.setting.searchType = 'local';
	            	  
	            	  //reset the localStorage to querying over the dataset type
	            	  ESGF.localStorage.remove('esgf_fq');
	            	  
	            	  /*
	            	  localStorage['fq'] = 'type:Dataset' + ';';
	            	  */
	            	  
	            	  localStorage['distrib'] == 'local';
	            		  
	            	  //reload the page
	            	  window.location.reload();
	            	  
	              },
	              dataType: 'json'
	          });
          }
          else {
	          jQuery.getJSON(this.solrUrl + servlet + 
	              '?' + this.store.string() + '&wt=json&json.wrf=?', 
	              {}, function (data) { self.handleResponse(data); });
          }
      },
  
      appendDistributedRequestHandler: function () {
    	  
    	  var self = this;
    	  
    	  /*
    	  self.getShardsFromSolrConfig();
    	  */
    	 
    	  self.getShardsFromService();
    	  
      	 
      },
      
      /**
       * This property (private) loads any existing constraint from the local store into the search
       * application.
       *
       * @field
       * @public
       * @type String
       */
  	loadExistingQueries: function () {
  		LOG.debug('In Manager.loadingExistingQueries');
  		var self = this;
  		

  		//put in the dataset type
        ESGF.localStorage.put('esgf_fq','type:Dataset','type:Dataset');
        Manager.store.addByValue('fq','type:Dataset');
        
        //get all of the fq parameters from the localstore
        var esgf_fq = ESGF.localStorage.getAll('esgf_fq');
        
        ESGF.localStorage.printMap('esgf_fq');
        
        //add each constraint
        for(var key in esgf_fq) {
        	var value = esgf_fq[key];
        	if(key != '') {
        		Manager.store.addByValue('fq',value);
        	} 
		}
        
        
  		
  		
  	},
      
    isDuplicate: function (fq,val) {
      
    		var flag = false;
    		var allFqs = fq.split(";");
    			//loop over all existing queries - note 'length-1' was used to ignore the trailing whitespace of the last split
    	  		for(var i=0;i<allFqs.length-1;i++)
    			{
    				if(allFqs[i] == ('text:' + val)) {
    					flag = true;
    				}
    			}
    		return flag;
    },
  	
    
  	getShardsFromService: function () {
  		var shardsString = ''; 
  	  
        for(var i=0;i<ESGF.search.shards.length;i++) {
       	 var shards = ESGF.search.shards[i];
   		 shardsString = shardsString + shards['nodeIp'] + ':8983/solr';
       	 if(i != ESGF.search.shards.length-1) {
           	 shardsString = shardsString + ',';
       	 }
        }
        

		 if(localStorage['distrib'] == 'distributed') {
      		Manager.store.addByValue('shards',shardsString);
     	 } else {
      		Manager.store.removeByValue('shards',shardsString);
     	 }
  	},
  	
  	getShardsFromSolrConfig: function() {
  		if(ESGF.setting.searchType == 'Distributed') {
  			Manager.store.addByValue('qt','/distrib');
  		} else {
  			Manager.store.removeByValue('qt','/distrib');
  		}
  	}
  	
  	
});


/*
	//compare the parameter store with the local store
	//if the local store contains something that is not in the parameter store, then add to parameter store
	
	//add the type here 
Manager.store.addByValue('fq','type:Dataset');
 	var fq = localStorage['fq'];

ESGF.localStorage.put('esgf_fq','type:Dataset','type:Dataset');


if(fq == null) {
	  	fq = 'type:Dataset' + ';';
	  	localStorage['fq'] = fq;
	} else if(fq == undefined) {
	fq = 'type:Dataset' + ';';
	  	localStorage['fq'] = fq;
} else if(fq = '') {
	fq = 'type:Dataset' + ';';
	  	localStorage['fq'] = fq;
}


alert('Manager: ' + ESGF.localStorage.get('esgf_fq','type:Dataset'));

	
	if(ESGF.setting.storage) {
		var fq = localStorage['fq'];

      //get all the fq parameters
		var esgf_fq = ESGF.localStorage.getAll('esgf_fq');
		
		for(var key in esgf_fq) {
			var value = esgf_fq[key];
			alert('key: ' + key + ' value: ' + esgf_fq[key]);
		}
		
		
		//place them into the Manager store
		
		
		if(fq != undefined) {
			var allFqs = fq.split(";");
			for(var i=0;i<allFqs.length-1;i++)
			{
				if(self.store.string().search(escape(allFqs[i])) == -1) {
					Manager.store.addByValue('fq',allFqs[i]);
				}
			}
	  	}
		LOG.debug('\tlocalStorage: ' + fq);
		
		
		
		ESGF.localStorage.remove('esgf_fq','type:Dataset');
	} 
	
	LOG.debug('\tparameter store: ' + Manager.store.values('fq'));
	
	
	LOG.debug('End in Manager.loadingExistingQueries');
	*/