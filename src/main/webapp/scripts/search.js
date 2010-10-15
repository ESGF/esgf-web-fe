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
	
	
	$('div#showOptions').click(function() {
		$('div#optionPane').slideToggle(
				'fast',  function() {
		    if ($(this).is(':hidden'))
                     $('div#showOptions').html('<a href="#">More Options</a>')
                 else						
			$('div#showOptions').html('<a href="#">Remove Options</a>')
		});
	});

	function display_map () {
		var mapDiv = document.getElementById('map_canvas');
		var latlng = new google.maps.LatLng(37.09, -95.71);
		var options = {
			center: latlng,
			zoom: 4,
			mapTypeId: google.maps.MapTypeId.ROADMAP
		};
		var map = new google.maps.Map(mapDiv, options);
	};
	
	$("input[name='mapGroup']").change(function() {
		if ($("input[name='mapGroup']:checked").val() == '2dmap') {
			$("div#mapPane").toggle('fast', display_map);
		};
	});

});
