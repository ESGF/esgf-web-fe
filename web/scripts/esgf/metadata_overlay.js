
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
'<h1>Metadata Report</h1>'+
'<div id="metadata_download">Download</div>'+
'<div id="metadata_title_gap"></div>'+
'<div id="metadata_return">Print</div>'+
'<p>'+
'Metadata report for...'+
'</p>'+

'<!-- root element for scrollable -->'+ 
'<div class="scrollable verticalscroll">'+ 
'	<!-- root element for the items --> '+
'	<div class="itemsscroll">'+
		
		
'		<div>'+
		
'			<div class="shortitem">'+ 
'				<!--<img src="http://farm1.static.flickr.com/3650/3323058611_d35c894fab_m.jpg" />'+ 
		
'				-->'+
'				<div class="leftsd">'+
'					<h3>Title</h3> '+
'				</div>'+
				
'				<div class="rightsd">'+
'					<div id="title"></div>'+
		
'				</div>'+
				
'			</div> '+
'		</div>'+
		
'		<div>'+
		
'			<div class="shortitem"> '+
'				<!--<img src="http://farm1.static.flickr.com/3650/3323058611_d35c894fab_m.jpg" /> '+
		
'				-->'+
'				<div class="leftsd">'+
'					<h3>URL:</h3> '+
'				</div>'+
				
'				<div class="rightsd">'+
'					<div id="url"></div>'+
		
'				</div>'+
				
'			</div> '+
'		</div>'+
		
'		<div>'+
		
'			<div class="shortitem"> '+
'				<!--<img src="http://farm1.static.flickr.com/3650/3323058611_d35c894fab_m.jpg" /> '+
		
'				-->'+
'				<div class="leftsd">'+
'					<h3>Project Leader(s):</h3> '+
'				</div>'+
				
'				<div class="rightsd">'+
'					<div id="leader"></div>'+
		
'				</div>'+
				
'			</div>'+ 
'		</div>'+
		
'		<div>'+
		
'			<div class="shortitem"> '+
'				<!--<img src="http://farm1.static.flickr.com/3650/3323058611_d35c894fab_m.jpg" /> '+
		
'				-->'+
'				<div class="leftsd">'+
'					<h3>Contact Info:</h3> '+
'				</div>'+
				
'				<div class="rightsd">'+
'					<div id="contact_metadata"></div>'+
		
'				</div>'+
				
'			</div> '+
'		</div>'+
		
'		<div>'+
		
'			<div class="geospatial_item"> '+
'				<!--<img src="http://farm1.static.flickr.com/3650/3323058611_d35c894fab_m.jpg" /> '+
		
'				-->'+
'				<div class="leftsd">'+
'					<h3>Geospatial Information:</h3> '+
'				</div>'+
				
				
'				<div class="rightsd">'+
'					<div id="geo_info">'+
'					Geographic boundaries'+
'						<div id="geospatial_metadata"></div>'+
'					</div>'+
					
'					<div id="geo_map">map here</div>'+
'				</div>'+
				
'			</div> '+
'		</div>'+
		
'		<div>'+
		
'			<div class="geospatial_item"> '+
'				<!--<img src="http://farm1.static.flickr.com/3650/3323058611_d35c894fab_m.jpg" /> '+
		
'				-->'+
'				<div class="leftsd">'+
'					<h3>Temporal Information:</h3> '+
'				</div>'+
				
'				<div class="rightsd">'+
'					<div id="temporal_metadata"></div>'+
		
'				</div>'+
				
'			</div> '+
'		</div>'+
		
'		<div>'+
		
'			<div class="shortitem"> '+
'				<!--<img src="http://farm1.static.flickr.com/3650/3323058611_d35c894fab_m.jpg" /> '+
		
'				-->'+
'				<div class="leftsd">'+
'					<h3>Keywords:</h3> '+
'				</div>'+
				
'				<div class="rightsd">'+
'					<div id="keyword_metadata"></div>'+
		
'				</div>'+
				
'			</div> '+
'		</div>'+
		
		
		
'		<div>'+
		
'			<div class="abstract_item"> '+
'				<!--<img src="http://farm1.static.flickr.com/3650/3323058611_d35c894fab_m.jpg" /> '+
		
'				-->'+
'				<div class="leftsd">'+
'					<h3>Abstract:</h3> '+
'				</div>'+
				
'				<div class="rightsd">'+
'					<div id="abstract"></div>'+
		
'				</div>'+
				
'			</div> '+
'		</div>'+
'	 	</div>'+

	
'</div>'+
'</div>'+


'<div id="backgroundPopupMetadataReport"></div>';



/* loaders */

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




/* disablers */

//disabling metadata report popup!
function disablePopupMetadataReport(){
	//disables popup only if it is enabled
	if(popupStatusMetadataReport==1){
		$("#backgroundPopupMetadataReport").fadeOut("slow");
		$("#popupMetadataReport").fadeOut("slow");
		popupStatusMetadataReport = 0;
	}
}





/* centerers */

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










/*function metadata_link(title,
						description,
						west_degrees,
						east_degrees,
						north_degrees,
						south_degrees,
						datetime_start,
						datetime_stop)*/
function metadata_link()
{
	if(popupStatusMetadataReport==0){
		$("#backgroundPopupMetadataReport").css({
			"opacity": "0.8"
		});
		$("#backgroundPopupMetadataReport").fadeIn("slow");
		$("#popupMetadataReport").fadeIn("slow");
		popupStatusMetadataReport = 1;
	}
	
	var title = 'unknown';
	var description = 'unknown';
	var west_degrees = '0';
	var east_degrees = '0';
	var north_degrees = '0';
	var south_degrees = '0';
	var datetime_start = 'unknown';
	var datetime_stop = 'unknown';
	
	$('.added1').remove();
	$('div#title').after('<div class="added1"><p>' + title + '</p></div>');
	$('div#abstract').after('<div class="added1"><p>' + description + '</p></div>');
	$('div#leader').after('<div class="added1"><p>' + 'John Harney' + '</p></div>');
	$('div#geospatial_metadata').after('<div class="added1"><p>west degrees: ' + west_degrees + '</p></div>');
	$('div#geospatial_metadata').after('<div class="added1"><p>east degrees: ' + east_degrees + '</p></div>');
	$('div#geospatial_metadata').after('<div class="added1"><p>north degrees: ' + north_degrees + '</p></div>');
	$('div#geospatial_metadata').after('<div class="added1"><p>south degrees: ' + south_degrees + '</p></div>');
	$('div#temporal_metadata').after('<div class="added1"><p>end: ' + datetime_stop + '</p></div>');
	$('div#temporal_metadata').after('<div class="added1"><p>begin: ' + datetime_start + '</p></div>');

	display_meta_map ();
}

function display_meta_map () {
	var mapDiv = document.getElementById('geo_map');
	var latlng = new google.maps.LatLng(37.09, -95.71);
	var options = {
		center: latlng,
		zoom: 4,
		mapTypeId: google.maps.MapTypeId.ROADMAP
	};

	// check to see if we already have a map object
	//if (!map) {
	var meta_map = new google.maps.Map(mapDiv, options);
	//}

	var location = new google.maps.LatLng(30,40);

	// create a new marker
	var marker = new google.maps.Marker({
		draggable: true,
		position: location,
		map: meta_map
	});
		
	
};


