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
		$("input#remove_all").die('click');
		
		//kill the uber script
		$("input#uber_script").die('click');
		
		//kill the show all files link
		$('a.showAllFiles').die('click');
		
		//kill the view more files link
		$('a.view_more_files').die('click');
		
		//kill the go gridftp
		$('.go_individual_gridftp').die('click');
		    
		//kill the all files globus online
		$('a.globusOnlineAllFiles').die('click');
			
		//kill the all files wget	
		$('a.wgetAllFiles').die('click');
				
		
		$('.remove_dataset').die('click');
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
		$("input#remove_all").live('click', function() {
			
			
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
		$("input#uber_script").live('click', function() {

			//alert('uber script');
			
			
			//gather the file_ids
        	var file_ids   = new Array();
            
        	//iterate over the selected array of datasets in the data cart
        	
        	
            for(var i=0;i<ESGF.localStorage.toKeyArr('dataCart').length;i++) {
            
            	var selectedDocId = ESGF.localStorage.toKeyArr('dataCart')[i];//self.selected_arr[i];
            	
            	var selectedDocCount = $('span.datasetCount_'+replaceChars(selectedDocId)).html();
            	
            	//if the count is greater than 10, check to see if the additional rows have been not been expanded for this dataset
            	//if not (null) then need an extra ajax call to get the rest of the file ids
            	var isAdded = $('tr.addedrow_' + replaceChars(selectedDocId)).html();//$(this).parent().parent().parent().find('tr.addedrow_' + replaceChars(selectedDocId)).html();
            	
            	
            	
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
                	var queryStr = {"id" : idStr, 
                					"peer" : peerStr, 
                					"technotes" : technoteStr, 
                					"showAll" : ESGF.setting.showAllContents, 
                					"fq" : fqParamStr, 
                					"initialQuery" : "false"};

                	
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
        	
            submitWGETScriptForm(queryString,file_ids);
			
		});
		
		
		/*---------End header level events----------*/
		
		
		/*---------Begin dataset level events---------*/
		
		/**
	     * Click event for removing individual datasets from the data cart
	     */
	    $('.remove_dataset').live ('click', function(e) {
	    	
	    	var selectedDocId = ($(this).parent().parent().find('span.datasetId').html()).trim();
			
	    	//remove the dataset from the localStorage
        	ESGF.localStorage.remove('dataCart',selectedDocId);

        	$('a#ai_select_'+ selectedDocId.replace(/\./g, "_")).html('Add To Cart');
        	
        	//re-issue request to search api
        	Manager.doRequest(0);
        	
	    });
		
		
		
		$('a.showAllFiles').live('click',function() {
			
			//extract the dataset Id from the span tag
			var selectedDocId = ($(this).parent().parent().find('span.datasetId').html()).trim();
			
			//change verbage of the expand link
			if(this.innerHTML === "Expand") {
                this.innerHTML="Collapse";
                $('tr.view_files_'+replaceChars(selectedDocId)).toggle();
            } else {
                this.innerHTML="Expand";
                $('tr.view_files_'+replaceChars(selectedDocId)).toggle();
                
            }
			//toggle the rows of files
			$('tr.file_rows_'+replaceChars(selectedDocId)).toggle();
			
		});
		
		
		$('a.view_more_files').live('click',function() {
			
			var selectedDocId = ($(this).parent().parent().find('span.datasetId').html());
			
			
			if(this.innerHTML == "View more files") {
				this.innerHTML="Collapse files";
				
				
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
				
				
				appendedRows.after('<tr class="addedrow_' + replaceChars(selectedDocId) + '"><td></td></tr>');
				
				
				$.ajax({
					url: '/esgf-web-fe/solrfileproxy2/datacart',
					global: false,
					type: "GET",
					data: queryStr,
					dataType: 'json',
					success: function(data) {
						var newRows = '';
						
						data.docs = rewriteDocsObject(data.docs);
						
						for(var i=0;i<data.docs.doc[0].files.file.length;i++){
							var file = data.docs.doc[0].files.file[i];
							var newRow = '<tr class="file_rows_' + replaceChars(selectedDocId) + ' addedrow_' + replaceChars(selectedDocId) + '">';
							
							//add the checkbox here
							newRow += '<td style="width: 40px;">';
							newRow += '<input style="margin-left: 10px;" '
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
							
							for(var j=0;j<file.services.service.length;j++) {
								if(file.services.service[j] == 'HTTPServer') {
									newRow += '<td style="float:right;font-size:11px;">';
									newRow += '<div style="word-wrap: break-word;vertical-align:middle">'
									newRow += '<a href="' + file.urls.url[j] + '">HTTP </a>';
									newRow += '</div>';
									newRow += '</td>';
								} else if(file.services.service[j] == 'GridFTP') {
									newRow += '<td style="float:right;font-size:11px;">';
									newRow += '<span style="display:none" class="gridftp">' + file.urls.url[j] + '</span>';
									newRow += '<div style="word-wrap: break-word;vertical-align:middle">'
									newRow += '<a href="' + '#' + '">GridFTP </a>';
									newRow += '</div>';
									newRow += '</td>';
								} else if(file.services.service[j] == 'OPENDAP') {
									newRow += '<td style="float:right;font-size:11px;">';
									newRow += '<div style="word-wrap: break-word;vertical-align:middle">'
									newRow += '<a href="' + file.urls.url[j] + '">OPENDAP </a>';
									newRow += '</div>';
									newRow += '</td>';
								} else if(file.services.service[j] == 'SRM') {
									newRow += '<td style="float:right;font-size:11px;">';
									newRow += '<div style="word-wrap: break-word;vertical-align:middle">'
										newRow += '<a href="' + file.urls.url[j] + '">SRM </a>';
									newRow += '</div>';
									newRow += '</td>';
								}
							}
							
							
							
							newRow += '</tr>';
							
							
							newRows += newRow;
						}
						
						appendedRows.after(newRows);
						
					},
					error: function() {
						alert('error in getting new rows');
					}
				});
				
			} else {
				this.innerHTML="View more files";
				
				$('tr.addedrow_'+replaceChars(selectedDocId)).remove();
				
			}
			
			
		});
		
		$('a.wgetAllFiles').live('click',function() {
			
			var selectedDocId = ($(this).parent().parent().find('span.datasetId').html());
			
			var selectedDocCount = ($(this).parent().parent().find('span.datasetCount_'+replaceChars(selectedDocId)).html());
			
        	//gather the ids and the urls for download
        	var file_ids   = new Array();
        	
        	//if the count is greater than 10, check to see if the additional rows have been not been expanded for this dataset
        	//if not (null) then need an extra ajax call to get the rest of the file ids
        	var isAdded = $(this).parent().parent().parent().find('tr.addedrow_' + replaceChars(selectedDocId)).html();
            	
        	if(isAdded == null && selectedDocCount > 10) {

        		//traverse for file id rows that are checked
            	$(this).parent().parent().parent().find('tr.file_rows_'+ replaceChars(selectedDocId)).find(':checkbox:checked').each( function(index) {
            	
            		var file_id = $(this).parent().find('input').attr('value');
            		
            		file_ids.push(file_id);
            	});
            	
				var idStr = selectedDocId;
				
            	var peerStr = getPeerStr();
            	
            	var technoteStr = getTechnoteStr();
				
            	var fqParamStr = getFqParamStr();
				
            	
		    	//assemble the queryStr obj
				var queryStr = {"id" : idStr, 
						"peer" : peerStr, 
						"technotes" : technoteStr, 
						"showAll" : ESGF.setting.showAllContents, 
						"fq" : fqParamStr, 
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
		            	
		                submitWGETScriptForm(queryString,file_ids);
		                
						
					},
					error: function() {
						alert('error in getting extra files');
					}
            	});
        		
        		
        	} else {
        		
            	//traverse for file id rows that are checked
            	$(this).parent().parent().parent().find('tr.file_rows_'+ replaceChars(selectedDocId)).find(':checkbox:checked').each( function(index) {
            	
            		var file_id = $(this).parent().find('input').attr('value');
            		
            		file_ids.push(file_id);
            		
            	});
            	var queryString = '/esg-search/wget/?';
            	
                var constraintCount = 0;
                
                queryString = addConstraintsToWGETQueryString(queryString);
            	
                submitWGETScriptForm(queryString,file_ids);
        		
        	}
        	
		});
		
		

		
		$('a.globusOnlineAllFiles').live('click',function() {
			
			//var selectedDocId = ($(this).parent().parent().find('span.datasetId').html()).trim();
			var selectedDocId = ($(this).parent().parent().find('span.datasetId').html());
			var selectedDocCount = ($(this).parent().parent().find('span.datasetCount_'+replaceChars(selectedDocId)).html());
			
			alert('globus online ' + selectedDocId + ' ' + selectedDocCount);
			
        	//gather the ids and the urls for download
        	var file_ids   = new Array();
        	var grid_urls   = new Array();
        	
        	//if the count is greater than 10, check to see if the additional rows have been not been expanded for this dataset
        	//if not (null) then need an extra ajax call to get the rest of the file ids
        	var isAdded = $(this).parent().parent().parent().find('tr.addedrow_' + replaceChars(selectedDocId)).html();
            	
        	if(isAdded == null && selectedDocCount > 10) {
        		alert('additional files need to be extracted');
        		
        		
        		//traverse for file id rows that are checked
            	$(this).parent().parent().parent().find('tr.file_rows_'+ replaceChars(selectedDocId)).find(':checkbox:checked').each( function(index) {
            	
            		var file_id = $(this).parent().find('input').attr('value');
            		
            		var grid_url = $(this).parent().parent().find('span.gridftp').html();
            		//if(index == 0) {
            		//}
            		LOG.debug('file_id: ' + file_id);
            		
            		file_ids.push(file_id);
            		grid_urls.push(grid_url);
            		//file_ids.push(file_id);
            	});
            	
            	
            	var idStr = getIdStr();
            	
				var peerStr = getPeerStr();
				var technoteStr = getTechnoteStr();
				
				
				
		    	var fqParamStr = getFqParamStr();
		    	
		    	
		    	//assemble the queryStr obj
				var queryStr = {"id" : idStr, 
						"peer" : peerStr, 
						"technotes" : technoteStr, 
						"showAll" : ESGF.setting.showAllContents, 
						"fq" : fqParamStr, 
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
						alert('success: ' + file_ids.length + ' ' + grid_urls.length);
						
						/*
						var queryString = '/esg-search/wget/?';
		            	
		                queryString = addConstraintsToWGETQueryString(queryString);
		            	
		                submitWGETScriptForm(queryString,file_ids);
		                */
						
					},
					error: function() {
						alert('error in getting extra files');
					}
            	});
				
        		
            	/*
            	//get the rest
            	
            	
        		*/
        		
        	} else {
        		alert('no additional files need to be extracted');
        		
        	}
        	
		});
		
		
		/**
	     * Grabs individual files and sends that information to the Globus Online view
	     */
	    $('.go_individual_gridftp').live('click',function(e) {
	    	
	    	alert('go gridftp');
	    	
	    	var selectedDocId = '';
	    	var selectedFileId = '';
	    	
	    	
	    	//gather the ids and the urls for download
	    	
	        
	    });
		
		
		
		
	    $('.technotes').live('click',function(){
	    	
	    	/*
	    	var self = this;
	    	
	    	var selectedItem = $.tmplItem(this);
	    	
	    	//get the selected id
        	var datasetId = selectedItem.data.datasetId;

	    	if(this.innerHTML === "Show Technotes") {
                this.innerHTML="Hide Technotes";
            } else {
                this.innerHTML="Show Technotes";
            }
	    	
	    	var newWord = datasetId.replace(/\./g,"_");
	        var newNewWord = newWord.replace(":","_");
	        
	    	$('tr.rows_'+ newNewWord + '_technotes').toggle();
	    	*/
	    });
		
		
	    
	    
	    
	    
	    
	    
	    
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
	    
	    
	    
	    
	    /**
	     * Grabs individual files and sends that information to the Globus Online view
	     */
	    $('.go_individual_gridftp').live('click',function(e) {
	    	
	    	/*
	    	var selectedDocId = $(this).parent().parent().parent().parent().find('input').attr('id');
	    	
	    	//gather the ids and the urls for download
	    	var ids   = new Array();
	        var values = new Array();
	        
	        var file_id = $(this).parent().find('a').attr('id');
	        var grid_url = $(this).parent().parent().find('div').attr('id');
	        
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

function submitWGETScriptForm(queryString,file_ids) {
	var form = '<form action="'+ queryString +'" method="post" >';
    
    //iterate over the file_ids and add to query string
    //this can probably be collapsed into the loop above
    for(var i=0;i<file_ids.length;i++) {
		var id = file_ids[i];
		id.replace("\\|","%7C");
		form += '<input type="hidden" name="id" value="' + id + '">';
	}
    form += '</form>';
    
    //send request using a dynamically generated form with the query string as the action
    //the method should be post because the query string may be long
    //jQuery('<form action="'+ queryString +'" method="post" >'+ '' +'</form>')
    jQuery(form).appendTo('body').submit().remove();
}

function assembleDataCartQueryString() {
	
}
