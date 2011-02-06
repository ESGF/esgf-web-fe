
var boundingboxWD, boundingboxED, boundingboxSD, boundingboxND;
var centroidCenter;
var centroidRadius;

$(document).ready(function(){

	var map, geocoder, infowindow;
	var num_of_markers = 0;
	var max_of_markers = 3;
	var bounds;
	var cbounds = new google.maps.LatLngBounds();
	var markerGroup = new Array(max_of_markers);
	var poly;
	

	
	$("div#geo a[rel]").overlay({

		mask: {opacity: 0.5, color: '#000'},
		effect: 'apple',

		onBeforeLoad: function() {
			
			$('.apple_overlay').css({'width' : '640px'});
			$('.overlay_header_buttons').hide();
			
			// grab wrapper element inside content
			var wrap = this.getOverlay().find(".contentWrap");

			// load the page specified in the trigger
			wrap.load(this.getTrigger().attr("href"));
		},

		onLoad: function() {
			clearMarkers();
			clearAreaChoice();
			display_map();
		}


	}); 
	
	
	//ensure that the radio buttons for encloses is checked by default	
	$("input[name='searchType']:first").attr('checked', true);
	
	
	
	$('#marker_fieldset1 input[name="clear_markers"]').live('click',function(e) {
		clearMarkers();
		clearAreaChoice();
	});
	
	//this event will be taken out soon 
	//right now it acts as a guard so that the user cannot perform and overlaps query with a radius search (unimplemented)
	$("input[name='searchType']").live('change',function(e) {
		var searchType = $("input[name='searchType']").val();
		//alert("changing search type to " + searchType);
		
	});
	
	$("#geoloc1 input[name='location']").live('keypress',function(e) {
		if (e.keyCode == 13) {
			var address = $(this).val();
			getCoordinates(address);
			// clear the field
			$(this).val('');
			return false;
		}
	});
	
	$("input[name='areaGroup']").live('change',function(e) {
		if ($("input[name='areaGroup']:checked").val() == 'square') {
			
			if (num_of_markers < 2) {
				//jMsg("warn", "Please define at least two markers!", 5000);
				alert('Please define at least two markers');
				$(this).attr('checked', false);
				return false;
			}
			getBoundingBox();
			//setGeographicConstraint();
			//do the submit here?
			
		} else {
			if (num_of_markers != 1) {
				alert("Please define ONE marker, center of interest!")
				$(this).attr('checked', false);
				return false;
			}

			if ($("#circleInputs1").is(":hidden")) {
				$("#circleInputs1").slideToggle('fast');
			}
				
			// put out something
			$("#areaSelected11").html('<p class="legend"> Center of Interest </p> + ' 
					+ markerGroup[0].getPosition().toString());
					
			if ($("#areaSelected1").is(":hidden")) {
				$("#areaSelected1").slideToggle('fast');
			}

			redraw_circle();
			
		}		
	});

	$('input[name="redraw_circle"]').live('click',function(e) {
		redraw_circle();
	});
	
	$('div#gButton').live('click',function() {
		
		//execute the geospatial query
	    if($("input[name='searchType']:checked").val() != null && $("input[name='areaGroup']:checked").val() != null) {
	    
	    	//erase any previous geospatial request
	    	var fq = Manager.store.values('fq');
	   
	    	for (var i = 0, l = fq.length; i < l; i++) {
	    		//any previous filter query that contains 'east_degrees' can be assumed to be a geo search
	    		if(fq[i].search('east_degrees') != -1) {
	    			Manager.store.removeByValue('fq', fq[i]);
	    		}
	    	}
		
			executeGeospatialQuery();
	    	Manager.doRequest(0);
			
	    } else {
			alert('A Geospatial search type must be selected');
		}
		
		
		
	});
	
	
	
	function display_map () {

		
		var mapDiv = document.getElementById('map_canvas1');
		var latlng = new google.maps.LatLng(38.09, -95.71);
		
		
		var options = {
			center: latlng,
			zoom: 4,
			mapTypeId: google.maps.MapTypeId.ROADMAP
		};
		// check to see if we already have a map object
		//if (!map) {
			map = new google.maps.Map(mapDiv, options);
		//}
		
		//alert('displaying map'); <-- if i keep this in the map displays ok?  MAKES NO SENSE!!!!
		var location = new google.maps.LatLng(30,40);
		
		// create a new marker
		var marker = new google.maps.Marker({
			draggable: true,
			position: location,
			map: map
		});
		
		google.maps.event.addListener(map, 'click', function(e) {
			if (poly)
				poly.setMap(null);
			clearAreaChoice();
			placeMarker(e.latLng);
		});
		
		
	};

	function clearAreaChoice() {
		//alert('in clear area choice');
		$("input:radio").attr('checked', false);

	}


	function placeMarker(location) {
		//alert('in place marker');
		var marker = addMarker(location);
		if (marker != null ) {
			appendMarker(marker,  num_of_markers);
		}
	}

	function clearMarkers() {
		for (var i=0; i < max_of_markers; i++) {
			if (markerGroup[i]) {
				markerGroup[i].setMap(null);
				markerGroup[i] = null;
			}
		}
		
		num_of_markers = 0;
		
		// close info window
		if (infowindow){
			infowindow.close();
		}
		if (poly) {
			poly.setMap(null);
		}
			
		// clear marker area content
		$("#markers1").html("");
		$("#areaSelected11").html("");
		
		clearAreaChoice();
	}




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








	function appendMarker(marker, i) {
		var content = '[' + i + '] ' + 'lat ' + 
				marker.getPosition().lat().toFixed(2) + ' , ' +
				'lon ' + 
				marker.getPosition().lng().toFixed(2) + "<br />";
		
		$("#markers1").append(content);
		
		if ($("#markers1").is(":hidden")) {
			$("#markers1").slideToggle('fast');
		}
			
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




	function addMarker(location) {
		//alert('adding marker? num markers: ' + num_of_markers);
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
			jMsg("warn", "Max markers reached, please clear markers first!", 5000);
			return null;
		}
		
	}








	///
	//  Output each marker's geolocation information
	//  
	// /

	function refreshMarkers() {
		
		$("#markers1").html("");
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
		var latConv = dist(center, new google.maps.LatLng(center.lat() + 0.1, center.lng()))*10;
		var lngConv = dist(center, new google.maps.LatLng(center.lat(), center.lng() + 0.1))*10;
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
		
		//since the circle can be resized, Im placing the call to this method here
		//setGeographicRadiusConstraint();
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
		bounds = new google.maps.LatLngBounds();
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
		$("#areaSelected1").html('<p class="legend"> Bounding Box </p> + ' 
				+ bounds.toString());
		
		if ($("#areaSelected1").is(":hidden"))
			$("#areaSelected1").slideToggle('fast');
	}

	function redraw_circle() {
		if (poly)
			poly.setMap(null);
		
		radius = $("input[name='radius']").val();
		quality = $("input[name='quality']").val();
		drawCircle(markerGroup[0], radius, quality);
	}



	function setGeographicConstraint() {
		
		swapGeoSearchType("BoundingBox");
		
		
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
		
	}


	///
	//  This function essentially does the same as addGeograpichConstraint, only
	//  now the cbounds are used.
	// /
	function setGeographicRadiusConstraint() {
		
		//alert('setting radius constraint?');
		swapGeoSearchType("Radius");
		
		var sw = cbounds.getSouthWest();
		var ne = cbounds.getNorthEast();
		
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
		
	}
	

	function executeGeospatialQuery() {
		console.log('In execute geospatial query');
		
		
		var geoSearchType = $("input[name='searchType']:checked").val();
		var geoShape = $("input[name='areaGroup']:checked").val();
		//alert('executing ' + geoSearchType);
		if(geoSearchType == 'Encloses') {
			encloses(geoShape);
		}
		else {
			overlaps(geoShape);
		}
		
		console.log('End execute geospatial query');
	}

	function encloses(geoSearchType) {
		console.log('In encloses ' + geoSearchType);

		console.log('Bounds: ' + bounds);
		
		//reset the geoQueryString
		geoQueryString = '';
		
		//alert('encloses sq: ' + ($("input[name='areaGroup']:checked").val() == 'square'));
		
		if(geoSearchType == 'circle') { // geosearch type is a bounding box

			var theCenter = markerGroup[0];
			centroidCenter = markerGroup[0].getPosition();
			console.log('Center  lat: ' + theCenter.getPosition().lat()) ;
			console.log('Bounds: ' + bounds);
			
			var sw = cbounds.getSouthWest();
			var ne = cbounds.getNorthEast();
			
			//this is the min long 
			//west_degrees limit
			boundingboxWD = sw.lng();
			
			//this is the min lat
			//south_degrees limit
			boundingboxSD = sw.lat();
			
			//this is the max long
			//east_degrees limit
			boundingboxED = ne.lng();
			
			//this is the max lat
			//north_degrees limit
			boundingboxND = ne.lat();
			
			west_degrees = boundingboxWD;
			east_degrees = boundingboxED;
			south_degrees = boundingboxSD;
			north_degrees = boundingboxND;
			
			
			
			
			south_degreesFQ = 'south_degrees:[' + south_degrees + ' TO *]';
			west_degreesFQ = 'west_degrees:[' + west_degrees + ' TO *]';
			north_degreesFQ = 'north_degrees:[ * TO ' + north_degrees + ']';
			east_degreesFQ = 'east_degrees:[ * TO ' + east_degrees + ']';
			
			geoQueryString += '(' + south_degreesFQ + ' AND ' + west_degreesFQ + ' AND ' + north_degreesFQ + ' AND ' + east_degreesFQ + ')';

			console.log(geoQueryString);
			
			
		}else { // geosearch type is a bounding box
			
			var sw = bounds.getSouthWest();
			var ne = bounds.getNorthEast();
			
			var west_degrees, east_degrees, north_degrees, south_degrees,
			west_degreesFQ, east_degreesFQ, north_degreesFQ, south_degreesFQ; 
			
			//this is the min long 
			//west_degrees limit
			boundingboxWD = sw.lng();
			
			//this is the min lat
			//south_degrees limit
			boundingboxSD = sw.lat();
			
			//this is the max long
			//east_degrees limit
			boundingboxED = ne.lng();
			
			//this is the max lat
			//north_degrees limit
			boundingboxND = ne.lat();
			
			//console.log('BoundingBoxND: ' + boundingboxND);
			
			west_degrees = boundingboxWD;
			east_degrees = boundingboxED;
			south_degrees = boundingboxSD;
			north_degrees = boundingboxND;
			
			south_degreesFQ = 'south_degrees:[' + south_degrees + ' TO *]';
			west_degreesFQ = 'west_degrees:[' + west_degrees + ' TO *]';
			north_degreesFQ = 'north_degrees:[ * TO ' + north_degrees + ']';
			east_degreesFQ = 'east_degrees:[ * TO ' + east_degrees + ']';
			
			geoQueryString += '(' + south_degreesFQ + ' AND ' + west_degreesFQ + ' AND ' + north_degreesFQ + ' AND ' + east_degreesFQ + ')';

			console.log(geoQueryString);
		}
		
		Manager.store.addByValue('fq',geoQueryString);
		
		console.log('End encloses');
		
	}

	
	
	
	function overlaps(geoSearchType) {

		geoQueryString = '';
		
		//alert('in overlaps checking to see if it is circle input');
		if(geoSearchType == 'circle') { // geosearch type is a bounding box
			var theCenter = markerGroup[0];
			centroidCenter = markerGroup[0].getPosition();
			
			var sw = cbounds.getSouthWest();
			var ne = cbounds.getNorthEast();
			
			//this is the min long 
			//west_degrees limit
			boundingboxWD = sw.lng();
			//this is the min lat
			//south_degrees limit
			boundingboxED = sw.lat();
			//this is the max long
			//east_degrees limit
			boundingboxSD = ne.lng();
			//this is the max lat
			//north_degrees limit
			boundingboxND = ne.lat();
			
			
			//case 1
		    //NE point in bounding box
		    geoQueryString += '(east_degrees:[' + boundingboxWD + ' TO ' + boundingboxED + '] AND ' +
		                 'north_degrees:[' + boundingboxSD + ' TO ' + boundingboxND + '])';
		    
		    geoQueryString += ' OR ';
		    
		    //case 2
		    //SE point in bounding box
		    geoQueryString += '(east_degrees:[' + boundingboxWD + ' TO ' + boundingboxED + '] AND ' +
		                 'south_degrees:[' + boundingboxSD + ' TO ' + boundingboxND + '])';
		    
		    geoQueryString += ' OR ';
		    
		    //case 3
		    //SW point in bounding box
		    geoQueryString += '(west_degrees:[' + boundingboxWD + ' TO ' + boundingboxED + '] AND ' +
		                 'south_degrees:[' + boundingboxSD + ' TO ' + boundingboxND + '])';
		    
		    
		    geoQueryString += ' OR ';
		    
		    //case 4
		    //NW point in bounding box
		    geoQueryString += '(west_degrees:[' + boundingboxWD + ' TO ' + boundingboxED + '] AND ' +
		                 'north_degrees:[' + boundingboxSD + ' TO ' + boundingboxND + '])';

		    geoQueryString += ' OR ';
		    
		    //case 5
		    //east degree in range and n & s are above and below respectively
		    geoQueryString += '(east_degrees:[' + boundingboxWD + ' TO ' + boundingboxED + '] AND ' +
		                 'south_degrees:[ * TO ' + boundingboxSD + '] AND ' +
		                 'north_degrees:[' + boundingboxND + ' TO ' + '* ])';

		    geoQueryString += ' OR ';
		    
		    //case 6
		    //west degree in range and n & s are above and below respectively
		    geoQueryString += '(west_degrees:[' + boundingboxWD + ' TO ' + boundingboxED + '] AND ' +
		                 'south_degrees:[ * TO ' + boundingboxSD + '] AND ' +
		                 'north_degrees:[' + boundingboxND + ' TO ' + '* ])';

		    geoQueryString += ' OR ';
		    
		    //case 7
		    //north degree in range and n & s are above and below respectively
		    geoQueryString += '(north_degrees:[' + boundingboxSD + ' TO ' + boundingboxND + '] AND ' +
		                 'west_degrees:[ * TO ' + boundingboxWD + '] AND ' +
		                 'east_degrees:[' + boundingboxED + ' TO ' + '* ])';

		    geoQueryString += ' OR ';
		    
		    //case 8
		    //south degree in range and n & s are above and below respectively
		    geoQueryString += '(south_degrees:[' + boundingboxWD + ' TO ' + boundingboxED + '] AND ' +
		                 'west_degrees:[ * TO ' + boundingboxWD + '] AND ' +
		                 'east_degrees:[' + boundingboxED + ' TO ' + '* ])';

		    geoQueryString += ' OR ';
		    
		    //case 9
		    //data box > user defined bounding box              
		    geoQueryString += '(east_degrees:[' + boundingboxED + ' TO ' + ' *] AND ' +
		                 'west_degrees:[ * TO ' + boundingboxWD + '] AND ' +
		                 'south_degrees:[ * TO ' + boundingboxSD + '] AND ' +
		                 'north_degrees:[' + boundingboxND + ' TO ' + '* ])';
				
			
		} else {
			var sw = bounds.getSouthWest();
			var ne = bounds.getNorthEast();
			//this is the min long 
			//west_degrees limit
			boundingboxWD = sw.lng();
			//this is the min lat
			//south_degrees limit
			boundingboxED = sw.lat();
			//this is the max long
			//east_degrees limit
			boundingboxSD = ne.lng();
			//this is the max lat
			//north_degrees limit
			boundingboxND = ne.lat();
			
			
			//case 1
		    //NE point in bounding box
		    geoQueryString += '(east_degrees:[' + boundingboxWD + ' TO ' + boundingboxED + '] AND ' +
		                 'north_degrees:[' + boundingboxSD + ' TO ' + boundingboxND + '])';
		    
		    geoQueryString += ' OR ';
		    
		    //case 2
		    //SE point in bounding box
		    geoQueryString += '(east_degrees:[' + boundingboxWD + ' TO ' + boundingboxED + '] AND ' +
		                 'south_degrees:[' + boundingboxSD + ' TO ' + boundingboxND + '])';
		    
		    geoQueryString += ' OR ';
		    
		    //case 3
		    //SW point in bounding box
		    geoQueryString += '(west_degrees:[' + boundingboxWD + ' TO ' + boundingboxED + '] AND ' +
		                 'south_degrees:[' + boundingboxSD + ' TO ' + boundingboxND + '])';
		    
		    
		    geoQueryString += ' OR ';
		    
		    //case 4
		    //NW point in bounding box
		    geoQueryString += '(west_degrees:[' + boundingboxWD + ' TO ' + boundingboxED + '] AND ' +
		                 'north_degrees:[' + boundingboxSD + ' TO ' + boundingboxND + '])';

		    geoQueryString += ' OR ';
		    
		    //case 5
		    //east degree in range and n & s are above and below respectively
		    geoQueryString += '(east_degrees:[' + boundingboxWD + ' TO ' + boundingboxED + '] AND ' +
		                 'south_degrees:[ * TO ' + boundingboxSD + '] AND ' +
		                 'north_degrees:[' + boundingboxND + ' TO ' + '* ])';

		    geoQueryString += ' OR ';
		    
		    //case 6
		    //west degree in range and n & s are above and below respectively
		    geoQueryString += '(west_degrees:[' + boundingboxWD + ' TO ' + boundingboxED + '] AND ' +
		                 'south_degrees:[ * TO ' + boundingboxSD + '] AND ' +
		                 'north_degrees:[' + boundingboxND + ' TO ' + '* ])';

		    geoQueryString += ' OR ';
		    
		    //case 7
		    //north degree in range and n & s are above and below respectively
		    geoQueryString += '(north_degrees:[' + boundingboxSD + ' TO ' + boundingboxND + '] AND ' +
		                 'west_degrees:[ * TO ' + boundingboxWD + '] AND ' +
		                 'east_degrees:[' + boundingboxED + ' TO ' + '* ])';

		    geoQueryString += ' OR ';
		    
		    //case 8
		    //south degree in range and n & s are above and below respectively
		    geoQueryString += '(south_degrees:[' + boundingboxWD + ' TO ' + boundingboxED + '] AND ' +
		                 'west_degrees:[ * TO ' + boundingboxWD + '] AND ' +
		                 'east_degrees:[' + boundingboxED + ' TO ' + '* ])';

		    geoQueryString += ' OR ';
		    
		    //case 9
		    //data box > user defined bounding box              
		    geoQueryString += '(east_degrees:[' + boundingboxED + ' TO ' + ' *] AND ' +
		                 'west_degrees:[ * TO ' + boundingboxWD + '] AND ' +
		                 'south_degrees:[ * TO ' + boundingboxSD + '] AND ' +
		                 'north_degrees:[' + boundingboxND + ' TO ' + '* ])';
				
				
			
			
		}

		//console.log(geoQueryString);
		Manager.store.addByValue('fq',geoQueryString);
		
		
	}

	
	
	
	
});





