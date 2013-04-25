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

				//gather the file_ids
	        	var file_ids   = new Array();
	            
				//gather the dataset_ids
	        	var dataset_ids = new Array();
	        	
	        	//iterate over the selected array of datasets in the data cart
	        	//grab all the keys from the datacart map and place in an array
		    	self.selected_arr = ESGF.localStorage.toKeyArr('dataCart');

		    	for(var i=0;i<self.selected_arr.length;i++) {
		            
	            	var selectedDocId = self.selected_arr[i];//self.selected_arr[i];
	            	dataset_ids.push(selectedDocId);
	            	
		    	}
		    	
		    	var queryString = '/esg-search/wget/?';
	        	
		    	var filtered = false;
				filtered = ($('#datacart_filtered').attr('checked') == 'checked');
				
		    	if(filtered) {
		    		var textQueryParam = '';
					var fqParams = ESGF.localStorage.getAll('esgf_queryString');
					for(var key in fqParams) {
						if(key.search('query:') > -1) {
							textQueryParam = fqParams[key];
						}
						
					}
					queryString += textQueryParam;
		    	} else {
		    		queryString += 'query=*';
		    	}
		    	
				
	            
	            ESGF.datacart.submitWGETScriptForm(queryString,file_ids,dataset_ids);
				

				
			});
			
			
			
			
			
			
			
			$('a.wgetAllFiles_short').live('click',function() {
				
				//extract the dataset Id from the span tag
				var selectedDocId = ($(this).parent().parent().find('span.datasetId').html()).trim();

				//var self = this;
				//self.innerHTML = "Downloading...";
				
				var parentElement = $(this).parent();
				

				parentElement.find('a.wgetAllFiles_short').hide();
				parentElement.find('span.wgetAllFiles_short').show();
				
				
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
				
								
				var queryString = '/esg-search/wget/?';

				filtered = ($('#datacart_filtered').attr('checked') == 'checked');
				
				
		    	if(filtered) {
		    		var textQueryParam = '';
					var fqParams = ESGF.localStorage.getAll('esgf_queryString');
					for(var key in fqParams) {
						if(key.search('query:') > -1) {
							textQueryParam = fqParams[key];
						}
						
					}
					queryString += textQueryParam;
		    	} else {
		    		queryString += 'query=*';
		    	}

				parentElement.find('a.wgetAllFiles_short').show();
				parentElement.find('span.wgetAllFiles_short').hide();
		    	
				ESGF.datacart.submitWGETScriptForm(queryString,file_ids,dataset_ids);
				
				
			});
			
			
			
			
		}
	});
})(jQuery);
		




	