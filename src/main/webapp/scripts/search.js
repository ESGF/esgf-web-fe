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
	

});
