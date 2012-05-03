/*****************************************************************************
 * Copyright © 2011 , UT-Battelle, LLC All rights reserved
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
 *
 * fwang2@ornl.gov
 * harneyjf@ornl.gov
 */


(function ($) {


AjaxSolr.DCEventsWidget = AjaxSolr.AbstractWidget.extend({

	/**
	 * An array of dataset ids selected by the user in the data cart
	 */
	selected_arr: null,
	
	init: function() {
		
		
		
	},
	
	beforeRequest: function () {

		//kill any live events that are associated with the datacart widget
		
		//kill tab select
		$('#myTabs').die('tabsselect');
		
		//kill the remove all event
		$("input#remove_all_short").die('click');
		
		//kill the uber script
		$("input#uber_script_short").die('click');
		
		//kill the show all files link
		$('a.showAllFiles_short').die('click');
		
		//kill the view more files link
		$('a.view_more_files_short').die('click');
		
			
		//kill the all files wget	
		$('a.wgetAllFiles_short').die('click');
				
		
		$('.remove_dataset_short').die('click');
		

    	$("input[name='datacart_filter']").die('change');
	},
	
	afterRequest: function () {
		
		var self = this;

		//grab all the keys from the datacart map and place in an array
    	self.selected_arr = ESGF.localStorage.toKeyArr('dataCart');

    	
		/* live datacart events */
        
    	

		/*---------Begin header level events---------*/
    	
		/**
	     * Event for tab selection (as of now, toggling between "Results" and "Datacart")
	     * If this is done, then a request should be sent to the manager
	     */
		$('#myTabs').live('tabsselect', function(event, ui) {
			if (ui.index == 1) {
								
				//leave the pagination on
				ESGF.setting.paginationOn = 'true';	
				
				
				Manager.doRequest(0);
				
			}
			
		});
		
		/**
		 * When a user selects a new radio button the global variable "showAllContents" should be changed
		 * (true if all files selected, false if filtered selected)
		 */
		$("input[name='datacart_filter']").change(	function() { 
			
        	
				if($("input[id='datacart_filtered']").attr("checked")) { 
					ESGF.setting.showAllContents = 'false';
				} else {
					ESGF.setting.showAllContents = 'true';
				}

				Manager.doRequest(0);
				
		}); 
		

		/**
		 * The "remove all" button event (will clear the data cart)
		 * Specifically it clears the localstorage entry for 'dataCart'
		 */
		$("input#remove_all_short").live('click', function() {

			
			
			for(var i=0;i<self.selected_arr.length;i++) {
				var selectedDocId = self.selected_arr[i];

	        	//remove the dataset from the localStorage
	        	ESGF.localStorage.remove('dataCart',selectedDocId);

	        	//change from remove from cart to add to cart
	        	$('a#ai_select_'+ selectedDocId.replace(/\./g, "_")).html('Add To Cart');
	        	
	        	
			}    
			

        	//re-issue request to search api
        	Manager.doRequest(0);
			
		});
		
		
		
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
		$("input#uber_script_short").live('click', function() {

			alert('uber script');
			
			/*
			//gather the file_ids
        	var file_ids   = new Array();
            
        	//gather the dataset_ids
        	var dataset_ids = new Array();
        	
        	//iterate over the selected array of datasets in the data cart
        	
        	
            for(var i=0;i<ESGF.localStorage.toKeyArr('dataCart').length;i++) {
            
            	var selectedDocId = ESGF.localStorage.toKeyArr('dataCart')[i];//self.selected_arr[i];
            	
            	var selectedDocCount = $('span.datasetCount_'+replaceChars(selectedDocId)).html();
            	
            	//if the count is greater than 10, check to see if the additional rows have been not been expanded for this dataset
            	//if not (null) then need an extra ajax call to get the rest of the file ids
            	var isAdded = $('tr.addedrow_' + replaceChars(selectedDocId)).html();//$(this).parent().parent().parent().find('tr.addedrow_' + replaceChars(selectedDocId)).html();
            	
            	dataset_ids.push(selectedDocId);
            	
            	if(isAdded == null && selectedDocCount > 10) {
            	
            		
            		$('tr.file_rows_'+ replaceChars(selectedDocId)).find(':checkbox:checked').each( function(index) {
            			var file_id = $(this).parent().find('input').attr('value');
            			file_ids.push(file_id);
            		});
            		
            		
            		
            		//get all of the search constraints (fq params)
                	var fqParamStr = getFqParamStr();
                	
                	//get the peers
                	var peerStr = getPeerStr();
                	
                	//get the technotes
                	var technoteStr = getTechnoteStr();
            		
                	//the id str is only one file
                	var idStr = selectedDocId;
                	
                	
            		//assemble the query string
                	var queryStr = {"idStr" : idStr, 
        					"peerStr" : peerStr, 
        					"technotesStr" : technoteStr, 
        					"showAllStr" : ESGF.setting.showAllContents, 
        					"fqStr" : fqParamStr, 
        					"initialQuery" : "true"};

                	
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
            		$('tr.file_rows_'+ replaceChars(selectedDocId)).find(':checkbox:checked').each( function(index) {
            			var file_id = $(this).parent().find('input').attr('value');
            			file_ids.push(file_id);
            		});
            		
            		
            	}
            }
            
            var queryString = '/esg-search/wget/?';
        	
            var constraintCount = 0;
            
            queryString = addConstraintsToWGETQueryString(queryString);
        	
            submitWGETScriptForm(queryString,file_ids,dataset_ids);
			*/
			
		});
		
		
		/*---------End header level events----------*/
		
		
		/*---------Begin dataset level events---------*/
		
		$('.remove_dataset_short').live ('click', function(e) {

	    	var selectedDocId = ($(this).parent().parent().find('span.datasetId').html()).trim();
			
	    	//remove the dataset from the localStorage
        	ESGF.localStorage.remove('dataCart',selectedDocId);

        	$('a#ai_select_'+ selectedDocId.replace(/\./g, "_")).html('Add To Cart');
        	
        	//re-issue request to search api
        	Manager.doRequest(0);
        	
	    });
		
		
		
		
	    $('a.showAllFiles_short').live('click',function() {
			
	    	
	    	
	    	//extract the dataset Id from the span tag
			var selectedDocId = ($(this).parent().parent().find('span.datasetId').html()).trim();
			
			
			//change verbage of the expand link
			if(this.innerHTML === "Collapse") {
				
				
				var idStr = selectedDocId;
				
				
				//collapse the first 10
				var removeTag = 'remove_' + 'file_rows_' + replaceChars(idStr);
				$('.'+removeTag).remove();
				
				
				
				
				
			} else {

				
				var idStr = selectedDocId;
				
				var peerStr = getPeerStr();
				var technoteStr = getTechnoteStr();
								
		    	var fqParamStr = getFqParamStr();
		    	
		    	
		    	
		    	var queryStr = {"idStr" : idStr, 
						"peerStr" : peerStr, 
						"technotesStr" : technoteStr, 
						"showAllStr" : ESGF.setting.showAllContents, 
						"fqStr" : fqParamStr, 
						"initialQuery" : "true"}; 
				
		    	
		    	//NEED TO FIX THIS!!!!
		    	selectedDocId = 'aa.gov';
		    	selectedDocId = Url.encode(selectedDocId);
		    	var url = '/esgf-web-fe/solrfileproxy2/datacart/'+selectedDocId;
				
				
				
				$.ajax({
					url: url,
					global: false,
					type: "POST",
					data: queryStr,
					dataType: 'json',
					success: function(data) {
						
						var tagid = 'file_rows_' + replaceChars(idStr);
						
						
						if(data.doc.files.file == undefined) {
							
							var appendedFiles = '';
							appendedFiles += '<tr class="remove_' + tagid + '">';
							appendedFiles += '<td style="width: 40px"></td>';
							appendedFiles += '<td style="width: 300px;font-size:13px">'
							appendedFiles += '	<div style="word-wrap: break-word;font-weight:bold;color:gray;font-style:italic;font-weight:bold;color:gray;margin-top:5px" class="datasetId">';
							
							appendedFiles += 'NOTE: There are no files in this dataset that match the search criteria';
							
							appendedFiles += '</div>';
							appendedFiles += '</td>';
							appendedFiles += '</tr>';
								
							$('.'+tagid).after(appendedFiles);
							
							
							
							
						} else {
							var fileLength = data.doc.files.file.length;

							
							if(fileLength == undefined) {
								var fileArray = new Array();
								fileArray.push(data.doc.files.file);
								data.doc.files['file'] = fileArray;
								fileLength = data.doc.files.file.length;
							}

							
							var fileDownloadTemplate = data.doc;
							
							
							for(var j=0;j<data.doc.files.file.length;j++) {
								if(data.doc.files.file[j].services.service == 'HTTPServer' ||
				    					data.doc.files.file[j].services.service == 'OPENDAP' || 
				    					data.doc.files.file[j].services.service == 'SRM' ||
				    					data.doc.files.file[j].services.service == 'GridFTP') {
									
									var serviceArray = new Array();
			    					serviceArray.push(data.doc.files.file[j].services.service);
			    					data.doc.files.file[j].services['service'] = serviceArray;
			    					
			    					var urlsArray = new Array();
			    					urlsArray.push(data.doc.files.file[j].urls.url);
			    					data.doc.files.file[j].urls['url'] = urlsArray;
			    					
			    					var mimesArray = new Array();
			    					mimesArray.push(data.doc.files.file[j].mimes.mime);
			    					data.doc.files.file[j].mimes['mime'] = mimesArray;
									
								}
							}
							

							var appendedFiles = '';
							
							for(var i=0;i<fileLength;i++) {
								
								
								appendedFiles += '<tr class="remove_' + tagid + '">';
									
								//appendedFiles += '<td>a</td>';
								appendedFiles += '<td style="width: 40px">' +
												 '<input style="margin-left: 10px;display:none"' + 
									   					'class="fileLevel"' + 
									   					'type="checkbox"' + 
									   					'class="fileId"' + 
									   					'id="${fileId}"' + 
									   					'checked="true"' + 
									   					'/>' +
									   					'</td>';
								
								
								appendedFiles += '<td style="width: 425px;padding-left:10px;font-size:11px;">' +
												 '<div style="word-wrap: break-word;">' + 
												 '<span style="font-weight:bold">' + data.doc.files.file[i].fileId + '</span>' +
												 '	<br />' + 
												 '<span style="font-style:italic">' + 'tracking_id: ' + data.doc.files.file[i].tracking_id + '</span>' +
												 '  <br />' +
												 '<span style="font-style:italic">checksum: ' + data.doc.files.file[i].checksum + ' (' + data.doc.files.file[i].checksum_type + ')' + '</span>' +
												 '</div>' +
												 '</td>';
								
								
								appendedFiles += '<td style="float-right;font-size:11px;text-align:right">';
								
								for(var j=0;j<data.doc.files.file[i].services.service.length;j++) {
									var service = data.doc.files.file[i].services.service[j];
									var url = data.doc.files.file[i].urls.url[j];
									if(service == 'HTTPServer') {
										service = 'HTTP';
									}
									if(service == 'GridFTP') {
										appendedFiles += '<span syle="word-wrap: break-word;vertical-align:middle;text-align:right"> <a style="cursor:pointer">' + service + '</a> </span>';
									} else {
										appendedFiles += '<span syle="word-wrap: break-word;vertical-align:middle;text-align:right"> <a href="'  + url  + '" ' + 'target="_blank">' + service + '</a> </span>';
									}
								}
								
								appendedFiles += '</td>';
								
								appendedFiles += '</tr>';
								
							}

							appendedFiles += '<tr class="remove_' + tagid + '">';
							appendedFiles += '<td></td>';
							appendedFiles += '</tr>';
							
							if(fileLength >= 10) {
								appendedFiles += '<tr class="view_files_' + 'oooo' + ' remove_' + tagid + '" style="">';
								appendedFiles += '<td></td>';
								appendedFiles += '<td style="display:none"><span class="datasetId">' + data.doc.datasetId + '</td>';
								appendedFiles += '<td><a class="view_more_files_short" style="cursor:pointer">' + 'View more files' + '</a></td>';
								appendedFiles += '</tr>';
								
								
								appendedFiles += '<tr class="file_append_' + replaceChars(data.doc.datasetId) + ' remove_' + tagid + '">';
								//appendedFiles += '<tr class="file_append_' + replaceChars(data.doc.datasetId) + ' remove_' + tagid + '">';
								appendedFiles += '<td></td>';
								appendedFiles += '</tr>';
							}
							
							
							
							$('.'+tagid).after(appendedFiles);
							
							
						}
						
						
					},
					error: function() {
						alert('error in getting new rows');
					}
				});
					
			}
			
			//change verbage of the expand link
			if(this.innerHTML === "Expand") {
                this.innerHTML="Collapse";
            } else {
                this.innerHTML="Expand";
            }
			
			
		});
		
		
	    
		
		$('a.view_more_files_short').live('click',function() {
			
			var selectedDocId = ($(this).parent().parent().find('span.datasetId').html());
			
			var self=this;
			
			if(this.innerHTML == "View more files") {
				
				
				var idStr = selectedDocId;
				
				var peerStr = getPeerStr();
				
				
				var technoteStr = getTechnoteStr();
				
		    	var fqParamStr = getFqParamStr();
		    	
		    	
		    	var queryStr = {"idStr" : idStr, 
						"peerStr" : peerStr, 
						"technotesStr" : technoteStr, 
						"showAll" : ESGF.setting.showAllContents, 
						"fqStr" : fqParamStr, 
						"initialQuery" : "false"}; 
				
		    	var appendedRows = $(this).parent().parent().parent().find('tr.file_append_' + replaceChars(selectedDocId));

		    	appendedRows.after('<tr id="spinner"><td></td><td><img src="images/ajax-loader.gif" /></td></tr>');
		    	
				$.ajax({
					url: '/esgf-web-fe/solrfileproxy2/datacart',
					global: false,
					type: "GET",
					data: queryStr,
					dataType: 'json',
					success: function(data) {

						$('tr#spinner').remove();
						
						var newRows = '';
						
						data.docs = rewriteDocsObject(data.docs);
						
						for(var i=0;i<data.docs.doc[0].files.file.length;i++){
							var file = data.docs.doc[0].files.file[i];
							var newRow = '<tr class="file_rows_' + replaceChars(selectedDocId) + ' addedrow_' + replaceChars(selectedDocId) + '">';
							
							//add the checkbox here
							newRow += '<td style="width: 40px;">';
							newRow += '<input style="margin-left: 10px;display:none" '
							newRow += 'class="fileLevel fileId" ';
							newRow += 'type="checkbox" ';
							newRow += 'checked="true" ';
							newRow += 'value="' + file.fileId + '" />';
							
							
							//add the fileId, checksum, tracking id here
							newRow += '<td style="width: 325px;padding-left:10px;font-size:11px;">';
							newRow += '<div style="word-wrap: break-word;">';
							newRow += '<span style="font-weight:bold"> ' + file.fileId + ' (' + sizeConversion(file.size)  + ') </span> <br />';
							newRow += '<span style="font-weight:italic"> Tracking Id: ' + file.tracking_id + '</span> <br />';
							newRow += '<span style="font-weight:italic"> Checksum: ' + file.checksum + ' (' + file.checksum_type + ') </span>';
							newRow += '</div>';
							newRow += '</td>';
							

							newRow += '<td style="float:right;font-size:11px;">';
							for(var j=0;j<file.services.service.length;j++) {
								if(file.services.service[j] == 'HTTPServer') {
									//newRow += '<td style="float:right;font-size:11px;">';
									newRow += '<span style="word-wrap: break-word;vertical-align:middle"> ';
									newRow += '<a href="' + file.urls.url[j] + '">HTTP</a>';
									newRow += '</span>';
									//newRow += '</td>';
								} else if(file.services.service[j] == 'GridFTP') {
									//newRow += '<td style="float:right;font-size:11px;">';
									newRow += '<span style="display:none" class="gridftp">' + file.urls.url[j] + '</span> ';
									newRow += '<span style="word-wrap: break-word;vertical-align:middle">';
									newRow += '<a style="cursor:pointer" class="go_individual_gridftp_short">GridFTP</a>';
									newRow += '</span>';
									//newRow += '</td>';
								} else if(file.services.service[j] == 'OPENDAP') {
									//newRow += '<td style="float:right;font-size:11px;">';
									newRow += '<span style="word-wrap: break-word;vertical-align:middle">' ;
									newRow += '<a href="' + file.urls.url[j] + '">OPENDAP</a>';
									newRow += '</span>';
									//newRow += '</td>';
								} else if(file.services.service[j] == 'SRM') {
									//newRow += '<td style="float:right;font-size:11px;">';
									newRow += '<span style="word-wrap: break-word;vertical-align:middle">' ;
										newRow += '<a href="' + file.urls.url[j] + '">SRM</a>';
									newRow += '</span>';
									//newRow += '</td>';
								}
							}
							newRow += '</td>';
							
							if(file.technote != 'NA') {
								newRow += '<td style="float:right;font-size:11px;">';
								newRow += '<div style="word-wrap: break-word;vertical-align:middle">';
								newRow += '<a href="${technote}" target="_blank">TECHNOTE </a>';
								newRow += '</div>';
								newRow += '</td>';
							}
							
							
							newRow += '</tr>';
							
							
							newRows += newRow;
						
						}

						appendedRows.after(newRows);
						
						self.innerHTML = 'Collapse files';
						
					},
					error: function() {
						alert('error');
					}
				});
				
				
			} else {

				$('tr.addedrow_'+replaceChars(selectedDocId)).remove();
				this.innerHTML = 'View more files';
			}
			
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
			
			var peerStr = getPeerStr();
			var technoteStr = getTechnoteStr();
							
	    	var fqParamStr = getFqParamStr();
	    	
	    	
	    	
	    	var queryStr = {"idStr" : idStr, 
					"peerStr" : peerStr, 
					"technotesStr" : technoteStr, 
					"showAllStr" : ESGF.setting.showAllContents, 
					"fqStr" : fqParamStr, 
					"initialQuery" : "true"}; 
	    	
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
									"initialQuery" : "false"}; 
							
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

									var queryString = '/esg-search/wget/?';
					            	
					                queryString = addConstraintsToWGETQueryString(queryString);
					            	
					                submitWGETScriptForm(queryString,file_ids,dataset_ids);
									

					    			
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
							var queryString = '/esg-search/wget/?';
			            	
			                queryString = addConstraintsToWGETQueryString(queryString);
			            	
			                submitWGETScriptForm(queryString,file_ids,dataset_ids);
			    			
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
		
		
		
		
		/**
	     * Grabs individual files and sends that information to the Globus Online view
	     */
	    //$('.go_individual_gridftp_short').live('click',function(e) {
	    	
	    	
	    	/*
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
	        */
	        
	    //});
		
	    
	    
	    /**
	     * Event for checkbox file selection
	     * NOTE: THIS NEEDS TO BE FIXED
	     */
	    $(".topLevel").live('change', function() {
	    	    	
	    	/*
	    	var self = this;
	    	
	    	var currentValue = $(this).attr('checked');

	    	
	    	if(currentValue) {
	    		var selectedItem = $.tmplItem(this);
		    	var selectedDocId = selectedItem.data.datasetId;
		    	
		    	var rowsId = selectedDocId.replace(/\./g,"_")
		    	
		        var newWord = rowsId.replace(/\./g,"_");
		        var newNewWord = newWord.replace(":","_");
		        var newNewNewWord = newWord.replace("|","_");
		    	
		    	//$(this).parent().parent().parent().find('tr.rows_'+ self.replacePeriod(selectedDocId)).each( function(index) {
                $(this).parent().parent().parent().find('tr.rows_'+ newNewNewWord).each( function(index) {
                	
                	var isChecked = $(this).find('input').attr('checked');
                	
                	if(!isChecked) {
                    	$(this).find('input').attr('checked','true');
                	}
                	
		        });
	    	} else {
	    		
	    		var selectedItem = $.tmplItem(this);
		    	var selectedDocId = selectedItem.data.datasetId;
		    	
		    	var rowsId = selectedDocId.replace(/\./g,"_")
		    	
		    	var newWord = rowsId.replace(/\./g,"_");
		        var newNewWord = newWord.replace(":","_");
		        var newNewNewWord = newWord.replace("|","_");
		    	
		    	$(this).parent().parent().parent().find('tr.rows_'+ newNewNewWord).each( function(index) {
                	
                	var isChecked = $(this).find('input').attr('checked');
                	
                	if(isChecked) {
                    	$(this).find('input').removeAttr('checked');//attr('checked','false');
                    }
		        });
	    	}
	    	*/
	    	
	    });
	    
		
	}

});



}(jQuery));


function addConstraintsToWGETQueryString(queryString) {
	//if filtering over search constraints...then add the search constraints to the wget
    if(ESGF.setting.showAllContents == 'false') {
    	
    	// traverse through the constraints and add to the querystring
		//for(var i in self.searchConstraints) {
    	var searchConstraints = ESGF.localStorage.toKeyArr('esgf_fq');
		for(var i=0;i<searchConstraints.length;i++) {
			if(searchConstraints[i].search('replica') == -1 && 
			   searchConstraints[i].search('type') == -1) {
			   //constraintCount = constraintCount + 1;
			   
			   //replace the : with =
			   var constraint = searchConstraints[i].replace(':','=');
			   
			   //replace 'text' with 'query' for free text searches
			   constraint = constraint.replace('text','query');
			   queryString += constraint + '&';
			   
			}
		}
    }
    
    return queryString;
}

function submitWGETScriptForm(queryString,file_ids,dataset_ids) {
	var form = '<form action="'+ queryString +'" method="post" >';
    
	
    //iterate over the file_ids and add to query string
    //this can probably be collapsed into the loop above
	/*
    for(var i=0;i<file_ids.length;i++) {
		var id = file_ids[i];
		id.replace("\\|","%7C");
		form += '<input type="hidden" name="id" value="' + id + '">';
	}
    form += '</form>';
    */
	
	for(var i=0;i<dataset_ids.length;i++) {
		var id = dataset_ids[i];
		id.replace("\\|","%7C");
		form += '<input type="hidden" name="dataset_id" value="' + id + '">';
	}
    form += '</form>';
	
    //send request using a dynamically generated form with the query string as the action
    //the method should be post because the query string may be long
    //jQuery('<form action="'+ queryString +'" method="post" >'+ '' +'</form>')
    jQuery(form).appendTo('body').submit().remove();
}



/**
*
*  URL encode / decode
*  http://www.webtoolkit.info/
*
**/
 
var Url = {
 
	// public method for url encoding
	encode : function (string) {
		return escape(this._utf8_encode(string));
	},
 
	// public method for url decoding
	decode : function (string) {
		return this._utf8_decode(unescape(string));
	},
 
	// private method for UTF-8 encoding
	_utf8_encode : function (string) {
		string = string.replace(/\r\n/g,"\n");
		var utftext = "";
 
		for (var n = 0; n < string.length; n++) {
 
			var c = string.charCodeAt(n);
 
			if (c < 128) {
				utftext += String.fromCharCode(c);
			}
			else if((c > 127) && (c < 2048)) {
				utftext += String.fromCharCode((c >> 6) | 192);
				utftext += String.fromCharCode((c & 63) | 128);
			}
			else {
				utftext += String.fromCharCode((c >> 12) | 224);
				utftext += String.fromCharCode(((c >> 6) & 63) | 128);
				utftext += String.fromCharCode((c & 63) | 128);
			}
 
		}
 
		return utftext;
	},
 
	// private method for UTF-8 decoding
	_utf8_decode : function (utftext) {
		var string = "";
		var i = 0;
		var c = c1 = c2 = 0;
 
		while ( i < utftext.length ) {
 
			c = utftext.charCodeAt(i);
 
			if (c < 128) {
				string += String.fromCharCode(c);
				i++;
			}
			else if((c > 191) && (c < 224)) {
				c2 = utftext.charCodeAt(i+1);
				string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
				i += 2;
			}
			else {
				c2 = utftext.charCodeAt(i+1);
				c3 = utftext.charCodeAt(i+2);
				string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
				i += 3;
			}
 
		}
 
		return string;
	}
 
}
