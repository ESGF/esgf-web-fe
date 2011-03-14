/**
 * 
 */
(function ($) {
    AjaxSolr.MetadataWidget = AjaxSolr.AbstractWidget.extend({
        /*
         * Metadata properties
         */
        globalRecordId: '',
	    metadatafileformat: '',
	    metadatafilename: '',
        title: null,
        west_degrees: null,
        east_degrees: null,
        north_degrees: null,
        south_degrees: null,
        projects: null,
        investigators: null,
        contact: null,
        start_time: null,
        stop_time: null,
        keywords: null,
        description: null,
        
        display_meta_map: function () {
            var self = this;
            var i = null;
            var bounds = new google.maps.LatLngBounds();
            var paths = [ ];
            var ne = new google.maps.LatLng(self.north_degrees,self.east_degrees);
            var nw = new google.maps.LatLng(self.north_degrees,self.west_degrees);
            var se = new google.maps.LatLng(self.south_degrees,self.east_degrees);
            var sw = new google.maps.LatLng(self.south_degrees,self.west_degrees);
            var map = new google.maps.Map(document.getElementById("geo_map"), {
                zoom: 4,
                center: new google.maps.LatLng(ne.lat(), ne.lng()),
                mapTypeId: google.maps.MapTypeId.TERRAIN
            });
            var points = [ne, se, sw, nw];
            for(i in points) {
                if(points.hasOwnProperty(i)) {
                    var marker = new google.maps.Marker({
                        draggable: false,
                        position: points[i],
                        map: map
                    });
                    bounds.extend(points[i]);
                 }   
             }
             var poly = new google.maps.Polygon({
                 paths: points,
                 fillColor: "#FEE5AC",
                 fillOpacity: 0.35,
                 strokeWeight: 2
             });
             poly.setMap(map);
             map.fitBounds(bounds);
        }, //end displayMetaMap
        metadata_report: function() {
            var self = this;
                jQuery.ajax({
                    url: 'http://localhost:8080/esgf-web-fe/metadataproxy',
                    data: 'metadataformat=' + self.metadatafileformat + '&metadatafile=' + self.metadatafilename + '&id=' + self.globalRecordId,
                    type: 'GET',
                    success: function(record) {self.processMetadataRecord(record);},
                    error: function() {alert("error http://localhost:8080/esgf-web-fe/metadataproxy");},
                    dataType: 'json'
                }); //end metadata_report
        },
        processMetadataRecord: function(record) {
            var self = this;
            $('.addedMetadata').remove();
            $('.addedMetadataTitle').remove();
            //branch logic depending on the metadata file format
            if(self.metadatafileformat === 'OAI') {
                self.processOAI(record,self.globalRecordId);
            }
            else if(self.metadatafileformat === 'FGDC') {
                self.processFGDC(record,self.globalRecordId);
            }
            else if(self.metadatafileformat === 'CAS') {
                self.processCAS(record,self.globalRecordId);
            }
            else{ //thredds
                self.processTHREDDS(record,self.globalRecordId);
            }
        }, //end processMetadataRecord
    
        processOAI: function(record) {
            var self = this;
            var i = null;
            self.title = record.record.metadata.DIF.Entry_Title;
            // Projects 
            //note i need to change the "0" index and place the correct information 
            //projects_metadata -> (record)/record[0]/metadata/DIF/Project[0]
            //var projects = record.record[0].metadata.DIF.Project;
            self.projects = record.record.metadata.DIF.Project;
            // Investigators 
            self.invesigators = record.record.metadata.DIF.Personnel[0].Last_Name;
            // Contact Info 
            self.contact = record.record.metadata.DIF.Personnel[0].Contact_Address;
            // Temporal Info 
            self.startTime = record.record.metadata.DIF.Temporal_Coverage.Start_Date;
            self.stopTime = record.record.metadata.DIF.Temporal_Coverage.Stop_Date;
            // Geo Info 
            var east_degreesText = record.record.metadata.DIF.Spatial_Coverage.Easternmost_Longitude;
            self.east_degrees = parseFloat(record.record.metadata.DIF.Spatial_Coverage.Easternmost_Longitude);
            var west_degreesText = record.record.metadata.DIF.Spatial_Coverage.Westernmost_Longitude;
            self.west_degrees = parseFloat(record.record.metadata.DIF.Spatial_Coverage.Westernmost_Longitude);
            var north_degreesText = record.record.metadata.DIF.Spatial_Coverage.Northernmost_Latitude;
            self.north_degrees = parseFloat(record.record.metadata.DIF.Spatial_Coverage.Northernmost_Latitude);
            var south_degreesText = record.record.metadata.DIF.Spatial_Coverage.Southernmost_Latitude;
            self.south_degrees = parseFloat(record.record.metadata.DIF.Spatial_Coverage.Southernmost_Latitude);
            //alert(self.north_degrees + " " + self.south_degrees + " " + self.east_degrees + " " + self.west_degrees);
            // Abstract 
            self.description = record.record.metadata.DIF.Summary;
            // Keywords 
            self.keywords = record.record.metadata.DIF.Keyword;
            //add projects to the page
            var projectsText = '';
            for(i = 0;i<self.projects.length;i++) {
                if(i === self.projects.length-1) {
                    projectsText += self.projects[i].Long_Name + ' (' + self.projects[i].Short_Name + ')';
                }
                else {
                    projectsText += self.projects[i].Long_Name + ' (' + self.projects[i].Short_Name + '), ';
                }
            }
            
            
            
            $('div#projects_metadata').after('<div class="addedMetadata">' + projectsText + '</div>');
            //self.writeToHTML();
            
        }, //end processOAI
        processTHREDDS: function(record) {
            var self = this;
            
            alert(record.catalog.dataset.metadata);
            //need to come back to this
            //the title is extracted from a hard coded place
            var datasetItem = record.catalog.dataset;
            alert(datasetItem);
            if('name' in datasetItem) {
                self.title = record.catalog.dataset.name;
                var projectsFirstChar = (record.catalog.dataset.name).search('project');
                var projectsLastChar = (record.catalog.dataset.name).search('model');
                self.projects = (record.catalog.dataset.name).substr(projectsFirstChar,projectsLastChar);
           
            } else {
            	self.title = 'N/A';
            	self.projects = 'N/A';
            }
        
          //var investigators = record.catalog.dataset.creator;
            if('creator' in datasetItem) {
            	self.investigators = record.catalog.dataset.creator;
            } else {
            	self.contact = 'N/A';
            }
            
            var metadataItem = record.catalog.dataset.metadata;
            if('timeCoverage' in metadataItem) {
            	self.startTime = record.catalog.dataset.metadata.timeCoverage.start;
            	self.stopTime = record.catalog.dataset.metadata.timeCoverage.end;
            } else {
            	self.startTime = 'N/A';
            	self.stopTime = 'N/A';
            }
            
            
            if('geospatialCoverage' in metadataItem) {
            	self.east_degrees = parseFloat(record.catalog.dataset.metadata.geospatialCoverage.eastwest.start);
            	self.west_degrees = (parseFloat(record.catalog.dataset.metadata.geospatialCoverage.eastwest.start) + parseFloat(record.catalog.dataset.metadata.geospatialCoverage.eastwest.size));
            	self.north_degrees = parseFloat(record.catalog.dataset.metadata.geospatialCoverage.northsouth.start);
            	self.south_degrees = (parseFloat(record.catalog.dataset.metadata.geospatialCoverage.northsouth.start ) + parseFloat(record.catalog.dataset.metadata.geospatialCoverage.northsouth.size));
                
            } else {
            	self.east_degrees = 'N/A';
            	self.west_degrees = 'N/A';
            	self.north_degrees = 'N/A';
            	self.west_degrees = 'N/A';
            }
            
            
            self.description = 'N/A';
            self.keywords = 'N/A';
            
            
            
            
    		
            /*
		//var projects = record.record.metadata.DIF.Project;
		//var projectsText = self.projects.substr(8,((self.projects.length)-3));
		
		//$('div#projects_metadata').after('<div class="addedMetadata">' + projectsText + '</div>');
		
		// Contact Info 
		//var investigators = record.catalog.dataset.creator;
		//investigators += '<br />' + record.catalog.dataset.creator + '<br />' + record.catalog.dataset.creator;
		//$('div#contact_metadata').after('<div class="addedMetadata"><p>' + investigators + '</p></div>');
		
		
		if(stopText == '.')	{
			var currentTime = new Date();
			stopText = currentTime.getFullYear(); //+ currentTime.getMonth() + currentTime.getDay();
		}
		
		
		//$('div#time_metadata').after('<div class="addedMetadata"><p>Begin: ' + startText + ' End: ' + stopText + '</p></div>');
		
		// Geo Info 
		//var east_degreesText = record.catalog.dataset.metadata.geospatialCoverage.eastwest.start;
		
		//self.east_degrees = parseFloat(record.record.metadata.DIF.Spatial_Coverage.Easternmost_Longitude);
		//var west_degreesText = record.catalog.dataset.metadata.geospatialCoverage.eastwest.start + record.catalog.dataset.metadata.geospatialCoverage.eastwest.size;
		//var north_degreesText = record.catalog.dataset.metadata.geospatialCoverage.northsouth.start;
		//var south_degreesText = record.catalog.dataset.metadata.geospatialCoverage.northsouth.start + record.catalog.dataset.metadata.geospatialCoverage.northsouth.size;
		//$('div#geospatial_metadata').after('<div class="addedMetadata"><p>' + 'coordinates (N,W,S,E):<br />(' + north_degreesText + ',' + west_degreesText + ',' + south_degreesText + ',' + east_degreesText + ')</p></div>');
		//self.display_meta_map(west_degreesText,east_degreesText,north_degreesText,south_degreesText);
		
		
		//$('div#keywords_metadata').after('<div class="addedMetadata"><p>' + ' N/A ' + '</p></div>');
		
		// Abstract 
		//var abstractText = record.catalog.dataset.name;
		//$('div#abstract_metadata').after('<div class="addedMetadata"><p>' + abstractText + '</p></div>');
		*/
            AjaxSolr.theme('metadata',self);
        },//end processTHREDDS
        
        /*
        defaultText: function(document) {
        	var self = document;
        	
        	if(self.title == null) {
            	self.title = 'N/A';
            }
            if(self.projects == null) {
            	self.projects = 'N/A';
            }
            if(self.invesigators == null) {
            	self.invesigators = 'N/A';
            }
            if(self.contact == null) {
            	self.contact = 'N/A';
            }
            if(self.startTime == null) {
            	self.startTime = 'N/A';
            }
            if(self.stopTime == null) {
            	self.stopTime = 'N/A';
            }
            if(self.east_degrees == null) {
            	self.east_degrees = 'N/A';
            }
            if(self.west_degrees == null) {
            	self.west_degrees = 'N/A';
            }
            if(self.north_degrees == null) {
            	self.north_degrees = 'N/A';
            }
            if(self.south_degrees == null) {
            	self.south_degrees = 'N/A';
            }
            if(self.description == null) {
            	self.description = 'N/A';
            }
            if(self.keywords == null) {
            	self.keywords = 'N/A';
            }
            if(projectsText == null) {
            	projectsText = 'N/A';
            }
        	return self;
        },
        */
        
    /*
    writeToHTML: function () {
        var self = this;
        var keywordsText = '';
        //add keywords to the page
        if(self.keywords != null && self.keywords != '')
        {
            for(var i = 0;i<self.keywords.length;i++) {
                if(i == self.keywords.length-1) {
                    keywordsText += self.keywords[i] + ' (' + self.keywords[i] + ')';
                }
                else {
                    keywordsText += self.keywords[i] + ' (' + self.keywords[i] + '), ';
                }
            }
            $('div#keywords_metadata').after('<div class="addedMetadata"><p>' + keywordsText + '</p></div>');
         }
		$('div#abstract_metadata').after('<div class="addedMetadata"><p>' + self.description + '</p></div>');
		//add title and constraints to the page
		$('div#metadata_summary_dataset').after('<div class="addedMetadataTitle">' + 'Dataset: ' + self.title);
		

		
		
		//add investigators to the page
		$('div#investigator_metadata').after('<div class="addedMetadata"><p>' + self.invesigators + '</p></div>');
		
		//add contact information to the page
		$('div#contact_metadata').after('<div class="addedMetadata"><p>' + self.contact + '</p></div>');
		
		//add start and stop times to the page
		$('div#time_metadata').after('<div class="addedMetadata"><p>Begin: ' + self.startTime + ' End: ' + self.stopTime + '</p></div>');
		
		//add geospatial info to the page
		$('div#geospatial_metadata').after('<div class="addedMetadata"><p>' + 'coordinates (N,W,S,E):<br />(' + self.north_degrees + ',' + self.west_degrees + ',' + self.south_degrees + ',' + self.east_degrees + ')</p></div>');
		//self.display_meta_map(west_degreesText,east_degreesText,north_degreesText,south_degreesText);
		self.display_meta_map();
	},
	*/
        afterRequest: function () {
            var self = this;
            //need to attempt to phase this function out
            $("a.met").click(function () {
                var idStr = $(this).parent().find("a").attr("id");
                self.globalRecordId = idStr;
                self.metadatafileformat = $(this).parent().find("a").attr("format");
                self.metadatafilename = $(this).parent().find("a").attr("metadata_url");
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
                },//end onBeforeLoad
                onLoad: function() {
                    $(".scrollable").scrollable({ vertical: true, mousewheel: true });	
                    self.metadata_report();
                    $(".overlay_header").show();
                    $(".overlay_content").show();
                    $(".overlay_footer").show();
                    $(".overlay_border").show();
                },//end onLoad
                onClose: function() {
                    $(".overlay_header").hide();
                    $(".overlay_content").hide();
                    $(".overlay_footer").hide();
                    $(".overlay_border").hide();
                }//end onClose
            });
        }
    });

}(jQuery));













/*
processCAS: function(record){
	var self = this;
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
	self.display_meta_map(west_degreesText,east_degreesText,north_degreesText,south_degreesText);
	
	//Keywords
	$('div#keywords_metadata').after('<div class="addedMetadata"><p>' + ' N/A ' + '</p></div>');
	
	// Abstract 
	//var abstractText = record.catalog.dataset.name;
	$('div#abstract_metadata').after('<div class="addedMetadata"><p>' + ' N/A ' + '</p></div>');
	
}, //end processCAS
*/












/*
processFGDC: function(record){
	var self = this;
	
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
	self.display_meta_map(west_degreesText,east_degreesText,north_degreesText,south_degreesText);
	
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
	
}, //end processFGDC
*/


















