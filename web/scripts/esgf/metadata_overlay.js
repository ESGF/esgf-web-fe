
//@website: www.yensdesign.com
//@email: yensamg@gmail.com
//@license: Feel free to use it, but keep this credits please!					
/***************************/

//SETTING UP OUR POPUP
//0 means disabled; 1 means enabled;



var popupStatusMetadataReport = 0;

var metadataString =
	'	<div id="popupMetadataReport" >'+
	'<a id="popupMetadataReportClose">x</a>'+
	'<h1>Metadata Summary</h1>'+
	'<p>'+
	
	'<div id="metadata_summary_dataset"></div>'+// found with constraints: '+
	
	'</p>'+
	 
	'<!-- root element for scrollable -->'+ 
	'<div class="scrollable verticalscroll">'+ 
	'	<!-- root element for the items --> '+
	'	<div class="itemsscroll">'+
	
/*	'		<div>'+
	'			<div class="shortitem">'+
	'				<div class="leftsd">'+
	'					<h3>Title:</h3> '+
	'				</div>'+
	'				<div class="rightsd">'+
	'					<div id="title_metadata"></div>'+
	'				</div>'+
	'			</div> '+
	'		</div>'+*/
	

	'		<div>'+
	'			<div class="shortitem">'+
	'				<div class="leftsd">'+
	'					Project(s): '+
	'				</div>'+
	'				<div class="rightsd">'+
	'					<div id="projects_metadata"></div>'+
	'				</div>'+
	'			</div> '+
	'		</div>'+

	'		<div>'+
	'			<div class="shortitem">'+
	'				<div class="leftsd">'+
	'					Investigator(s): '+
	'				</div>'+
	'				<div class="rightsd">'+
	'					<div id="investigator_metadata"></div>'+
	'				</div>'+
	'			</div> '+
	'		</div>'+

	'		<div>'+
	'			<div class="mediumitem">'+
	'				<div class="leftsd">'+
	'					Contact Info: '+
	'				</div>'+
	'				<div class="rightsd">'+
	'					<div id="contact_metadata"></div>'+
	'				</div>'+
	'			</div> '+
	'		</div>'+
	
	'		<div>'+
	'			<div class="shortitem">'+
	'				<div class="leftsd">'+
	'					Content Time Range: '+
	'				</div>'+
	'				<div class="rightsd">'+
	'					<div id="time_metadata"></div>'+
	'				</div>'+
	'			</div> '+
	'		</div>'+
	
	'		<div>'+
	'			<div class="geospatial_item"> '+
	'				<div class="leftsd">'+
	'					Geospatial Information: '+
	'				</div>'+
					
					
	'				<div class="rightsd">'+
	'					<div id="geo_info">'+
	'						<div id="geospatial_metadata"></div>'+
	'					</div>'+
						
	'					<div id="geo_map">map here</div>'+
	'				</div>'+
					
	'			</div> '+
	'		</div>'+

	'		<div>'+		
	'			<div class="shortitem"> '+
	'				<div class="leftsd">'+
	'					Keyword(s): '+
	'				</div>'+
					
	'				<div class="rightsd">'+
	'					<div id="keywords_metadata"></div>'+
			
	'				</div>'+
					
	'			</div> '+
	'		</div>'+'		<div>'+		
	'			<div class="shortitem"> '+
	'				<div class="leftsd">'+
	'					Description: '+
	'				</div>'+
					
	'				<div class="rightsd">'+
	'					<div id="abstract_metadata"></div>'+
			
	'				</div>'+
					
	'			</div> '+
	'		</div>'+
	
	
	/*
	
	*/
	
