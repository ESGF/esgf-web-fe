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
	  
	  get_param_arr: function () {
		  
		  //first check if completely empty,
		  //if so, then load type and offset params
		  if(ESGF.localStorage.get('esgf_queryString', 'offset') == undefined) {
	  		  ESGF.localStorage.put('esgf_queryString', 'offset', 'offset=' + 0);

		      //put in the dataset type
		      ESGF.localStorage.put('esgf_queryString','type:Dataset','type=Dataset');
	  	  } 
	        
	  		
		  
		  
		  
		  var useURLParams = false;
    	  
    	  if($('#urlparamscheckbox').attr('checked') == 'checked') {
    		  useURLParams = true;
    	  }
		  
		  if(useURLParams) {
    		  //alert('useURLParams is true');
    	  } else {
    		  //alert('useURLParams is false');
    	  }
		  
		  
		  var searchConstraints = '';

		  var searchStringMap = ESGF.localStorage.getAll('esgf_queryString');

		  for(var key in searchStringMap) {
			  searchConstraints += searchStringMap[key] +'&';
		  }

		  // get querystring as an array split on "&"
		  var querystringparams = location.search.replace( '?', '' ).split( '&' );
  	      var queryStr = new String('');
  	      // declare object
  	      var queryObj = {};
  	    
  	      // loop through each name-value pair and populate object
  	    
  	      for ( var i=0; i<querystringparams.length; i++ ) {
  	          // get name and value
  	          var name = querystringparams[i].split('=')[0];
  	          var value = querystringparams[i].split('=')[1];
  	          // populate object
  	          queryObj[name] = value;
  	          queryStr += querystringparams[i] + '&';
  	      }

  	    
          
  	      
  	      
  	      LOG.debug('queryStr: ' + queryStr);
  	      LOG.debug('searchConstraints: ' + searchConstraints);
  	      
  	      var facet_param_arr = new Array();
  	    
  	      var dataObj = {'facet_param_list' : queryStr, 'search_constraint_list' : searchConstraints, 'useURLParams' : useURLParams};//queryObj};

			 //get only the facets that matter
  	      jQuery.ajax({
        	  url: '/esgf-web-fe/live/facetList',
        	  type: 'GET',
        	  data: dataObj,
        	  async: false,
        	  success: function(data) {   
        		  facet_param_arr = data;
        	  },
        	  error: function() {
        		  alert('error');
        	  }
  	    
  	      });
  	    
  	      
  	      var oldMap = ESGF.localStorage.getAll('esgf_queryString');
  		
  		
  		for(var key in oldMap) {
  			var value = oldMap[key];
  			
  			//alert('key: ' + key + ' value: ' + value);
  		}
  	    
  	      return facet_param_arr;
		  
	  },
	  
      executeRequest: function (servlet) {
    	  
    	  
    	  
    	  //alert('latest versions: ' + ESGF.setting.versionsLatest);
    	  
    	  
          var self = this;

          self.loadCheckboxConstraints();
          
          
          //get all of the facets here
          var facet_param_arr = self.get_param_arr();
          

          //loads everything in the html5 'fq' store
          //self.loadExistingQueries();
          
          var sidebarWidget = Manager.widgets['facet-sidebar'];
          
          var namesArr = new Array();

          for(var key in sidebarWidget) {
        	  if(key == 'nameMap') {
        		  var nameMap = sidebarWidget[key];
            	  for(var i=0;i<nameMap.length;i++) {
            		  var shortName = nameMap[i].split(':')[0];
            		  namesArr.push(shortName);
            	  }
        	  }
          }
          
          //assemble the querystring here
          var queryString = '/esg-search/search?';
          //assemble the format parameter
          queryString += self.loadFormat('json');
          //assemble the facet names
          queryString += self.loadFacetNames(namesArr);
          
          
          
          
          
          
          
          //assemble the search constraints
          queryString += self.loadSearchConstraints(facet_param_arr);

          
          
          var revisedQueryString = self.rewriteTextQuery(queryString);

          
          LOG.debug("Manager's querystring: " + revisedQueryString);

  		var selected = $( "#myTabs" ).tabs( "option", "selected" );
          
  			
  		  //if it is a distributed search ... mask the page so that user cannot initiate any more events 
          if(revisedQueryString.indexOf('distrib=false') == -1) {
        	  //alert('distributed search taking place...put in overlay');
        	  $("#prompt").overlay().load();
          }
          
          /**
           * Ajax call to the search API
           */
          
          jQuery.ajax({
        	  url: revisedQueryString,
        	  type: 'GET',
        	  timeout: ESGF.setting.ajaxTimeout,
        	  success: function(data) {   
        		  self.handleResponse(data);
        	   	     
                  if(revisedQueryString.indexOf('distrib=false') == -1) {
                	  //alert('query done ... take down overlay here');
                	  $("#prompt").overlay().close();
                  }
                  
        		  
        	  },
        	  error: function() {
        		  alert("There was an error in processing your query.  Try your search again.");
            	  
            	  //remove the existing parameter store
            	  Manager.store.remove('fq');
            	  
            	  //reset the localStorage for search constraints
            	  ESGF.localStorage.removeAll('esgf_fq');

            	  //reset the localStorage esgf_queryString map
            	  ESGF.localStorage.removeAll('esgf_queryString');

            	  //reset the localStorage dataCart map
            	  ESGF.localStorage.removeAll('dataCart');
            	  
            	  //reset the localStorage searchStates map
            	  ESGF.localStorage.removeAll('searchStates');
            	  

                  if(revisedQueryString.indexOf('distrib=false') == -1) {
                	  //alert('query done ... take down overlay here');
                	  $("#prompt").overlay().close();
                  }
            	  //legacy
            	  //reset the distributed search flag to local
            	  //localStorage['distrib'] == 'local';

            	  //legacy
            	  //set the search back to local type
            	  //ESGF.setting.searchType = 'local';
            	  
            	  //reload the page
            	  //window.location.reload();
        	  }
          });
          
          
          
          
          
      },
  
      
      loadCheckboxConstraints: function() {
      	
    	  
    	  LOG.debug('ESGF.setting.versionsLatest: ' + ESGF.setting.versionsLatest + 
    			  'ESGF.setting.replicas: ' + ESGF.setting.replicas + 
    			  'ESGF.setting.distributed: ' + ESGF.setting.distributed);
    	  
    	  //alert('in loadCheckbox constraints versionslatest: ' + ESGF.setting.versionsLatest);
    	  
    	  //if version is not null then it is true
    	  if(ESGF.setting.versionsLatest != null) {
    		  //remove any existing versions from queryString
			  ESGF.localStorage.remove('esgf_queryString','latest:true');
			  ESGF.localStorage.remove('esgf_queryString','latest:false');
    		  
			  //alert('putting latest=true');
			  //put in latest = true
			  ESGF.localStorage.put('esgf_queryString','latest:true','latest=true');
    	  } 
    	  //else remove the versions argument
    	  else {
    		  
    		  //alert('removing latest= from the querystring');
    		  
    		  //remove any existing versions from queryString
			  ESGF.localStorage.remove('esgf_queryString','latest:true');
			  ESGF.localStorage.remove('esgf_queryString','latest:false');

				$('#versioncheckbox').attr('checked','checked');
				
    	  }
    	  
    	//if replica is not null then it is true
    	  if(ESGF.setting.replicas != null) {
    		  //remove any existing replicas from queryString
			  ESGF.localStorage.remove('esgf_queryString','replica:true');
			  ESGF.localStorage.remove('esgf_queryString','replica:false');
    		  
			  //put in replica = false
			  ESGF.localStorage.put('esgf_queryString','replica:false','replica=false');
    	  } 
    	  //else remove the replicas argument
    	  else {
    		  
    		  
    		  
    		  //remove any existing versions from queryString
			  ESGF.localStorage.remove('esgf_queryString','replica:true');
			  ESGF.localStorage.remove('esgf_queryString','replica:false');

			  $('#replicacheckbox').attr('checked','checked');
    	  }
    	  
    	//if distrib is false 
    	  if(ESGF.setting.distributed == 'false') {
    		  //remove any existing versions from queryString
			  ESGF.localStorage.remove('esgf_queryString','distrib');
    		  
			  //put in distrib=false
			  ESGF.localStorage.put('esgf_queryString','distrib','distrib=false');
    	  } 
    	  //else distrib is true
    	  else {
    		  //remove any existing distrib from queryString
			  ESGF.localStorage.remove('esgf_queryString','distrib');
			  
			  //put in distrib=true
			  ESGF.localStorage.put('esgf_queryString','distrib','distrib=true');
			  $('#distribcheckbox').attr('checked','checked');
			  
    	  }
    	  
    	  
    	  LOG.debug('distrib: ' + $('#distribcheckbox').attr('checked') + 
    			    ' replica: ' + $('#replicacheckbox').attr('checked') + 
    			    ' version: ' + $('#versioncheckbox').attr('checked'));;
    	  
    	  
      },
      
      
      
      
      /**
       * This property (private) loads any existing constraint from the localStorage into the search
       * application.
       *
       * @field
       * @public
       * @type String
       */
  	loadExistingQueries: function () {
  		LOG.debug('In Manager.loadingExistingQueries');
  		var self = this;
  		
  		
  		//for direct requests to solr
  		//put in the dataset type
  		/*
        ESGF.localStorage.put('esgf_fq','type:Dataset','type:Dataset');
        Manager.store.addByValue('fq','type:Dataset');
        */
        
  		//put in the replica type (which for results is "false")
        //ESGF.localStorage.put('esgf_fq','replica:false','replica:false');
        //Manager.store.addByValue('fq','replica:false');
        
        if(ESGF.localStorage.get('esgf_queryString', 'offset') == undefined) {
  		  ESGF.localStorage.put('esgf_queryString', 'offset', 'offset=' + 0);
  	  	} 
        
        //for search API
  		
        //put in the dataset type
        ESGF.localStorage.put('esgf_queryString','type:Dataset','type=Dataset');
  		
        //put in the replica type (which for results is "false")
        //ESGF.localStorage.put('esgf_queryString','replica:false','replica=false');
        
  		//put in the replica type (which for results is "false")
        //ESGF.localStorage.put('esgf_queryString','latest:true','latest=true');
        
        //if(ESGF.localStorage.get('esgf_queryString', 'offset') == undefined) {
		//  ESGF.localStorage.put('esgf_queryString', 'offset', 'offset=' + 0);
	    //}
        
        var searchQuery = ESGF.localStorage.toString('esgf_queryString');
        ESGF.localStorage.printMap('esgf_queryString');
        
        
        //get all of the fq parameters from the localstore
        var esgf_fq = ESGF.localStorage.getAll('esgf_fq');
        
        
        //FIXME: May have to take this out
        //legacy
        //add each constraint
        for(var key in esgf_fq) {
        	var value = esgf_fq[key];
        	if(key != '') {
        		Manager.store.addByValue('fq',value);
        	} 
		}
        
  		
  	},
  	
  	/**
  	 * DOCUMENT ME
  	 * @param format
  	 * @returns {String}
  	 */
  	loadFormat: function(format) {
  		return 'format=application%2Fsolr%2B' + format + '&';
  	},
      
  	/**
  	 * DOCUMENT ME
  	 * @param namesArr
  	 * @returns {String}
  	 */
  	loadFacetNames: function(namesArr) {
  		var facetNames = 'facets=';
  		
  		for(var i=0;i<namesArr.length;i++) {
  			if(i < (namesArr.length-1)) {
  				facetNames += namesArr[i] + ',';
  			} else {
  				facetNames += namesArr[i];
  			}
  		}
  		facetNames += '&';
  		return facetNames;
  	},
  	
  	/**
  	 * DOCUMENT ME
  	 * @returns {String}
  	 */
  	loadSearchConstraints: function(facet_param_arr) {
  		

  	  //alert('in load search constraints len ' + facet_param_arr.length);
  		
  		var self = this;
  		
  		var searchConstraints = '';
  		
  		
  		//alert('in loadSearchConstraints: ' + facet_param_arr);
  		
  		
  		var oldMap = ESGF.localStorage.getAll('esgf_queryString');
  		
  		
  		for(var key in oldMap) {
  			var value = oldMap[key];
  			
  			//alert('keyy: ' + key + ' value: ' + value);
  		}
  		
  		
  		
  		//need to remove the entire esgf_querystring map and refresh
  		//for each param in the facet array, add to the cookie

		//ESGF.localStorage.put('esgf_queryString','model:kkk1','model=kkk1');
  		
		//ESGF.localStorage.remove('esgf_queryString','model:kkk1');
		
  		
  		ESGF.localStorage.removeAll('esgf_queryString');
  		
  		
  		/*
  		for(var key in oldMap) {
  			var value = oldMap[key];
  			
  			ESGF.localStorage.put('esgf_queryString',key,value);
  		}
  		*/
  		for(var i=0;i<facet_param_arr.length;i++) {
  			//alert('i: ' + facet_param_arr[i]);
  			var key = '';
  			var value = '';
  			if(facet_param_arr[i].search('distrib') > -1) {
  				//alert('keyy: ' + 'distrib' + ' valuey: ' + facet_param_arr[i]);
  				key = 'distrib';
  				value = facet_param_arr[i];
  			} else if(facet_param_arr[i].search('offset') > -1) {
  				//alert('keyy: ' + 'offset' + ' valuey: ' + facet_param_arr[i]);
  				key = 'offset';
  				value = facet_param_arr[i];
  			} else if(facet_param_arr[i].search('query') > -1) {
  				key = facet_param_arr[i].replace('=',':');
  				value = facet_param_arr[i];
  				var text_value = value.split('=')[1]; 
  				$('input#query').val(text_value);
  				ESGF.setting.textbox = text_value;
  			} else {
  				//alert('keyy: ' + facet_param_arr[i].replace('=',':') + ' valuey: ' + facet_param_arr[i]);
  				key = facet_param_arr[i].replace('=',':');
  				value = facet_param_arr[i];
  			}

  			ESGF.localStorage.put('esgf_queryString',key,value);
  		}
  		
  		var searchStringMap = ESGF.localStorage.getAll('esgf_queryString');

    	
        for(var key in searchStringMap) {
        	searchConstraints += searchStringMap[key] +'&';        	
        }

        var searchConstraintsArr = searchConstraints.split('&');
        LOG.debug('searchConstraintsArr: ' + searchConstraintsArr);

        return searchConstraints;
  	},
  	
  	
  	/**
  	 * DOCUMENT ME
  	 */
    rewriteTextQuery: function(queryString) {
    	var newQueryString = '';
    	
    	var paramArr = queryString.split('&');
    	
    	newQueryString = paramArr[0];
    	
    	var fullText = '';
    	for(var i=1;i<paramArr.length;i++) {
    		var constraint = paramArr[i];
    		if(constraint != '' && constraint != ' ') {
        		if(constraint.search('query=') > -1) {
            		var queryClause = constraint.split('=');
            		//alert('queryClause: ' + queryClause[1]);
            		//alert('constraint: ' + constraint + ' queryClause: ' + queryClause);
            		//fullText += queryClause + ' ';
            		fullText = fullText + queryClause[1] + ' ';
        		} 
        		//process the offset/pagination
        		else if(constraint.search('offset=') > -1) {
        			//alert('offset: ' + constraint + ' ESGF.setting.paginationOn: ' + ESGF.setting.paginationOn);
        			if(ESGF.setting.paginationOn == 'false') {
        				//alert('use offset = 0')
        				newQueryString += '&' + 'offset=0';
        			} else {
        				//alert('use the standard offset and set paginationOn to false');
            			ESGF.setting.paginationOn = 'false';
            			newQueryString += '&' + constraint;
        			}
        		} else {
        			newQueryString += '&' + constraint;
        		}
    		}
    		
    	}
    	if(fullText != '') {
        	newQueryString += '&' + 'query=' + fullText;
    	}

    	newQueryString += '&' + 'limit=' + ESGF.setting.datasetCounter;
    	//alert('new query string: ' + newQueryString + ' fulltext: ' + fullText);
    	
    	return newQueryString;
    }
  	
  	
  	
});
