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
        
    	
    	//grab all the keys from the datacart map and place in an array
    	self.selected_arr = ESGF.localStorage.toKeyArr('dataCart');
    	
	},
	
	checkUberAccess: function (accessType) {
		
		var datacart = ESGF.localStorage.getAll('dataCart');
		
		var hasAccess = false;
		
		for(var key in datacart) {
			//if(datacart[key]['access'] != undefined) {
			if(datacart[key]['access'] == undefined) {
				hasAccess = true;
			} else {
				var access = new String(datacart[key]['access']);
				if(access.search(accessType) > -1) {
					hasAccess = true;
				}
			}
				
			//} 
		}
		
		
		return hasAccess;
	},
	
	checkDatasetAccess: function (accessType,datasetId,access) {
		
		var hasAccess = false;
		
		var accessStr = new String(access);
		
		//alert('accessStr: ' + accessStr + ' ' + accessType + ' ' + accessStr.search(accessType));
		
		if(accessStr.search(accessType) > -1) {
			hasAccess = true;
		}
		/*
		
			var datacart = ESGF.localStorage.getAll('dataCart');
			
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
		}
		
		//alert('hasAccess: ' + hasAccess + " for " + accessType);
		
		//LOG.debug('access: ' + datacart[datasetId]['access'] + ' hasAccess: ' + hasAccess);
		*/
		
		
		return hasAccess;
		
	},
	
	writeTopMenuMarkup: function () {
	
		var self = this;
		
		//this string represents the options for the data cart
    	//as of now, the options are:
    	// - radio box that the user chooses to show files
    	// - the "uber" script option
    	var optionsStr = '<div id="radio" style="margin-bottom:30px;display:none">' + 
    				 	 '<table>' +
    				 	 '<tr>' +
    				 	 '<td style="width:80px;font-size:12px;padding:0px; ">' +
    				 	 '<input type="radio" id="datacart_filter_all" name="datacart_filter" value="all" /> ' +
    				 	 '<span id="filter_over_all">Show all </span>' +
    				 	 '</td>' +
    				 	 '<td style="width:150px;font-size:12px;padding:0px; ">' +
    				 	 '<input type="radio" class="north" id="datacart_filtered" name="datacart_filter" value="filtered" /> ' +
    				 	 '<span id="filter_over_text">Filter over text </span>' +
    				 	 '</td>';
    	
    	optionsStr += '<td style="font-size:12px;width:20px;">' +
		  '</td>';
    	
		
    	var hasGridFTP = self.checkUberAccess('GridFTP');
    	if(hasGridFTP) {
    		optionsStr += '<td style="font-size:12px;width:80px;">' +
		 	 			  '<a id="uber_GO_script_short" style="cursor:pointer">Globus Online All Selected</a>' +
		 	 			  '</td>';
    	} else {
    		optionsStr += '<td style="font-size:12px;width:80px">' +
			  '</td>';
    	}
    	
    	var hasHTTP = self.checkUberAccess('HTTPServer');
    	if(hasHTTP) {
        	optionsStr += '<td style="font-size:12px;width:60px;">' +
        				  '<a id="uber_script_short" style="cursor:pointer">WGET All Selected</a>' +
        				  '</td>';
    	} else {
    		optionsStr += '<td style="font-size:12px;width:60px;">' +
			  '</td>';
    	}
    	
	 	optionsStr += '<td style="font-size:12px;width:40px; ">' +
	 	 '<a id="remove_all_short" style="cursor:pointer">Remove All</a>' +
	 	 '</td>';
    	
    	optionsStr +=	 '</tr>' +
    				 	 '</table>' +
    				 	 '</div>';
    	
    	return optionsStr;
		
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

    	var optionsStr = self.writeTopMenuMarkup();

    	
    	/*
    	if($('span#datacartOpen').html() == 'true') {
    		$( "#myTabs" ).tabs({ selected: 1 });
    	}
    	*/
    	
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
    	
    	
    	var urlParams = ESGF.datacart.getURLParams();
		
    	if(urlParams['tab'] == 'datacart') {
    		//override check
    		if(urlParams['override'] == 'true') {
    			ESGF.setting.datacartOverride = true;
    		} else {
    			ESGF.setting.datacartOverride = false;
    		}
    	}
		

    	if(self.selected_arr != null) {
    		
    		
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
    			
    			var dataset_arr = '';//self.selected_arr;
    			
    			//override with url params
    			if(ESGF.setting.datacartOverride) {
    				var datasetParams = ESGF.datacart.getDatasetIdsFromURL();
    				//alert('datasetParams ' + datasetParams);
    	        	
    				//at this point, need to convert the datasetParams array to the 
    				//alert('convert');
    				
    				dataset_arr = datasetParams;
    				
    			} else {
    				
    				dataset_arr = self.selected_arr;
    			
    			}
    			
    			
    	        self.createTemplateShort(dataset_arr);
    	        
    	        /*
    	        if(ESGF.setting.datacartOverride) {
    	        	
    	        	//apply the new settings to the datacart cookie
    	        	var datasetParams = ESGF.datacart.getDatasetIdsFromURL();
    	        	
    	        	
    	        	
    	        	
    	        	var dataset_id = datasetParams[0];
    	        	
    	        	alert('dataset_id: ' + dataset_id);
    	        	
    	        	self.updateDatacartLocalStorage(dataset_id);
    	        	
           	
    	        	
    	        	ESGF.setting.datacartOverride = false;
    	        	
    	        }
    	        */
    		}
    		
    		
    	}
    	
		
	},
	
	updateDatacartLocalStorage: function(dataset_id) {
		
		var self = this;
		
		var datacart_entry = self.getDataCartEntryFromURLParams(dataset_id);
    	
		if(datacart_entry != undefined) {
			// at this point defined the changing variables
			var datasetId = datacart_entry['datasetId'];
			var numFiles = datacart_entry['numFiles'];
			var peer = datacart_entry['numFiles'];
			var access = datacart_entry['access'];
			var xlink = datacart_entry['xlink'];
			
			var datasetInfo = {
					
					'numFiles' : numFiles, 
					'peer' : peer, 
					'xlink' : xlink, 
					'access' : access,
					
			};

			ESGF.localStorage.remove('dataCart',dataset_id);
			ESGF.localStorage.put('dataCart',dataset_id,datasetInfo);
			
			ESGF.localStorage.printMap('dataCart');
			
			var a = ESGF.localStorage.get('dataCart',dataset_id);
		}
		
		
		
		
	},
	
	
	
	getDataCartEntryFromURLParams: function(dataset_id) {
		
		var srmdatasetdatacarturl = '/esgf-web-fe/srmdatacartproxy/datacart';
		
		
		var queryString = 
    	{
    			"dataset_id" : dataset_id
		};
		
		var datacart_entry = undefined;

		
		$.ajax({
			url: srmdatasetdatacarturl,
			global: false,
			type: 'POST',
			async: false,
			dataType: 'json',
			data: queryString,
			success: function(data) {
				//alert('data: ' + data);
				//if(datacart_entry != "failure") {
					datacart_entry = data['datacartdataset'];
				//} else {
					//alert('failure');
				//}
			},
			error: function( jqXHR, textStatus, errorThrown ) {
				alert('textStatus: ' + textStatus + ' errorThrown: ' + errorThrown);
				//alert('error in srm dataset datacart');
				
			}
		});
		
		if(datacart_entry['datasetId'] != "undefined") {
			//slight glitch in access -> needs fixing
			var access = datacart_entry['access'];
			
			var access_arr = new Array();
			for(var i=0;i<access.split(';').length;i++) {
				//alert('access i: ' + i + ' ' + access[i]);
				access_arr.push(access.split(';')[i]);
			}
			datacart_entry['access'] = access_arr;
		}
		
		
		return datacart_entry;
		
	},

	
	
	
	createTemplateShort: function(dataset_arr) {
		
		var self = this;
		
		//alert('dataset_arr: ' + dataset_arr + ' len: ' + dataset_arr.length);
		
		for(var i=0;i<dataset_arr.length;i++) {
			
			//write dataset list here
			var datasetList = self.writeDatacartOverrideDatasetMarkup(dataset_arr, i);
			$( "#datasetList").append(datasetList);
			
		}
		
		
		/*
		for(var i=0;i<dataset_arr.length;i++) {
			var datasetList = self.writeDatasetMarkup(dataset_arr,i);
			$( "#datasetList").append(datasetList);
		}
		*/
		
		
		
		/*
		for(var i=0;i<self.selected_arr.length;i++) {
			var datasetList = self.writeDatasetMarkup(i);
			$( "#datasetList").append(datasetList);
		}
		*/
		
	},
	
	writeDatacartOverrideDatasetMarkup: function (dataset_arr,i) {
		
		var self = this;
		
		var dataset_id = dataset_arr[i];
		
		var datasetList = '';
		
		var datacart_entry = '';
		
		
		if(ESGF.setting.datacartOverride) {
			
			datacart_entry = self.getDataCartEntryFromURLParams(dataset_id);
			
		} else {
			
			datacart_entry = ESGF.localStorage.get('dataCart',self.selected_arr[i]);
			
		}
		
		//evaluate the datacart_entry map
		/*
		for(var key in datacart_entry) {
			alert('key: ' + key + ' value: ' + datacart_entry[key]);
		}
		*/
		
		//need to define 
		//datasetId
		var datasetId = dataset_id;//datacart_entry['datasetId'];
		
		//numFiles
		var numFiles = datacart_entry['numFiles'];
		
		//access
		var access = datacart_entry['access'];
		
		//alert('access');
		//for(var key in access) {
		//	alert('key: ' + key + ' value: ' + access[key]);
		//}
		
		datasetList += '<tr style="margin-top:50px;" class="top_level_data_item"  >';
		
		datasetList += '<td style="width: 40px;"><input class="topLevel selected_dataset" type="checkbox" checked="true" />' + 
						'<span class="datasetId" style="display:none">' + dataset_id + '</span>' + 
						'</td>';	
		
		datasetList += '<td style="width: 325px;font-size:13px">';
		
		datasetList += '<div style="word-wrap: break-word;font-weight:bold"  ><span class="datasetId">' + dataset_id + '</span></div>';
		
		datasetList += '<span>' + ' (Total Number of Files for All Variables: ' +  numFiles + ')</span>';
		
		datasetList += '</td>';
		
		datasetList += '<td style="font-size:11px;float:right" >';
		
		
		//test to see if datacart_entry is valid
		if(datacart_entry['datasetId'] != "undefined") {
			

			
			
			datasetList += '<span class="show_files">';
			datasetList += '<span class="showAllFiles_short" style="display:none;font-weight:bold;"> Expanding... </span>';
			datasetList += '<a class="hideAllFiles_short" style="display:none;cursor:pointer">Hide Files</a>' + 
						   '<a class="showAllFiles_short" style="cursor:pointer">Show Files</a> | ';
			datasetList += '</span>';
			
			
			
			var accessType = 'HTTPServer';
			var hasHTTP = self.checkDatasetAccess(accessType,datasetId,access);
			if(hasHTTP) {
				datasetList += '<span class="wgetAllFiles_short" style="display:none;font-weight:bold;"> Downloading... </span>';
				datasetList += '<a class="wgetAllFiles_short" style="cursor:pointer"> WGET </a> |';
			}
			accessType = 'GridFTP';
			var hasGridFTP = self.checkDatasetAccess(accessType, datasetId, access);
			
			
			if(hasGridFTP) {
				if(ESGF.setting.globusonline) {
					datasetList += '<span class="globusOnlineAllFiles_short" style="display:none;font-weight:bold;"> Transferring to GO page...</span>';	
					datasetList += ' <a class="globusOnlineAllFiles_short" style="cursor:pointer">Globus Online</a> |';
				}
			}
			
			
			accessType = 'SRM';
			var hasSRM = self.checkDatasetAccess(accessType, datasetId, access);
			
			
			//alert('hasSRM: ' + hasSRM + ' disabled: ' + !ESGF.setting.srm_disabled);
			
			if(!ESGF.setting.srm_disabled) {
				if(hasSRM) {
					//first check to see if it is cached
					var isCached_url = '/esgf-web-fe/isCachedDataset';
					
					var isCached = false;
					
					var dataset_id = datasetId;//self.selected_arr[i];
					
					var queryString = 
			    	{
			    			"dataset_id" : dataset_id,
			    			"openid" : 'openid'
					};
					
					$.ajax({
						url: isCached_url,
						global: false,
						type: 'GET',
						async: false,
						data: queryString,
						success: function(data) {
							if(data == 'success') {
								isCached = true;
							}
						},
						error: function() {
							alert('error in isCached');
						}
					});
					
					//isCached = true;
					
					if(isCached) {
						datasetList += '<span class="wgetAllFiles_short_SRMConvert" style="display:none;font-weight:bold;"> Downloading... </span>';
						datasetList += '<a class="wgetAllFiles_short_SRMConvert" style="cursor:pointer"> WGET </a> |';
						datasetList += '<span class="globusOnlineAllFiles_short_SRMConvert" style="display:none;font-weight:bold;"> Transferring to GO page...</span>';	
						datasetList += ' <a class="globusOnlineAllFiles_short_SRMConvert" style="cursor:pointer">Globus Online</a> |';
						
					} else {
						datasetList += '<span class="srm_dataset_event" style="display:none;font-weight:bold;"> Transferring to SRM page... </span>';
						datasetList += '<a class="srm_dataset_event" style="cursor:pointer"> SRM </a> |';
					}
				}
				
			} else {
				if(hasSRM) {
					datasetList += '<span class="srm_dataset_event" style="display:none;font-weight:bold;"> Transferring to SRM page... </span>';
					datasetList += '<a class="srm_dataset_event" style="cursor:pointer"> SRM </a> |';
				}
			}
			
			
			
		
		
		
		}
		

		datasetList += ' <a class="remove_dataset_short" style="cursor:pointer">Remove</a>'; 
		datasetList += '</td>';	
		datasetList += '</tr>';
		
		

		//datasetList += '<tr class="file_rows_' + ESGF.datacart.replaceChars(self.selected_arr[i]) + '" style="">';
		datasetList += '<tr class="file_rows_' + ESGF.datacart.replaceChars(dataset_id) + '" style="">';
		datasetList += '</tr>';
		
		
		return datasetList;
		
	},
	
	
        
	/*
	writeDatasetMarkup: function (i) {
		alert('???');
		
		var self = this;
		
		var datasetList = '';
		
		datasetList += '<tr style="margin-top:50px;" class="top_level_data_item"  >';
		
		datasetList += '<td style="width: 40px;"><input class="topLevel" type="checkbox" checked="true" /> </td>';	
		
		datasetList += '<td style="width: 325px;font-size:13px">';
		
		
		datasetList += '<div style="word-wrap: break-word;font-weight:bold"  ><span class="datasetId">' + self.selected_arr[i] + '</span></div>';
		
		datasetList += '<span>' + ' (Total Number of Files for All Variables: ' +  ESGF.localStorage.get('dataCart',self.selected_arr[i])['numFiles'] + ')</span>';
		
		
		datasetList += '</td>';
		
		datasetList += '<td style="font-size:11px;float:right" >';
		
		datasetList += '<span class="show_files">';
		datasetList += '<span class="showAllFiles_short" style="display:none;font-weight:bold;"> Expanding... </span>';
		datasetList += '<a class="hideAllFiles_short" style="display:none;cursor:pointer">Hide Files</a>' + 
					   '<a class="showAllFiles_short" style="cursor:pointer">Show Files</a> | ';
		datasetList += '</span>';
		
		var datasetId = self.selected_arr[i];
		var accessType = 'HTTPServer';
		var hasHTTP = self.checkDatasetAccess(accessType,datasetId);
		if(hasHTTP) {
			datasetList += '<span class="wgetAllFiles_short" style="display:none;font-weight:bold;"> Downloading... </span>';
			datasetList += '<a class="wgetAllFiles_short" style="cursor:pointer"> WGET </a> |';
		}
		accessType = 'GridFTP';
		var hasGridFTP = self.checkDatasetAccess(accessType, datasetId);
		if(hasGridFTP) {
			if(ESGF.setting.globusonline) {
				datasetList += '<span class="globusOnlineAllFiles_short" style="display:none;font-weight:bold;"> Transferring to GO page...</span>';	
				datasetList += ' <a class="globusOnlineAllFiles_short" style="cursor:pointer">Globus Online</a> |';
			}
		}
		
		
		accessType = 'SRM';
		var hasSRM = self.checkDatasetAccess(accessType, datasetId);
		if(hasSRM) {
			//first check to see if it is cached
			var isCached_url = '/esgf-web-fe/isCachedDataset';
			
			var isCached = false;
			
			var dataset_id = self.selected_arr[i];
			
			var queryString = 
	    	{
	    			"dataset_id" : dataset_id,
	    			"openid" : 'openid'
			};
			
			$.ajax({
				url: isCached_url,
				global: false,
				type: 'GET',
				async: false,
				data: queryString,
				success: function(data) {
					if(data == 'success') {
						isCached = true;
					}
				},
				error: function() {
					alert('error in isCached');
				}
			});
			
			//isCached = true;
			
			if(isCached) {
				datasetList += '<span class="wgetAllFiles_short_SRMConvert" style="display:none;font-weight:bold;"> Downloading... </span>';
				datasetList += '<a class="wgetAllFiles_short_SRMConvert" style="cursor:pointer"> WGET </a> |';
				datasetList += '<span class="globusOnlineAllFiles_short_SRMConvert" style="display:none;font-weight:bold;"> Transferring to GO page...</span>';	
				datasetList += ' <a class="globusOnlineAllFiles_short_SRMConvert" style="cursor:pointer">Globus Online</a> |';
				
			} else {
				datasetList += '<span class="srm_dataset_event" style="display:none;font-weight:bold;"> Transferring to SRM page... </span>';
				datasetList += '<a class="srm_dataset_event" style="cursor:pointer"> SRM </a> |';
			}
		}
		
		datasetList += ' <a class="remove_dataset_short" style="cursor:pointer">Remove</a>'; 
		datasetList += '</td>';	
		datasetList += '</tr>';
		
		datasetList += '<tr class="file_rows_' + ESGF.datacart.replaceChars(self.selected_arr[i]) + '" style="">';
		datasetList += '</tr>';
		
		return datasetList;
	},
	*/
    
	
	rewriteTextQuery: function(queryString) {
    	var newQueryString = '';

    	
    	var root = queryString.split('?')[0];
    	
    	
    	
    	var paramArr = (queryString.split('?')[1]).split('&');
    	
    	newQueryString = root + '?';
    	
    	var fullText = '';
    	for(var i=0;i<paramArr.length;i++) {
    		var constraint = paramArr[i];
    		if(constraint != '' && constraint != ' ') {
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
    
    
    
    
    
    
});



}(jQuery));




