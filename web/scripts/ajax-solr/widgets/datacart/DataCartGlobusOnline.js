/* Copyright � 2011 , UT-Battelle, LLC All rights reserved
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
	
	
	file_ids_arr: null,
	
	grid_urls_arr: null,
	
	
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

    	self.file_ids_arr = new Array();
    	self.grid_urls_arr = new Array();
    	
    	
		//kill the go gridftp
		$('.go_individual_gridftp_short').die('click');
		    
		//kill the all files globus online
		$('a.globusOnlineAllFiles_short').die('click');
	},
	
	
	checkDatasetAccess: function (accessType,datasetId) {
		
		
		
		var datacart = ESGF.localStorage.getAll('dataCart');
	
		
		var hasAccess = false;
		
		if(datacart[datasetId] != undefined) {
			if(datacart[datasetId]['access'] != undefined) {
				var access = new String(datacart[datasetId]['access']);
				if(access.search(accessType) > -1) {
					hasAccess = true;
				}
			} else {
				hasAccess = true;
			}
		
		}
		
		
		return hasAccess;
		
	},
	
	
	checkPostQueryAccess: function (selectedDocId,accessType) {
		
		var self = this;
		
		var hasAccess = false;

    	var idStr = selectedDocId;
    	var peerStr = ESGF.datacart.getPeerStr();
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
			async: false,
			type: "GET",
			data: queryStr,
			dataType: 'json',
			success: function(data) {
				
				if(data.docs.doc.files.file != undefined) {
					
					//if there is only one file in the data set
					if(data.docs.doc.files.file.length == undefined) {
					
						var file_arr = new Array();

					    file_arr.push(data.docs.doc.files.file);

					    data.docs.doc.files['file'] = file_arr;

					}
					
					var file = data.docs.doc.files.file[0];
					
					for(var j=0;j<file.services.service.length;j++) {
						if(file.services.service[j] == accessType) {
							hasAccess = true;
						}
					}
					
					
				}
			}, 
			error: function () {
				alert('this error');
			}
    	});
    	
    	return hasAccess;
		
	},
	
	
	writeNonDatasetMessage: function (non_grid_arr) {
		
		if(non_grid_arr.length > 0) {
    		var message = 'The dataset(s) ';
    		for(var j=0;j<non_grid_arr.length;j++) {
    			if(j == non_grid_arr.length-1) {
    				message += non_grid_arr[j];
    			} else {
	    			message += non_grid_arr[j] + ' and ';
    			}
    		}
    		message += ' are not available using Globus Online.  Please try another download option (e.g WGET) for those datasets.';
    		alert(message);
    	}
	},
	
	/**
	 * DOCUMENT ME!
	 * @param datasetId
	 */
	getFileAndGridInfo: function(datasetId) {
		
		var self = this;
		
		var idStr = datasetId;
    	var peerStr = ESGF.datacart.getPeerStr();
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
			async: false,
			type: "GET",
			data: queryStr,
			dataType: 'json',
			success: function(data) {
				var fileCount = data.docs.doc['count'];
    			
				
				if(data.docs.doc.files.file != undefined) {
					
					//if there is only one file in the data set
					if(data.docs.doc.files.file.length == undefined) {
			
						var file_arr = new Array();

						file_arr.push(data.docs.doc.files.file);

						data.docs.doc.files['file'] = file_arr;

					}
			
					
					
					for(var i=0;i<data.docs.doc.files.file.length;i++){
						var file = data.docs.doc.files.file[i];
						
						
						self.file_ids_arr.push(file.fileId);
				

						for(var j=0;j<file.services.service.length;j++) {
							if(file.services.service[j] == 'GridFTP') {
								self.grid_urls_arr.push(file.urls.url[j]);
							}
						}
				
				
					}

					if(fileCount > 10) {
					
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
							async: false,
							data: queryStr,
							dataType: 'json',
							success: function(data) {
								for(var i=0;i<data.docs.doc.files.file.length;i++){
									var file = data.docs.doc.files.file[i];
									self.file_ids_arr.push(file.fileId);
									
									for(var j=0;j<file.services.service.length;j++) {
										if(file.services.service[j] == 'GridFTP') {
											self.grid_urls_arr.push(file.urls.url[j]);
										}
									}
									
								}

								
								
							},
							error: function(jqXHR, textStatus, errorThrown) {
								alert('error: ' + errorThrown);
							}
						});
					
					}
					
					
					
					
				}
			},
			error: function() {
				alert('error finding gridFTP');
			}
    	});
	},
	
	
	
	
	
	
	/**
	 * DOCUMENT ME
	 */
	afterRequest: function() {
		
		var self = this;
		

    	
		
		$('a#uber_GO_script_short').click(function() {

			alert('accessing globus online');
			
			/*
	    	self.file_ids_arr = new Array();
	    	self.grid_urls_arr = new Array();
			

			var datacart = ESGF.localStorage.getAll('dataCart');
			
			//iterate over the selected array of datasets in the data cart
        	//grab all the keys from the datacart map and place in an array
	    	self.selected_arr = ESGF.localStorage.toKeyArr('dataCart');

	    	var non_grid_arr = new Array();
	    	
	    	for(var i=0;i<self.selected_arr.length;i++) {
	    		
	    		
	    		var datasetId = self.selected_arr[i];
				var accessType = 'GridFTP';
				var hasGridFTP = self.checkDatasetAccess(accessType,datasetId);
	    	
				//if dataset has access to gridftp
				if(hasGridFTP) {
					
					//if the dataset access is "undefined", we must do additional processing
		    		if(datacart[datasetId]['access'] == undefined) {
		    			
		    			var hasAccess = self.checkPostQueryAccess(datasetId,accessType);
		    			if(hasAccess) {
		    				
		    				self.getFileAndGridInfo(datasetId);
		    				
		    			} else {

			    			non_grid_arr.push(datasetId);
		    			}
		    			
		    		} 
		    		//otherwise all is good, grab the file_ids and grid_urls
		    		else {
		    			self.getFileAndGridInfo(datasetId);
		    			
		    		}
				}
				//otherwise add to the list of datasets in the alert message 
				else {
	    			non_grid_arr.push(datasetId);
				}
	    	
	    	}
	    	
	    	//write a message if there are are
	    	if(non_grid_arr.length > 0) {
		    	self.writeNonDatasetMessage(non_grid_arr); 
	    	}
	    	
	    	var globus_url = '/esgf-web-fe/goauthview1';

            var openid = $('span.footer_openid').html();
            var go_credential = ESGF.localStorage.get('GO_Credential',openid);
	        //begin assembling queryString
            var queryString = 'type=create&id=' + datasetId + '&credential=' + go_credential;
	        
	        //assemble the input fields with the query string
	        for(var i=0;i<self.file_ids_arr.length;i++) {
	        	queryString += '&child_url=' + self.grid_urls_arr[i] + '&child_id=' + self.file_ids_arr[i];
	        }
	        var input = '';
	        jQuery.each(queryString.split('&'), function(){
	          var pair = this.split('=');
	          input+='<input type="hidden" name="'+ pair[0] +'" value="'+ pair[1] +'" />';
	        });
	        
	        //send request
	        jQuery('<form action="'+ globus_url +'" method="post">'+input+'</form>')
	        .appendTo('body').submit().remove();
	    	
	        
	        
	        
	        
	        */
	        
	        
	    	
	    	
		});
	    	
		
		
		
		$('a.globusOnlineAllFiles_short').live('click',function() {

	    	self.file_ids_arr = new Array();
	    	self.grid_urls_arr = new Array();

	    	var non_grid_arr = new Array();

			var datacart = ESGF.localStorage.getAll('dataCart');
			
			var parentElement = $(this).parent();
			
			parentElement.find('a.globusOnlineAllFiles_short').hide();
			parentElement.find('span.globusOnlineAllFiles_short').show();

	    	
			var datasetId = ($(this).parent().parent().find('span.datasetId').html());
			
			var accessType = 'GridFTP';
			var hasGridFTP = self.checkDatasetAccess(accessType,datasetId);
    	
			//if dataset has access to gridftp
			if(hasGridFTP) {
				
				//if the dataset access is "undefined", we must do additional processing
	    		if(datacart[datasetId]['access'] == undefined) {
	    			
	    			var hasAccess = self.checkPostQueryAccess(datasetId,accessType);
	    			if(hasAccess) {
	    				
	    				self.getFileAndGridInfo(datasetId);
	    				
	    			} else {

		    			non_grid_arr.push(datasetId);
	    			}
	    			
	    		} 
	    		//otherwise all is good, grab the file_ids and grid_urls
	    		else {
	    			self.getFileAndGridInfo(datasetId);
	    			
	    		}
			}
			//otherwise add to the list of datasets in the alert message 
			else {
    			non_grid_arr.push(datasetId);
			}
			
			
			//write a message if there are are
	    	if(non_grid_arr.length > 0) {
		    	self.writeNonDatasetMessage(non_grid_arr); 
	    	}
	    	
	    	var globus_url = '/esgf-web-fe/goauthview1';

            var openid = $('span.footer_openid').html();
            var go_credential = ESGF.localStorage.get('GO_Credential',openid);
	        //begin assembling queryString
            var queryString = 'type=create&id=' + datasetId + '&credential=' + go_credential;
	        
	        //assemble the input fields with the query string
	        for(var i=0;i<self.file_ids_arr.length;i++) {
	        	queryString += '&child_url=' + self.grid_urls_arr[i] + '&child_id=' + self.file_ids_arr[i];
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
		
		

		/**
	     * Grabs individual files and sends that information to the Globus Online view
	     */
	    $('.go_individual_gridftp_short').live('click',function(e) {
	    	
	    	var selectedDocId = ($(this).parent().parent().parent().parent().find('span.datasetId').html());
	    	
	    	//gather the ids and the urls for download
	    	var ids   = new Array();
	        var values = new Array();
	    	
	    	//var file_id = $(this).parent().parent().parent().find('input').attr('value');
    		//var grid_url = $(this).parent().parent().find('span.gridftp').html();
    		var file_id = $(this).parent().find('span.file_id').html();
    		var grid_url = $(this).parent().find('span.globus_url').html();
	        
    		ids.push(file_id);
	        values.push(grid_url);
    		
	    	
    		var globus_url = '/esgf-web-fe/goauthview1';
	        
    		var openid = $('span.footer_openid').html();
    		var go_credential = ESGF.localStorage.get('GO_Credential',openid);
    		//alert('openid: ' + openid + ' GO credential: ' + go_credential);
    		
	        //begin assembling queryString
	        var queryString = 'type=create&id=' + selectedDocId + '&credential=' + go_credential;

  		  

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
		
		
		
	}




});

})(jQuery);



/*
var gridFTPExists_arr = new Array();

//first check if gridftp endpoints exist for this dataset
//this will go away once the esg-search feature is in place
for(var i=0;i<self.selected_arr.length;i++) {
    
	gridFTPExists_arr.push(false);
	
	var selectedDocId = self.selected_arr[i];//self.selected_arr[i];

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
	

	var gridFTPExists = false;
	
	
	$.ajax({
		url: '/esgf-web-fe/solrfileproxy2/datacart',
		global: false,
		async: false,
		type: "GET",
		data: queryStr,
		dataType: 'json',
		success: function(data) {
			
			if(data.docs.doc.files.file != undefined) {
				
				//if there is only one file in the data set
				if(data.docs.doc.files.file.length == undefined) {
				
					var file_arr = new Array();

				    file_arr.push(data.docs.doc.files.file);

				    data.docs.doc.files['file'] = file_arr;

				}
				
				var file = data.docs.doc.files.file[0];
				
				for(var j=0;j<file.services.service.length;j++) {
					if(file.services.service[j] == 'GridFTP') {
						gridFTPExists_arr[i] = true;
					}
				}
				
			}
		},
		error: function() {
			alert('error finding gridFTP');
		}
	});
	
	
	
	
}


for(var i=0;i<self.selected_arr.length;i++) {
	
	if(gridFTPExists_arr[i]) {
		
		var selectedDocId = self.selected_arr[i];//self.selected_arr[i];
    	
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
			async: false,
			type: "GET",
			data: queryStr,
			dataType: 'json',
			success: function(data) {
				var fileCount = data.docs.doc['count'];
		
		
		
				if(data.docs.doc.files.file != undefined) {
			
					//if there is only one file in the data set
					if(data.docs.doc.files.file.length == undefined) {
			
						var file_arr = new Array();

						file_arr.push(data.docs.doc.files.file);

						data.docs.doc.files['file'] = file_arr;

					}
			
					for(var i=0;i<data.docs.doc.files.file.length;i++){
						var file = data.docs.doc.files.file[i];
						file_ids.push(file.fileId);
				

						for(var j=0;j<file.services.service.length;j++) {
							if(file.services.service[j] == 'GridFTP') {
								grid_urls.push(file.urls.url[j]);
							}
						}
				
				
					}

					if(fileCount > 10) {
					
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
							async: false,
							data: queryStr,
							dataType: 'json',
							success: function(data) {
								for(var i=0;i<data.docs.doc.files.file.length;i++){
									var file = data.docs.doc.files.file[i];
									file_ids.push(file.fileId);
									
									for(var j=0;j<file.services.service.length;j++) {
										if(file.services.service[j] == 'GridFTP') {
											grid_urls.push(file.urls.url[j]);
										}
									}
									
								}

								
								
							},
							error: function(jqXHR, textStatus, errorThrown) {
								alert('error: ' + errorThrown);
							}
						});
					
					}
					
			    	
				}
			},
			error: function() {
				alert('error');
			}
		
		});

	}

}

var globus_url = '/esgf-web-fe/goauthview1';

var openid = $('span.footer_openid').html();
var go_credential = ESGF.localStorage.get('GO_Credential',openid);
//begin assembling queryString
var queryString = 'type=create&id=' + selectedDocId + '&credential=' + go_credential;

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
*/
	


/* old
var datacart = ESGF.localStorage.getAll('dataCart');
var hasAccess = false;

if(datacart[datasetId] != undefined) {
	if(datacart[datasetId]['access'] != undefined) {
		var access = new String(datacart[datasetId]['access']);
		if(access.search(accessType) > -1) {
			hasAccess = true;
		}
	}

}
return hasAccess;
*/










/*
//gather the file_ids
var file_ids   = new Array();

//gather the grid_urls
var grid_urls = new Array();


//iterate over the selected array of datasets in the data cart
//grab all the keys from the datacart map and place in an array
self.selected_arr = ESGF.localStorage.toKeyArr('dataCart');

var non_grid_arr = new Array();

for(var i=0;i<self.selected_arr.length;i++) {
	var datasetId = self.selected_arr[i];
	var accessType = 'GridFTP';
	var hasGridFTP = self.checkDatasetAccess(accessType,datasetId);
	
	//alert('in uber script gridftp ' + hasGridFTP);
	
	
	
	if(hasGridFTP) {
		
		//'need to check bc it is undefined - need to find GRID FTP');
		
		
		var selectedDocId = self.selected_arr[i];

		var datacart = ESGF.localStorage.getAll('dataCart');
		
		
		if(datacart[datasetId]['access'] == undefined) {
    		//alert('need to check bc it is undefined - need to find GRID FTP');
    		
    		var accessType = 'GridFTP';
			
    		var postQueryHasGridFTP = self.checkPostQueryAccess(selectedDocId,accessType)
    		
    		//alert('checkPostAccess? ' + postQueryHasGridFTP);
    		
    		if(postQueryHasGridFTP) {
    			//alert('good to go!');
    			
    			
    			
    			var selectedDocId = self.selected_arr[i];
    	    	
    			
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
    				async: false,
    				type: "GET",
    				data: queryStr,
    				dataType: 'json',
    				success: function(data) {
    					var fileCount = data.docs.doc['count'];
    	    			
    					
    					if(data.docs.doc.files.file != undefined) {
    						
    						//if there is only one file in the data set
    						if(data.docs.doc.files.file.length == undefined) {
					
    							var file_arr = new Array();

    							file_arr.push(data.docs.doc.files.file);

    							data.docs.doc.files['file'] = file_arr;

    						}
					
    						
    						
    						for(var i=0;i<data.docs.doc.files.file.length;i++){
    							var file = data.docs.doc.files.file[i];
    							
    							
    							file_ids.push(file.fileId);
						

    							for(var j=0;j<file.services.service.length;j++) {
    								if(file.services.service[j] == 'GridFTP') {
    									grid_urls.push(file.urls.url[j]);
    								}
    							}
						
						
    						}

    						if(fileCount > 10) {
    						
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
									async: false,
									data: queryStr,
									dataType: 'json',
									success: function(data) {
										for(var i=0;i<data.docs.doc.files.file.length;i++){
											var file = data.docs.doc.files.file[i];
											file_ids.push(file.fileId);
											
											for(var j=0;j<file.services.service.length;j++) {
												if(file.services.service[j] == 'GridFTP') {
													grid_urls.push(file.urls.url[j]);
												}
											}
											
										}

										
										
									},
									error: function(jqXHR, textStatus, errorThrown) {
										alert('error: ' + errorThrown);
									}
								});
    						
    						}
    						
    						
    						
    						
    					}
    				},
    				error: function() {
    					alert('error finding gridFTP');
    				}
    	    	});
    	    	
    	    	
    	    	
    	    	
    			
    			
    			
    			
    		} else {
    			
    			non_grid_arr.push(datasetId);
    			
    		}
    		
    		
		} else {
			
			

			
			var selectedDocId = self.selected_arr[i];
	    	
			
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
				async: false,
				type: "GET",
				data: queryStr,
				dataType: 'json',
				success: function(data) {
					var fileCount = data.docs.doc['count'];
	    			
					
					if(data.docs.doc.files.file != undefined) {
						
						//if there is only one file in the data set
						if(data.docs.doc.files.file.length == undefined) {
				
							var file_arr = new Array();

							file_arr.push(data.docs.doc.files.file);

							data.docs.doc.files['file'] = file_arr;

						}
				
						
						
						for(var i=0;i<data.docs.doc.files.file.length;i++){
							var file = data.docs.doc.files.file[i];
							
							
							file_ids.push(file.fileId);
					

							for(var j=0;j<file.services.service.length;j++) {
								if(file.services.service[j] == 'GridFTP') {
									grid_urls.push(file.urls.url[j]);
								}
							}
					
					
						}

						if(fileCount > 10) {
						
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
								async: false,
								data: queryStr,
								dataType: 'json',
								success: function(data) {
									for(var i=0;i<data.docs.doc.files.file.length;i++){
										var file = data.docs.doc.files.file[i];
										file_ids.push(file.fileId);
										
										for(var j=0;j<file.services.service.length;j++) {
											if(file.services.service[j] == 'GridFTP') {
												grid_urls.push(file.urls.url[j]);
											}
										}
										
									}

									
									
								},
								error: function(jqXHR, textStatus, errorThrown) {
									alert('error: ' + errorThrown);
								}
							});
						
						}
						
						
						
						
					}
				},
				error: function() {
					alert('error finding gridFTP');
				}
	    	});
			
		}
		
		
    	
	} else {
		non_grid_arr.push(datasetId);
	}

	
	
}



if(non_grid_arr.length > 0) {
	var message = 'The dataset(s) ';
	for(var j=0;j<non_grid_arr.length;j++) {
		if(j == non_grid_arr.length-1) {
			message += non_grid_arr[j];
		} else {
			message += non_grid_arr[j] + ' and ';
		}
	}
	message += ' are not available using Globus Online.  Please try another download option (e.g WGET) for those datasets.';
	alert(message);
}

var globus_url = '/esgf-web-fe/goauthview1';

var openid = $('span.footer_openid').html();
var go_credential = ESGF.localStorage.get('GO_Credential',openid);
//begin assembling queryString
var queryString = 'type=create&id=' + selectedDocId + '&credential=' + go_credential;

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


*/





/*
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
		
		var fileCount = data.docs.doc['count'];
		
		
		if(data.docs.doc.files.file != undefined) {
		
			var gridFTPFound = false;
			//if there is only one file in the data set
			//
			if(data.docs.doc.files.file.length == undefined) {

				var file_arr = new Array();

				file_arr.push(data.docs.doc.files.file);

				data.docs.doc.files['file'] = file_arr;

			}
			
			for(var i=0;i<data.docs.doc.files.file.length;i++){
				var file = data.docs.doc.files.file[i];
				file_ids.push(file.fileId);
				

				for(var j=0;j<file.services.service.length;j++) {
					if(file.services.service[j] == 'GridFTP') {
						grid_urls.push(file.urls.url[j]);
						gridFTPFound = true;
					}
				}
				
				
			}
			
			//alert('gridFTPFound: ' + gridFTPFound);
			
			if(gridFTPFound) {
				
				//first need to check if more files are included with this dataset
				if(fileCount > 10) {
					
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
						async: false,
						data: queryStr,
						dataType: 'json',
						success: function(data) {
							for(var i=0;i<data.docs.doc.files.file.length;i++){
								var file = data.docs.doc.files.file[i];
								file_ids.push(file.fileId);
								
								for(var j=0;j<file.services.service.length;j++) {
									if(file.services.service[j] == 'GridFTP') {
										grid_urls.push(file.urls.url[j]);
									}
								}
								
							}

							
							
						},
						error: function(jqXHR, textStatus, errorThrown) {
							alert('error: ' + errorThrown);
						}
					});
					
					
					
				}
				
				
				
				var globus_url = '/esgf-web-fe/goauthview1';

				var openid = $('span.footer_openid').html();
				var go_credential = ESGF.localStorage.get('GO_Credential',openid);
		        //begin assembling queryString
                                    var queryString = 'type=create&id=' + selectedDocId + '&credential=' + go_credential;
		        
		        //var queryString = 'type=create&id=' + selectedDocId;


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

				parentElement.find('a.globusOnlineAllFiles_short').show();
				parentElement.find('span.globusOnlineAllFiles_short').hide();
				alert('Globus Online is not applicable to this dataset.  Please try WGET option.');
			}

			
			
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
*/

