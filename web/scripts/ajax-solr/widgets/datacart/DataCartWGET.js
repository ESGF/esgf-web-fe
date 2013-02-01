/* Copyright © 2011 , UT-Battelle, LLC All rights reserved
 *
 * OPEN SOURCE LICENSE
 *
 * Subject to the conditions of this License, UT-Battelle, LLC (the
 * “Licensor”) hereby grants to any person (the “Licensee”) obtaining a copy
 * of this software and associated documentation files (the "Software"), a
 * perpetual, worldwide, non-exclusive, irrevocable copyright license to use,
 * copy, modify, merge, publish, distribute, and/or sublicense copies of the
 * Software.
 *
 * 1. Redistributions of Software must retain the above open source license
 * grant, copyright and license notices, this list of conditions, and the
 * disclaimer listed below.  Changes or modifications to, or derivative works
 * of the Software must be noted with comments and the contributor and
 * organization’s name.  If the Software is protected by a proprietary
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
 *    Contract No. DE-AC05-00OR22725 with the Department of Energy.”
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
 * Experimental Solr Front for ESG
 *
 * fwang2@ornl.gov
 * harneyjf@ornl.gov
 */



(function ($) {
	
	AjaxSolr.DataCartWGETWidget = AjaxSolr.AbstractWidget.extend({

		/**
		 * 
		 */
		searchConstraints: null,

		/**
		 * An array of dataset ids selected by the user in the data cart
		 */
		selected_arr: null,
		
		/**
		 * DOCUMENT ME
		 */
		beforeRequest: function() {
			
			var self = this;

			
	    	//grab the search constraints
	    	self.searchConstraints = ESGF.localStorage.toKeyArr('esgf_fq');
	    	
	    	
	    	//grab all the keys from the datacart map and place in an array
	    	self.selected_arr = ESGF.localStorage.toKeyArr('dataCart');
	    	
			//kill the uber script
			$("input#uber_script_short").die('click');
			$("a#uber_script_short").die('click');
				
			//kill the all files wget	
			$('a.wgetAllFiles_short').die('click');
			
		},
		
		/**
		 * DOCUMENT ME
		 */
		afterRequest: function() {
		
			/**
			 * DOCUMENT ME
			 * When the user clicks "Download All Files", this method is executed
			 * 
			 * A query string is assembled using the file ids and the search constraints (if 'filtered' is selected)
			 * 
			 * The string is then executed as a form action and sent to the wget api.  Example:
			 * 
			 * esg-search/wget/?project=cmip5
			 * 
			 * Would get all files that have been placed in the data cart bucketed in the project cmip5 
			 */
			$("a#uber_script_short").live('click', function() {

				
				//alert('uber');
				
				var self = this;
				
				
				//gather the file_ids
	        	var file_ids   = new Array();
	            
	        	//gather the dataset_ids
	        	var dataset_ids = new Array();
	        	
	        	//iterate over the selected array of datasets in the data cart
	        	//grab all the keys from the datacart map and place in an array
		    	self.selected_arr = ESGF.localStorage.toKeyArr('dataCart');

            	//get the peers
            	var peerStr = ESGF.datacart.getPeerStr();
	        	
	            for(var i=0;i<self.selected_arr.length;i++) {
	            
	            	var selectedDocId = self.selected_arr[i];//self.selected_arr[i];
	            	
	            	
	            	var selectedDocCount = $('span.datasetCount_'+ESGF.datacart.replaceChars(selectedDocId)).html();
	            	
	            	//if the count is greater than 10, check to see if the additional rows have been not been expanded for this dataset
	            	//if not (null) then need an extra ajax call to get the rest of the file ids
	            	var isAdded = $('tr.addedrow_' + ESGF.datacart.replaceChars(selectedDocId)).html();//$(this).parent().parent().parent().find('tr.addedrow_' + replaceChars(selectedDocId)).html();
	            	
	            	dataset_ids.push(selectedDocId);
	            	
	            	if(isAdded == null && selectedDocCount > ESGF.setting.fileCounter) {
	            	
	            		
	            		$('tr.file_rows_'+ ESGF.datacart.replaceChars(selectedDocId)).find(':checkbox:checked').each( function(index) {
	            			var file_id = $(this).parent().find('input').attr('value');
	            			file_ids.push(file_id);
	            		});
	            		
	            		
	            		
	            		//get all of the search constraints (fq params)
	                	var fqParamStr = ESGF.datacart.getFqParamStr();
	                	
	                	
	                	//get the technotes
	                	var technoteStr = ESGF.datacart.getTechnoteStr();
	            		
	                	//the id str is only one file
	                	var idStr = selectedDocId;
	                	
	                	
	                	
	            		//assemble the query string
	                	var queryStr = {"idStr" : idStr, 
	        					"peerStr" : peerStr, 
	        					"technotesStr" : technoteStr, 
	        					"showAllStr" : ESGF.setting.showAllContents, 
	        					"fqStr" : fqParamStr, 
	        					"initialQuery" : "true",
	        					"fileCounter" : ESGF.setting.fileCounter};

	                	
	                	$.ajax({
	    					url: '/esgf-web-fe/solrfileproxy2/datacart',
	    					global: false,
	    					type: "GET",
	    					data: queryStr,
	    				    async: false,
	    					dataType: 'json',
	    					success: function(data) {
	    						for(var i=0;i<data.docs.doc.files.file.length;i++){
	    							var file = data.docs.doc.files.file[i];
	    							file_ids.push(file.fileId);
	    						}
	    						
	    					},
	    					error: function() {
	    						alert('error in uber script');
	    					}
	                	});
	            		
	            	
	            	} else {
	            		$('tr.file_rows_'+ ESGF.datacart.replaceChars(selectedDocId)).find(':checkbox:checked').each( function(index) {
	            			var file_id = $(this).parent().find('input').attr('value');
	            			file_ids.push(file_id);
	            		});
	            		
	            		
	            	}
	            	
	            }
	            
	            var queryString = '/esg-search/wget/?';
	        	
	            var constraintCount = 0;
	            
	            var peers = peerStr.split(";");
	            var peerAppend = '';
	            for(var i=0;i<peers.length;i++) {
	            	/*
	            	if(peers[i] == 'esg-datanode.jpl.nasa.gov') {
	            		peerAppend += 'localhost:18983/solr';
	            	} else if (peers[i] == 'pcmdi9.llnl.gov') {
	            		peerAppend += 'localhost:28983/solr';
	            	} else*/ {
		            	peerAppend += peers[i] + ':8983/solr';
	            	}
	            	if(i != (peers.length-1)) {
	            		peerAppend += ',';
	            	}
	            }
	            
	            //alert('peerAppend: ' + peerAppend);
	            
	            queryString = ESGF.datacart.addConstraintsToWGETQueryString(queryString);
	        	
	            //alert('peerStr: ' + peerStr + ' queryString: ' + queryString);
	            
	            queryString += '&shards=' + peerAppend;
	            
	            //alert('queryStringUber: ' + queryString);
                
	            
	            ESGF.datacart.submitWGETScriptForm(queryString,file_ids,dataset_ids);
				
				
			});
			
			
			
			
			
			
			
			$('a.wgetAllFiles_short').live('click',function() {
				
				//extract the dataset Id from the span tag
				var selectedDocId = ($(this).parent().parent().find('span.datasetId').html()).trim();
				
				
				//var self = this;
				//self.innerHTML = "Downloading...";
				
				var parentElement = $(this).parent();
				
				//alert(parentElement.html());

				parentElement.find('a.wgetAllFiles_short').hide();
				parentElement.find('span.wgetAllFiles_short').show();
				
				//alert('sele: ' + selectedDocId + ' ' + replaceChars(selectedDocId));
				
				//$('.wgetAllFiles_short_'+replaceChars(selectedDocId)).remove();
				
				
				var selectedDocId = ($(this).parent().parent().find('span.datasetId').html());
				
	        	//gather the ids and the urls for download
	        	var file_ids   = new Array();
	        	

	        	//gather the dataset_ids
	        	var dataset_ids = new Array();
	        	dataset_ids.push(selectedDocId);
	        	
	        	
	        	//just make the two ajax calls for now
	        	//1 - for the first ten
	        	//2 - for >10

				var idStr = selectedDocId;
				
				//alert('idStr ' + idStr);
				
				var peerStr = ESGF.datacart.getIndividualPeer(idStr);//getPeerStr();
				
				var technoteStr = ESGF.datacart.getTechnoteStr();
								
		    	var fqParamStr = ESGF.datacart.getFqParamStr();
		    	
		    	
		    	
		    	var queryStr = {"idStr" : idStr, 
						"peerStr" : peerStr, 
						"technotesStr" : technoteStr, 
						"showAllStr" : ESGF.setting.showAllContents, 
						"fqStr" : fqParamStr, 
						"initialQuery" : "true",
    					"fileCounter" : ESGF.setting.fileCounter};
		    	
		    	$.ajax({
					url: '/esgf-web-fe/solrfileproxy2/datacart',
					global: false,
					type: "GET",
					data: queryStr,
					dataType: 'json',
					success: function(data) {
						
						if(data.docs.doc.files.file != undefined) {
							for(var i=0;i<data.docs.doc.files.file.length;i++){
								var file = data.docs.doc.files.file[i];
								file_ids.push(file.fileId);
							}
							
							
							if(data.docs.doc.files.file.length >= 10) {
								queryStr = {"idStr" : idStr, 
										"peerStr" : peerStr, 
										"technotesStr" : technoteStr, 
										"showAllStr" : ESGF.setting.showAllContents, 
										"fqStr" : fqParamStr, 
										"initialQuery" : "false",
			        					"fileCounter" : ESGF.setting.fileCounter};
								
								$.ajax({
									url: '/esgf-web-fe/solrfileproxy2/datacart',
									global: false,
									type: "GET",
									data: queryStr,
									dataType: 'json',
									success: function(data) {
										for(var i=0;i<data.docs.doc.files.file.length;i++){
											var file = data.docs.doc.files.file[i];
											file_ids.push(file.fileId);
										}

										/*
										if(peerStr == 'esg-datanode.jpl.nasa.gov') {
											peerStr = 'localhost:18983/solr';
										} else if (peerStr == 'pcmdi9.llnl.gov') {
											peerStr = 'localhost:28983/solr';
										}*/
										peerStr += ':8983/solr';
										
										var queryString = '/esg-search/wget/?' + 'shards=' + peerStr + '&';
						            	
										//var queryString = '/esg-search/wget/?';
						            	
										//alert('queryStringShort: ' + queryString);
						                
										
						                queryString = ESGF.datacart.addConstraintsToWGETQueryString(queryString);
						            	
						                ESGF.datacart.submitWGETScriptForm(queryString,file_ids,dataset_ids);
										

						    			
										parentElement.find('a.wgetAllFiles_short').show();
										parentElement.find('span.wgetAllFiles_short').hide();
					
										
									},
									error: function() {
										alert('error');

										
										parentElement.find('a.wgetAllFiles_short').show();
										parentElement.find('span.wgetAllFiles_short').hide();
					
									}
								});
							} else {
								

								//alert('peerStr: ' + peerStr);
								/*
								if(peerStr == 'esg-datanode.jpl.nasa.gov') {
									peerStr = 'localhost:18983/solr';
								} else if (peerStr == 'pcmdi9.llnl.gov') {
									peerStr = 'localhost:28983/solr';
								}
								*/
								peerStr += ':8983/solr';
								var queryString = '/esg-search/wget/?' + 'shards=' + peerStr + '&';
				            	
				                queryString = ESGF.datacart.addConstraintsToWGETQueryString(queryString);
				            	
				                //alert('queryString: ' + queryString);
				                
				                ESGF.datacart.submitWGETScriptForm(queryString,file_ids,dataset_ids);
				    			
										parentElement.find('a.wgetAllFiles_short').show();
										parentElement.find('span.wgetAllFiles_short').hide();
					
							}
						} else {
							
							
							parentElement.find('a.wgetAllFiles_short').show();
							parentElement.find('span.wgetAllFiles_short').hide();
							alert('There are no files in this dataset that match the search criteria.');
						}
						
						
					},
					error: function() {
						alert('error');

						
						parentElement.find('a.wgetAllFiles_short').show();
						parentElement.find('span.wgetAllFiles_short').hide();
				
					}
		    	});
	        	
	        	
			});
			
			
			
			
		}
	});
})(jQuery);
		