/*
writeDatasetMarkup: function (dataset_arr,i) {
	
	var self = this;
	
	var datasetId = dataset_arr[i];
	
	var dataset_id = datasetId;
	
	//alert('start here');
	
	var srmdatasetdatacarturl = '/esgf-web-fe/srmdatacartproxy/datacart';
	
	var queryString = 
	{
			"dataset_id" : dataset_id
	};
	
	var datacart_entry = '';
	
	$.ajax({
		url: srmdatasetdatacarturl,
		global: false,
		type: 'POST',
		async: false,
		dataType: 'json',
		data: queryString,
		success: function(data) {
			datacart_entry = data['datacartdataset'];
		},
		error: function() {
			alert('error in srm dataset datacart');
		}
	});
	
	
	//alert('stop here');
	
	var datasetList = '';
	
	datasetList += '<tr style="margin-top:50px;" class="top_level_data_item"  >';
	
	datasetList += '<td style="width: 40px;"><input class="topLevel" type="checkbox" checked="true" /> </td>';	
	
	datasetList += '<td style="width: 325px;font-size:13px">';
	
	datasetList += '<div style="word-wrap: break-word;font-weight:bold"  ><span class="datasetId">' + datacart_entry['datasetId'] + '</span></div>';
	
	//datasetList += '<div style="word-wrap: break-word;font-weight:bold"  ><span class="datasetId">' + self.selected_arr[i] + '</span></div>';
	
	datasetList += '<span>' + ' (Total Number of Files for All Variables: ' +  datacart_entry['numFiles'] + ')</span>';
	
	
	//datasetList += '<span>' + ' (Total Number of Files for All Variables: ' +  ESGF.localStorage.get('dataCart',self.selected_arr[i])['numFiles'] + ')</span>';
	//var datasetInfo = {'numFiles' : evt.data.doc['number_of_files'], 'peer' : evt.data.doc['index_node'] , 'xlink' : evt.data.doc['xlink'], 'access' : evt.data.doc['access']};

	//datasetList += '<span>' + ' (Total Number of Files for All Variables: ' +  ESGF.localStorage.get('dataCart',self.selected_arr[i])['numFiles'] + ')</span>';
	
	
	datasetList += '</td>';
	
	datasetList += '<td style="font-size:11px;float:right" >';
	
	datasetList += '<span class="show_files">';
	datasetList += '<span class="showAllFiles_short" style="display:none;font-weight:bold;"> Expanding... </span>';
	datasetList += '<a class="hideAllFiles_short" style="display:none;cursor:pointer">Hide Files</a>' + 
				   '<a class="showAllFiles_short" style="cursor:pointer">Show Files</a> | ';
	datasetList += '</span>';
	
	
	
	
	var accessType = 'HTTPServer';
	var hasHTTP = self.checkDatasetAccess(accessType,datasetId);
	if(hasHTTP) {
		datasetList += '<span class="wgetAllFiles_short" style="display:none;font-weight:bold;"> Downloading... </span>';
		datasetList += '<a class="wgetAllFiles_short" style="cursor:pointer"> WGET </a> |';
	}
	accessType = 'GridFTP';
	var hasGridFTP = self.checkDatasetAccess(accessType, datasetId);
	if(hasGridFTP) {
		if(ESGF.setting.globusonline) {
			datasetList += '<span class="globusOnlineAllFiles_short" style="display:none;font-weight:bold;"> Transferring to GO page...</span>';	
			datasetList += ' <a class="globusOnlineAllFiles_short" style="cursor:pointer">Globus Online</a> |';
		}
	}
	
	
	accessType = 'SRM';
	var hasSRM = self.checkDatasetAccess(accessType, datasetId);
	if(hasSRM) {
		//first check to see if it is cached
		var isCached_url = '/esgf-web-fe/isCachedDataset';
		
		var isCached = false;
		
		var dataset_id = self.selected_arr[i];
		
		var queryString = 
    	{
    			"dataset_id" : dataset_id,
    			"openid" : 'openid'
		};
		
		$.ajax({
			url: isCached_url,
			global: false,
			type: 'GET',
			async: false,
			data: queryString,
			success: function(data) {
				if(data == 'success') {
					isCached = true;
				}
			},
			error: function() {
				alert('error in isCached');
			}
		});
		
		//isCached = true;
		
		if(isCached) {
			datasetList += '<span class="wgetAllFiles_short_SRMConvert" style="display:none;font-weight:bold;"> Downloading... </span>';
			datasetList += '<a class="wgetAllFiles_short_SRMConvert" style="cursor:pointer"> WGET </a> |';
			datasetList += '<span class="globusOnlineAllFiles_short_SRMConvert" style="display:none;font-weight:bold;"> Transferring to GO page...</span>';	
			datasetList += ' <a class="globusOnlineAllFiles_short_SRMConvert" style="cursor:pointer">Globus Online</a> |';
			
		} else {
			datasetList += '<span class="srm_dataset_event" style="display:none;font-weight:bold;"> Transferring to SRM page... </span>';
			datasetList += '<a class="srm_dataset_event" style="cursor:pointer"> SRM </a> |';
		}
	}
	
	datasetList += ' <a class="remove_dataset_short" style="cursor:pointer">Remove</a>'; 
	datasetList += '</td>';	
	datasetList += '</tr>';
	
	datasetList += '<tr class="file_rows_' + ESGF.datacart.replaceChars(self.selected_arr[i]) + '" style="">';
	datasetList += '</tr>';
	
	
	return datasetList;
},
*/





