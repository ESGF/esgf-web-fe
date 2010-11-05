/*
 * Author: 		Feiyi Wang <fwang2@ornl.gov>
 * 
 * Created: 	October 12, 2010
 */

$(document).ready(function(){

	var map, geocoder, infowindow;
	var num_of_markers = 0;
	var max_of_markers = 3;
	var bounds;
	var cbounds = new google.maps.LatLngBounds();
	var markerGroup = new Array(max_of_markers);
	var poly;
	

	function clearAreaChoice() {
		
		$("input:radio").attr('checked', false);

	}
	
	function clearMarkers() {
		for (var i=0; i < max_of_markers; i++) {
			if (markerGroup[i]) 
				markerGroup[i].setMap(null);
				markerGroup[i] = null;
		}
		
		num_of_markers = 0;
		
		// close info window
		if (infowindow)
			infowindow.close();
		if (poly)
			poly.setMap(null);
		
		// clear marker area content
		$("#markers").html("");
		$("#areaSelected").html("");
		
		clearAreaChoice();
	}
	
	
	
	function addMarker(location) {
		if (num_of_markers < max_of_markers) {
			
			// create a new marker
			var marker = new google.maps.Marker({
				draggable: true,
				position: location,
				map: map
			});
			
			markerGroup[num_of_markers] = marker;
			num_of_markers += 1;
			

			google.maps.event.addListener(marker, "dragstart", function() {
				if (infowindow)
					infowindow.close();
				
			});
			
			google.maps.event.addListener(marker, 'dragend', function() {
				//alert("number of marker:" + num_of_markers);
				if (poly)
					poly.setMap(null);

				clearAreaChoice();
				refreshMarkers();
			});			
			
			return marker;
			
		} else {
			alert("Max markers reached, please clear markers first!")
			return null;
		}
		
	}
	
	function appendMarker(marker, i) {
		var content = '[' + i + '] ' + 'lat ' + 
				marker.getPosition().lat().toFixed(2) + ' , ' +
				'lon ' + 
				marker.getPosition().lng().toFixed(2) + "<br />";
		
		$("#markers").append(content);
		
		if ($("#markers").is(":hidden")) 
			$("#markers").slideToggle('fast');
	}
	
	$('div#showOptions').click(function() {
		$('div#search_wrapper').html("");
		$('div#optionPane').slideToggle(
				'fast',  function() {
		    if ($(this).is(':hidden'))
                     $('div#showOptions').html('<a href="#">Show Options</a>');
                 else						
			$('div#showOptions').html('<a href="#">Hide Options</a>');
		});
	});
	
	function placeMarker(location) {
		var marker = addMarker(location);
		if (marker != null ) {
			appendMarker(marker,  num_of_markers);
		}
	}

	/**
	 * initialize maps
	 */
	function display_map () {
		var mapDiv = document.getElementById('map_canvas');
		var latlng = new google.maps.LatLng(37.09, -95.71);
		var options = {
			center: latlng,
			zoom: 4,
			mapTypeId: google.maps.MapTypeId.ROADMAP
		};
		// check to see if we already have a map object
		if (!map) {
			map = new google.maps.Map(mapDiv, options);
		}
		
		google.maps.event.addListener(map, 'click', function(e) {
			if (poly)
				poly.setMap(null);
			clearAreaChoice();
			placeMarker(e.latLng);
		});
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
		if (e.keyCode == 13) {
			var address = $(this).val();
			getCoordinates(address);
			// clear the field
			$(this).val('');
			return false;
		}
	});
	
	$('#marker_fieldset input[name="clear_markers"]').click(function(e) {
		
		clearMarkers();
		clearAreaChoice();
	});

	function updateInfo(pos) {
		var content = '<div class="infowindow"> Lat: ' + pos.lat().toFixed(4) + "<br />";
		content += "Lng:" + pos.lng().toFixed(4) + "</div>";
		infowindow.setContent(content);		
	}
	
	function dispInfo(marker) {
		var pos = marker.getPosition();
		var content = '<div class="infowindow"> ';  
		content += "Lat: " + pos.lat().toFixed(4) + "<br />";
		content += "Lng:" + pos.lng().toFixed(4) + "</div>";
		if (!infowindow) {
			infowindow = new google.maps.InfoWindow();
		
		}		
		infowindow.setContent(content);		
		infowindow.open(map, marker);
	}
	
	function getCoordinates(address) {
		if (!geocoder) {
			geocoder = new google.maps.Geocoder();
		}
		
		// Creating GeocoderRequest object
		var geocoderRequest = {
				address: address
		};
		
		
		
		// Making the Geocode request
		geocoder.geocode(geocoderRequest, function(results, status) {
			// check status
			if (status == google.maps.GeocoderStatus.OK) {
				// center the map on the returned location
				map.setCenter(results[0].geometry.location);
			}
			
			var marker = addMarker(results[0].geometry.location);
			
			if (marker != null) {
			
				// check if we have a info window
				if (!infowindow) {
					infowindow = new google.maps.InfoWindow();
				
				}
				
				// create contents
				var content = '<div class="infowindow"> <p class="legend">'  
					+ results[0].formatted_address + '</p>'
				content += "Lat: " + results[0].geometry.location.lat() + '<br />';
				content += "Lng: " + results[0].geometry.location.lng();
				content += "</div>"
				
				// Adding the content
				infowindow.setContent(content);
				
				// Open the infowindows
				infowindow.open(map, marker);
								
				// refresh info to panel
				appendMarker(marker,  num_of_markers);
				

				
			}
		});

	}
	
	

	/**
	 * Output each marker's geolocation information
	 * 
	 */
	
	function refreshMarkers() {
		
		$("#markers").html("");
		for (var i=0; i < num_of_markers; i++) {
			appendMarker(markerGroup[i], i+1);
		}
		
	}


	rad = function(x) { return x*Math.PI/180;}
	
	// credit: stackflow
	// Haversine formula
	dist = function(p1, p2) {
		
		  var R = 6371; // earth's mean radius in km
		  var dLat  = rad(p2.lat() - p1.lat());
		  var dLong = rad(p2.lng() - p1.lng());

		  var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
		          Math.cos(rad(p1.lat())) * Math.cos(rad(p2.lat())) * Math.sin(dLong/2) * Math.sin(dLong/2);
		  var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		  var d = R * c;

		  return d.toFixed(3);
		
	}
	
	
	function drawCircle(marker, radius, nodes) {
		var center = marker.getPosition();
		
		var latConv = dist(center, new google.maps.LatLng(center.lat() + 0.1, center.lng()));
		var lngConv = dist(center, new google.maps.LatLng(center.lat(), center.lng() + 0.1));
		
		// loop
		var points = [];
		var step = parseInt(360/nodes) || 40;
		for (var i = 0; i <= 360; i+= step) {
			var pint = new google.maps.LatLng( center.lat() + (radius/latConv * Math.cos(i * Math.PI/180)),
					center.lng() + (radius/lngConv * Math.sin(i * Math.PI/180)));
			points.push(pint);
			cbounds.extend(pint);
		}
		
		points.push(points[0]); // close circle
		
		poly = new google.maps.Polygon({
			paths: points,
			fillColor: "#0055ff",
			fillOpacity: 0.35,
			strokeWeight: 2
		});
		
		if (infowindow)
			infowindow.close();
		
		poly.setMap(map);
		map.fitBounds(cbounds);
	}

	function draw_rect() {
		var ne = bounds.getNorthEast();
		var sw = bounds.getSouthWest();
		
		// need to double check on this
		var se = new google.maps.LatLng(sw.lat(), ne.lng());
		var nw = new google.maps.LatLng(ne.lat(), sw.lng());
		
		var points = [ne, se, sw, nw];
		poly = new google.maps.Polygon({
			paths: points,
			fillColor: "#0055ff",
			fillOpacity: 0.35,
			strokeWeight: 2
		});
		
		
		poly.setMap(map);
		map.fitBounds(bounds);	
	}
	
	
	function getBoundingBox() {
		bounds = new google.maps.LatLngBounds()
		for (var i=0; i < num_of_markers; i++) {
			var marker = markerGroup[i];
			if (marker)
				bounds.extend(marker.getPosition());
		}
		if (infowindow)
			infowindow.close();
		
		// draw rectangle
		draw_rect();
		
		
		
		
		// put out something
		$("#areaSelected").html('<p class="legend"> Bounding Box </p> + ' 
				+ bounds.toString());
		
		if ($("#areaSelected").is(":hidden"))
			$("#areaSelected").slideToggle('fast');
	}

	function redraw_circle() {
		if (poly)
			poly.setMap(null);
		
		radius = $("input[name='radius']").val();
		quality = $("input[name='quality']").val();
		drawCircle(markerGroup[0], radius, quality);
	}
	
	$("input[name='areaGroup']").change(function(e) {

		if ($("input[name='areaGroup']:checked").val() == 'square') {
			
			if (num_of_markers < 2) {
				alert("Please define at least two markers!");
				$(this).attr('checked', false);
				return false;
			}
			getBoundingBox();
			setGeographicConstraint();
			//do the submit here?
			
		} else {
			
			if (num_of_markers != 1) {
				alert("Please define ONE marker, center of interest!")
				$(this).attr('checked', false);
				return false;
			}
			
			if ($("#circleInputs").is(":hidden"))
				$("#circleInputs").slideToggle('fast');
				
			// put out something
			$("#areaSelected").html('<p class="legend"> Center of Interest </p> + ' 
					+ markerGroup[0].getPosition().toString());
			
			if ($("#areaSelected").is(":hidden"))
				$("#areaSelected").slideToggle('fast');
			redraw_circle();
		}		
	});

	$('input[name="redraw_circle"]').click(function(e) {
		
		redraw_circle();
		
	});

	
	
	//not used...for now
	function geosearch() {
		var searchForm = document.getElementById("geo-form");
		
		searchForm.submit();
	}
	
	function setGeographicConstraint() {
		var sw = bounds.getSouthWest();
		var ne = bounds.getNorthEast();
		
		//this is the min long
		var swLng = sw.lng();
		
		//this is the min lat
		var swLat = sw.lat();
		
		//this is the max long
		var neLng = ne.lng();
		
		//this is the max lat
		var neLat = ne.lat();
		
		//var searchForm = document.getElementById("geo-form");
		var searchForm = document.getElementById("search-form");
		var wdinput = searchForm["west_degrees"];
		wdinput.value = swLng;
		var edinput = searchForm["east_degrees"];
		edinput.value = neLng;
		var sdinput = searchForm["south_degrees"];
		sdinput.value = swLat;
		var ndinput = searchForm["north_degrees"];
		ndinput.value = neLat;
		
		//geosearch();
	}
	
	
	
});
