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
