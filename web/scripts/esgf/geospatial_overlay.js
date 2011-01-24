var map, geocoder, infowindow;
	var num_of_markers = 0;
	var max_of_markers = 3;
	var bounds;
	var cbounds = new google.maps.LatLngBounds();
	var markerGroup = new Array(max_of_markers);
	var poly;
	var boundingboxWD, boundingboxED, boundingboxSD, boundingboxND;
var	popupStatusGeospatialSearch =0;
var geospatialString = 
'	<div id="popupGeospatialSearch" >'+
'<a id="popupGeospatialSearchClose">x</a>'+
'<h1>Geospatial Search</h1>'+
'<div id="geo_helper">'+
'<p>'+
'First define the points of interest by putting markers on'+
'the map; if you select square option, then we will try'+
'to fit a square with all markers in it; if you select circle option, then '+
'you will be asked for a radius and quality.'+
'</p>'+
'</div>'+
'<div id="map_canvas1"> </div>'+
'<div class="map_info1"> '+
'	<fieldset id="search_type_area">'+
'		<p class=legend> Search Type: </p>'+
'		<input type="radio" name="searchType" value="Encloses" checked /> Encloses '+
'		<input type="radio" name="searchType" value="Overlaps" /> Overlaps '+

'	</fieldset>'+

'	<fieldset id="marker_fieldset1">'+

'		<p class=legend> Enter address: </p>'+
'		<div id="geoloc1"> '+   
'			<input type="text" name="location" size="20" /> <br />'+
'		</div>'+

'		<input type="button" name="clear_markers" value="Clear Markers" />'+

'		<div id="markers1" style="display:none"></div>'+

'	</fieldset>'+
	
'	<fieldset id="area">'+
'		<p class=legend> Define Area: </p>'+

'		<input type="radio" name="areaGroup" value="square" /> Square '+
'		<input type="radio" name="areaGroup" value="circle" /> Circle '+

'		<div id="circleInputs1" style="display:none">'+
'    		<label> Radius (km):</label><input type="text" name="radius" size="3" value="5" />'+
'    		<label> Quality:</label><input type="text" size="3" name="quality" value="40" />'+
' 			<br />  '+
'				<input type="button" name="redraw_circle" value="Redraw" />'+

'		</div>'+

'		<div id="areaSelected11" style="display:none"></div>'+

'	</fieldset>'+
	
'</div>'+

'<div id="gButton"><input id="geo-search-button" type="submit" value="Submit Geospatial Constraints" /></div>' + 

'</div>  '+ 

'<div id="backgroundPopupGeospatialSearch"></div>';


var geoQueryString = '';




function geospatial_link()
{
	if(popupStatusGeospatialSearch==0){
		$("#backgroundPopupGeospatialSearch").css({
			"opacity": "0.8"
		});
		$("#backgroundPopupGeospatialSearch").fadeIn("slow");
		$("#popupGeospatialSearch").fadeIn("slow");
		popupStatusGeospatialSearch = 1;
	}
	

	
	
	display_geo_map();
}





