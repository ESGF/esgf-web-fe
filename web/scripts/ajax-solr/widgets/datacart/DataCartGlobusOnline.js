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

AjaxSolr.DataCartGlobusOnlineWidget = AjaxSolr.AbstractWidget.extend({
	
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
	beforeRequest: function () {
		var self = this;

    	//grab the search constraints
    	self.searchConstraints = ESGF.localStorage.toKeyArr('esgf_fq');
    	
		//add the spinning wheel in case there is a delay in loading the items in the data cart
        
    	//CHANGE ME!!!
    	//$(this.target).html($('<img/>').attr('src', 'images/ajax-loader.gif'));
    	
    	//grab all the keys from the datacart map and place in an array
    	self.selected_arr = ESGF.localStorage.toKeyArr('dataCart');

		//kill the go gridftp
		$('.go_individual_gridftp_short').die('click');
		    
		//kill the all files globus online
		$('a.globusOnlineAllFiles_short').die('click');
	},
	
	
	/**
	 * DOCUMENT ME
	 */
	afterRequest: function() {
		
		var self = this;
		
		
		/**
	     * Grabs individual files and sends that information to the Globus Online view
	     */
	    $('.go_individual_gridftp_short').live('click',function(e) {
	    	
	    	var selectedDocId = ($(this).parent().parent().parent().parent().find('span.datasetId').html());
	    	
	    	//gather the ids and the urls for download
	    	var ids   = new Array();
	        var values = new Array();
	    	
	    	var file_id = $(this).parent().parent().parent().find('input').attr('value');
    		var grid_url = $(this).parent().parent().find('span.gridftp').html();
    		
    		ids.push(file_id);
	        values.push(grid_url);
    		
	    	
    		var globus_url = '/esgf-web-fe/goformview1';
	        
	        //begin assembling queryString
	        var queryString = 'type=create&id=' + selectedDocId;


	        //assemble the input fields with the query string
	        for(var i=0;i<ids.length;i++) {
	        	queryString += '&child_url=' + values[i] + '&child_id=' + ids[i];
	        }
	        var input = '';
	        jQuery.each(queryString.split('&'), function(){
	          var pair = this.split('=');
	          input+='<input type="hidden" name="'+ pair[0] +'" value="'+ pair[1] +'" />';
	        });
	        
	        //send request
	        jQuery('<form action="'+ globus_url +'" method="post">'+input+'</form>')
	        .appendTo('body').submit().remove();
	        
	        
	    });
		
		$('a.globusOnlineAllFiles_short').live('click',function() {
			
			
			var parentElement = $(this).parent();
			
			
			
			parentElement.find('a.globusOnlineAllFiles_short').hide();
			parentElement.find('span.globusOnlineAllFiles_short').show();

			var selectedDocId = ($(this).parent().parent().find('span.datasetId').html());
			
        	//gather the ids and the urls for download
        	var file_ids   = new Array();
        	var grid_urls   = new Array();
        	
        	var idStr = selectedDocId;
			
			var peerStr = getPeerStr();
			var technoteStr = getTechnoteStr();
							
	    	var fqParamStr = getFqParamStr();
	    	
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
							

							for(var j=0;j<file.services.service.length;j++) {
								if(file.services.service[j] == 'GridFTP') {
									grid_urls.push(file.urls.url[j]);
								}
							}
							
							
						}

						var globus_url = '/esgf-web-fe/goformview1';
				        
				        //begin assembling queryString
				        var queryString = 'type=create&id=' + selectedDocId;


				        //assemble the input fields with the query string
				        for(var i=0;i<file_ids.length;i++) {
				        	queryString += '&child_url=' + grid_urls[i] + '&child_id=' + file_ids[i];
				        }
				        var input = '';
				        jQuery.each(queryString.split('&'), function(){
				          var pair = this.split('=');
				          input+='<input type="hidden" name="'+ pair[0] +'" value="'+ pair[1] +'" />';
				        });
				        
				        //send request
				        jQuery('<form action="'+ globus_url +'" method="post">'+input+'</form>')
				        .appendTo('body').submit().remove();
						
					} else {
						alert('There are no files in this dataset that match the search criteria.');

						parentElement.find('a.globusOnlineAllFiles_short').show();
						parentElement.find('span.globusOnlineAllFiles_short').hide();
						
					}
					
					
					
					
					
				},
				error: function() {
					alert('error in getting files for globus online');
					parentElement.find('a.globusOnlineAllFiles_short').show();
					parentElement.find('span.globusOnlineAllFiles_short').hide();

					
				}
	    	});
	    	
	    	
        	
		});
		
		
		
		
	}




});

})(jQuery);
	