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
	
	AjaxSolr.DataCartSRMWidget = AjaxSolr.AbstractWidget.extend({

		
		/**
		 * DOCUMENT ME
		 */
		beforeRequest: function() {
			
			var self = this;

			//grab the search constraints
	    	self.searchConstraints = ESGF.localStorage.toKeyArr('esgf_fq');
	    
	    	//grab all the keys from the datacart map and place in an array
	    	self.selected_arr = ESGF.localStorage.toKeyArr('dataCart');

			$('a.single_srm').die('click');
			$('a.srm_event').die('click');
			$('a.srm_dataset_event').die('click');
			
		},
	
		/**
		 * DOCUMENT ME
		 */
		afterRequest: function() {
		
			//event for sending the file id through
			$('a.single_srm').live('click',function() {

				var file_id = $(this).parent().find('.file_id').html().trim();
				
				var file_url = $(this).parent().find('.srm_urll').html().trim();
				
				var dataset_id = $(this).parent().parent().parent().parent().find('span.datasetId').html();
				
				
				//type
				//dataset
				//filtered
				//file
				//file_url
				
				var filtered = '';
				if($("input[id='datacart_filtered']").attr("checked") == undefined) {
					filtered = 'false';
				} else {
					filtered = 'true';
				}
				
				alert('filtered: ' + filtered);
				
				//var datasetArg = 'datasetId=' + selectedDocId;
				var typeArg = "type=File";
				var filteredArg = 'filtered=' + filtered;
				var fileArg = 'file_id=' + file_id;
				var fileUrlArg = 'file_url=' + file_url;
				var selectedDocIdArg = "dataset_id=" + dataset_id;
				
				
				//var srm_url = '/esgf-web-fe/srmview?datasetId=' + selectedDocId + '&type=Dataset';
		    	
				var srm_service_url = '/esgf-web-fe/srmview?' + typeArg + '&' + filteredArg + '&' + fileArg + '&' + fileUrlArg + '&' + selectedDocIdArg;
				
				alert('srm_service_url: ' + srm_service_url);
				//send request
		        jQuery('<form action="'+ srm_service_url +'" method="post">'+''+'</form>')
		        .appendTo('body').submit().remove();
		        
	    	});
	    	
	    	
			
			
			
			//for sending the dataset level srm request
			$('a.srm_dataset_event').live('click',function() {
				
				var selectedDocId = ($(this).parent().parent().find('span.datasetId').html());
				
				
				
				//srm params
				//type
				//dataset
				//filtered
				//file
				//file_url
				var filtered = '';
				if($("input[id='datacart_filtered']").attr("checked") == undefined) {
					filtered = 'false';
				} else {
					filtered = 'true';
				}
				var datasetArg = 'dataset_id=' + selectedDocId;
				var typeArg = "type=Dataset";
				var filteredArg = 'filtered=' + filtered;
				
				
				//filtered params
		    	//idStr (taken)
				//peerStr
				//technoteStr
				//fqParamStr
				var peerStr = ESGF.datacart.getIndividualPeer(selectedDocId);//getPeerStr();
				var technoteStr = ESGF.datacart.getTechnoteStr();
		    	var fqParamStr = ESGF.datacart.getFqParamStr();
				
		    	var peerStrArg = 'peerStr=' + peerStr;
		    	var technoteStrArg = 'technoteStr=' + technoteStr;
		    	var fqParamStrArg = 'fqParamStr=' + fqParamStr;
		    	var initialQueryArg = 'initialQuery=' + "true";
		    	var fileCounterArg = 'fileCounter=' + ESGF.setting.fileCounter;
		    	
		    
				
				
				
				//var srm_url = '/esgf-web-fe/srmview?datasetId=' + selectedDocId + '&type=Dataset';
		    	
				var srm_service_url = '/esgf-web-fe/srmview?' + 
										datasetArg + '&' + 
										typeArg + '&' + 
										filteredArg + '&' +
										peerStrArg + '&' +
										technoteStrArg + '&' +
										fqParamStrArg + '&' + 
										initialQueryArg + '&' +
										fileCounterArg;
				
				
				//send request
		        jQuery('<form action="'+ srm_service_url +'" method="post">'+''+'</form>')
		        .appendTo('body').submit().remove();
		        
		        
		        
		        
		        
		        
		        /*
				var srm_url = '/esgf-web-fe/srmview?datasetId=' + file_id + 
								'&peerStr=' + peerStr + 
								'&technoteStr=' + technoteStr + 
								'&fqParamStr=' + fqParamStr + 
								'&type=File';
		    	
				*/
		        
				//alert($(this).parent().find('span').html());
				/*
				
				
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
						
						//alert('data: ' + data);
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
										

										peerStr += ':8983/solr';
										
										var input = '';
										
								        //begin assembling queryString
							            //var queryString = 'type=create&id=' + datasetId + '&credential=' + go_credential;
								        var queryString = 'type=create&id=' + selectedDocId;
							            
								        
								        
								        jQuery.each(queryString.split('&'), function(){
								          var pair = this.split('=');
								          input+='<input type="hidden" name="'+ pair[0] +'" value="'+ pair[1] +'" />';
								        });
								        
								        
								        
								        var srm_url = '/esgf-web-fe/srmview?';
								    	srm_url += 'datasetId=' + selectedDocId;
								    	srm_url += '&type=Dataset';
								    	srm_url += '&peerStr=' + peerStr;
								    	srm_url += '&technoteStr=' + technoteStr;
								    	srm_url += '&fqParamStr=' + fqParamStr;
								    	
								    	
								        
								        //send request
								        jQuery('<form action="'+ srm_url +'" method="post">'+input+'</form>')
								        .appendTo('body').submit().remove();
										//alert($(this).parent().find('span').html());
										
						            	

									}
								});
							}
						}
					}
		    	
		    	
		    	});
		    	
		    	
		    	
		    	
		    	
		    	
		    	*/
		    	
			});

			
			
			$('a.srm_event').live('click',function() {
				alert('launch srm workflow here');
				
				/*
				//alert($(this).parent().find('span').html());
				var selectedDocId = ($(this).parent().find('span').html());
				
				var srm_url = '/esgf-web-fe/srmview?datasetId=' + selectedDocId + '&type=File';
		    	
				var input = '';
				
				//send request
		        jQuery('<form action="'+ srm_url +'" method="post">'+input+'</form>')
		        .appendTo('body').submit().remove();
				//alert($(this).parent().find('span').html());
				*/
			});
			
		}
	
	
	
	});
	
	
	
	
})(jQuery);