function display_geo_map () {

	var mapDiv = document.getElementById('map_canvas1');
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



/* from map.js */


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


function clearAreaChoice() {
	
	$("input:radio").attr('checked', false);

}



function placeMarker(location) {
	alert('in place marker');
	var marker = addMarker(location);
	if (marker != null ) {
		appendMarker(marker,  num_of_markers);
	}
}


function appendMarker(marker, i) {
	var content = '[' + i + '] ' + 'lat ' + 
			marker.getPosition().lat().toFixed(2) + ' , ' +
			'lon ' + 
			marker.getPosition().lng().toFixed(2) + "<br />";
	
	$("#markers1").append(content);
	
	if ($("#markers1").is(":hidden")) 
		$("#markers1").slideToggle('fast');
	
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
	alert('adding marker? num markers: ' + num_of_markers);
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





//CONTROLLING EVENTS IN jQuery
$(document).ready(function(){
	
	$('div#geospatial_overlay').after(geospatialString);
	
	
	 /*$('div#geo').click(function(){
			if(popupStatusGeospatialSearch==0){
				$("#backgroundPopupGeospatialSearch").css({
					"opacity": "0.8"
				});
				$("#backgroundPopupGeospatialSearch").fadeIn("slow");
				$("#popupGeospatialSearch").fadeIn("slow");
				popupStatusGeospatialSearch = 1;
			}
			

			display_geo_map();
		});*/
		 
	
	$('#marker_fieldset1 input[name="clear_markers"]').click(function(e) {
		
		clearMarkers();
		clearAreaChoice();
	});
	
	function clearMarkers() {
		for (var i=0; i < max_of_markers; i++) {
			alert('clearing marker: ' + i);
			if (markerGroup[i]) 
			{
				markerGroup[i].setMap(null);
				alert('confirm: ' + i);
				markerGroup[i] = null;
			}
		}
		
		num_of_markers = 0;
		
		// close info window
		if (infowindow){
			infowindow.close();
		}
		if (poly)
			poly.setMap(null);
		
		// clear marker area content
		$("#markers1").html("");
		$("#areaSelected11").html("");
		
		clearAreaChoice();
	}
	
	$("#geoloc1 input[name='location']").keypress(function(e) {
		if (e.keyCode == 13) {
			var address = $(this).val();
			getCoordinates(address);
			// clear the field
			$(this).val('');
			return false;
		}
	});
	
	
	
	//loading geospatial search popup!
	function loadPopupGeospatialSearch(){
		//loads popup only if it is disabled
		if(popupStatusGeospatialSearch==0){
			$("#backgroundPopupGeospatialSearch").css({
				"opacity": "0.8"
			});
			$("#backgroundPopupGeospatialSearch").fadeIn("slow");
			$("#popupGeospatialSearch").fadeIn("slow");
			popupStatusGeospatialSearch = 1;
		}
	}


	//disabling geospatial search popup!
	function disablePopupGeospatialSearch(){
		//disables popup only if it is enabled
		if(popupStatusGeospatialSearch==1){
			$("#backgroundPopupGeospatialSearch").fadeOut("slow");
			$("#popupGeospatialSearch").fadeOut("slow");
			popupStatusGeospatialSearch = 0;
		}
	}

	//centering popup
	function centerPopupGeospatialSearch(){
		//request data for centering
		var windowWidth = document.documentElement.clientWidth;
		var windowHeight = document.documentElement.clientHeight;
		var popupHeight = $("#popupGeospatialSearch").height();
		var popupWidth = $("#popupGeospatialSearch").width();
		//centering
		$("#popupGeospatialSearch").css({
			"position": "absolute",
			"top": windowHeight/2-popupHeight/2,
			"left": windowWidth/2-popupWidth/2
		});
		//only need force for IE6
		
		$("#backgroundPopupGeospatialSearch").css({
			"height": windowHeight
		});
		
	}

	
	
	//Click the x event for geospatial!
	$("#popupGeospatialSearchClose").click(function(){
		disablePopupGeospatialSearch();
	});
	
	//Click out event!
	$("#backgroundPopupGeospatialSearch").click(function(){
		disablePopupGeospatialSearch();
	});
	
	
	
	
	
	//this event will be taken out soon 
	//right now it acts as a guard so that the user cannot perform and overlaps query with a radius search (unimplemented)
	$("input[name='searchType']").change(function(e) {
		var searchType = $("input[name='searchType']").val();
		//alert("changing search type to " + searchType);
		
	});

	$("input[name='areaGroup']").change(function(e) {
		if ($("input[name='areaGroup']:checked").val() == 'square') {
			
			if (num_of_markers < 2) {
				jMsg("warn", "Please define at least two markers!", 5000);
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

			
			if ($("#circleInputs1").is(":hidden"))
				$("#circleInputs1").slideToggle('fast');
				
			// put out something
			$("#areaSelected11").html('<p class="legend"> Center of Interest </p> + ' 
					+ markerGroup[0].getPosition().toString());
			
			
			if ($("#areaSelected1").is(":hidden"))
				$("#areaSelected1").slideToggle('fast');
			

			redraw_circle();
			
		}		
	});

	$('input[name="redraw_circle"]').click(function(e) {
		
		redraw_circle();
		
	});
	
	$('div#gButton').click(function() {
		//alert('execute Geospatial Query');
		executeGeospatialQuery();
		
		Manager.doRequest(0);
		//alert('submitted: ' + geoQueryString);
		
	});

	
});





/**
 * Output each marker's geolocation information
 * 
 */

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
	
	//geosearch();
}


/*
 * This function essentially does the same as addGeograpichConstraint, only
 * now the cbounds are used.
 */
function setGeographicRadiusConstraint() {
	
	alert('setting radius constraint?');
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
/*
function swapGeoSearchType(type)
{
	var searchForm = document.getElementById("search-form");
	var whichGeo = searchForm["whichGeo"];
	whichGeo.value = type;
}
*/

function executeGeospatialQuery()
{
	
	var geoSearchType = $("input[name='searchType']:checked").val();
	//alert('executing ' + geoSearchType);
	if(geoSearchType == 'Encloses')
	{
		encloses();
	}
	else
	{
		overlaps();
	}
	
	
}

function encloses()
{
	//reset the geoQueryString
	geoQueryString = '';
	
	var west_degrees, east_degrees, north_degrees, south_degrees,
	west_degreesFQ, east_degreesFQ, north_degreesFQ, south_degreesFQ; 

	var sw = bounds.getSouthWest();
	var ne = bounds.getNorthEast();
	
	//alert('encloses sq: ' + ($("input[name='areaGroup']:checked").val() == 'square'));
	
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
		
		
		//west_degrees
		if(boundingboxWD)
		{
			west_degrees = boundingboxWD;
		}
		else
		{
			west_degrees = '*';
		}

		//west_degrees
		if(boundingboxED)
		{
			east_degrees = boundingboxED;
		}
		else
		{
			east_degrees = '*';
		}

		//south_degrees
		if(boundingboxSD)
		{
			south_degrees = boundingboxSD;
		}
		else
		{
			south_degrees = '*';
		}

		//north_degrees
		if(boundingboxND)
		{
			north_degrees = boundingboxND;
		}
		else
		{
			north_degrees = '*';
		}
		
		
		south_degreesFQ = 'south_degrees:[' + south_degrees + ' TO *]';
		west_degreesFQ = 'west_degrees:[' + west_degrees + ' TO *]';
		north_degreesFQ = 'north_degrees:[ * TO ' + north_degrees + ']';
		east_degreesFQ = 'east_degrees:[ * TO ' + east_degrees + ']';
		
		geoQueryString += '(' + south_degreesFQ + ' AND ' + west_degreesFQ + ' AND ' + north_degreesFQ + ' AND ' + east_degreesFQ + ')';

	
	
	Manager.store.addByValue('fq',geoQueryString);
	
	/*Manager.store.addByValue('fq', south_degreesFQ );	
	Manager.store.addByValue('fq', west_degreesFQ );	
	Manager.store.addByValue('fq', north_degreesFQ );	
	Manager.store.addByValue('fq', east_degreesFQ );	*/
	
	//Manager.doRequest(0);
}

function overlaps()
{

	geoQueryString = '';
	
	//alert('overlaps sq: ' + ($("input[name='areaGroup']:checked").val() == 'square'));
	
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
		
		
	
    Manager.store.addByValue('fq', geoQueryString );	
	

	//Manager.doRequest(0);
	

         
}

/*function loadScript() {
	alert('locading script');
    var script = document.createElement("script");
    script.type = "text/javascript";
    script.src = "http://maps.google.com/maps/api/js?sensor=false&callback=initialize";
    document.body.appendChild(script);
  }*/
