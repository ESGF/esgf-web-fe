var map, geocoder, infowindow;
	var num_of_markers = 0;
	var max_of_markers = 3;
	var bounds;
	var cbounds = new google.maps.LatLngBounds();
	var markerGroup = new Array(max_of_markers);
	var poly;
var	popupStatusGeospatialSearch =0;

var geospatialString = 
'	<div id="popupGeospatialSearch" >'+
'<a id="popupGeospatialSearchClose">x</a>'+
'<h1>Geospatial Search</h1>'+
'<!--<div id="metadata_return">Print</div>'+
'-->'+
'<div id="geo_helper">'+
'<p>'+
'Help: first define the points of interest by putting markers on'+
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

'		<div id="circleInputs" style="display:none">'+
'    		<label> Radius (km):</label><input type="text" name="radius" size="3" value="5" />'+
'    		<label> Quality:</label><input type="text" size="3" name="quality" value="40" />'+
' 			<br />  '+
'				<input type="button" name="redraw_circle" value="Redraw" />'+

'		</div>'+

'		<div id="areaSelected1" style="display:none"></div>'+

'	</fieldset>'+
	
'</div>'+


'</div>  '+ 


'<div id="backgroundPopupGeospatialSearch"></div>';






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
	
	
	
	$('div#geo').click(function(){
		if(popupStatusGeospatialSearch==0){
			$("#backgroundPopupGeospatialSearch").css({
				"opacity": "0.8"
			});
			$("#backgroundPopupGeospatialSearch").fadeIn("slow");
			$("#popupGeospatialSearch").fadeIn("slow");
			popupStatusGeospatialSearch = 1;
		}
		

		display_geo_map();
	});
	
	
	$('#marker_fieldset1 input[name="clear_markers"]').click(function(e) {
		
		clearMarkers();
		clearAreaChoice();
	});
	
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
	
	

	
});





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
	setGeographicRadiusConstraint();
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
	
	executeGeospatialQuery();
	
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

/*function swapGeoSearchType(type)
{
	var searchForm = document.getElementById("search-form");
	var whichGeo = searchForm["whichGeo"];
	whichGeo.value = type;
}*/


function executeGeospatialQuery()
{
	
	var geoSearchType = $("input[name='searchType']:checked").val();
	
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
	var west_degrees, east_degrees, north_degrees, south_degrees,
	west_degreesFQ, east_degreesFQ, north_degreesFQ, south_degreesFQ; 

	var sw = bounds.getSouthWest();
	var ne = bounds.getNorthEast();
	
	
	//this is the min long 
	//west_degrees limit
	var swLng = sw.lng();
	
	//this is the min lat
	//south_degrees limit
	var swLat = sw.lat();
	
	//this is the max long
	//east_degrees limit
	var neLng = ne.lng();
	
	//this is the max lat
	//north_degrees limit
	var neLat = ne.lat();
	
	
	//west_degrees
	if(swLng)
	{
		west_degrees = swLng;
	}
	else
	{
		west_degrees = '*';
	}

	//west_degrees
	if(neLng)
	{
		east_degrees = neLng;
	}
	else
	{
		east_degrees = '*';
	}

	//south_degrees
	if(swLat)
	{
		south_degrees = swLat;
	}
	else
	{
		south_degrees = '*';
	}

	//north_degrees
	if(neLat)
	{
		north_degrees = neLat;
	}
	else
	{
		north_degrees = '*';
	}
	
	
	south_degreesFQ = 'south_degrees:[' + south_degrees + ' TO *]';
	west_degreesFQ = 'west_degrees:[' + west_degrees + ' TO *]';
	north_degreesFQ = 'north_degrees:[ * TO ' + north_degrees + ']';
	east_degreesFQ = 'east_degrees:[ * TO ' + east_degrees + ']';
	
	Manager.store.addByValue('fq', south_degreesFQ );	
	Manager.store.addByValue('fq', west_degreesFQ );	
	Manager.store.addByValue('fq', north_degreesFQ );	
	Manager.store.addByValue('fq', east_degreesFQ );	
	
	Manager.doRequest(0);
}

