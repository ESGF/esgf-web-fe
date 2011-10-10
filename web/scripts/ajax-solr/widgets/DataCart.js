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
 * Experimental Solr Front for ESG
 *
 * fwang2@ornl.gov
 * harneyjf@ornl.gov
 */


(function ($) {

AjaxSolr.DataCartWidget = AjaxSolr.AbstractWidget.extend({
	
	init: function() {
		//alert('distributed search init');
	},
	
	beforeRequest: function () {
		//alert('distributed search beforeRequest');
	},
	
	afterRequest: function () {
		var self = this;
    
		
		/**
	     * Event for tab selection (as of now, toggling between "Results" and "Datacart")
	     */
	    $('#myTabs').bind('tabsselect', function(event, ui) {
	        if (ui.index == 1) {
	            $("#datasetList").empty();
	            // selection tab
	            LOG.debug("Selection tab");
	            // convert object to array
	            var arr = ESGF.util.toArray(ESGF.search.selected);
	            //need a function that replaces periods in the name of the dataset (events in jquery cannot access elements that have these)
	            
	            if(arr != null || arr != undefined || arr.length == 0 || arr != '') {
	            	if(ESGF.setting.dataCartVersion == 'v1') {
		            	self.createTemplate(arr);
	            	}
	            }
	            
	        }
	    });
	    
	    
	    /**
	     * Click event for generating the wget script
	     * Submits a form with hidden values that calls the wget proxy class that returns the 
	     * wget script as content type text/x-sh.
	     * The form is deleted upon completion.
	     */
	    $(".wgetAllChildren").live ('click', function (e){
	    	
	    	if(ESGF.setting.dataCartVersion == 'v1') {
	    		//grab the dataset id from the template
	        	var selectedItem = $.tmplItem(this);
	        	var selectedDocId = selectedItem.data.dataset_id;

	        	//begin assembling queryString
	            var queryString = 'type=create&id=' + selectedDocId;
	        	
	            
	            
	            //gather the ids and the urls for download
	        	var ids   = new Array();
	            var values = new Array();
	            
	            var dataset = self.replacePeriod(selectedDocId);
	            
	            
	            $(this).parent().parent().parent().find('tr.rows_'+ dataset).find(':checkbox:checked').each( function(index) {
                    if(this.id != selectedDocId) {
                    ids.push(this.id);
                    values.push(this.value);
                   }
	            });
	            
	            
	            //assemble parameters of the queryString
	            for(var i=0;i<ids.length;i++) {
	            	queryString += '&child_url=' + values[i] + '&child_id=' + ids[i];
	            }
	            

	            var url = '/esgf-web-fe/wgetproxy';
	            var security = 'wgetv3';
	            queryString += '&security=' + security;
	            
	            //assemble the input fields with the query string
	            var input = '';
	            jQuery.each(queryString.split('&'), function(){
	                var pair = this.split('=');
	                input+='<input type="hidden" name="'+ pair[0] +'" value="'+ pair[1] +'" />';
	            });
	            
	            //send request
	            jQuery('<form action="'+ url +'" method="post">'+input+'</form>')
	            .appendTo('body').submit().remove();
	    		
	    	}
	    	
	    });
	    
	    
	    /**
	     * Click event for removing datasets from the data cart
	     */
	    $('.remove_dataset_from_datacart').live ('click', function(e) {
	    	
	    	if(ESGF.setting.dataCartVersion == 'v1') {
	    		//grab the dataset id from the template
	        	var selectedItem = $.tmplItem(this);
	        	var selectedDocId = selectedItem.data.dataset_id;

	        	//remove the dataset_id from the selected store
	        	delete ESGF.search.selected[selectedDocId];
	        	
	        	//remove the dataset and files visually
	        	//file rows in the template
	        	($('tr.rows_'+ self.replacePeriod(selectedDocId))).remove();
	        	//dataset rows in the template
	        	$('tr#' + self.replacePeriod(selectedDocId)).remove();
	        	
	        	//change from remove from cart to add to cart
	        	$('a#ai_select_'+ selectedDocId.replace(/\./g, "_")).html('Add To Cart');
	        	
	    	} 
	    	
	    	
	    });
	    
	    /**
	     * Event for checkbox file selection
	     */
	    $(".topLevel").live('change', function() {
	        LOG.debug("top level changed");

	        
	        var currentValue = $(this).attr('checked');

	    	//grab the dataset id from the template
	    	var selectedItem = $.tmplItem(this);
	    	var selectedDocId = selectedItem.data.dataset_id;

	        $(this).parent().parent().parent().find('tr.rows_'+ replacePeriod(selectedDocId)).find(':checkbox').each( function(index) {
	                    $(this).attr('checked', currentValue);
	        });
			
	        
	    });
	    
	},
	
	/*
     * Create the template for the datacart
     */
    createTemplate: function(arr) {

    	var self = this;
    	
    	self.loadCartShardsFromService(arr);
    	
    	//loadCartShardsFromSolrConfig(arr);
    	
	},
	
	loadCartShardsFromService: function (arr) {
		
		var self = this;
		
		var query_arr = new Array();
        //create a query string of just the dataset ids
    	for(var i=0;i<arr.length;i++) {
    		query_arr.push(arr[i].doc.id);
    	}
    	
    	
    	var file_download_template_url = ESGF.search.fileDownloadTemplateProxyUrl;

    	//Not sure if we need the search type at this point, but I kept it in
    	var query = { "id" : query_arr , "version" : ESGF.setting.dataCartVersion};
    	//var query = { "id" : dataset_id , "version" : ESGF.setting.dataCartVersion, "shards" : ESGF.search.shards };
    	
    	//only make the ajax call when there has been something added to the data cart
    	if(query_arr.length != 0) {
    		//show status spinning wheel
    		self.addDataCartSpinWheel();
    		$.ajax({
        		url: ESGF.search.fileDownloadTemplateProxyUrl,
        		global: false,
        		type: "GET",
        		data: query,
        		dataType: 'json',
        		success: function(data) {
        			self.removeDataCartSpinWheel();
        			//show the contents of the data cart
        			self.showFileContents(data);
        			
        		},
    			error: function() {
        			self.removeDataCartSpinWheel();
        			alert('There is a problem with one of your dataset selections.  Please contact your administrator.');
        			
                	//change from remove from cart to add to cart for all selected datasets
        			for(var i=0;i<query_arr.length;i++) {
                    	$('a#ai_select_'+ query_arr[i].replace(/\./g, "_")).html('Add To Cart');
        			}
    				
    			}
        		
        	});
    	}
    	
    	
    	
    	
	},
	
	loadCartShardsFromSolrConfig: function(arr) {
		//create an array of dataset id strings that have been selected for download
		var query_arr = createQueryArr(arr);

    	LOG.debug("-datasetID " + query_arr);
    	LOG.debug("-version " + ESGF.setting.dataCartVersion);
    	LOG.debug("-shards " + ESGF.search.shards);

    	//create an array consisting of ip addresses of the active shards
    	var shardsArr = createShardsArr();
    	
    	
    	//append 3 parameteters to the ajax call
    	//"id" - array of dataset ids that have been added to data cart
    	//"version" - version of the type of datacart (NOTE: THIS MAY BE TAKEN OUT)
    	//"shards" - array of ips that are the active shards in the configuration
    	var query = { "id" : query_arr , "version" : ESGF.setting.dataCartVersion, "shards" : shardsArr };
    	
    	//only make the ajax call when there has been something added to the data cart
    	if(query_arr.length != 0) {
    		//show status spinning wheel
    		addDataCartSpinWheel();
    		
        	$.ajax({
        		url: ESGF.search.fileDownloadTemplateProxyUrl,
        		global: false,
        		type: "GET",
        		data: query,
        		dataType: 'json',
        		success: function(data) {
        			$('#waitWarn').remove();
        			$('#spinner').remove();
        			//remove the status spinning wheel
        			removeDataCartSpinWheel();
        	    	
        			//show the contents of the data cart
        			showFileContentsV1(data);
        		},
    			error: function() {
    				$('#waitWarn').remove();
    				$('#spinner').remove();
    				alert('There is a problem with one of your dataset selections.  Please contact your administrator.');
        			
                	//change from remove from cart to add to cart for all selected datasets
        			for(var i=0;i<query_arr.length;i++) {
                    	$('a#ai_select_'+ query_arr[i].replace(/\./g, "_")).html('Add To Cart');
        			}
    			}
        		
        	});
        
    	}
	},
	
	
	//dynamically add spinning wheel to the datacart space
	addDataCartSpinWheel: function () {
    	//add a spin wheel to indicate that the system is processing
    	$('tbody#datasetList').after('<img id="spinner" src="images/ajax-loader.gif" />');
    	$('tbody#datasetList').after('<p id="waitWarn">Waiting for files...</p>');
    	
    },

    //dynamically remove spinning wheel to the datacart space
    removeDataCartSpinWheel: function() {
    	$('#waitWarn').remove();
		$('#spinner').remove();
    },
    
  //create an array of dataset id strings that have been selected for download
    createQueryArr: function(arr) {
    	var query_arr = new Array();
    	for(var i=0;i<arr.length;i++) {
    		query_arr.push(arr[i].doc.id);
    	}
    	return query_arr;
    },
    
    //create an array consisting of ip addresses of the active shards
    createShardsArr: function() {
    	var shardsArr = new Array();
    	for(var i=0;i<ESGF.search.shards.length;i++) {
    		var shards = ESGF.search.shards[i];
    		shardsArr.push(shards['nodeIp']);
    	}
    	return shardsArr;
    },
    
    
    showFileContents: function(data) {
    	
    	var self = this;
    	
		var fileDownloadTemplate = data.response.doc;
		
		
		$( "#cartTemplateStyled").tmpl(fileDownloadTemplate, {
			
			replacePeriods : function (word) {
                return self.replacePeriod(word);
            },
            abbreviate : function (word) {
                var abbreviation = word;
                if(word.length > 50) {
                    abbreviation = word;//word.slice(0,20) + '...' + word.slice(word.length-21,word.length);
                }
                return abbreviation;
            },
            addOne: function(num) {
                return (num+1);
            },
            sizeConversion : function(size) {
                var convSize;
                if(size == null) {
                    convSize = 'N/A';
                } else {
                    var sizeFlt = parseFloat(size,10);
                    if(sizeFlt > 1000000000) {
                        var num = 1000000000;
                        convSize = (sizeFlt / num).toFixed(2) + ' GB';
                    } else if (sizeFlt > 1000000) {
                        var num = 1000000;
                        convSize = (sizeFlt / num).toFixed(2) + ' MB';
                    } else {
                        var num = 1000;
                        convSize = (sizeFlt / num).toFixed(2) + ' KB';
                    }
                }
                return convSize;
            }
        })
        .appendTo("#datasetList")
        .find( "a.showAllChildren" ).click(function() {
        	var selectedItem = $.tmplItem(this);
            var selectedDoc = selectedItem;
           
            
            var selectedDocId = selectedDoc.data.dataset_id;
            $('input[name=' + selectedDocId + ']').toggle();

            var id = $(this).parent().attr("id").replace(/\./g,"_");
            $('tr.rows_'+id).toggle();
            if(this.innerHTML === "Expand") {
                this.innerHTML="Collapse";
            } else {
                this.innerHTML="Expand";
            }
            
        });
        
    	
    	
	},
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	 /*
     * This function is used primarily to avoid annoying errors that occur with strings that have periods in them
     */
    replacePeriod: function (word)
    {
        var newWord = word.replace(/\./g,"_");
        return newWord;
    },

    /*
     * This function is primarily used for debugging
     */
    printObject: function (object) {
        var output = '';
        for (var property in object) {
          output += property + ': ' + object[property]+'; ';
        }
        alert(output);
    },
    
    
    abbreviateWord: function(word) {
    	var abbreviation = word;
        if(word.length > 25) {
            abbreviation = word.slice(0,10) + '...' + word.slice(word.length-11,word.length);
        }
        return abbreviation;
    },
    
    sizeConvert: function (size) {
    	var convSize;
        if(size == null) {
            convSize = 'N/A';
        } else {
            var sizeFlt = parseFloat(size,10);
            if(sizeFlt > 1000000000) {
                var num = 1000000000;
                convSize = (sizeFlt / num).toFixed(2) + ' GB';
            } else if (sizeFlt > 1000000) {
                var num = 1000000;
                convSize = (sizeFlt / num).toFixed(2) + ' MB';
            } else {
                var num = 1000;
                convSize = (sizeFlt / num).toFixed(2) + ' KB';
            }
        }
        return convSize;
    }
    
    
    
	

});

}(jQuery));