/*
datasetList += '<tr style="margin-top:50px;" class="top_level_data_item"  >';

datasetList += '<td style="width: 40px;"><input class="topLevel" type="checkbox" checked="true" /> </td>';	

datasetList += '<td style="width: 325px;font-size:13px">';

datasetList += '<div style="word-wrap: break-word;font-weight:bold"  ><span class="datasetId">' + datasetId + '</span></div>';

datasetList += '<span>' + ' (Total Number of Files for All Variables: ' +  numFiles + ')</span>';


datasetList += '</td>';


datasetList += '<td style="font-size:11px;float:right" >';

datasetList += '<span class="show_files">';
datasetList += '<span class="showAllFiles_short" style="display:none;font-weight:bold;"> Expanding... </span>';
datasetList += '<a class="hideAllFiles_short" style="display:none;cursor:pointer">Hide Files</a>' + 
			   '<a class="showAllFiles_short" style="cursor:pointer">Show Files</a> | ';
datasetList += '</span>';


var accessType = 'HTTPServer';
var hasHTTP = self.checkDatasetAccess(accessType,datasetId);
if(hasHTTP) {
	datasetList += '<span class="wgetAllFiles_short" style="display:none;font-weight:bold;"> Downloading... </span>';
	datasetList += '<a class="wgetAllFiles_short" style="cursor:pointer"> WGET </a> |';
}
accessType = 'GridFTP';
var hasGridFTP = self.checkDatasetAccess(accessType, datasetId);
if(hasGridFTP) {
	if(ESGF.setting.globusonline) {
		datasetList += '<span class="globusOnlineAllFiles_short" style="display:none;font-weight:bold;"> Transferring to GO page...</span>';	
		datasetList += ' <a class="globusOnlineAllFiles_short" style="cursor:pointer">Globus Online</a> |';
	}
}

accessType = 'SRM';
var hasSRM = self.checkDatasetAccess(accessType, datasetId);
if(hasSRM) {
	//first check to see if it is cached
	var isCached_url = '/esgf-web-fe/isCachedDataset';
	
	var isCached = false;
	
	var dataset_id = datasetId;//self.selected_arr[i];
	
	var queryString = 
	{
			"dataset_id" : dataset_id,
			"openid" : 'openid'
	};
	
	$.ajax({
		url: isCached_url,
		global: false,
		type: 'GET',
		async: false,
		data: queryString,
		success: function(data) {
			if(data == 'success') {
				isCached = true;
			}
		},
		error: function() {
			alert('error in isCached');
		}
	});
	
	//isCached = true;
	
	if(isCached) {
		datasetList += '<span class="wgetAllFiles_short_SRMConvert" style="display:none;font-weight:bold;"> Downloading... </span>';
		datasetList += '<a class="wgetAllFiles_short_SRMConvert" style="cursor:pointer"> WGET </a> |';
		datasetList += '<span class="globusOnlineAllFiles_short_SRMConvert" style="display:none;font-weight:bold;"> Transferring to GO page...</span>';	
		datasetList += ' <a class="globusOnlineAllFiles_short_SRMConvert" style="cursor:pointer">Globus Online</a> |';
		
	} else {
		datasetList += '<span class="srm_dataset_event" style="display:none;font-weight:bold;"> Transferring to SRM page... </span>';
		datasetList += '<a class="srm_dataset_event" style="cursor:pointer"> SRM </a> |';
	}
}



datasetList += ' <a class="remove_dataset_short" style="cursor:pointer">Remove</a>'; 
datasetList += '</td>';	
datasetList += '</tr>';



//datasetList += '<tr class="file_rows_' + ESGF.datacart.replaceChars(self.selected_arr[i]) + '" style="">';
datasetList += '<tr class="file_rows_' + ESGF.datacart.replaceChars(datasetId) + '" style="">';
datasetList += '</tr>';
*/

