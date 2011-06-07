/*****************************************************************************
 * Copyright © 2011 , UT-Battelle, LLC All rights reserved
 *
 * OPEN SOURCE LICENSE
 *
 * Subject to the conditions of this License, UT-Battelle, LLC (the
 * “Licensor”) hereby grants to any person (the “Licensee”) obtaining a copy
 * of this software and associated documentation files (the "Software"), a
 * perpetual, worldwide, non-exclusive, irrevocable copyright license to use,
 * copy, modify, merge, publish, distribute, and/or sublicense copies of the
 * Software.
 *
 * 1. Redistributions of Software must retain the above open source license
 * grant, copyright and license notices, this list of conditions, and the
 * disclaimer listed below.  Changes or modifications to, or derivative works
 * of the Software must be noted with comments and the contributor and
 * organization’s name.  If the Software is protected by a proprietary
 * trademark owned by Licensor or the Department of Energy, then derivative
 * works of the Software may not be distributed using the trademark without
 * the prior written approval of the trademark owner.
 *
 * 2. Neither the names of Licensor nor the Department of Energy may be used
 * to endorse or promote products derived from this Software without their
 * specific prior written permission.
 *
 * 3. The Software, with or without modification, must include the following
 * acknowledgment:
 *
 *    "This product includes software produced by UT-Battelle, LLC under
 *    Contract No. DE-AC05-00OR22725 with the Department of Energy.”
 *
 * 4. Licensee is authorized to commercialize its derivative works of the
 * Software.  All derivative works of the Software must include paragraphs 1,
 * 2, and 3 above, and the DISCLAIMER below.
 *
 *
 * DISCLAIMER
 *
 * UT-Battelle, LLC AND THE GOVERNMENT MAKE NO REPRESENTATIONS AND DISCLAIM
 * ALL WARRANTIES, BOTH EXPRESSED AND IMPLIED.  THERE ARE NO EXPRESS OR
 * IMPLIED WARRANTIES OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE,
 * OR THAT THE USE OF THE SOFTWARE WILL NOT INFRINGE ANY PATENT, COPYRIGHT,
 * TRADEMARK, OR OTHER PROPRIETARY RIGHTS, OR THAT THE SOFTWARE WILL
 * ACCOMPLISH THE INTENDED RESULTS OR THAT THE SOFTWARE OR ITS USE WILL NOT
 * RESULT IN INJURY OR DAMAGE.  The user assumes responsibility for all
 * liabilities, penalties, fines, claims, causes of action, and costs and
 * expenses, caused by, resulting from or arising out of, in whole or in part
 * the use, storage or disposal of the SOFTWARE.
 *
 *
 ******************************************************************************/