/*
//extract the dataset Id from the span tag
var selectedDocId = ($(this).parent().parent().find('span.datasetId').html()).trim();

//alert('selectedDocId: ' + selectedDocId);

var peerStr = getIndividualPeer(selectedDocId);
var technoteStr = getTechnoteStr();
var fqParamStr = getFqParamStr();

//
//var idStr = selectedDocId;
//
//var peerStr = getIndividualPeer(idStr);//getPeerStr();
//
//
//var technoteStr = getTechnoteStr();
//				
//var fqParamStr = getFqParamStr();
//
//
//
//var queryStr = {"idStr" : idStr, 
//		"peerStr" : peerStr, 
//		"technotesStr" : technoteStr, 
//		"showAllStr" : ESGF.setting.showAllContents, 
//		"fqStr" : fqParamStr, 
//		"initialQuery" : "true",
//   					"fileCounter" : ESGF.setting.fileCounter};


var srm_url = '/esgf-web-fe/srmview?';
srm_url += 'datasetId=' + selectedDocId;
srm_url += '&type=Dataset';
srm_url += '&peerStr=' + peerStr;
srm_url += '&technoteStr=' + technoteStr;
srm_url += '&fqParamStr=' + fqParamStr;





//var openid = $('span.footer_openid').html();

var input = '';

//begin assembling queryString
//var queryString = 'type=create&id=' + datasetId + '&credential=' + go_credential;


//assemble the input fields with the query string
for(var i=0;i<self.file_ids_arr.length;i++) {
	queryString += '&child_url=' + self.grid_urls_arr[i] + '&child_id=' + self.file_ids_arr[i];
}
jQuery.each(queryString.split('&'), function(){
  var pair = this.split('=');
  input+='<input type="hidden" name="'+ pair[0] +'" value="'+ pair[1] +'" />';
});



//send request
jQuery('<form action="'+ srm_url +'" method="post">'+input+'</form>')
.appendTo('body').submit().remove();
//alert($(this).parent().find('span').html());
*/