/*
	'		<div>'+
	'			<div class="shortitem">'+
	'				<div class="leftsd">'+
	'					<h3>Status</h3> '+
	'				</div>'+
	'				<div class="rightsd">'+
	'					<div id="status_metadata"></div>'+
	'				</div>'+
	'			</div> '+
	'		</div>'+

	'		<div>'+
	'			<div class="shortitem">'+
	'				<div class="leftsd">'+
	'					<h3>Access Restrictions</h3> '+
	'				</div>'+
	'				<div class="rightsd">'+
	'					<div id="access_metadata"></div>'+
	'				</div>'+
	'			</div> '+
	'		</div>'+


	'		<div>'+
	'			<div class="shortitem">'+
	'				<div class="leftsd">'+
	'					<h3>Data Set Location:</h3> '+
	'				</div>'+
	'				<div class="rightsd">'+
	'					<div id="datasetLocation_metadata"></div>'+
	'				</div>'+
	'			</div> '+
	'		</div>'+

	'		<div>'+
	'			<div class="shortitem">'+
	'				<div class="leftsd">'+
	'					<h3>Data Center Contact:</h3> '+
	'				</div>'+
	'				<div class="rightsd">'+
	'					<div id="datacenterContact_metadata"></div>'+
	'				</div>'+
	'			</div> '+
	'		</div>'+

	'		<div>'+
	'			<div class="shortitem">'+
	'				<div class="leftsd">'+
	'					<h3>Data Center URL:</h3> '+
	'				</div>'+
	'				<div class="rightsd">'+
	'					<div id="datacenterURL_metadata"></div>'+
	'				</div>'+
	'			</div> '+
	'		</div>'+

	'		<div>'+
	'			<div class="shortitem">'+
	'				<div class="leftsd">'+
	'					<h3>Data Set Citation:</h3> '+
	'				</div>'+
	'				<div class="rightsd">'+
	'					<div id="datacenterCitation_metadata"></div>'+
	'				</div>'+
	'			</div> '+
	'		</div>'+


	'		<div>'+
	'			<div class="shortitem">'+
	'				<div class="leftsd">'+
	'					<h3>Variable(s):</h3> '+
	'				</div>'+
	'				<div class="rightsd">'+
	'					<div id="variables_metadata"></div>'+
	'				</div>'+
	'			</div> '+
	'		</div>'+

	'		<div>'+
	'			<div class="shortitem">'+
	'				<div class="leftsd">'+
	'					<h3>Keyword(s):</h3> '+
	'				</div>'+
	'				<div class="rightsd">'+
	'					<div id="datacenterContact_metadata"></div>'+
	'				</div>'+
	'			</div> '+
	'		</div>'+

	'		<div>'+
	'			<div class="shortitem">'+
	'				<div class="leftsd">'+
	'					<h3>Data Center Contact:</h3> '+
	'				</div>'+
	'				<div class="rightsd">'+
	'					<div id="datacenterContact_metadata"></div>'+
	'				</div>'+
	'			</div> '+
	'		</div>'+
			
			
	'		<div>'+
			
	'			<div class="geospatial_item"> '+
	'				<div class="leftsd">'+
	'					<h3>Temporal Information:</h3> '+
	'				</div>'+
					
	'				<div class="rightsd">'+
	'					<div id="temporal_metadata"></div>'+
			
	'				</div>'+
					
	'			</div> '+
	'		</div>'+
			
	'		<div>'+
			
*/
	
	
	'   </div>'+
	'</div>' +
	

	'</div>  <!-- end popupMetadataReport -->'+ 
	'<div id="backgroundPopupMetadataReport"></div>';



//CONTROLLING EVENTS IN jQuery
$(document).ready(function(){
	
	//LOADING POPUP
	//Click the button event!
	$("#button1").click(function(){
		alert('button1');
		//centering with css
		centerPopupMetadataReportup();
		//load popup
		loadPopupMetadataReportPopup();
	});
				
	$('div#metadata_overlay').after(metadataString);
	
	
	

	//loading metadata report popup!
	function loadPopupMetadataReport(){
		//loads popup only if it is disabled
		if(popupStatusMetadataReport==0){
			$("#backgroundPopupMetadataReport").css({
				"opacity": "0.8"
			});
			$("#backgroundPopupMetadataReport").fadeIn("slow");
			$("#popupMetadataReport").fadeIn("slow");
			popupStatusMetadataReport = 1;
		}
	}




	//disabling metadata report popup!
	function disablePopupMetadataReport(){
		//disables popup only if it is enabled
		if(popupStatusMetadataReport==1){
			$("#backgroundPopupMetadataReport").fadeOut("slow");
			$("#popupMetadataReport").fadeOut("slow");
			popupStatusMetadataReport = 0;
		}
	}






	//centering popup
	function centerPopupMetadataReport(){
		//request data for centering
		var windowWidth = document.documentElement.clientWidth;
		var windowHeight = document.documentElement.clientHeight;
		var popupHeight = $("#popupMetadataReport").height();
		var popupWidth = $("#popupMetadataReport").width();
		//centering
		$("#popupMetadataReport").css({
			"position": "absolute",
			"top": windowHeight/2-popupHeight/2,
			"left": windowWidth/2-popupWidth/2
		});
		//only need force for IE6
		
		$("#backgroundPopupMetadataReport").css({
			"height": windowHeight
		});
		
	}

	
	
	
	
	//CLOSING POPUP
	//Click the x event for metadata report!
	$("#popupMetadataReportClose").click(function(){
		disablePopupMetadataReport();
	});
	
	
	
	//Click out event!
	$("#backgroundPopupMetadataReport").click(function(){
		disablePopupMetadataReport();
	});
	
	
	
	
	//Press Escape event!
	$(document).keypress(function(e){
		if(e.keyCode==27 && popupStatus==1){
			disablePopup();
		}
	});

	
	
	
	$(function() {		
		
		// initialize scrollable with mousewheel support
		$(".scrollable").scrollable({ vertical: true, mousewheel: true });	
		
	});
	
});



