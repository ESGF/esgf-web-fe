(function ($) {

	AjaxSolr.FacetSideBarWidget = AjaxSolr.AbstractFacetWidget.extend({
	
		
		afterRequest: function () {
		

            
			var self = this;
			var facet = '';
			
			var facet_arr = new Array();
			
		    for (facet in self.manager.response.facet_counts.facet_fields) {
		    	var facet_obj = new Object();
		    	var facet_val_arr = new Array();
		    	var facet_val_counts = new Array();
		    	for(var facet_value in self.manager.response.facet_counts.facet_fields[facet]) {
		    		var count = parseInt(self.manager.response.facet_counts.facet_fields[facet][facet_value]);
				    facet_val_counts.push(count);
		    		facet_val_arr.push(facet_value);
		    		if (facet === 'project') {
			    		//alert('facet_value: ' + facet_value);
		    			var radix = 10;
		    			//var count = parseInt(self.manager.response.facet_counts,radix);
		    			//alert('facet_value: ' + facet_value + ' ' + count);
		    		}
		    		
		    	}
		    	facet_obj.Facet_name = facet;
		    	facet_obj.Facet_values = facet_val_arr;
		    	facet_obj.Facet_counts = facet_val_counts;
		    	facet_arr.push(facet_obj);
		    	
	    	}
		    
		    
		    
		    if($( "#facetTemplate").html() != null) {

	            $("#facetList").empty();
		    	$( "#facetTemplate").tmpl(facet_arr, {
					
		        })
		    	.appendTo("#facetList")
		    	.find( "a.showFacetValues" ).click(function() {
	                var selectedItem = $.tmplItem(this);
	                for(var i = 0;i<selectedItem.data.Facet_values.length;i++) {
	                    $('li#' + selectedItem.data.Facet_name + '_' + selectedItem.data.Facet_values[i].toString()).toggle();
	                }
	                
		   		});
		    }
		    
		    $('a.alink').click( function () {
				var facet_value = $(this).html();
				
				var facet = $(this).parent().parent().find('a.showFacetValues').html();
				
				/* NEED TO COME BACK - IT ONLY MATCHES whitespace */
				var index = facet_value.search(' ');
				var trimmedFacetValue = facet_value.substr(0,index);
				
				Manager.store.addByValue('fq', facet + ':' + trimmedFacetValue );
				if(ESGF.setting.storage) {
					var fq = localStorage['fq'];
		     	   	if(fq == null) {
		     	   		fq = facet + ':' + trimmedFacetValue + ';';
		     	   		localStorage['fq'] = fq;
		     	   	} else {
			     		  fq += facet + ':' + trimmedFacetValue + ';';
			              localStorage['fq'] = fq;
			     	}
				}
				
	     	   	Manager.doRequest(0);
				
				
			});
	    	
            
	    	
		}
	
	});


	
	
	
	
	
	$(document).ready( function() {
		

		$.fx.speeds._default = 1000;
		
		
		
	});
	
}(jQuery));






/* other facet browsers
 * implementations here
 * 
 * 
if($( "#facetTemplate2").html() != null) {

	$("#facetList2").empty();
	$( "#facetTemplate2").tmpl(facet_arr, {
		
    })
	.appendTo("#facetList2")
	.find( "a.showFacetValues2" ).click(function() {
        var selectedItem = $.tmplItem(this);
        $('li#' + selectedItem.data.Facet_name.toString() + '_facet_container_class2').toggle();
       
		});
	
}

$("#facetList3").empty();


$( "#facetTemplate3").tmpl(facet_arr, {
	
})
.appendTo("#facetList3")
.find( "a.showFacetValues3" ).click(function() {
    var selectedItem = $.tmplItem(this);
    $('li#' + selectedItem.data.Facet_name.toString() + '_facet_container_class3').toggle();
    
	});
*/


/*
if($( "#facetTemplate4").html() != null) {

	$("#facetList4").empty();
    
	
	$( "#facetTemplate4").tmpl(facet_arr, {
		
    })
	.appendTo("#facetList4")
	.find( "a.showFacetValues4" ).click(function() {
        var selectedItem = $.tmplItem(this);
        
		});
}
*/



/*
// main vertical scroll
$("#mainMenu").scrollable({
 
	
	// basic settings
	vertical: true,
	
	
	// up/down keys will always control this scrollable
	keyboard: 'static',
	
	// assign left/right keys to the actively viewed scrollable
	onSeek: function(event, i) {
		//alert('onseek');
		horizontal.eq(i).data("scrollable").focus();
	}
	
 
// main navigator (thumbnail images)
})
.navigator("#mainMenu_navi");

// horizontal scrollables. each one is circular and has its own navigator instance
var horizontal = $(".scrollable").scrollable({ circular: true }).navigator(".navi");
 

// when page loads setup keyboard focus on the first horzontal scrollable
if(horizontal.eq(0) == null) {
	horizontal.eq(0).data("scrollable").focus();
}

$('body').click(function() {
	 //Hide the menus if visible
	 $('#mainMenu').hide();
});
	
$('#mainMenu_navi').click(function(){
    event.stopPropagation();
	//$('#main').css('margin-top','0px');
	$('#mainMenu').show();
});	

$('li#facet_project').click(function(event) {
	event.stopPropagation();
	$('#mainMenu').css('margin-top','0px');
	$('#mainMenu').show();
});

$('li#facet_model').click(function(event) {
	event.stopPropagation();
	$('#mainMenu').css('margin-top','30px');
	$('#mainMenu').show();
});

$('li#facet_experiment').click(function(event) {
	event.stopPropagation();
	$('#mainMenu').css('margin-top','60px');
	$('#mainMenu').show();
});

$('li#facet_frequency').click(function(event) {
	event.stopPropagation();
	$('#mainMenu').css('margin-top','90px');
	$('#mainMenu').show();
});

$('li#facet_realm').click(function(event) {
	event.stopPropagation();
	$('#mainMenu').css('margin-top','120px');
	$('#mainMenu').show();
});

$('li#facet_instrument').click(function(event) {
	event.stopPropagation();
	$('#mainMenu').css('margin-top','150px');
	$('#mainMenu').show();
});

$('li#facet_variable').click(function(event) {
	event.stopPropagation();
	$('#mainMenu').css('margin-top','180px');
	$('#mainMenu').show();
});

$('li#facet_cf_variable').click(function(event) {
	event.stopPropagation();
	$('#mainMenu').css('margin-top','210px');
	$('#mainMenu').show();
});

$('li#facet_gcmd_variable').click(function(event) {
	event.stopPropagation();
	$('#mainMenu').css('margin-top','240px');
	$('#mainMenu').show();
});

$('div#mainMenuPages').click(function(event) {
	event.stopPropagation();
});
*/