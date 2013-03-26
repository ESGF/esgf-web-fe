ESGF.datacart.a = '';

ESGF.datacart.b = function(category) {
	
};

ESGF.datacart.submitWGETScriptForm = function (queryString,file_ids,dataset_ids) {
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
};

ESGF.datacart.addConstraintsToWGETQueryString = function(queryString,constraints) {
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
};

ESGF.datacart.addConstraintsToWGETQueryString = function(queryString,fqParamStr) {
	//if filtering over search constraints...then add the search constraints to the wget
	
	//var fqParamStr = ESGF.datacart.getFqParamStr();
	
	alert('fqParamStr: ' + fqParamStr);
	
	/*
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
    */
	
    return queryString;
};

ESGF.datacart.replaceChars = function (word) {

    var newWord = word.replace(/\./g,"_");
    newWord = newWord.replace(":","_");
    newWord = newWord.replace("|","_");
    
    //in case of two slashes...needs to be replaced
    newWord = newWord.replace("/","_");
    newWord = newWord.replace("/","_");
    
    return newWord;
};

ESGF.datacart.sizeConversion = function (size) {
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
};


/*-----*/

/*
ESGF.datacart.replacePeriodGlobal = function (word)
{
    var newWord = word.replace(/\./g,"_");
    var newNewWord = newWord.replace(":","_");
    var newNewNewWord = newWord.replace("|","_");
    return newNewNewWord;
};
*/

/*
ESGF.datacart.abbreviateWordGlobal = function (word) {
	var abbreviation = word;
    if(word.length > 25) {
        abbreviation = word.slice(0,10) + '...' + word.slice(word.length-11,word.length);
    }
    return abbreviation;
};
*/

/*
ESGF.datacart.sizeConversionGlobal = function (size) {
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
};
*/


ESGF.datacart.getFqParamStr = function () {
	var fqParamStr = '';
	var fqParams = ESGF.localStorage.getAll('esgf_queryString');
	for(var key in fqParams) {
		if(key != 'type:Dataset' && (key.search('distrib') < 0)) {
			fqParamStr += fqParams[key] + ';';
		}
	}
	return fqParamStr;
};

ESGF.datacart.getIdStr = function () {
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
};

ESGF.datacart.getIndividualPeer = function (id) {
	var peerStr = '';
	
	var datasetInfo = ESGF.localStorage.get('dataCart',id);
	
	peerStr = datasetInfo['peer'];
	
	if(peerStr == undefined) {
		peerStr = 'localhost';
	}
	
	return peerStr.toString();
	
};

ESGF.datacart.getPeerStr = function () {
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
};

ESGF.datacart.getTechnoteStr = function () {
	
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
};




ESGF.datacart.rewriteDocsObject = function (docs) {
	
	
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
	}
	
	
	return docs;
	
};

