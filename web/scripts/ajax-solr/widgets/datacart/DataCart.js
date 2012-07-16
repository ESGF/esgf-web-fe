/*****************************************************************************
 * Copyright � 2011 , UT-Battelle, LLC All rights reserved
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
    	
		//add the spinning wheel in case there is a delay in loading the items in the data cart
        
    	//CHANGE ME!!!
    	//$(this.target).html($('<img/>').attr('src', 'images/ajax-loader.gif'));
    	
    	//grab all the keys from the datacart map and place in an array
    	self.selected_arr = ESGF.localStorage.toKeyArr('dataCart');
    	
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
		
		
		
    	//empty the carts tab and append/initialize the datacart table 
    	$('#carts').empty();
    	
    	
    	//this string represents the options for the data cart
    	//as of now, the options are:
    	// - radio box that the user chooses to show files
    	// - the "uber" script option
    	var optionsStr = '<div id="radio" style="margin-bottom:30px;display:none">' + 
    				 	 '<table>' +
    				 	 '<tr>' +
    				 	 '<td style="width:80px;font-size:12px;padding:0px">' +
    				 	 '<input type="radio" id="datacart_filter_all" name="datacart_filter" value="all" /> ' +
    				 	 'Show all ' +
    				 	 '</td>' +
    				 	 '<td style="width:200px;font-size:12px;padding:0px">' +
    				 	 '<input type="radio" id="datacart_filtered" name="datacart_filter" value="filtered" /> ' +
    				 	 'Filter over search constraints ' +
    				 	 '</td>' +
    				 	 '<td class="sfileCounter" style="width:220;display:none;padding:0px">Show initial <select class="fileCounter" name="fileC">' +
    				 	 '<option id="fileCounter5" value="5">5</option>'+
    					 '<option id="fileCounter10" value="10" selected="selected">10</option>' +
    					 '<option id="fileCounter25" value="25" >25</option>' +
    					 '<option id="fileCounter50" value="50">50</option>' +
    					 '</select> files</td>' + 
    				 	 '<td style="font-size:12px;padding:0px">' +
    				 	 '<a id="remove_all_short" style="cursor:pointer">Remove All</a>' +
    				 	 //'<input class="datacart-buttons" type="submit" value="Remove All" />'+
    			      	//'<input id="remove_all_short" type="submit" value="Remove All" /> ' +
    				 	 '</td>' +
    				 	 '<td style="font-size:12px;padding:0px">' +
    				 	 //'<input class="datacart-buttons" type="submit" value="WGET All Selected" />'+

    				 	 '<a id="uber_script_short" style="cursor:pointer">WGET All Selected</a>' +
    				 	 //'<input id="uber_script_short" type="submit" value="WGET All Selected" /> ' +
    				 	 '</td>' +
    				 	 '</tr>' +
    				 	 '</table>' +
    				 	 '</div>';
    	
    	
    	
    	
    	//add the options to the page
    	$('#carts').append(optionsStr);

		$("select[name='fileC']").attr("selectedIndex",3);
    	
		
    	if(ESGF.setting.fileCounter == 5) {
    		$("select[name='fileC']").attr("selectedIndex",0);
		} else if(ESGF.setting.fileCounter == 10) {
    		$("select[name='fileC']").attr("selectedIndex",1);
		} else if(ESGF.setting.fileCounter == 25) { 
    		$("select[name='fileC']").attr("selectedIndex",2);
		} else {
    		$("select[name='fileC']").attr("selectedIndex",3);
		}
    	
    	$('td.sfileCounter').show();
    	
    	
    	
    	
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
	        self.createTemplateShort(self.selected_arr);
		}
		
		
       
		
	},

	
	
	
	createTemplateShort: function() {
		
		var self = this;
		
    	
		//alert(self.selected_arr);
		
		
		for(var i=0;i<self.selected_arr.length;i++) {
			var datasetList = '';
			
			datasetList += '<tr style="margin-top:50px;" class="top_level_data_item"  >'
			
			datasetList += '<td style="width: 40px;"><input class="topLevel" type="checkbox" checked="true" /> </td>';	
			
			datasetList += '<td style="width: 325px;font-size:13px">';
			
			//alert(ESGF.localStorage.get('dataCart',self.selected_arr[i])['numFiles']);
			
			datasetList += '<div style="word-wrap: break-word;font-weight:bold"  ><span class="datasetId">' + self.selected_arr[i] + '</span></div>';
			datasetList += '<span>' + ' (Total Number of Files: ' +  ESGF.localStorage.get('dataCart',self.selected_arr[i])['numFiles'] + ')</span>';
			//<span class="datasetId">${datasetId}</span>
			//(<span class="datasetCount_${$item.replaceChars(datasetId)}">${count}</span> files) 
		    //</div>
			
			
			datasetList += '</td>';
			
			datasetList += '<td style="font-size:11px;float:right" >';
			
			datasetList += '<span class="showAllFiles_short" style="display:none;font-weight:bold;"> Expanding... </span>';
			datasetList += '<a class="showAllFiles_short" style="cursor:pointer">Expand</a> | ';
			datasetList += '<span class="wgetAllFiles_short" style="display:none;font-weight:bold;"> Downloading... </span>';
			datasetList += '<a class="wgetAllFiles_short" style="cursor:pointer"> WGET </a> |';
			datasetList += '<span class="globusOnlineAllFiles_short" style="display:none;font-weight:bold;"> Transferring to GO page...</span>';
			datasetList += ' <a class="globusOnlineAllFiles_short" style="cursor:pointer">Globus Online</a> |';
			datasetList += ' <a class="remove_dataset_short" style="cursor:pointer">Remove</a>'; 
			datasetList += '</td>';	
			
			
			datasetList += '</tr>';
			datasetList += '<tr class="file_rows_' + replaceChars(self.selected_arr[i]) + '" style="">';
			datasetList += '</tr>';
			
			$( "#datasetList").append(datasetList);
			//alert('datasetList length: ' + datasetList.length);
		}
		
		//$( "#datasetList").append(datasetList);
		
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
    
    
    
    // old version of the data cart template

    /**
     * Create the template for the datacart
     * 
     * There is a new procedure for creating the datacart template because the search API is now called
     * 
     * 
     * 
     */
    /*
    createTemplate: function() {

    	var self = this;
    	
    	//get all of the search constraints (fq params)
    	var fqParamStr = getFqParamStr();
    	
    	//get the peers
    	var peerStr = getPeerStr();
    	
    	//get the technotes
    	var technoteStr = getTechnoteStr();
    	
    	//get the ids
    	var idStr = getIdStr();
    	
    	//assemble the query string
    	var queryStr = {"idStr" : idStr, 
    					"peerStr" : peerStr, 
    					"technotesStr" : technoteStr, 
    					"showAllStr" : ESGF.setting.showAllContents, 
    					"fqStr" : fqParamStr, 
    					"initialQuery" : "true"};
		
    	
    	//getter for the data cart tab
		var selected = $( "#myTabs" ).tabs( "option", "selected" );
		
		//only make the ajax call when the data cart is selected
		if(selected == 1) {
			
			self.addDataCartSpinWheel();
			
			$.ajax({
				url: '/esgf-web-fe/solrfileproxy2/datacart',
				global: false,
				type: "GET",
				data: queryStr,
				dataType: 'json',
				success: function(data) {
					
					self.removeDataCartSpinWheel();
					
					var fileDownloadTemplate = rewriteDocsObject(data.docs);
					
					$( "#cartTemplateStyledNew2").tmpl(fileDownloadTemplate, {
						
						replaceChars : function (word) {
							LOG.debug("Replacing Char: " + word + " " + word.length);
							return replaceChars(word);
			            },
			            abbreviate : function (word) {
			            },
			            addOne: function(num) {
			            },
			            sizeConversion : function(size) {
			            	return sizeConversion(size);
			            }
			            
			        })
			        .appendTo("#datasetList")
			        .find( "a.showAllChildren" ).click(function() {

			        });
					
				},
				error: function() {
					alert('Error loading data cart');
				}
			})
		}
    	
	}
    */
    
    
    
    
    
});



}(jQuery));

