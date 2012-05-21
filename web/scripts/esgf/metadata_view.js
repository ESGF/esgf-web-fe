

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
