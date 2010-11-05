/*
 * Author: 		Feiyi Wang
 * Created: 	October 12, 2010
 */

$(document).ready(function(){

	
	//global vars
	
	var searchBox = $("#searchbox");
	var data = $("#searchbox").val();
	var searchBoxDefault = "Search projects, datasets, and more";
	
	
	//Effects for both searchbox
	searchBox.focus(function(e){
		$(this).addClass("active");
	});
	searchBox.blur(function(e){
		$(this).removeClass("active");
	});
	
	searchBox.focus(function(){
		if($(this).attr("value") == searchBoxDefault) $(this).attr("value", "");
	});
	searchBox.blur(function(){
		if($(this).attr("value") == "") $(this).attr("value", searchBoxDefault);
	});	
	

	/**
	 * handle reset link
	 */
	$("div#showReset").click(function(){
		if ($('div#optionPane').is(":visible")) {
			$('div#optionPane').hide();
			$('div#showOptions').html('<a href="#">More Options</a>');
		}
		
		if ($('div#search_wrapper').is(":visible"))
			$('div#search_wrapper').hide();
		
		window.location='./search';
	});
	
	/** 
	 * Set up tool tip
	 */
	
	$("#showOptions").tooltip({effect: 'fade'});
	$("#showConstraints").tooltip({effect: 'fade'});
	$("#showReset").tooltip({effect: 'fade'});
	$("#map_canvas").tooltip({effect: 'fade'});
});