function overlaps()
{
	var geoString = '';
	
	var sw = bounds.getSouthWest();
	var ne = bounds.getNorthEast();
	//this is the min long 
	//west_degrees limit
	var boundingboxWD = sw.lng();
	//this is the min lat
	//south_degrees limit
	var boundingboxED = sw.lat();
	//this is the max long
	//east_degrees limit
	var boundingboxSD = ne.lng();
	//this is the max lat
	//north_degrees limit
	var boundingboxND = ne.lat();
	
	//case 1
    //NE point in bounding box
    geoString += '(east_degrees:[' + boundingboxWD + ' TO ' + boundingboxED + '] AND ' +
                 'north_degrees:[' + boundingboxSD + ' TO ' + boundingboxND + '])';
    
    geoString += ' OR ';
    
    //case 2
    //SE point in bounding box
    geoString += '(east_degrees:[' + boundingboxWD + ' TO ' + boundingboxED + '] AND ' +
                 'south_degrees:[' + boundingboxSD + ' TO ' + boundingboxND + '])';
    
    geoString += ' OR ';
    
    //case 3
    //SW point in bounding box
    geoString += '(west_degrees:[' + boundingboxWD + ' TO ' + boundingboxED + '] AND ' +
                 'south_degrees:[' + boundingboxSD + ' TO ' + boundingboxND + '])';
    
    
    geoString += ' OR ';
    
    //case 4
    //NW point in bounding box
    geoString += '(west_degrees:[' + boundingboxWD + ' TO ' + boundingboxED + '] AND ' +
                 'north_degrees:[' + boundingboxSD + ' TO ' + boundingboxND + '])';

    geoString += ' OR ';
    
    //case 5
    //east degree in range and n & s are above and below respectively
    geoString += '(east_degrees:[' + boundingboxWD + ' TO ' + boundingboxED + '] AND ' +
                 'south_degrees:[ * TO ' + boundingboxSD + '] AND ' +
                 'north_degrees:[' + boundingboxND + ' TO ' + '* ])';

    geoString += ' OR ';
    
    //case 6
    //west degree in range and n & s are above and below respectively
    geoString += '(west_degrees:[' + boundingboxWD + ' TO ' + boundingboxED + '] AND ' +
                 'south_degrees:[ * TO ' + boundingboxSD + '] AND ' +
                 'north_degrees:[' + boundingboxND + ' TO ' + '* ])';

    geoString += ' OR ';
    
    //case 7
    //north degree in range and n & s are above and below respectively
    geoString += '(north_degrees:[' + boundingboxSD + ' TO ' + boundingboxND + '] AND ' +
                 'west_degrees:[ * TO ' + boundingboxWD + '] AND ' +
                 'east_degrees:[' + boundingboxED + ' TO ' + '* ])';

    geoString += ' OR ';
    
    //case 8
    //south degree in range and n & s are above and below respectively
    geoString += '(south_degrees:[' + boundingboxWD + ' TO ' + boundingboxED + '] AND ' +
                 'west_degrees:[ * TO ' + boundingboxWD + '] AND ' +
                 'east_degrees:[' + boundingboxED + ' TO ' + '* ])';

    geoString += ' OR ';
    
    //case 9
    //data box > user defined bounding box              
    geoString += '(east_degrees:[' + boundingboxED + ' TO ' + ' *] AND ' +
                 'west_degrees:[ * TO ' + boundingboxWD + '] AND ' +
                 'south_degrees:[ * TO ' + boundingboxSD + '] AND ' +
                 'north_degrees:[' + boundingboxND + ' TO ' + '* ])';
	
	
    Manager.store.addByValue('fq', geoString );	
	

	Manager.doRequest(0);
	

         
}

















