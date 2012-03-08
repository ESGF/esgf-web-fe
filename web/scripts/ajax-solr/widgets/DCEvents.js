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

			alert('uber script');
			
			/*
			LOG.debug("Uber script: " + JSON.stringify(self.searchConstraints));
			
			var sentConstraints = new Array();

			var queryString = '/esg-search/wget/?';
			//if the show all contents is the filter...then add the search constraints to the wget
            if(ESGF.setting.showAllContents == 'false') {
            	
            	
				// traverse through the constraints and add to the querystring
				//for(var i in self.searchConstraints) {
				for(var i=0;i<self.searchConstraints.length;i++) {

					if(self.searchConstraints[i].search('replica') == -1 && 
					   self.searchConstraints[i].search('type') == -1) {
					   
					   //FIXME
					   //replace the : with =
					   var constraint = self.searchConstraints[i].replace(':','=');
					   
					   //replace 'text' with 'query' for free text searches
					   constraint = constraint.replace('text','query');
					   
					   queryString += constraint + '&';
					   
					}
				}
			
            }
            
            //gather the file_ids
        	var file_ids   = new Array();
            
            //iterate over the selected array of datasets in the data cart
            for(var i=0;i<self.selected_arr.length;i++) {
            	var selectedDocId = self.selected_arr[i];
            	var datasetId = self.replacePeriod(self.selected_arr[i]);
            	
            	
            	//grab the ids from the elements in the jquery template
            	//FIXME: need clearer representation of these ids
            	$(this).parent().parent().parent().parent().parent().parent().find('tr.rows_'+ datasetId).find(':checkbox:checked').each( function(index) {
            		
            		var file_id = this.id;
                	
                	//push 
                	file_ids.push(file_id);
                });
            	
            }
            
            
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
			
			*/
			
		});
		
		
		/*---------End header level events----------*/
		
		
		/*---------Begin dataset level events---------*/
		
		
		
		
		
		
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
		
		
		
		
		$('a.view_more_results').live('click',function() {
			
			/*
			var id = ';';
			
        	var selectedItem = $.tmplItem(this);
        	var selectedDocId = selectedItem.data.datasetId;

	    	var rowsId = selectedDocId.replace(/\./g,"_")
	    	
	        var newWord = rowsId.replace(/\./g,"_");
	        var newNewWord = newWord.replace(":","_");
	        var newNewNewWord = newWord.replace("|","_");
	    	
			//alert('selectedDocId: ' + newNewNewWord);
			var id = newNewNewWord;
			//alert('origSelectedDocId: ' + selectedDocId);
    		var datasetInfo = ESGF.localStorage.get('dataCart',selectedDocId);
    		
    		var peer = datasetInfo.peer;
    		
    		//alert('peer: ' + peer);

        	var fqParamArr = self.createFqParamArray();
        	
			
			
			if(this.innerHTML == "View more files") {
				this.innerHTML="Collapse files";
				
				
				
				var appendedRows = $(this).parent().parent().parent().parent().find('tr.appendRow_'+self.replacePeriod(selectedDocId));
				
				appendedRows.toggle();
				
				//get the remaining results from the back end
				var queryStr = {"id" : selectedDocId, "initialQuery" : "false", "peer" : peer, "showAll" : ESGF.setting.showAllContents, "fq" : fqParamArr}; //, "peer" : peerArr, "technotes" : technoteArr, "showAll" : ESGF.setting.showAllContents, "fq" : fqParamArr, "initialQuery" : "true"};
				
				self.addDataCartSpinWheel();
				
				$.ajax({
					url: '/esgf-web-fe/solrfileproxy',
					global: false,
					type: "GET",
					data: queryStr,
					dataType: 'json',
					
					//Upon success remove the spinning wheel and show the contents given by solr
					success: function(data) {
						//alert('success');
		    			self.removeDataCartSpinWheel();
		    			
		    			var newRows = '';
		    			
		    			for(var i=2;i<data.doc.file.length;i++ ) {//data.doc.file.length;i++) {
		    				
		    				var newRow = '<tr class="hhh rows_' + self.replacePeriod(data.doc.datasetId) + '">';
							
							//checkbox
							newRow += '<td style="width: 40px;">' +
										 '<input style="margin-left: 10px;" ' +
										         'class="fileLevel" ' +
										         'id="' + data.doc.file[i].fileId + '" ' + 
										         'type="checkbox" ' +
										         'class="fileId" ' +
										         'checked="true" ' + 
										         'value=" ' + data.doc.file[i].urls.url[1]+ '"' +
										         '/>' +
										 '</td>';
	
							
							//file id, checksum, tracking id
							newRow += '<td style="width: 325px;padding-left:10px;font-size:11px;">';
							newRow += '  <div style="word-wrap: break-word;"> ';
							newRow += '    <span style="font-weight:bold"> ' + data.doc.file[i].fileId + '(' + self.sizeConvert(data.doc.file[i].size) + ')' + ' </span>';
							newRow += '    <br />';
							newRow += '    <span style="font-style:italic">Tracking Id: ' + data.doc.file[i].tracking_id + '</span>';
							newRow += '    <br />';
							newRow += '    <span style="font-style:italic">Checksum: ' + data.doc.file[i].checksum + '(' + data.doc.file[i].checksum_type + ') ' + '</span>';
							newRow += '  </div>';
							newRow += '</td>';
							
							//file access methods
							for(var j=0;j<data.doc.file[i].urls.url.length;j++) {
								if(data.doc.file[i].services.service[j] == 'HTTPServer') {
									newRow += '<td id="' + self.replacePeriod(data.doc.datasetId) + '_http" style="float:right;font-size:11px;"><div id="' + data.doc.file[i].urls.url[j] + '" style="word-wrap: break-word;vertical-align:middle"><a href="' + data.doc.file[i].urls.url[j] + '">HTTP </a></div></td>';
								}
								if(data.doc.file[i].services.service[j] == 'GridFTP') {
									newRow += '<td id="" style="float:right;font-size:11px;"><div id="" style="word-wrap: break-word;vertical-align:middle"><a id="" class="go_individual_gridftp" href="#">GridFTP </a></div></td>';
								}
								if(data.doc.file[i].services.service[j] == 'OPENDAP') {
									newRow += '<td id="" style="float:right;font-size:11px;"><div id="" style="word-wrap: break-word;vertical-align:middle"><a href="">OPENDAP </a></div></td>';
								}
							}
							
							//technotes (if any)
							if(data.doc.file[i].technotes.technote.length > 2) {
								newRow += '<td id=" ' + replacePeriod(data.doc.datasetId) + '_openid' + '"' +
								'style="float:right;font-size:11px;">' +
								'<div id="d" ' + 
								'style="word-wrap: break-word;vertical-align:middle">' +
								'<a href="" title="" target="_blank">TECHNOTE </a></div></td>';
								
							}
							
							
			    			newRow += '</tr>';
			    			
			    			newRows += newRow;
			    			
		    			}

		    			
		    			appendedRows.after(newRows);
		    			
					},
					error: function() {
						alert('error');
					}
				});
				
				
				
			} else {
				this.innerHTML="View more files";
				//alert($(this).parent().parent().parent().parent().find('tr.hhh').html());
				
				var thing = $(this).parent().parent().parent().parent().find('tr.appendRow_'+newNewNewWord);
				
				thing.toggle();
				
				$(this).parent().parent().parent().parent().find('tr.hhh').remove();
				
			}
			*/
		});
		
		
		
		
		
		
		
	    
	    /**
	     * Click event for generating the wget script for individual datasets
	     * 
	     * Similar to the uber script above
	     */
	    $(".wgetAllChildren").live ('click', function (e){
	    	
	    	/*
	    	alert('wget for: ' + $(this).parent().find('a').attr('id'));
        	
        	var selectedDocId = $(this).parent().find('a').attr('id');

        	//gather the ids and the urls for download
        	var file_ids   = new Array();
        	
        	//traverse for file id rows that are checked
        	$(this).parent().parent().parent().find('tr.rows_'+ selectedDocId).find(':checkbox:checked').each( function(index) {
        		if(this.id != selectedDocId) {
                	var file_id = this.id;
                	
                	
                	//push 
                	file_ids.push(file_id);
                	
               }
            });

            var queryString = '/esg-search/wget/?';
        	
            var constraintCount = 0;
            
            //if the show all contents is the filter...then add the search constraints to the wget
            if(ESGF.setting.showAllContents == 'false') {
            	// traverse through the constraints and add to the querystring
    			//for(var i in self.searchConstraints) {
    			for(var i=0;i<self.searchConstraints.length;i++) {
    				if(self.searchConstraints[i].search('replica') == -1 && 
    				   self.searchConstraints[i].search('type') == -1) {
    				   constraintCount = constraintCount + 1;
    				   
    				   //replace the : with =
    				   var constraint = self.searchConstraints[i].replace(':','=');
    				   
					   //replace 'text' with 'query' for free text searches
					   constraint = constraint.replace('text','query');
    				   queryString += constraint + '&';
    				   
    				}
    			}
            }
            
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
        	*/
	    	
	    });
	    
	    
	    /**
	     * Click event for removing datasets from the data cart
	     */
	    $('.remove_dataset_from_datacart').live ('click', function(e) {
	    	
	    	/*
	    	//var selectedDocId = $(this).parent().find('a').attr('id');

	    	//alert($(this).parent().parent().find('span.datasetId').html());
	    	
	    	var selectedDocId = ($(this).parent().parent().find('span.datasetId').html()).trim();
	    	alert(selectedDocId);
	    	
	    	//remove the dataset from the localStorage
        	ESGF.localStorage.remove('dataCart',selectedDocId);

        	//change from remove from cart to add to cart
        	$('a#ai_select_'+ selectedDocId.replace(/\./g, "_")).html('Add To Cart');
        	
	    	
        	
        	//re-issue request to search api
        	Manager.doRequest(0);
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
	   
	    
	    /**
	     * Click event for launching globus online bulk data transfer
	     */
	    $(".globusOnlineAllChildren").live('click',function(e){
	    	
	    	/*
	    	//grab the dataset id from the template
	    	var selectedItem = $.tmplItem(this);
        	var selectedDocId = selectedItem.data.datasetId;

	    	//gather the ids and the urls for download
	    	var ids   = new Array();
	        var values = new Array();
	        
	        var rowsStr = 'tr.rows_'+ self.replacePeriod(selectedDocId);

	        
	        var $element = $(this).parent().parent().parent().find(rowsStr);
	        
	        $element.each(function(index) {
	        	var isChecked = $(this).find('input').attr('checked');
        	
	        	if(isChecked) {
	        		var gridElement = 'td#' + self.replacePeriod(selectedDocId) + '_gridftp';
		        	var grid_url = $(this).find(gridElement).find('div').attr('id');
		        	values.push(grid_url);
		        	var file_id = $(this).find(gridElement).find('a').attr('id');
		        	ids.push(file_id);
	        	}
	        	
	        });
	        
	        //get the globus online
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
	        
            //add the search constraints
	    	input+= '<input type="hidden" name="constraints" value="' + (JSON.stringify(self.searchConstraints)).replace(/\"/gi,'\'') + '" />';
            
	        //send request
	        jQuery('<form action="'+ globus_url +'" method="post">'+input+'</form>')
	        .appendTo('body').submit().remove();
	        */
	    });
		
		
	}

});

}(jQuery));