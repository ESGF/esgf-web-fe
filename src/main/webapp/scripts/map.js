/*
 * Author: 		Feiyi Wang
 * Created: 	October 12, 2010
 */

$(document).ready(function(){

	var map, geocoder, marker, infowindow;
	
	
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
		map = new google.maps.Map(mapDiv, options);
	};
	
	$("input[name='mapGroup']").change(function() {
		if ($("input[name='mapGroup']:checked").val() == '2dmap') {
			$("div#3dmapPane").hide();
			$("div#mapPane").toggle('fast', display_map);
		} else {
			$("div#mapPane").hide();
			$("div#3dmapPane").toggle('fast');
		}
	});
	
	$("#geoloc input[name='location']").keypress(function(e) {
		e.preventDefault();
		if (e.keyCode == 13) {
			alert('you pressed enter');
		}
	});
	
	function getCoordinates() {
		
	}

});