function metadata_report(id,title,metadatafilename,metadatafileformat)
{
	if(popupStatusMetadataReport==0){
		$("#backgroundPopupMetadataReport").css({
			"opacity": "0.8"
		});
		$("#backgroundPopupMetadataReport").fadeIn("slow");
		$("#popupMetadataReport").fadeIn("slow");
		popupStatusMetadataReport = 1;
		
	}

	//alert('starting ajax call to MetadataExtractorController');
	
	jQuery.ajax({
		url: 'http://localhost:8080/esgf-web-fe/metadataproxy',
		data: 'metadataformat=' + metadatafileformat + '&metadatafile=' + metadatafilename + '&id=' + id,
		type: 'GET',
		success: function(record) {processMetadataRecord(record,id,metadatafileformat);},
		error: function() {alert("error http://localhost:8080/esgf-web-fe/metadataproxy");},
		dataType: 'json'
	});
	
}

function processMetadataRecord(record,id,metadatafileformat)
{
	
	
	
	//remove any previous metadata report from the page - note that this class is used as a division in each of the metadata
	$('.addedMetadata').remove();
	$('.addedMetadataTitle').remove();
	var title = record.record.metadata.DIF.Entry_Title;
	
	//add title and constraints to the page
	$('div#metadata_summary_dataset').after('<div class="addedMetadataTitle">' + 'Dataset: ' + title); //+ '</div><p>&lt;insert constraints here&gt;</p>');
	
	
	//branch logic depending on the metadata file format
	if(metadatafileformat == 'oai') {
		processOAI(record,id);
	}
	else if(metadatafileformat == 'fgdc') {
		processFGDC(record,id);
	}
	else if(metadatafileformat == 'cas') {
		processCAS(record,id);
	}
	else{ //thredds
		processTHREDDS(record,id);
	}
	
}

