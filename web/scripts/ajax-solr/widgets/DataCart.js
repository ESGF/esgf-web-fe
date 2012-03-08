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
    	
    	/*
		$(".wgetAllChildren").die('click');
		$(".globusOnlineAllChildren").die('click');
		$('.remove_dataset_from_datacart').die('click');
		$("input#remove_all").die('click');
		$('.go_individual_gridftp').die('click');
		$('.technotes').die('click');
		$(".topLevel").die('change');
		$(".fileLevel").die('change');
		$('a.view_more_results').die('click');
		*/
    	
		//add the spinning wheel in case there is a delay in loading the items in the data cart
        $(this.target).html($('<img/>').attr('src', 'images/ajax-loader.gif'));
	},
	
	/**
	 * DOCUMENT ME
	 */
	afterRequest: function () {
		
		
		
		//empty the tab
        $(this.target).empty();

		var self = this;
    
		//getter for the data cart tab
		var selected = $( "#myTabs" ).tabs( "option", "selected" );
		
		
		//grab all the keys from the datacart map and place in an array
    	self.selected_arr = ESGF.localStorage.toKeyArr('dataCart');
    	
    	
    	
    	
    	
    	//empty the carts tab and append/initialize the datacart table 
    	$('#carts').empty();
    	
    	//this string represents the options for the data cart
    	//as of now, the options are:
    	// - radio box that the user chooses to show files
    	// - the "uber" script option
    	var optionsStr = '<div id="radio" style="margin-bottom:30px;display:none">' + 
    				 	 '<table>' +
    				 	 '<tr>' +
    				 	 '<td>' +
    				 	 '<input type="radio" id="datacart_filter_all" name="datacart_filter" value="all" /> ' +
    				 	 'Show all files ' +
    				 	 '</td>' +
    				 	 '<td>' +
    				 	 '<input type="radio" id="datacart_filtered" name="datacart_filter" value="filtered" /> ' +
    				 	 'Filter over search constraints ' +
    				 	 '</td>' +
    				 	 '<td></td>' + 
    				 	 '<td></td>' +
    				 	 '<td></td>' + 
    				 	 '<td></td>' + 
    				 	 '<td></td>' + 
    				 	 '<td>' +
    				 	 '<input id="remove_all" type="submit" value="Remove All" /> ' +
    				 	 '</td>' +
    				 	 '<td>' +
    				 	 '<input id="uber_script" type="submit" value="WGET All Selected" /> ' +
    				 	 '</td>' +
    				 	 '</tr>' +
    				 	 '</table>' +
    				 	 '</div>';
    	
    	//add the options to the page
    	$('#carts').append(optionsStr);
    	
    	
    	//toggle the "checked" attribute of the showAllContents radio button
    	if(ESGF.setting.showAllContents == 'true') {
    		$("input[id='datacart_filtered']").attr("checked","false");
    		$("input[id='datacart_filter_all']").attr("checked","true");
    	} else {
    		$("input[id='datacart_filter_all']").attr("checked","false");
    		$("input[id='datacart_filtered']").attr("checked","true");
    	}
    	
    	//if there are no items in the datacart don't show the radio buttons
    	if(self.selected_arr.length > 0) {
    		$('div#radio').show();
    	}
    	
		//initialize the contents in the data cart
    	$('#carts').append('<table style="width:100%;table-layout: fixed"><tbody id="datasetList"></tbody></table>');
        $("#datasetList").empty();
		
        

		//getter for the data cart tab
		var selected = $( "#myTabs" ).tabs( "option", "selected" );
		
		//create the data cart template
        //only create if the data cart tab is selected
		if(selected == 1) {
	        self.createTemplate(self.selected_arr);
		}
		
       
	},

    
	/**
     * Create the template for the datacart
     * 
     * There is a new procedure for creating the datacart template because the search API is now called
     * 
     * 
     * 
     */
    createTemplate: function() {

    	alert('creating template');
    	
    	var self = this;
    	
    	//Get all of the search constraints (fq params)
    	var fqParamArr = new Array();
    	fqParams = ESGF.localStorage.getAll('esgf_queryString');
    	for(var key in fqParams) {
    		if(key != 'type:Dataset' && (key.search('distrib') < 0))
    			fqParamArr.push(fqParams[key]);
    	}
    	
    	//get the peerArr and technoteArr
    	//these will actually be ';' delimited strings...
    	//will probably need to create an array for these
    	var peerArr='',technoteArr='';

		//the datasetInfo object will have a 'peer' and 'xlink' property
    	for(var i=0;i<self.selected_arr.length;i++) {
    		var datasetInfo = ESGF.localStorage.get('dataCart',self.selected_arr[i]);

    		//extract the peer and 'xlink' and add it to the arrs
    		if(i!=0) {
    			peerArr += ';'+datasetInfo.peer;
    			technoteArr += ';'+datasetInfo.xlink;
    		} else {
    			peerArr += datasetInfo.peer;
    			technoteArr += datasetInfo.xlink;
    		};
    	}
    	
    	//assemble the query string
    	//
    	// 
    	var queryStr = {"id" : self.selected_arr, 
    					"peer" : peerArr, 
    					"technotes" : technoteArr, 
    					"showAll" : ESGF.setting.showAllContents, 
    					"fq" : fqParamArr, 
    					"initialQuery" : "true"};
		
    	//getter for the data cart tab
		var selected = $( "#myTabs" ).tabs( "option", "selected" );
		
		//only make the ajax call when the data cart is selected
		if(selected == 1) {
			$.ajax({
				url: '/esgf-web-fe/solrfileproxy2/datacart',
				global: false,
				type: "GET",
				data: queryStr,
				dataType: 'json',
				success: function(data) {
					
					if(data.docs.doc != undefined) {
						var docLength = data.docs.doc.length;
						
						//if the doc length is undefined, then the number of docs returned may be one
						//there is a bug in the json java code that will automatically convert this to a json object
						if(docLength == undefined) {
							var docArray = new Array();
							docArray.push(data.docs.doc);
							data.docs['doc'] = docArray;
							docLength = data.docs.doc.length;
						}
						
		    			var fileDownloadTemplate = data.docs;
		    			
		    			$( "#cartTemplateStyledNew2").tmpl(fileDownloadTemplate, {
		        			
		        			replacePeriods : function (word) {
		                    },
		                    abbreviate : function (word) {
		                    },
		                    addOne: function(num) {
		                    },
		                    sizeConversion : function(size) {
		                    }
		                    
		                })
		                .appendTo("#datasetList")
		                .find( "a.showAllChildren" ).click(function() {

		                });
		    			
						
					} else {
						alert('No data sets have been added to your cart');
					}
					
					
				},
				error: function() {
					alert('Error loading data cart');
				}
			})
		}
    	/*
		
		LOG.debug('queryStr... id: ' + queryStr.id + ' peerArr: ' + 
				queryStr.peer + ' technotes: ' + queryStr.technotes + ' showAll: ' + 
				queryStr.showAll + ' fq: ' + queryStr.fq + 
				' initialQ: ' + queryStr.initialQuery);
		
		//getter for the data cart tab
		var selected = $( "#myTabs" ).tabs( "option", "selected" );
		
		
		if(selected == 1) {
			
			$.ajax({
				url: '/esgf-web-fe/solrfileproxy2/datacart',
				global: false,
				type: "GET",
				data: queryStr,
				dataType: 'json',
				
				//Upon success remove the spinning wheel and show the contents given by solr
				 
				success: function(data) {

					var docLength = data.docs.doc.length;
					
					if(docLength == undefined) {
						var docArray = new Array();
						docArray.push(data.docs.doc);
						
						data.docs['doc'] = docArray;
						
						length = data.docs.doc.length;
					}
					
	    			var fileDownloadTemplate = data.docs;
	        		
	    			$( "#cartTemplateStyledNew2").tmpl(fileDownloadTemplate, {
	        			
	        			replacePeriods : function (word) {
	        				LOG.debug('replacePeriods: ' + self.replacePeriod(word));
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
	                    	return self.sizeConvert(size);
	                    }
	                    
	                })
	                .appendTo("#datasetList")
	                .find( "a.showAllChildren" ).click(function() {

	                	
	                	//need to get the dataset id here
	                	
	                	
	                	alert($(this).parent().find('a').attr('id'));
	                	
	                	var selectedDocId = $(this).parent().find('a').attr('id');
	                	
	                	
	                	
	                	
	                	if(this.innerHTML === "Expand") {
	                        this.innerHTML="Collapse";
	                        
	                        
	                    } else {
	                        this.innerHTML="Expand";
	                    }
	                	
	                	//$('tr.rows_'+self.replacePeriod(selectedDocId)).toggle();
	                	$('tr.rows_'+selectedDocId).toggle();
	                	
	                });
				},
				error: function() {
					alert('error');
				}
			});
			
			
			
		}
    	
		*/
		
	},
	
	
    
	
	rewriteTextQuery: function(queryString) {
    	var newQueryString = '';

    	
    	var root = queryString.split('?')[0];
    	
    	
    	
    	var paramArr = (queryString.split('?')[1]).split('&');
    	
    	newQueryString = root + '?';
    	
    	var fullText = '';
    	for(var i=0;i<paramArr.length;i++) {
    		var constraint = paramArr[i];
    		if(constraint != '' && constraint != ' ') {
    			//alert('constraint: ' + constraint);
        		if(constraint.search('query=') > -1) {
            		var queryClause = constraint.split('=');
            		fullText = fullText + queryClause[1] + ' ';
        		} else {
        			newQueryString += '&' + constraint;
        		}
    		}
    		
    	}
    	
    	if(fullText != '') {
        	newQueryString += '&' + 'query=' + fullText;
    	}
    	
    	
    	return newQueryString;
    },
	
	
	
	
	getDataCartUsingSearchAPI: function(queryStr) {
		var self = this;
		
		//add a spinning wheel to show user that progress is being made in finding the files
		self.addDataCartSpinWheel();
		
		$.ajax({
			url: '/esgf-web-fe/solrfileproxy',
			global: false,
			type: "GET",
			data: queryStr,
			dataType: 'json',
			
			//Upon success remove the spinning wheel and show the contents given by solr
			success: function(data) {
    			self.removeDataCartSpinWheel();
    			self.showFileContents(data); 
			},
			
			//Upon an error remove the spinning wheel and give an alert 
    		error: function() {
    			self.removeDataCartSpinWheel();
    			alert('There is a problem with one of your dataset selections.  Please contact your administrators.');
    			
            	//change from remove from cart to add to cart for all selected datasets
    			for(var i=0;i<query_arr.length;i++) {
                	$('a#ai_select_'+ arr[i].replace(/\./g, "_")).html('Add To Cart');
    			}
			}
		});
	},
	
	
	/**
	 * This function creates the datacart template by loading shards from the service instead of the solrconfig
	 * It is passed in the array of keys of all the datasets that have been selected
	 */
	
	getDataCart: function(queryStr) {

		var self = this;
		
    	
		
		//add a spinning wheel to show user that progress is being made in finding the files
		self.addDataCartSpinWheel();

		//alert('queryStr: ' + queryStr.peer);
		
		$.ajax({
			url: '/esgf-web-fe/solrfileproxy',
			global: false,
			type: "GET",
			data: queryStr,
			dataType: 'json',
			
			//Upon success remove the spinning wheel and show the contents given by solr
			 
			success: function(data) {
				
    			self.removeDataCartSpinWheel();
    			self.showFileContents(data); 
			},
			
			//Upon an error remove the spinning wheel and give an alert 
			 
    		error: function() {
    			self.removeDataCartSpinWheel();
    			alert('There is a problem with one of your dataset selections.  Please contact your administratorss.');
    			
            	//change from remove from cart to add to cart for all selected datasets
    			
    			for(var i=0;i<self.selected_arr.length;i++) {
    			//for(var i=0;i<query_arr.length;i++) {
                	$('a#ai_select_'+ arr[i].replace(/\./g, "_")).html('Add To Cart');
    			}
			}
		});
		
	},
	
	
	/*
	 * Assembles the search constraint array
	 * 
	 * Extracts the current search constraints from the esgf_queryString map and places them in an array to send to the datacart backend 
	 * Example:
	 * fqParamArray = ('variable=hus','project=obs4MIPs')
	 * 
	 * This is useful when the user wants to filter the files using the search constraints
	 * 
	 */
	createFqParamArray: function () {
		var fqParamArr = new Array();
    	
		
		
    	fqParams = ESGF.localStorage.getAll('esgf_queryString');
    	for(var key in fqParams) {
    		if(key != 'type:Dataset' && (key.search('distrib') < 0))
    			fqParamArr.push(fqParams[key]);
    	}
    	
    	return fqParamArr;
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
    
   
   
    /**
     * DOCUMENT ME
     * @param data
     */
    showFileContents: function(data) {
    	
    	LOG.debug("Showing file contents" + data);
    	
    	
    	var self = this;
    	
    	if(data != null) {
    	
    		
    		var fileDownloadTemplate = data.doc;
    		
    		
    		$( "#cartTemplateStyledNew").tmpl(fileDownloadTemplate, {
    			
    			replacePeriods : function (word) {
    				LOG.debug('replacePeriods: ' + self.replacePeriod(word));
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
                	return self.sizeConvert(size);
                }
            })
            .appendTo("#datasetList")
            /*
            .find( "a.showAllChildren" ).click(function() {

            	
            	alert('a.showAllChildren');
            	
            	
            	var selectedItem = $.tmplItem(this);
                var selectedDoc = selectedItem;

            	var selectedDocId = selectedItem.data.datasetId;
            	
            	alert('selectedDoc: ' + selectedDocId);
            	
            	//alert('tr.view_more_results_' + self.replacePeriod(selectedDocId));
            	
            	$('tr.view_more_results_' + self.replacePeriod(selectedDocId)).toggle();
            	
            	if(this.innerHTML === "Expand") {
                    this.innerHTML="Collapse";
                    
                    
                } else {
                    this.innerHTML="Expand";
                }
            	
            	
            	
            	var id = $(this).parent().parent().parent().html();
                
            	$('tr.rows_'+self.replacePeriod(selectedDocId)).toggle();
            	
            	
            })*/
            ;
    		
    		
    		
    	}
		
	},
	
	
	
	
	
	/* Utility functions for the jquery template */
	
	
	 /**
     * This function is used primarily to avoid annoying errors that occur with strings that have periods in them
     */
	/**
	 * Obviously needs revision
	 */
    replacePeriod: function (word)
    {
    	LOG.debug('word: ' + word);
        var newWord = word.replace(/\./g,"_");
        var newNewWord = newWord.replace(":","_");
        var newNewNewWord = newWord.replace("|","_");
        return newNewNewWord;
    },
	

    /**
     * This function prints contents of an object
     */
    printObject: function (object) {
        var output = '';
        for (var property in object) {
          output += property + ': ' + object[property]+'; ';
        }
        alert(output);
    },
    
    /**
     * This function abbreviates long strings
     */
    abbreviateWord: function(word) {
    	var abbreviation = word;
        if(word.length > 25) {
            abbreviation = word.slice(0,10) + '...' + word.slice(word.length-11,word.length);
        }
        return abbreviation;
    },
    
    /**
     * This function converts file sizes to a string representing its size in bytes
     */
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

function formatPrice(price) {
    return "ZZZZ" + price;
}


function replacePeriodGlobal(word)
{
    var newWord = word.replace(/\./g,"_");
    var newNewWord = newWord.replace(":","_");
    var newNewNewWord = newWord.replace("|","_");
    return newNewNewWord;
}

function abbreviateWordGlobal(word) {
	var abbreviation = word;
    if(word.length > 25) {
        abbreviation = word.slice(0,10) + '...' + word.slice(word.length-11,word.length);
    }
    return abbreviation;
}

function sizeConversionGlobal(size) {
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