/*
if(request.getParameterValues("west_degrees")!=null &&
        request.getParameterValues("east_degrees")!=null &&
        request.getParameterValues("north_degrees")!=null &&
        request.getParameterValues("south_degrees")!=null)
     {
         String boundingboxWD = "";
         String boundingboxED = "";
         String boundingboxSD = "";
         String boundingboxND = "";
         
         boundingboxWD = request.getParameterValues("west_degrees")[0];
         boundingboxED = request.getParameterValues("east_degrees")[0];
         boundingboxSD = request.getParameterValues("south_degrees")[0];
         boundingboxND = request.getParameterValues("north_degrees")[0];
         
         
         String geoString = "";
         
         //case 1
         //NE point in bounding box
         geoString += "(east_degrees:[" + boundingboxWD + " TO " + boundingboxED + "] AND " +
                      "north_degrees:[" + boundingboxSD + " TO " + boundingboxND + "])";
         
         geoString += " OR ";
         
         //case 2
         //SE point in bounding box
         geoString += "(east_degrees:[" + boundingboxWD + " TO " + boundingboxED + "] AND " +
                      "south_degrees:[" + boundingboxSD + " TO " + boundingboxND + "])";
         
         geoString += " OR ";
         
         //case 3
         //SW point in bounding box
         geoString += "(west_degrees:[" + boundingboxWD + " TO " + boundingboxED + "] AND " +
                      "south_degrees:[" + boundingboxSD + " TO " + boundingboxND + "])";
         
         
         geoString += " OR ";
         
         //case 4
         //NW point in bounding box
         geoString += "(west_degrees:[" + boundingboxWD + " TO " + boundingboxED + "] AND " +
                      "north_degrees:[" + boundingboxSD + " TO " + boundingboxND + "])";

         geoString += " OR ";
         
         //case 5
         //east degree in range and n & s are above and below respectively
         geoString += "(east_degrees:[" + boundingboxWD + " TO " + boundingboxED + "] AND " +
                      "south_degrees:[ * TO " + boundingboxSD + "] AND " +
                      "north_degrees:[" + boundingboxND + " TO " + "* ])";

         geoString += " OR ";
         
         //case 6
         //west degree in range and n & s are above and below respectively
         geoString += "(west_degrees:[" + boundingboxWD + " TO " + boundingboxED + "] AND " +
                      "south_degrees:[ * TO " + boundingboxSD + "] AND " +
                      "north_degrees:[" + boundingboxND + " TO " + "* ])";

         geoString += " OR ";
         
         //case 7
         //north degree in range and n & s are above and below respectively
         geoString += "(north_degrees:[" + boundingboxSD + " TO " + boundingboxND + "] AND " +
                      "west_degrees:[ * TO " + boundingboxWD + "] AND " +
                      "east_degrees:[" + boundingboxED + " TO " + "* ])";

         geoString += " OR ";
         
         //case 8
         //south degree in range and n & s are above and below respectively
         geoString += "(south_degrees:[" + boundingboxWD + " TO " + boundingboxED + "] AND " +
                      "west_degrees:[ * TO " + boundingboxWD + "] AND " +
                      "east_degrees:[" + boundingboxED + " TO " + "* ])";

         geoString += " OR ";
         
         //case 9
         //data box > user defined bounding box              
         geoString += "(east_degrees:[" + boundingboxED + " TO " + " *] AND " +
                      "west_degrees:[ * TO " + boundingboxWD + "] AND " +
                      "south_degrees:[ * TO " + boundingboxSD + "] AND " +
                      "north_degrees:[" + boundingboxND + " TO " + "* ])";

         LOG.debug("GeoStringOverlaps: " + geoString);
         
         //only input if a geo spatial search was created...
         if(!boundingboxND.equals("") && 
            !boundingboxSD.equals("") &&
            !boundingboxED.equals("") && 
            !boundingboxWD.equals(""))
             input.addGeospatialRangeConstraint(geoString);
     }
     
     
    */ 