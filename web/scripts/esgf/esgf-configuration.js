

//GO restriction
$.ajax({
    url: "searchconfigurationproxy",
    dataType: 'json',
    type: "GET",
    async: false,
    success: function(data) {
    	var params = data['params'];
    	for(var key in params) {
    		var value = new String(params[key]);
    		if(key == 'enableGlobusOnline') {
    			if(value == 'true') {
    				ESGF.setting.globusonline = true;
    			}
    		}
    	}
    },
	error: function() {
		alert('error in getting search params...setting defaults');
	}

});

//LAS restriction
$.ajax({
    url: "lasconfigurationproxy",
    dataType: 'json',
    type: "GET",
    async: false,
    success: function(data) {
    	
    	//alert('d: ' + ' ' + (data['lasconfig']['regex'] instanceof Array) + ' ' + data['lasconfig']['regex'].length);
    	
    	//array object
    	if((data['lasconfig']['regex'] instanceof Array)) {
    		for(var i=0;i<data['lasconfig']['regex'].length;i++) {
        		(ESGF.setting.lasRestrictions).push(data['lasconfig']['regex'][i]);
        		//alert('i: ' + i + ' ' + data['lasconfig']['regex'][i]); 
        	}
    	} 
    	//single object
    	else if (data['lasconfig']['regex'].length > 0) {
    		//alert('single data item: ' + data['lasconfig']['regex']);
    		(ESGF.setting.lasRestrictions).push(data['lasconfig']['regex']);
    		
    	} 
    	//no object
    	else {
    		//alert('no item');
    	}
    	
    	
    },
	error: function() {
		alert('error in getting las config params...setting defaults');
		ESGF.setting.lasRestrictions = new Array();
	}

});
//alert('restriction: ' + ESGF.setting.lasRestrictions);
