


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