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
    AjaxSolr.MetadataWidget = AjaxSolr.AbstractOverlayWidget.extend({
    	
    	/** 
    	   * The Id of the dataset for which the metadata report represents
    	   * 
    	   * @field 
    	   * @public
    	   * @type String
    	   */
        globalRecordId: '',
	    
        /**
         * All events regarding the metadata display are placed here.
         *
         * <p>This method is executed after the Solr response is received.</p>
         */
    	afterRequest: function () {
            var self = this;
            //need to attempt to phase this function out
            $("a.met").click(function () {
                self.globalRecordId = $(this).parent().find("a").attr("id");
                
            });
            
            $(".ai_meta a[rel]").overlay({
                mask: {opacity: 0.5, color: '#000'},
                effect: 'apple',
                top: '2%',
                left: '20%',
                onBeforeLoad: function() {
                	
                	LOG.debug("In Metadata - onBeforeLoad");
                	
                	//style the apple overlay to a bigger width and height
                    $('.apple_overlay').css({'width' : '750px'},{'height':'700px'});
                    
                    //install the trigger
                    var wrap = this.getOverlay().find(".contentWrap");
				    wrap.load(this.getTrigger().attr("href"));
				    
                	
                },//end onBeforeLoad
                
                onLoad: function() {
                	LOG.debug("In Metadata - onLoad");
                	
                	//find the appropriate document in the solr index
                	//note this HAS to be changed to a more efficient way to grab this data
                    var doc = self.findDoc(self.globalRecordId);
                	
                    self.metadata_report(doc);
                    
                    self.showOverlay();
                    
                    
                },//end onLoad
                onClose: function() {
                	LOG.debug("In Metadata - on close");
                	
                	self.hideOverlay();
                	
                	//rehide the overlay
                    self.hideOverlay();
                }//end onClose
            });
        },
        
        /**
         * method that finds a doc based on an id
         *
         * <p>very slow linear search - need a better way to do this</p>
         */
        findDoc: function (docId) {
        	for (i = 0, l = this.manager.response.response.docs.length; i < l; i++) {
                var doc = this.manager.response.response.docs[i];
                
                if(doc.id == docId) {
                	return doc;
                }
        	}
        	return null;
        },
        
        
        
        
        /**
         * Function metadata_report takes a metadata document, extracts relevant metadata and places them in a templating structure for the summary
         * 
         * <p></p>
         */
        metadata_report: function(doc) {
            var self = this;
            
            //For solr output, all the metadata is included as they are stored in the index
            //However, there should be some controller that determines what storage device is being used
            //and returned in a specific format that the metadata summary template can read
            
            self.processMetadataRecord(doc);
            
        },
        
        
        /**
         * Processes the metadata record
         * 
         * 
         */
        processMetadataRecord: function(doc) {
            var self = this;
            $('.addedMetadata').remove();
            $('.addedMetadataTitle').remove();
            
            //should probably use a closure here
         	self.processHtml(doc);
            
            
        }, //end processMetadataRecord
        
        
        /**
         * Processes the metadata record as html (helper method)
         * 
         * 
         */
        processHtml: function(doc) {
        	
        	//add the title first 
    		$('div#metadata_summary_dataset').after('<div class="addedMetadataTitle">' + 'Dataset: ' + doc['title']);
        	var degreeCount = 0;
        	var self = this;
        	
    		for (var property in doc) {
        		
        		//only do this if it is NOT the title 
        		if(property != 'title') {
        			
        			var field = doc[property];
            		
        			$('.m_items').append('<div class="m_item" id="' + property + '"></div>');
            		
            		/*
            		 * Property output
            		 */
            		//special case for variables
            		if(property != 'text') {//don't want to display random text in report
            			self.processMetadataVariables(property);
            		}
            		
            		
            		/*
            		 * Property value output
            		 */
            		//if the value is a "geo" property, it must be handled differently
            		if(property.search("degrees") != -1) {
            			degreeCount = degreeCount + 1;
            			
            			//degree count of 4 means that we can place the 
            			if(degreeCount == 4) {
            				
            				self.processMetadataGeo(property,field,doc);
            				
            				
            				
            			}
            		} else if (property != 'text'){ //don't want to display random text
            			
            			//if the value is an array it must be handled differently, as browser will view it as one giant string
            			//otherwise, just display the field on the right side
                		if(field instanceof Array)  {
                			self.processMetadataArrays(property,field);
                		} else {
                			$('#'+property).append('<div class="rightsd">' + field + '</div>');
                    		
                		}
            		}
            		
        		}//end if property != title
        	}
    		
    		//add border
    		$('.overlay_footer').before('<div class="overlay_border"></div>');
    		
    		
        },
        
        
        /**
         * 
         * 
         * 
         */
        processMetadataArrays: function (property,field) {
        	var str = '';
			for(var i=0;i<field.length;i++) {
				if(field.length > 1 && i < field.length-1) {
    				str += field[i] + ', ';
				} else {
					str += field[i];
				}
			}
    		$('#'+property).append('<div class="rightsd">' + str + '</div>');
        },
        
        processMetadataVariables: function(property) {
        	if(property == 'variable') {
        		$('#'+property).append('<div class="leftsd">' + property + '(s):</div>');
    		} else if (property.search("degrees") == -1) {
        		$('#'+property).append('<div class="leftsd">' + property + ':</div>');
    		}
        },
        
        
        processMetadataGeo: function (property,field,doc) {
        	/*
        	$('#'+property).append('<div class="leftsd">' + 'Geospatial Information' + ':</div>');
			
			var str = '<span style="padding-bottom:10px;font-weight:bold">Bounding Coordinates</span><br /><span style="padding-right:50px;">West degree: ' + doc['west_degrees'] + ' &deg;</span>  East degree: ' + doc['east_degrees'] +  
			          ' &deg;<br /><span style="padding-right:50px;">South degree: ' + doc['south_degrees'] +  ' &deg;</span>North degree: ' + doc['north_degrees'] + ' &deg;';
			
			$('#'+property).append('<div class="rightsd">' + str + '<div id="geo_map">' + 'map her' + '</div></div>');
			
			self.display_meta_map(doc['north_degrees'], doc['south_degrees'], doc['west_degrees'], doc['east_degrees']);
			*/
        },
        
   
        
        display_meta_map: function (north_degrees, south_degrees, west_degrees, east_degrees) {
            var self = this;
            var i = null;
            var bounds = new google.maps.LatLngBounds();
            var paths = [ ];
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