function processOAI(record,id)
{

	// Projects 
	//note i need to change the "0" index and place the correct information 
	//projects_metadata -> (record)/record[0]/metadata/DIF/Project[0]
	//var projects = record.record[0].metadata.DIF.Project;
	var projects = record.record.metadata.DIF.Project;
	var projectsText = '';
	for(var i = 0;i<projects.length;i++) {
		if(i == projects.length-1) {
			projectsText += projects[i].Long_Name + ' (' + projects[i].Short_Name + ')';
		}
		else {
			projectsText += projects[i].Long_Name + ' (' + projects[i].Short_Name + '), ';
		}
	}
	$('div#projects_metadata').after('<div class="addedMetadata">' + projectsText + '</div>');
	
	// Investigators 
	var invesigatorText = record.record.metadata.DIF.Personnel[0].Last_Name;
	$('div#investigator_metadata').after('<div class="addedMetadata"><p>' + invesigatorText + '</p></div>');
	
	// Contact Info 
	var contactText = record.record.metadata.DIF.Personnel[0].Contact_Address;
	$('div#contact_metadata').after('<div class="addedMetadata"><p>' + contactText + '</p></div>');
	
	// Temporal Info 
	var startText = record.record.metadata.DIF.Temporal_Coverage.Start_Date;
	var stopText = record.record.metadata.DIF.Temporal_Coverage.Stop_Date
	$('div#time_metadata').after('<div class="addedMetadata"><p>Begin: ' + startText + ' End: ' + stopText + '</p></div>');
	
	
	// Geo Info 
	var east_degreesText = record.record.metadata.DIF.Spatial_Coverage.Easternmost_Longitude;
	var west_degreesText = record.record.metadata.DIF.Spatial_Coverage.Westernmost_Longitude;
	var north_degreesText = record.record.metadata.DIF.Spatial_Coverage.Northernmost_Latitude;
	var south_degreesText = record.record.metadata.DIF.Spatial_Coverage.Southernmost_Latitude;
	$('div#geospatial_metadata').after('<div class="addedMetadata"><p>' + 'coordinates (N,W,S,E):<br />(' + north_degreesText + ',' + west_degreesText + ',' + south_degreesText + ',' + east_degreesText + ')</p></div>');
	display_meta_map(west_degreesText,east_degreesText,north_degreesText,south_degreesText);
	
	
	// Keywords 
	var keywords = record.record.metadata.DIF.Keyword;
	var keywordsText = '';
	for(var i = 0;i<keywords.length;i++) {
		if(i == keywords.length-1) {
			keywordsText += keywords[i] + ' (' + keywords[i] + ')';
		}
		else {
			keywordsText += keywords[i] + ' (' + keywords[i] + '), ';
		}
	}
	$('div#keywords_metadata').after('<div class="addedMetadata"><p>' + keywordsText + '</p></div>');
	
	
	// Abstract 
	var abstractText = record.record.metadata.DIF.Summary;
	$('div#abstract_metadata').after('<div class="addedMetadata"><p>' + abstractText + '</p></div>');
	
	
	

}

function processFGDC(record,id){
	
}

function processCAS(record,id){
	
}

function processTHREDDS(record,id) {
	
}

function display_meta_map (west_degrees,east_degrees,north_degrees,south_degrees) {
	
var bounds = new google.maps.LatLngBounds();

var paths = [
              new google.maps.LatLng(38.872886, -77.054720),
              new google.maps.LatLng(38.872602, -77.058046),
              new google.maps.LatLng(38.870080, -77.058604),
              new google.maps.LatLng(38.868894, -77.055664),
              new google.maps.LatLng(38.870598, -77.053346)
            ];

	var ne = new google.maps.LatLng(parseFloat(north_degrees),parseFloat(east_degrees));
	var nw = new google.maps.LatLng(parseFloat(north_degrees),parseFloat(west_degrees));
	var se = new google.maps.LatLng(parseFloat(south_degrees),parseFloat(east_degrees));
	var sw = new google.maps.LatLng(parseFloat(south_degrees),parseFloat(west_degrees));
	
	var map = new google.maps.Map(document.getElementById("geo_map"), {
	      zoom: 4,
	      center: new google.maps.LatLng(ne.lat(), ne.lng()),
	      //center: new google.maps.LatLng(30, -90),
	      mapTypeId: google.maps.MapTypeId.TERRAIN
	    });
	
	
	var points = [ne, se, sw, nw];
	 
	for(var i in points)
	{
		 var marker = new google.maps.Marker({
				draggable: false,
				position: points[i],
				map: map
			});
		 bounds.extend(points[i]);
		 
	}
	 
	var poly = new google.maps.Polygon({
		paths: points,
		//fillColor: "#0055ff",
		fillColor: "#FEE5AC",
		fillOpacity: 0.35,
		strokeWeight: 2
	});
	
	poly.setMap(map);
	 
	map.fitBounds(bounds);
	
};