function formatPrice(price) {
    return "ZZZZ" + price;
}

function replaceChars(word) {

    var newWord = word.replace(/\./g,"_");
    newWord = newWord.replace(":","_");
    newWord = newWord.replace("|","_");
    
    return newWord;
}

function sizeConversion(size) {
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

function getFqParamStr() {
	var fqParamStr = '';
	var fqParams = ESGF.localStorage.getAll('esgf_queryString');
	for(var key in fqParams) {
		if(key != 'type:Dataset' && (key.search('distrib') < 0)) {
			fqParamStr += fqParams[key] + ';';
		}
	}
	return fqParamStr;
}

function getIdStr () {
	var idStr = '';
	var selected_arr = ESGF.localStorage.toKeyArr('dataCart');;
	for(var i=0;i<selected_arr.length;i++) {
		if(i != (selected_arr.length - 1)) {
    		idStr += selected_arr[i] + ';';
		} else {
			idStr += selected_arr[i];
		}
	}
	return idStr;
}

function getIndividualPeer(id) {
	var peerStr = '';
	
	var datasetInfo = ESGF.localStorage.get('dataCart',id);
	
	peerStr = datasetInfo['peer'];
	
	
	return peerStr.toString();
	
}

function getPeerStr() {
	var peerStr = '';
	
	//the datasetInfo object will have a 'peer' and 'xlink' property
	var selected_arr = ESGF.localStorage.toKeyArr('dataCart');
	for(var i=0;i<selected_arr.length;i++) {
		var datasetInfo = ESGF.localStorage.get('dataCart',selected_arr[i]);

		//extract the peer and 'xlink' and add it to the arrs
		if(i!=0) {
			peerStr += ';'+datasetInfo.peer;
			//technoteArr += ';'+datasetInfo.xlink;
		} else {
			peerStr += datasetInfo.peer;
			//technoteArr += datasetInfo.xlink;
		};
	}
	
	return peerStr;
}

function getTechnoteStr() {
	
	var technoteStr = '';
	
	//the datasetInfo object will have a 'peer' and 'xlink' property
	var selected_arr = ESGF.localStorage.toKeyArr('dataCart');
	
	
	for(var i=0;i<selected_arr.length;i++) {
		var datasetInfo = ESGF.localStorage.get('dataCart',selected_arr[i]);

		
		//extract the peer and 'xlink' and add it to the arrs
		if(i!=0) {
			//peerArr += ';'+datasetInfo.peer;
			technoteStr += ';'+datasetInfo.xlink;
		} else {
			//peerArr += datasetInfo.peer;
			technoteStr += datasetInfo.xlink;
		};
	}
	
	return technoteStr;
}




function rewriteDocsObject(docs) {
	
	
	if(docs.doc != undefined) {
		var docLength = docs.doc.length;
		
		//if the doc length is undefined, then the number of docs returned may be one
		//there is a bug in the json java code that will automatically convert this to a json object
		if(docLength == undefined) {
			var docArray = new Array();
			docArray.push(docs.doc);
			docs['doc'] = docArray;
			docLength = docs.doc.length;
		}
		
		//if the file length is zero
		//if the file length is undefined then the number of files returned may be one
		//if(data.docs.doc.count > 0) {
		for(var i=0;i<docs.doc.length;i++) {
				
			if(docs.doc[i].count > 0) {
				//alert('doc id: ' + data.docs.doc[i].datasetId);
				//alert(data.docs.doc[i].files.file.length);
				var fileLength = docs.doc[i].files.file.length;
					
				if(fileLength == undefined) {
					var fileArray = new Array();
					fileArray.push(docs.doc[i].files.file);
					docs.doc[i].files['file'] = fileArray;
				} 
			}
				
		}
		
		//This code ensures that the services are arrays
		//Why is this code needed? Bug in the JSON java code
		for(var i=0;i<docs.doc.length;i++) {
			if(docs.doc[i].count > 0) {
					
				for(var j=0;j<docs.doc[i].files.file.length;j++) {
					if(docs.doc[i].files.file[j].services.service == 'HTTPServer' ||
    					docs.doc[i].files.file[j].services.service == 'OPENDAP' || 
    					docs.doc[i].files.file[j].services.service == 'SRM' ||
    					docs.doc[i].files.file[j].services.service == 'GridFTP') {
    						
    					var serviceArray = new Array();
    					serviceArray.push(docs.doc[i].files.file[j].services.service);
    					docs.doc[i].files.file[j].services['service'] = serviceArray;
    					
    					var urlsArray = new Array();
    					urlsArray.push(docs.doc[i].files.file[j].urls.url);
    					docs.doc[i].files.file[j].urls['url'] = urlsArray;
    					
    					var mimesArray = new Array();
    					mimesArray.push(docs.doc[i].files.file[j].mimes.mime);
    					docs.doc[i].files.file[j].mimes['mime'] = mimesArray;
    					
    				}
				}
			}
		}
		
		
	} else {
		//alert('No data sets have been added to your cart');
	}
	
	
	return docs;
	
}
