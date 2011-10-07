
    
$(function(){
	
    	function addTextToStorage(fq,val) {
    		
    		if(fq == undefined) {
    			if(val == '') {
  	  				fq += 'type:Dataset;text:' + '*;';
  	  			} else {
  	  				fq += 'type:Dataset;text:' + val + ';';
  	  			}
  	  			localStorage['fq'] = fq;
    		} else if(fq == null) {
    			if(val == '') {
  	  				fq += 'type:Dataset;text:' + '*;';
  	  			} else {
  	  				fq += 'type:Dataset;text:' + val + ';';
  	  			}
  	  			localStorage['fq'] = fq;
    		} else {
    			var duplicateFlag = isDuplicate(fq,val);
      	  		//first make sure this text does not duplicate a previous text
      	  		if(!duplicateFlag) {
      	  			if(val == '') {
      	  				fq += 'text:' + '*;';
      	  			} else {
      	  				fq += 'text:' + val + ';';
      	  			}
      	  			localStorage['fq'] = fq;
      	  		}
    		}
    	}
    	
    	/*
    	 * Check for duplicate in the storage string (i.e. make sure a user doesn't query "temperature" twice)
    	 */
    	function isDuplicate(fq,val) {
    		var isDuplicate = false;
    		var allFqs = fq.split(";");
  			//loop over all existing queries - note 'length-1' was used to ignore the trailing whitespace of the last split
  	  		for(var i=0;i<allFqs.length-1;i++)
  			{
  				if(allFqs[i] == ('text:' + val)) {
  					isDuplicate = true;
  				}
  			}
    		return isDuplicate;
    	}
    	
    	
    	/* 
    	 * Search button event places the text in html5 storage and navigates user to the live search page
    	 */
    	$('#home_search-button').click(function(){
    		//alert($('input#home_query').val());
    		var searchStr = $('input#home_query').val();
    		
    		if(ESGF.setting.storage) {
    			//get the previous local storage
    			var fq = localStorage['fq'];
    			//call the add text to storage methods to make it part of the fq string
    			addTextToStorage(fq,searchStr);
    		}
      	  	location.href=window.location+'live';
    	});
    	
    	
    	
    	$('#home_query').bind('keydown', function(e) {
    		var searchStr = $('input#home_query').val();
    		
    		if (e.which === 13) {
    			if(ESGF.setting.storage) {
    	    		var fq = localStorage['fq'];
    	    		addTextToStorage(fq,searchStr);
    			}
          	  	location.href=window.location+'/live';
    		}
    	});
    	
    	
    	
    	$('h3#navigate').hover(
    		function () {
        		$(this).css({'color':'blue','cursor':'pointer'});
    		},
    		function () {
    			$(this).css({'color':'#7d5f45'});
    		}
    	);
    	
    	$('h3#navigate').live('click',function(){
      	  	location.href=window.location+'/live';
    	});
    });