/**
 * Metadata component for ESG
 *
 * fwang2@ornl.gov
 * harneyjf@ornl.gov
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
	     * All metadata params should be stored in ONE structure (an array?)
	     */
        metadataArray: null,
    	
    	
        
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
                //alert($(this).parent().find("a").attr("href"));
            });
            $(".ai_meta a[rel]").overlay({
                //mask: 'darkred',
                mask: {opacity: 0.5, color: '#000'},
                effect: 'apple',
                top: '2%',
                left: '20%',
                onBeforeLoad: function() {
                	
                	LOG.debug("In Metadata - onBeforeLoad");
                	/*
                    $('.apple_overlay').css({'width' : '720px'});
                    var wrap = this.getOverlay().find(".contentWrap");
				    wrap.load(this.getTrigger().attr("href"));
				    */
                	
                },//end onBeforeLoad
                onLoad: function() {
                	LOG.debug("In Metadata - onLoad");
                	alert('Metadata summary disabled');
                	/*
                    $(".scrollable").scrollable({ vertical: true, mousewheel: true });	
                    

                    //find the appropriate document in the solr index
                    var doc = self.findDoc(self.globalRecordId);
                    
                    //generate the metadata report
                    //self.metadata_report(doc);
                    
                    //show the overlay
                    $(".overlay_header").show();
                    $(".overlay_header_buttons").hide(); 
                    $(".overlay_content").show();
                    $(".overlay_footer").show();
                    $(".overlay_border").show();
                    */
                },//end onLoad
                onClose: function() {
                	LOG.debug("In Metadata - on close")
                	
                	/*
                	//rehide the overlay
                    $(".overlay_header").hide();
                    $(".overlay_content").hide();
                    $(".overlay_header_buttons").hide();    
                    $(".overlay_footer").hide();
                    $(".overlay_border").hide();
                    */
                }//end onClose
            });
        },
        
        
        /*
         * Function metadata_report takes a metadata document, extracts relevant metadata and places them in a templating structure for the summary
         */
        metadata_report: function(doc) {
            var self = this;
            
            var metatdata_url = '/esgf-web-fe/metadataproxy';
            
            //We need another separate controller for getting the metadata for tds?
            	
            if(self.metadatafileformat == 'THREDDS') {
            	//metatdata_url = 'http://localhost:8080/esgf-web-fe/threddsproxy';
            }
            
            //For solr output, all the metadata is included as they are stored in the index
            //However, there should be some controller that determines what storage device is being used
            //and returned in a specific format that the metadata summary template can read
            jQuery.ajax({
                url: metatdata_url,
                data: doc,
                type: 'GET',
                //on success process the metadata record
                success: function(record) {self.processMetadataRecord(record,doc);},
                error: function() {alert("error http://localhost:8080/esgf-web-fe/metadataproxy");}
                //dataType: 'json'
            }); 
            
        },
        
        
        /*
         * Processes the metadata from both solr and the raw xml file
         */
        processMetadataRecord: function(record,doc) {
        	alert('process metadata record here for ' + doc.id);

            var self = this;
            $('.addedMetadata').remove();
            $('.addedMetadataTitle').remove();
            
            //self.processUsingTemplate(record,doc);
            
            //self.processUsingHtml(record,doc);  
            
            
            
            
        }, //end processMetadataRecord
        
        
        processUsingTemplate: function(record,doc) {
        	for (var property in doc) {
        		$('.m_items').append('<div class="m_item"><div class="leftsd">' + property + '</div><div>' + doc[property] + '</div></div>' );
        	}
        	
        },
        
        processUsingHtml: function(record,doc) {
        	var self = this;
        	
        	for (var property in doc) {
            	//alert('property: ' + property + " val: " + doc[property]);
                //output += property + ': ' + doc[property]+'; ';
              
                //title
            	if(property == 'title') { 
            		$('div#metadata_summary_dataset').after('<div class="addedMetadataTitle">' + 'Dataset: ' + doc['title']);
            	}
            	
            	//project(s)
            	if(property == 'project') {
            	    $('div#projects_metadata').after('<div class="addedMetadata"><p>' + doc['project'] + '</p></div>');
            	}
            	
            	//temporal
            	if(property == 'datetime_start') {
            		//temporal
                    $('div#time_metadata').after('<div class="addedMetadata"><p>Begin: ' + doc.datetime_start + ' End: ' + doc.datetime_stop + '</p></div>');

            	}
            	
            	//geospatial
            	if(property == 'east_degrees') {
            		//temporal
                    $('div#geospatial_metadata').after('<div class="addedMetadata"><p>' + 'coordinates (N,W,S,E):<br />(' + doc.north_degrees + ',' + doc.west_degrees + ',' + doc.south_degrees + ',' + doc.east_degrees + ')</p></div>');
                    
                    self.display_meta_map(doc.north_degrees, doc.south_degrees, doc.west_degrees, doc.east_degrees);
            	}
            	
            	//keywords
            	if(property == 'misc_keywords') {
                    $('div#keywords_metadata').after('<div class="addedMetadata"><p>' + doc.misc_keywords + '</p></div>');
            	}
            	
            	//description
            	if(property == 'description') {
                    //abstract/description
                    $('div#abstract_metadata').after('<div class="addedMetadata"><p>' + doc.description + '</p></div>');

            	}
            	
            }
        	
        	
        },
               
        
        
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
        //
        //The better way is to identify the id of the click event
        //Will do this later
        findDoc: function (docId) {
        	for (i = 0, l = this.manager.response.response.docs.length; i < l; i++) {
                var doc = this.manager.response.response.docs[i];
                
                if(doc.id == docId) {
                	return doc;
                }
        	}
        	return null;
        },
        
        
        display_meta_map: function (north_degrees, south_degrees, west_degrees, east_degrees) {
            var self = this;
            var i = null;
            var bounds = new google.maps.LatLngBounds();
            var paths = [ ];
            /*
            var ne = new google.maps.LatLng(self.searchable_north_degrees,self.searchable_east_degrees);
            var nw = new google.maps.LatLng(self.searchable_north_degrees,self.searchable_west_degrees);
            var se = new google.maps.LatLng(self.searchable_south_degrees,self.searchable_east_degrees);
            var sw = new google.maps.LatLng(self.searchable_south_degrees,self.searchable_west_degrees);
            */
            var ne = new google.maps.LatLng(north_degrees,east_degrees);
            var nw = new google.maps.LatLng(north_degrees,west_degrees);
            var se = new google.maps.LatLng(south_degrees,east_degrees);
            var sw = new google.maps.LatLng(south_degrees,west_degrees);
            
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
processCAS: function(record) {
	var self = this;
    
	 //
     //Need to grab the content from the xml/controller/RESTservice here
     //
  

    //write all the params to HTML
    AjaxSolr.theme('metadata',self);
},
*/


/*
processFGDC: function(record) {
	var self = this;
    
	 
     * Need to grab the content from the xml/controller/RESTservice here
     
  
    //write all the params to HTML
    AjaxSolr.theme('metadata',self);
},
*/



/*
processOAI: function(record) {
    var self = this;
    
    //
   // Need to grab the content from the xml/controller/RESTservice here
    //
  
    
    //
    //Temporary adjustments of params
    //
    //
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
    
    
    //write all the params to HTML
    AjaxSolr.theme('metadata',self);
    
    
}, //end processOAI
*/


/*
processTHREDDS: function(record) {
	var self = this;
    
	 
     * Need to grab the content from the xml/controller/RESTservice here
     
  
    AjaxSolr.theme('metadata',self);
},//end processTHREDDS
*/


/* old process metadata record logic


//remove all previously "added metadata" and its title ahead of time 


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

//
// Need to grab the searchable content from solr here
//
//loop over the searchable array getting either a 'N/A' string or the actual string
self.map(self.createResultingStringArrayForSolrFields, searchable_arr, doc);


//
 // Need to grab the facetable content from solr here
 //
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
*/



/*
 * Solr searchable properties
 */
/*
searchable_id: 'searchable',
searchable_title: 'searchable',
searchable_description: 'searchable',
searchable_datetime_start: 'searchable',
searchable_datetime_stop: 'searchable',
searchable_north_degrees: 'searchable',
searchable_east_degrees: 'searchable',
searchable_south_degrees: 'searchable',
searchable_west_degrees: 'searchable',
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


 * Solr faceted properties
 

facet_project: 'facet',
facet_instrument: 'facet',
facet_variable: 'facet',
facet_cf_variable: 'facet',
facet_gcmd_variable: 'facet',



 * Misc properties - properties directly from the metadata file
 

misc_level: 'Misc',
misc_frequency: 'Misc',
misc_mission: 'Misc',
misc_product: 'Misc',
misc_realm: 'Misc',
misc_keywords: 'Misc',
misc_investigators: 'Misc',
misc_contactinfo: 'Misc',



 * Reinitialize params
 * This is a necessary evil - I need to change the logic behind this ASAP
 
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
	
	
	self.facet_project = 'facet';
	self.facet_instrument = 'facet';
	self.facet_variable = 'facet';
	self.facet_cf_variable = 'facet';
	self.facet_gcmd_variable = 'facet';
	
	self.misc_level = 'N/A';
	self.misc_frequency = 'N/A';
	self.misc_mission = 'N/A';
	self.misc_product = 'N/A';
	self.misc_realm = 'N/A';
	self.misc_keywords = 'N/A';
	self.misc_investigators = 'N/A';
	self.misc_contactinfo = 'N/A';
	
},
*/
