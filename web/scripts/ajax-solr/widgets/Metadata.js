/**
 * 
 */
(function ($) {
    AjaxSolr.MetadataWidget = AjaxSolr.AbstractWidget.extend({
    	
    	/*
    	 * Need these properties to extract the metadata file from the system
    	 */
        globalRecordId: '',
	    metadatafileformat: '',
	    metadatafilename: '',
	    
        
    	
    	/*
    	 * Solr searchable properties
    	 */
    	searchable_id: 'searchable',
    	searchable_title: 'searchable',
    	searchable_description: 'searchable',
    	searchable_datetime_start: 'searchable',
    	searchable_datetime_stop: 'searchable',
    	searchable_north_degrees: 'searchable',
    	searchable_east_degrees: 'searchable',
    	searchable_south_degrees: 'searchable',
    	searchable_west_degrees: 'searchable',
    	/*
    	searchable_url: 'searchable',
    	searchable_type: 'searchable',
    	searchable_version: 'searchable',
    	searchable_source_url: 'searchable',
    	searchable_timestamp: 'searchable',
    	searchable_metadata_format: 'searchable',
    	searchable_metadata_url: 'searchable',
    	searchable_metadata_file_name: 'searchable',
    	searchable_file_id: 'searchable',
    	searchable_file_url: 'searchable',
    	searchable_size: 'searchable',
    	*/
    	
    	/*
    	 * Solr faceted properties
    	 */
    	facet_project: 'facet',
    	facet_instrument: 'facet',
    	facet_variable: 'facet',
    	facet_cf_variable: 'facet',
    	facet_gcmd_variable: 'facet',
    	
    	
    	/*
    	 * Misc properties - properties directly from the metadata file
    	 */
    	/*
    	misc_level: 'Misc',
    	misc_frequency: 'Misc',
    	misc_mission: 'Misc',
    	misc_product: 'Misc',
    	misc_realm: 'Misc',
    	*/
    	misc_keywords: 'Misc',
    	misc_investigators: 'Misc',
    	misc_contactinfo: 'Misc',
    	
    	
    	/*
    	 * Reinitialize params
    	 * This is a necessary evil - I need to change the logic behind this ASAP
    	 */
    	initializeMetadataParams: function () {
    		var self = this;
    		self.searchable_id = 'searchable';
    		self.searchable_title = 'searchable';
    		self.searchable_description = 'searchable';
    		self.searchable_datetime_start = 'searchable';
    		self.searchable_datetime_stop = 'searchable';
    		self.searchable_north_degrees = 'searchable';
    		self.searchable_east_degrees = 'searchable';
    		self.searchable_south_degrees = 'searchable';
    		self.searchable_west_degrees = 'searchable';
    		
    		/*
    		self.searchable_url = 'searchable';
    		self.searchable_type = 'searchable';
    		self.searchable_version = 'searchable';
    		self.searchable_source_url = 'searchable';
    		self.searchable_timestamp = 'searchable';
    		self.searchable_metadata_format = 'searchable';
    		self.searchable_metadata_url = 'searchable';
    		self.searchable_metadata_file_name = 'searchable';
    		self.searchable_file_id = 'searchable';
    		self.searchable_file_url = 'searchable';
    		self.searchable_size = 'searchable';
    		*/
    		
    		self.facet_project = 'facet';
    		self.facet_instrument = 'facet';
    		self.facet_variable = 'facet';
    		self.facet_cf_variable = 'facet';
    		self.facet_gcmd_variable = 'facet';
    		
    		/*
    		self.misc_level = 'N/A';
    		self.misc_frequency = 'N/A';
    		self.misc_mission = 'N/A';
    		self.misc_product = 'N/A';
    		self.misc_realm = 'N/A';
    		self.misc_keywords = 'N/A';
        	*/
        	self.misc_investigators = 'N/A';
        	self.misc_contactinfo = 'N/A';
    	},
    	
    	/*
    	 * Event listener
    	 */
    	afterRequest: function () {
            var self = this;
            //need to attempt to phase this function out
            $("a.met").click(function () {
                self.globalRecordId = $(this).parent().find("a").attr("id");
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
                    

                    //find the appropriate document in the solr index
                    var doc = self.findDoc(self.globalRecordId);
                    
                    
                    //generate the metadata report
                    self.metadata_report(doc);
                    
                    //reinitialize the params so they be processed for different 
                    self.initializeMetadataParams();
                    
                    
                    //show the overlay
                    $(".overlay_header").show();
                    $(".overlay_content").show();
                    $(".overlay_footer").show();
                    $(".overlay_border").show();
                },//end onLoad
                onClose: function() {
                	//rehide the overlay
                    $(".overlay_header").hide();
                    $(".overlay_content").hide();
                    $(".overlay_footer").hide();
                    $(".overlay_border").hide();
                }//end onClose
            });
        },
        
        
        /*
         * This is the entry point in the metadata.js
         */
        metadata_report: function(doc) {
            var self = this;
            
            //find the metadata file and get a json object representing the entire record for the one dataset
                jQuery.ajax({
                    url: 'http://localhost:8080/esgf-web-fe/metadataproxy',
                    data: 'metadataformat=' + self.metadatafileformat + '&metadatafile=' + self.metadatafilename + '&id=' + self.globalRecordId,
                    type: 'GET',
                    //on success process the metadata record
                    success: function(record) {self.processMetadataRecord(record,doc);},
                    error: function() {alert("error http://localhost:8080/esgf-web-fe/metadataproxy");},
                    dataType: 'json'
                }); 
        },
        
        
        /*
         * Processes the metadata from both solr and the raw xml file
         */
        processMetadataRecord: function(record,doc) {
            var self = this;
            
            //remove all previously "added metadata" and its title ahead of time 
            $('.addedMetadata').remove();
            $('.addedMetadataTitle').remove();
            
            var searchable_arr = new Array();
            var facet_arr = new Array();
            var miscellaneous_arr = new Array();
            for(att in self){
            	if(self[att] == 'searchable') {
            		searchable_arr.push(att);
                } else if(self[att] == 'facet') {
                    facet_arr.push(att);
                }
        	}
            
            /*
             * Need to grab the searchable content from solr here
             */
            //loop over the searchable array getting either a 'N/A' string or the actual string
            self.map(self.createResultingStringArrayForSolrFields, searchable_arr, doc);
            
            
            /*
             * Need to grab the facetable content from solr here
             */
            self.map(self.createResultingStringArrayForSolrFields, facet_arr, doc);
            
            
            //branch logic depending on the metadata file format
            if(self.metadatafileformat === 'OAI') {
                self.processOAI(record);
            }
            else if(self.metadatafileformat === 'FGDC') {
                self.processFGDC(record);
            }
            else if(self.metadatafileformat === 'CAS') {
                self.processCAS(record);
            }
            else{ //thredds
                self.processTHREDDS(record);
            }
        }, //end processMetadataRecord
        
        
        
                
        processOAI: function(record) {
            var self = this;
            
            /*
             * Need to grab the content from the xml/controller/RESTservice here
             */
          
            
            /*
             * Temporary adjustments of params
             */
            /*
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
            */
            
            //write all the params to HTML
            AjaxSolr.theme('metadata',self);
            
            
        }, //end processOAI
        
        processCAS: function(record) {
        	var self = this;
            
        	 /*
             * Need to grab the content from the xml/controller/RESTservice here
             */
          

            //write all the params to HTML
            AjaxSolr.theme('metadata',self);
        },
        processFGDC: function(record) {
        	var self = this;
            
        	 /*
             * Need to grab the content from the xml/controller/RESTservice here
             */
          
            //write all the params to HTML
            AjaxSolr.theme('metadata',self);
        },
        
        processTHREDDS: function(record) {
        	var self = this;
            
        	 /*
             * Need to grab the content from the xml/controller/RESTservice here
             */
          
            AjaxSolr.theme('metadata',self);
        },//end processTHREDDS
        
        
        /*
         * Helper methods for bulk array processing (similar to map reduce)
         */
        forEach: function(arrFields, action) {
        	for(var i=0;i<arrFields.length;i++) {
            	action(arrFields[i]);
        	}
        },
        
        map: function(func, array, doc) {
        	var self = this;
        	var result = [];
        	self.forEach(array, function(element){
        		result.push(func(element,doc,self));
        	});
        	return result;
        },
        
        createResultingStringArrayForSolrFields: function(element,doc,self) {
        	var found = false;
        	for(var att in doc) {
        		if(att == element.slice(11)) {
        			found = true;
        			//alert('element: ' + element + ' found' + ' replace ' + self[element] + ' with: ' + doc[att]);
        			self[element] = doc[att];
        		}
        	}
        	if(!found) {
        		self[element] = 'N/A';
        	} 
        	
        	
        },
        
        
        
        
   
        
        //method that finds a doc based on an id
        //very slow linear search - need a better way to do this
        findDoc: function (docId) {
        	for (i = 0, l = this.manager.response.response.docs.length; i < l; i++) {
                var doc = this.manager.response.response.docs[i];
                
                if(doc.id == docId) {
                	return doc;
                }
        	}
        	return null;
        },
        
        
        display_meta_map: function () {
            var self = this;
            var i = null;
            var bounds = new google.maps.LatLngBounds();
            var paths = [ ];
            var ne = new google.maps.LatLng(self.searchable_north_degrees,self.searchable_east_degrees);
            var nw = new google.maps.LatLng(self.searchable_north_degrees,self.searchable_west_degrees);
            var se = new google.maps.LatLng(self.searchable_south_degrees,self.searchable_east_degrees);
            var sw = new google.maps.LatLng(self.searchable_south_degrees,self.searchable_west_degrees);
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
        } //end displayMetaMap
        
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










/* this is the old configuration of properties */
/*
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
size: null,
instrument: null,
level: null,
mission: null,
product: null,
realm: null,
variable: new Array(),
*/





/* OLD thredds implementation
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
*/






/* old OAI implementation
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







