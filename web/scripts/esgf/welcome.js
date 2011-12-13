
    
$(function(){
	
    	function addTextToStorage(esgf_fq,val) {
    		
    		//put dataset type
    		ESGF.localStorage.put('esgf_fq','type:Dataset','type:Dataset');
    		
    		//put replica type
    		ESGF.localStorage.put('esgf_fq','replica:false','replica:false');
            
    		//put text 
    		if(val == '') {
    			ESGF.localStorage.put('esgf_fq','text:' + '*','text:' + '*');
    		} else {
        		ESGF.localStorage.put('esgf_fq','text:' + val,'text:' + val);
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
    		var searchStr = $('input#home_query').val();
    		
    		if(ESGF.setting.storage) {
    			//get the previous local storage
    			//var fq = localStorage['fq'];

    	        //get all of the fq parameters from the localstore
    	        var esgf_fq = ESGF.localStorage.getAll('esgf_fq');
    	        

    	        ESGF.localStorage.printMap('esgf_fq');
    			
    			
    			//call the add text to storage methods to make it part of the fq string
    			addTextToStorage(esgf_fq,searchStr);
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