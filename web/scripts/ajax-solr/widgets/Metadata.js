/**
 * 
 */


var globalRecordId = '';    		
var metadatafileformat = 'OAI';

var metadatafilename = 'ORNL-oai_dif.json';

(function ($) {

AjaxSolr.MetadataWidget = AjaxSolr.AbstractWidget.extend({
  afterRequest: function () {
    var self = this;

    
	
	
    $("a.met").click(function () {
    	
    	var idStr = $(this).parent().find("a").attr("id");
	  	globalRecordId = idStr;
		metadatafileformat = $(this).parent().find("a").attr("format");
		metadatafilename = $(this).parent().find("a").attr("metadata_url");
    });
    
    
    
    $(".ai_meta a[rel]").overlay({
    	
		//mask: 'darkred',
		mask: {opacity: 0.5, color: '#000'},
		
		effect: 'apple',
		top: '2%',
		left: '2%',

		onBeforeLoad: function() {
			
			$('.apple_overlay').css({'width' : '720px'});
			var wrap = this.getOverlay().find(".contentWrap");
			wrap.load(this.getTrigger().attr("href"));
		},
    
    	onLoad: function() {

			$(".overlay_header").show();
			$(".overlay_content").show();
			$(".overlay_footer").show();
			$(".overlay_border").show();


			$(".scrollable").scrollable({ vertical: true, mousewheel: true });	
			
    		id = globalRecordId;
    		var title = 'title';
    		
    		
    		metadata_report(id,title,metadatafilename,metadatafileformat);
    	  	
    	},
		
		onClose: function() {
			$(".overlay_header").hide();
			$(".overlay_content").hide();
			$(".overlay_footer").hide();
			$(".overlay_border").hide();
		}

	});
    
    $(".m a[rel]").overlay({
    	
		//mask: 'darkred',
		mask: {opacity: 0.5, color: '#000'},
		
		effect: 'apple',
		top: '2%',
		left: '2%',

		onBeforeLoad: function() {
			
			$('.apple_overlay').css({'width' : '720px'});
			var wrap = this.getOverlay().find(".contentWrap");
			wrap.load(this.getTrigger().attr("href"));
			
			alert('before loading met report');
		},
    
    	onLoad: function() {

			$(".overlay_header").show();
			$(".overlay_content").show();
			$(".overlay_footer").show();
			$(".overlay_border").show();


			$(".scrollable").scrollable({ vertical: true, mousewheel: true });	
			
    		id = globalRecordId;
    		var title = 'title';
    		
    		
    		metadata_report(id,title,metadatafilename,metadatafileformat);
    	  	
    	},
		
		onClose: function() {
			$(".overlay_header").hide();
			$(".overlay_content").hide();
			$(".overlay_footer").hide();
			$(".overlay_border").hide();
		}

	});
  }
  
   
	
});

})(jQuery);


	
	
	function metadata_report(id,title,metadatafilename,metadatafileformat) {
		jQuery.ajax({
			url: 'http://localhost:8080/esgf-web-fe/metadataproxy',
			data: 'metadataformat=' + metadatafileformat + '&metadatafile=' + metadatafilename + '&id=' + id,
			type: 'GET',
			success: function(record) {processMetadataRecord(record,id,metadatafileformat);},
			error: function() {alert("error http://localhost:8080/esgf-web-fe/metadataproxy");},
			dataType: 'json'
		}); //end metadata_report
	}
		
	function processMetadataRecord(record,id,metadatafileformat) {
		$('.addedMetadata').remove();
		$('.addedMetadataTitle').remove();
		
		
		//branch logic depending on the metadata file format
		if(metadatafileformat == 'OAI') {
			processOAI(record,id);
		}
		else if(metadatafileformat == 'FGDC') {
			processFGDC(record,id);
		}
		else if(metadatafileformat == 'CAS') {
			processCAS(record,id);
		}
		else{ //thredds
			processTHREDDS(record,id);
		}
		
	} //end processMetadataRecord

	function processCAS(record){
		//need to come back to this
		//the title is extracted from a hard coded place
		var title = record.he5.Filename.content;
		//add title and constraints to the page
		$('div#metadata_summary_dataset').after('<div class="addedMetadataTitle">' + 'Dataset: ' + title); //+ '</div><p>&lt;insert constraints here&gt;</p>');
		var projectsText = 'MLS';
		$('div#projects_metadata').after('<div class="addedMetadata">' + projectsText + '</div>');
		
		// Contact Info 
		var investigators = ' N/A ';
		$('div#contact_metadata').after('<div class="addedMetadata"><p>' + investigators + '</p></div>');
		
		// Temporal Info 
		var startText = (record.he5.RANGEBEGINNINGDATE) + (record.he5.RANGEBEGINNINGTIME);
		var stopText = (record.he5.RANGEENDINGDATE) + (record.he5.RANGEENDINGTIME);
		if(stopText == '.')	{
			var currentTime = new Date();
			stopText = currentTime.getFullYear(); //+ currentTime.getMonth() + currentTime.getDay();
		}
		$('div#time_metadata').after('<div class="addedMetadata"><p>Begin: ' + startText + ' End: ' + stopText + '</p></div>');
		
		// Geo Info 
		var east_degreesText = record.he5.EASTBOUNDINGCOORDINATE - 0.001;
		var west_degreesText = record.he5.WESTBOUNDINGCOORDINATE + 0.001;
		var north_degreesText = record.he5.NORTHBOUNDINGCOORDINATE - 0.001;
		var south_degreesText = record.he5.SOUTHBOUNDINGCOORDINATE + 0.001;
		$('div#geospatial_metadata').after('<div class="addedMetadata"><p>' + 'coordinates (N,W,S,E):<br />(' + north_degreesText + ',' + west_degreesText + ',' + south_degreesText + ',' + east_degreesText + ')</p></div>');
		display_meta_map(west_degreesText,east_degreesText,north_degreesText,south_degreesText);
		
		//Keywords
		$('div#keywords_metadata').after('<div class="addedMetadata"><p>' + ' N/A ' + '</p></div>');
		
		// Abstract 
		//var abstractText = record.catalog.dataset.name;
		$('div#abstract_metadata').after('<div class="addedMetadata"><p>' + ' N/A ' + '</p></div>');
		
	} //end processCAS

 
	function processTHREDDS(record) {
		
		//need to come back to this
		//the title is extracted from a hard coded place
		var title = record.catalog.dataset.property[9].value;
		
		//add title and constraints to the page
		$('div#metadata_summary_dataset').after('<div class="addedMetadataTitle">' + 'Dataset: ' + title); //+ '</div><p>&lt;insert constraints here&gt;</p>');
		
		// Projects 
		//note i need to change the "0" index and place the correct information 
		//projects_metadata -> (record)/record[0]/metadata/DIF/Project[0]
		//var projects = record.record[0].metadata.DIF.Project;
		
		var projectsFirstChar = (record.catalog.dataset.name).search('project');
		var projectsLastChar = (record.catalog.dataset.name).search('model');
		var projects = (record.catalog.dataset.name).substr(projectsFirstChar,projectsLastChar);
		//var projects = record.record.metadata.DIF.Project;
		var projectsText = projects.substr(8,((projects.length)-3));
		$('div#projects_metadata').after('<div class="addedMetadata">' + projectsText + '</div>');
		
		// Contact Info 
		var investigators = record.catalog.dataset.creator.name;
		investigators += '<br />' + record.catalog.dataset.creator.contact.url + '<br />' + record.catalog.dataset.creator.contact.email;
		$('div#contact_metadata').after('<div class="addedMetadata"><p>' + investigators + '</p></div>');
		
		// Temporal Info 
		var startText = record.catalog.dataset.metadata.timeCoverage.start;
		var stopText = record.catalog.dataset.metadata.timeCoverage.end;
		if(stopText == '.')	{
			var currentTime = new Date();
			stopText = currentTime.getFullYear(); //+ currentTime.getMonth() + currentTime.getDay();
		}
		$('div#time_metadata').after('<div class="addedMetadata"><p>Begin: ' + startText + ' End: ' + stopText + '</p></div>');
		
		// Geo Info 
		var east_degreesText = record.catalog.dataset.metadata.geospatialCoverage.eastwest.start;
		var west_degreesText = record.catalog.dataset.metadata.geospatialCoverage.eastwest.start + record.catalog.dataset.metadata.geospatialCoverage.eastwest.size;
		var north_degreesText = record.catalog.dataset.metadata.geospatialCoverage.northsouth.start;
		var south_degreesText = record.catalog.dataset.metadata.geospatialCoverage.northsouth.start + record.catalog.dataset.metadata.geospatialCoverage.northsouth.size;
		$('div#geospatial_metadata').after('<div class="addedMetadata"><p>' + 'coordinates (N,W,S,E):<br />(' + north_degreesText + ',' + west_degreesText + ',' + south_degreesText + ',' + east_degreesText + ')</p></div>');
		display_meta_map(west_degreesText,east_degreesText,north_degreesText,south_degreesText);
		
		
		$('div#keywords_metadata').after('<div class="addedMetadata"><p>' + ' N/A ' + '</p></div>');
		
		// Abstract 
		var abstractText = record.catalog.dataset.name;
		$('div#abstract_metadata').after('<div class="addedMetadata"><p>' + abstractText + '</p></div>');
		
		
	} //end processTHREDDS

	
	function processFGDC(record){
		
		var title = record.metadata.idinfo.citation.citeinfo.title;
		
		//add title and constraints to the page
		$('div#metadata_summary_dataset').after('<div class="addedMetadataTitle">' + 'Dataset: ' + title); //+ '</div><p>&lt;insert constraints here&gt;</p>');
		
		// Investigators 
		var investigators = record.metadata.idinfo.citation.citeinfo.origin;
		var investigatorsText = '';
		for(var i = 0;i<investigators.length;i++) {
			if(i == investigators.length-1) {
				investigatorsText += investigators[i];
			}
			else {
				investigatorsText += investigators[i] + ', ';
			}
		}	
		
		$('div#investigator_metadata').after('<div class="addedMetadata"><p>' + investigatorsText + '</p></div>');
		
		// Contact Info 
		var org = record.metadata.distinfo.distrib.cntinfo.cntperp.cntorg;
		var contactText = org;
		$('div#contact_metadata').after('<div class="addedMetadata"><p>' + contactText + '</p></div>');
		
		// Temporal Info 
		var startText = record.metadata.idinfo.timeperd.timeinfo.rngdates.begdate;
		var stopText = record.metadata.idinfo.timeperd.timeinfo.rngdates.enddate;
		if(stopText == '.')	{
			var currentTime = new Date();
			stopText = currentTime.getFullYear(); //+ currentTime.getMonth() + currentTime.getDay();
		}
		$('div#time_metadata').after('<div class="addedMetadata"><p>Begin: ' + startText + ' End: ' + stopText + '</p></div>');
		
		// Geo Info 
		var east_degreesText = record.metadata.idinfo.spdom.bounding.eastbc;
		var west_degreesText = record.metadata.idinfo.spdom.bounding.westbc;
		var north_degreesText = record.metadata.idinfo.spdom.bounding.northbc;
		var south_degreesText = record.metadata.idinfo.spdom.bounding.southbc;
		$('div#geospatial_metadata').after('<div class="addedMetadata"><p>' + 'coordinates (N,W,S,E):<br />(' + north_degreesText + ',' + west_degreesText + ',' + south_degreesText + ',' + east_degreesText + ')</p></div>');
		//display_meta_map(west_degreesText,east_degreesText,north_degreesText,south_degreesText);
		
		// Abstract 
		var abstractText = record.metadata.idinfo.descript.abstract;
		$('div#abstract_metadata').after('<div class="addedMetadata"><p>' + abstractText + '</p></div>');
		
		
		// Keywords  - neerds some work
		var keywords = record.metadata.idinfo.keywords.theme[0];
		var keywordsText = '';
		for(var i = 0;i<keywords.themekey.length;i++) {
			if(i == keywords.themekey.length-1) {
				keywordsText += keywords.themekey[i];
			}
			else {
				keywordsText += keywords.themekey[i] + ', ';
			}
		}
		$('div#keywords_metadata').after('<div class="addedMetadata"><p>' + keywordsText + '</p></div>');
		
	} //end processFGDC

	function processOAI(record)
	{
		var title = record.record.metadata.DIF.Entry_Title;
		
		//add title and constraints to the page
		$('div#metadata_summary_dataset').after('<div class="addedMetadataTitle">' + 'Dataset: ' + title); //+ '</div><p>&lt;insert constraints here&gt;</p>');
		

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
		if(keywords != null && keywords != '')
		{
			for(var i = 0;i<keywords.length;i++) {
				if(i == keywords.length-1) {
					keywordsText += keywords[i] + ' (' + keywords[i] + ')';
				}
				else {
					keywordsText += keywords[i] + ' (' + keywords[i] + '), ';
				}
			}
			$('div#keywords_metadata').after('<div class="addedMetadata"><p>' + keywordsText + '</p></div>');
		}
		
		// Abstract 
		var abstractText = record.record.metadata.DIF.Summary;
		$('div#abstract_metadata').after('<div class="addedMetadata"><p>' + abstractText + '</p></div>');
		

	} //end processOAI


	


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
			fillColor: "#FEE5AC",
			fillOpacity: 0.35,
			strokeWeight: 2
		});
		
		poly.setMap(map);
		 
		map.fitBounds(bounds);
		
	} //end displayMetaMap
		