/*
function metadata_link(id,title,url,description,west_degrees,east_degrees,north_degrees,south_degrees,datetime_start,datetime_stop)
{
	
	if(popupStatusMetadataReport==0){
		$("#backgroundPopupMetadataReport").css({
			"opacity": "0.8"
		});
		$("#backgroundPopupMetadataReport").fadeIn("slow");
		$("#popupMetadataReport").fadeIn("slow");
		popupStatusMetadataReport = 1;
	}
	
	var title = title;
	var description = description;
	var url = url;
	var west_degrees = west_degrees;
	var east_degrees = east_degrees;
	var north_degrees = north_degrees;
	var south_degrees = south_degrees;
	var datetime_start = datetime_start;
	var datetime_stop = datetime_stop;
	
	$('.addedMetadata').remove();
	$('div#title').after('<div class="addedMetadata"><p>' + title + '</p></div>');
	$('div#url').after('<div class="addedMetadata"><p>' + url + '</p></div>');
	$('div#abstract').after('<div class="addedMetadata"><p>' + description + '</p></div>');
	$('div#leader').after('<div class="addedMetadata"><p>' + 'John Doe' + '</p></div>');
	$('div#geospatial_metadata').after('<div class="addedMetadata"><p>west degrees: ' + west_degrees + '</p></div>');
	$('div#geospatial_metadata').after('<div class="addedMetadata"><p>east degrees: ' + east_degrees + '</p></div>');
	$('div#geospatial_metadata').after('<div class="addedMetadata"><p>north degrees: ' + north_degrees + '</p></div>');
	$('div#geospatial_metadata').after('<div class="addedMetadata"><p>south degrees: ' + south_degrees + '</p></div>');
	$('div#temporal_metadata').after('<div class="addedMetadata"><p>end: ' + datetime_stop + '</p></div>');
	$('div#temporal_metadata').after('<div class="addedMetadata"><p>begin: ' + datetime_start + '</p></div>');

	display_meta_map (west_degrees,east_degrees,north_degrees,south_degrees);
}
*/

//for prototype purposes, im just hard coding the result
//need to fix how this is being fed

//var result = {"record":[{"header":{"datestamp":"2009-07-21T22:28:58Z","identifier":"oai:daac.ornl.gov:ornldaac_1"},"metadata":{"DIF":{"Access_Constraints":"","Parameters":[{"Variable_Level_1":"DISCHARGE/FLOW","Category":"EARTH SCIENCE","Topic":"HYDROSPHERE","Term":"SURFACE WATER"},{"Variable_Level_1":"STAGE HEIGHT","Category":"EARTH SCIENCE","Topic":"HYDROSPHERE","Term":"SURFACE WATER"}],"Originating_Metadata_Node":"GCMD","Entry_ID":"FIFE_STRM_15M","Keyword":["EOSDIS","HYDROLOGY","STREAM DYNAMICS","AQUATIC PROPERTIES"],"IDN_Node":{"Short_Name":"USA/NASA"},"Data_Set_Citation":{"Other_Citation_Details":"doi:10.3334/ORNLDAAC/1","Dataset_Publisher":"Oak Ridge National Laboratory Distributed Active Archive Center","Online_Resource":1,"Dataset_Creator":"WOOD, E.F.","Data_Presentation_Form":"Online Files","Dataset_Title":"15 MINUTE STREAM FLOW DATA: USGS (FIFE)","Version":"","Dataset_Release_Place":"Oak Ridge, Tennessee, U.S.A.","Dataset_Release_Date":2008},"Spatial_Coverage":{"Westernmost_Longitude":-96.6,"Easternmost_Longitude":-96.6,"Northernmost_Latitude":39.1,"Southernmost_Latitude":39.1},"Data_Center":{"Data_Set_ID":1,"Personnel":{"Contact_Address":{"Province_or_State":"Tennessee","Postal_Code":"37831-6407","Address":["ORNL DAAC USER SERVICES OFFICE","P.O. Box 2008, MS 6407","Oak Ridge National Laboratory"],"Country":"USA","City":"Oak Ridge"},"Phone":"+1(865)241-3952","Email":"ornldaac@ornl.gov","Fax":"+1(865)574-4665","Last_Name":"ORNL DAAC USER SERVICES","Role":"DATA CENTER CONTACT"},"Data_Center_URL":"http://daac.ornl.gov/","Data_Center_Name":{"Short_Name":"ORNL_DAAC","Long_Name":"THE OAK RIDGE NATIONAL LABORATORY (ORNL) DISTRIBUTED ACTIVE ARCHIVE CENTER (DAAC)"}},"Summary":"ABSTRACT: The Fifteen Minute Stream Flow Data from the USGS Data Set contains 15 minute stream flow data from the USGS station located 2.9 miles upstream from the mouth of Kings Creek. The record extends from April 1, 1979 through September 2, 1988. The purpose of this data set was to provide accurate measurements of the stream flow from Kings Creek so that a water budget analysis for the northwest quadrant of the FIFE study area could be performed. The stilling pipe installed at the USGS station operates on the principle that the water level in a standpipe at a specific location within a stream bed can be converted to a volume of water in the stream bed. The tracking of the change in stream height with time then enables the calculation of stream flow.","Metadata_Name":"CEOS IDN DIF","xmlns:fo":"http://www.w3.org/1999/XSL/Format","Temporal_Coverage":{"Start_Date":"1984-12-25","Stop_Date":"1988-03-04"},"Data_Set_Language":"ENGLISH","Data_Set_Progress":"COMPLETE","Metadata_Version":"VERSION 9.0","Source_Name":[{"Short_Name":"","Long_Name":"SURFACE WATER WEIR"},{"Short_Name":"","Long_Name":"SURFACE WATER WEIR"}],"Personnel":[{"Contact_Address":"ORNL DAAC USER SERVICES OFFICE\n        OAK RIDGE NATIONAL LABORATORY\n        OAK RIDGE, TENNESSEE 37831 USA","Last_Name":"WOOD, E.F.","First_Name":"","Role":"TECHNICAL CONTACT"},{"Contact_Address":{"Province_or_State":"Tennessee","Postal_Code":"37831-6407","Address":["ORNL DAAC User Services Office","P.O. Box 2008, MS 6407","Oak Ridge National Laboratory"],"Country":"USA","City":"Oak Ridge"},"Phone":"(865) 241-3952","Email":"ornldaac@ornl.gov","Fax":"(865) 574-4665","Last_Name":"ORNL DAAC User Services","Role":"DIF AUTHOR"}],"Project":[{"Short_Name":"ESIP","Long_Name":"Earth Science Information Partners Program"},{"Short_Name":"EOSDIS","Long_Name":"Earth Observing System Data Information System"}],"Sensor_Name":[{"Short_Name":"","Long_Name":"STILLING WELL"},{"Short_Name":"","Long_Name":"STILLING WELL"}],"Entry_Title":"15 MINUTE STREAM FLOW DATA: USGS (FIFE)","Use_Constraints":"","DIF_Creation_Date":"2008-12-02","Related_URL":[{"URL":"http://daac.ornl.gov/cgi-bin/dsviewer.pl?ds_id=1","URL_Content_Type":{"Subtype":"","Type":"GET DATA"}},{"Description":"15 MINUTE STREAM FLOW DATA: USGS (FIFE)","URL":"http://daac.ornl.gov//FIFE/guides/15_min_strm_flow.html","URL_Content_Type":{"Type":"VIEW RELATED INFORMATION"}}]}}},{"header":{"datestamp":"2009-07-21T22:28:58Z","identifier":"oai:daac.ornl.gov:ornldaac_10"},"metadata":{"DIF":{"Access_Constraints":"","Parameters":[{"Variable_Level_1":"AIR TEMPERATURE","Category":"EARTH SCIENCE","Topic":"ATMOSPHERE","Term":"ATMOSPHERIC TEMPERATURE"},{"Variable_Level_1":"ATMOSPHERIC PRESSURE MEASUREMENTS","Category":"EARTH SCIENCE","Topic":"ATMOSPHERE","Term":"ATMOSPHERIC PRESSURE"},{"Variable_Level_1":"BAROMETRIC ALTITUDE","Category":"EARTH SCIENCE","Topic":"ATMOSPHERE","Term":"ALTITUDE"},{"Variable_Level_1":"CARBON DIOXIDE","Category":"EARTH SCIENCE","Topic":"ATMOSPHERE","Term":"ATMOSPHERIC CHEMISTRY"},{"Variable_Level_1":"CARBON DIOXIDE","Category":"EARTH SCIENCE","Topic":"ATMOSPHERE","Term":"ATMOSPHERIC CHEMISTRY"},{"Variable_Level_1":"GEOPOTENTIAL HEIGHT","Category":"EARTH SCIENCE","Topic":"ATMOSPHERE","Term":"ALTITUDE"},{"Variable_Level_1":"HEAT FLUX","Category":"EARTH SCIENCE","Topic":"ATMOSPHERE","Term":"ATMOSPHERIC RADIATION"},{"Variable_Level_1":"IRRADIANCE","Category":"EARTH SCIENCE","Topic":"ATMOSPHERE","Term":"ATMOSPHERIC RADIATION"},{"Variable_Level_1":"POTENTIAL TEMPERATURE","Category":"EARTH SCIENCE","Topic":"ATMOSPHERE","Term":"ATMOSPHERIC TEMPERATURE"},{"Variable_Level_1":"SURFACE AIR TEMPERATURE","Category":"EARTH SCIENCE","Topic":"ATMOSPHERE","Term":"ATMOSPHERIC TEMPERATURE"},{"Variable_Level_1":"SURFACE WINDS","Category":"EARTH SCIENCE","Topic":"ATMOSPHERE","Term":"ATMOSPHERIC WINDS"},{"Variable_Level_1":"UPPER LEVEL WINDS","Category":"EARTH SCIENCE","Topic":"ATMOSPHERE","Term":"ATMOSPHERIC WINDS"},{"Variable_Level_1":"VEGETATION COVER","Category":"EARTH SCIENCE","Topic":"BIOSPHERE","Term":"VEGETATION"},{"Variable_Level_1":"WATER VAPOR","Category":"EARTH SCIENCE","Topic":"ATMOSPHERE","Term":"ATMOSPHERIC WATER VAPOR"},{"Variable_Level_1":"WATER VAPOR","Category":"EARTH SCIENCE","Topic":"ATMOSPHERE","Term":"ATMOSPHERIC WATER VAPOR"}],"Originating_Metadata_Node":"GCMD","Entry_ID":"FIFE_AF_RAW_K","Keyword":["EOSDIS","ATMOSPHERIC PROPERTIES","ATMOSPHERIC BOUNDARY LAYER"],"IDN_Node":{"Short_Name":"USA/NASA"},"Data_Set_Citation":{"Other_Citation_Details":"doi:10.3334/ORNLDAAC/10","Dataset_Publisher":"Oak Ridge National Laboratory Distributed Active Archive Center","Online_Resource":10,"Dataset_Creator":"KELLY, R.D.","Data_Presentation_Form":"Online Files","Dataset_Title":"AIRCRAFT FLUX-RAW: U OF WY. (FIFE)","Version":"","Dataset_Release_Place":"Oak Ridge, Tennessee, U.S.A.","Dataset_Release_Date":1994},"Spatial_Coverage":{"Westernmost_Longitude":-102,"Easternmost_Longitude":-95,"Northernmost_Latitude":40,"Southernmost_Latitude":37},"Data_Center":{"Data_Set_ID":10,"Personnel":{"Contact_Address":{"Province_or_State":"Tennessee","Postal_Code":"37831-6407","Address":["ORNL DAAC USER SERVICES OFFICE","P.O. Box 2008, MS 6407","Oak Ridge National Laboratory"],"Country":"USA","City":"Oak Ridge"},"Phone":"+1(865)241-3952","Email":"ornldaac@ornl.gov","Fax":"+1(865)574-4665","Last_Name":"ORNL DAAC USER SERVICES","Role":"DATA CENTER CONTACT"},"Data_Center_URL":"http://daac.ornl.gov/","Data_Center_Name":{"Short_Name":"ORNL_DAAC","Long_Name":"THE OAK RIDGE NATIONAL LABORATORY (ORNL) DISTRIBUTED ACTIVE ARCHIVE CENTER (DAAC)"}},"Summary":"ABSTRACT: The University of Wyoming (UW) King Air atmospheric boundary layer measurement missions were flown in 1987 during IFCs 3 and 4. This Raw Boundary Layer Fluxes data set contains parameters that describe the environment in which the flux data were collected and the flux data itself. The fluctuations in all variables were calculated with three different methods (the arithmetic means removed, the linear trends removed, or filtered with a high-pass recursive filter) prior to the eddy correlation calculations. This data set contains the data with the arithmetic means removed (i.e., RAW). All the flux measurements were obtained with the eddy-correlation method, wherein the aircraft is equipped with an inertial platform, accelerometers, and a gust probe for measurement of earth-relative gusts in the x, y, and z directions. Gusts in these dimensions are then correlated with each other for momentum fluxes and with fluctuations in other variables to obtain the various scalar fluxes, such as temperature (for sensible heat flux) and water vapor mixing ratio (for latent heat flux). The summary of data calculated from each aircraft pass includes various statistics, correlations, and fluxes calculated after the time series for each variable with the arithmetic means removed.","Metadata_Name":"CEOS IDN DIF","xmlns:fo":"http://www.w3.org/1999/XSL/Format","Temporal_Coverage":{"Start_Date":"1987-08-11","Stop_Date":"1989-10-31"},"Data_Set_Language":"ENGLISH","Data_Set_Progress":"COMPLETE","Metadata_Version":"VERSION 9.0","Source_Name":[{"Short_Name":"","Long_Name":"KINGAIR"},{"Short_Name":"","Long_Name":"KINGAIR"},{"Short_Name":"","Long_Name":"KINGAIR"},{"Short_Name":"","Long_Name":"KINGAIR"},{"Short_Name":"","Long_Name":"KINGAIR"},{"Short_Name":"","Long_Name":"KINGAIR"},{"Short_Name":"","Long_Name":"KINGAIR"},{"Short_Name":"","Long_Name":"KINGAIR"},{"Short_Name":"","Long_Name":"KINGAIR"},{"Short_Name":"","Long_Name":"KINGAIR"},{"Short_Name":"","Long_Name":"KINGAIR"},{"Short_Name":"","Long_Name":"KINGAIR"},{"Short_Name":"","Long_Name":"KINGAIR"},{"Short_Name":"","Long_Name":"KINGAIR"},{"Short_Name":"","Long_Name":"KINGAIR"}],"Personnel":[{"Contact_Address":"ORNL DAAC USER SERVICES OFFICE\n        OAK RIDGE NATIONAL LABORATORY\n        OAK RIDGE, TENNESSEE 37831 USA","Last_Name":"KELLY, R.D.","First_Name":"","Role":"TECHNICAL CONTACT"},{"Contact_Address":{"Province_or_State":"Tennessee","Postal_Code":"37831-6407","Address":["ORNL DAAC User Services Office","P.O. Box 2008, MS 6407","Oak Ridge National Laboratory"],"Country":"USA","City":"Oak Ridge"},"Phone":"(865) 241-3952","Email":"ornldaac@ornl.gov","Fax":"(865) 574-4665","Last_Name":"ORNL DAAC User Services","Role":"DIF AUTHOR"}],"Project":[{"Short_Name":"ESIP","Long_Name":"Earth Science Information Partners Program"},{"Short_Name":"EOSDIS","Long_Name":"Earth Observing System Data Information System"}],"Sensor_Name":[{"Short_Name":"","Long_Name":"INFRARED RADIOMETER"},{"Short_Name":"","Long_Name":"PRESSURE TRANSDUCER"},{"Short_Name":"","Long_Name":"PRESSURE TRANSDUCER"},{"Short_Name":"","Long_Name":"EDDY CORRELATION APPARATUS"},{"Short_Name":"","Long_Name":"NDIR GAS ANALYZER"},{"Short_Name":"","Long_Name":"RADIO ALTIMETER"},{"Short_Name":"","Long_Name":"EDDY CORRELATION APPARATUS"},{"Short_Name":"","Long_Name":"PYRANOMETER"},{"Short_Name":"","Long_Name":"HYGROMETER"},{"Short_Name":"","Long_Name":"INFRARED RADIOMETER"},{"Short_Name":"","Long_Name":"WIND PROFILER"},{"Short_Name":"","Long_Name":"WIND PROFILER"},{"Short_Name":"","Long_Name":"ANALYSIS"},{"Short_Name":"","Long_Name":"HYGROMETER"},{"Short_Name":"","Long_Name":"NDIR GAS ANALYZER"}],"Entry_Title":"AIRCRAFT FLUX-RAW: U OF WY. (FIFE)","Use_Constraints":"","DIF_Creation_Date":"1994-07-24","Related_URL":[{"URL":"http://daac.ornl.gov/cgi-bin/dsviewer.pl?ds_id=10","URL_Content_Type":{"Subtype":"","Type":"GET DATA"}},{"Description":"AIRCRAFT FLUX-RAW: U OF WY. (FIFE)","URL":"http://daac.ornl.gov//FIFE/guides/air_flux_raw_wy.html","URL_Content_Type":{"Type":"VIEW RELATED INFORMATION"}}]}}}]}


//record = result;

//alert(record.record.length);