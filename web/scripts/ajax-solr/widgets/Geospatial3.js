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
 * Geospatial Component for ESG
 *
 * fwang2@ornl.gov
 * harneyjf@ornl.gov
 */


(function ($) {
    AjaxSolr.GeospatialSearchWidget = AjaxSolr.AbstractGeospatialWidget.extend({

    	map: null,
        geocoder: null,
        infowindow: null,
        bounds: null,
        cbounds: new google.maps.LatLngBounds(),
        num_of_markers: 0,
        max_of_markers: 3,
        markerGroup: new Array(this.max_of_markers),
        poly: null,
        
        boundingboxND: 0,
        boundingboxSD: 0,
        boudningboxED: 0,
        boundingboxWD: 0,
        centroidCenter: 0,
        centroidRadius: 0,
        
        /**
         * 
         */
        beforeRequest: function () {
    		
        	
        },
        
        
        /**
         * 
         */
        afterRequest: function () {
        	
        	var self = this;
            var i = null;
            
            
    		$('a#geospatial').click(function() {
                
            	$( "#geodialog" ).dialog({
            		width: 700,
            		maxWidth: 700,
            		height: 500,
            		maxHeight: 550,
            		show: 'blind',
            		
            		
            		open: function() {
            			
            			self.clearMarkers();

                        self.clearAreaChoice();
                        

                        //display a fresh map
                        self.display_map();
                        
            		}
                    
            		
            	});
    		});
    		
    		
    		$('button#submitGeo').live('click', function(){
            	
    			alert('here?');
    			
            	//execute the geospatial query
                if($("input[name='searchType']:checked").val() !== null && $("input[name='areaGroup']:checked").val() !== null) {
                    
                	//erase any previous geospatial request
                    var fq = Manager.store.values('fq');
                    for (i = 0, l = fq.length; i < l; i++) {
                        //any previous filter query that contains 'east_degrees' can be assumed to be a geo search
                        if(fq[i].search('east_degrees') !== -1) {
                            Manager.store.removeByValue('fq', fq[i]);
                        }
                    }

                    self.executeGeospatialQuery();
                    Manager.doRequest(0);					
                
                } else {
                    alert('A Geospatial search type must be selected');
                }
            	
            	
            });
            
            //clear markers event
            $('#geospatial_marker_fieldset input[name="clear_markers"]').live('click',function(e) {
                self.clearMarkers();
                self.clearAreaChoice();
            });
            
            //this event will be taken out soon 
            //right now it acts as a guard so that the user cannot perform an overlaps query with a radius search (unimplemented)
            $("input[name='searchType']").live('change',function(e) {
                var searchType = $("input[name='searchType']").val();
            });
            
            //location text box event handler
            $("#geospatial_location input[name='location']").live('keypress',function(e) {
                if (e.keyCode === 13) {
                    var address = $(this).val();
                    self.getCoordinates(address);
                    // clear the field
                    $(this).val('');
                    return false;
                }
            });
            
            
            $("input[name='areaGroup']").live('change',function(e) {
                if ($("input[name='areaGroup']:checked").val() === 'square') {
                    if (self.num_of_markers < 2) {
                        alert('Please define at least two markers');
                        $(this).attr('checked', false);
                        return false;
                    }
                    self.getBoundingBox();
                } else {
                    if (self.num_of_markers !== 1) {
                        alert("Please define ONE marker, center of interest!");
                        $(this).attr('checked', false);
                        return false;
                    }
                    if ($("#geospatial_circleInputs").is(":hidden")) {
                        $("#geospatial_circleInputs").slideToggle('fast');
                    }
                    
                    // put out something
                    $("#geospatial_areaSelected").html('<p class="legend"> Center of Interest </p> + ' 
                        + self.markerGroup[0].getPosition().toString());
                    if ($("#areaSelected1").is(":hidden")) {
                        $("#areaSelected1").slideToggle('fast');
                    }
                    self.redraw_circle();
                }		
            });
            
            $('input[name="redraw_circle"]').live('click',function(e) {
                self.redraw_circle();
            });
            
    		
    		
        },
        
        /**
         * Clears all the markers off the google map
         */
	    clearMarkers: function () {
		    var i = null;
		    var self = this;
            for (i=0; i < self.max_of_markers; i++) {
                if (self.markerGroup[i]) {
                    self.markerGroup[i].setMap(null);
                    self.markerGroup[i] = null;
                }
            }
            self.num_of_markers = 0;
            if (self.infowindow){
                self.infowindow.close();
            }
            if (self.poly) {
                self.poly.setMap(null);
		    }
            $("#geospatial_markers").html("");
            $("#geospatial_areaSelected").html("");
            self.clearAreaChoice();
	    },
	    
	    /**
	     * Clears the radio buttons of the google map
	     * 
	     */
	    clearAreaChoice: function() {
		    $("input:radio").attr('checked', false);
	    },
	    
	    /**
	     * 
	     * @param location
	     */
        placeMarker: function(location) {
            var self = this;
            var marker = self.addMarker(location);
            if (marker !== null ) {
		        self.appendMarker(marker,  self.num_of_markers);
	        }
        },
        
        /**
         * 
         */
        display_map: function() {
		    var self = this;
            var mapDiv = document.getElementById('geospatial_map_canvas');
            var latlng = new google.maps.LatLng(38.09, -95.71);
            var options = {
                center: latlng,
                zoom: 4,
                mapTypeId: google.maps.MapTypeId.ROADMAP
            };
            // check to see if we already have a map object
            //if (!map) {
                self.map = new google.maps.Map(mapDiv, options);
            //}
            var location = new google.maps.LatLng(30,40);
            // create a new marker
            var marker = new google.maps.Marker({
                draggable: true,
                position: location,
                map: self.map
            });
            google.maps.event.addListener(self.map, 'click', function(e) {
                if (self.poly) {
                    self.poly.setMap(null);
                }
                self.clearAreaChoice();
                self.placeMarker(e.latLng);
            });
        },
        
        /**
         * 
         * @param address
         */
        getCoordinates: function(address) {
            var self = this;
            if (!self.geocoder) {
                self.geocoder = new google.maps.Geocoder();
            }
            // Creating GeocoderRequest object
            var geocoderRequest = {
                address: address
            };
            // Making the Geocode request
            self.geocoder.geocode(geocoderRequest, function(results, status) {
                // check status
                if (status === google.maps.GeocoderStatus.OK) {
                    // center the map on the returned location
                    self.map.setCenter(results[0].geometry.location);
                }
                var marker = self.addMarker(results[0].geometry.location);
                if (marker !== null) {
                    // check if we have a info window
                    if (!self.infowindow) {
                        self.infowindow = new google.maps.InfoWindow();
                    }
                    // create contents
                    var content = '<div class="infowindow"> <p class="legend">'  
                        + results[0].formatted_address + '</p>';
                    content += "Lat: " + results[0].geometry.location.lat() + '<br />';
                    content += "Lng: " + results[0].geometry.location.lng();
                    content += "</div>";
                    // Adding the content
                    self.infowindow.setContent(content);
                    // Open the infowindows
                    self.infowindow.open(self.map, marker);				
                    // refresh info to panel
                    self.appendMarker(marker,  self.num_of_markers);
                }
            });
        },
        
        /**
         * 
         * @param marker
         * @param i
         */
        appendMarker: function (marker, i) {
            var self = this;
            var content = '[' + i + '] ' + 'lat ' + 
                marker.getPosition().lat().toFixed(2) + ' , ' +
		        'lon ' + 
		        marker.getPosition().lng().toFixed(2) + "<br />";
            $("#geospatial_markers").append(content);
            if ($("#geospatial_markers").is(":hidden")) {
                $("#geospatial_markers").slideToggle('fast');
            }
       },
       
       /**
        * 
        * @param location
        * @returns
        */
       addMarker: function (location) {
            var self = this;
            if (self.num_of_markers < self.max_of_markers) {
                // create a new marker
                var marker = new google.maps.Marker({
                    draggable: true,
                    position: location,
                    map: self.map
                });
                self.markerGroup[self.num_of_markers] = marker;
                self.num_of_markers += 1;
                google.maps.event.addListener(marker, "dragstart", function() {
                    if (self.infowindow) {
                        self.infowindow.close();
                    }
                });
                google.maps.event.addListener(marker, 'dragend', function() {
                    if (self.poly) {
                        self.poly.setMap(null);                	
                    }
                    self.clearAreaChoice();
                    self.refreshMarkers();
                });			
                return marker;
            } else {
                jMsg("warn", "Max markers reached, please clear markers first!", 5000);
                return null;
            }
        },
        
        /**
         * 
         */
        getBoundingBox: function () {
            var self = this;
            var i = null;
            self.bounds = new google.maps.LatLngBounds();
            for (i=0; i < self.num_of_markers; i++) {
                var marker = self.markerGroup[i];
                if (marker){
                    self.bounds.extend(marker.getPosition());
                }
            }
            if (self.infowindow) {
                self.infowindow.close();
            }
            // draw rectangle
            self.draw_rect();
            // put out something
            $("#areaSelected1").html('<p class="legend"> Bounding Box </p> + ' 
                + self.bounds.toString());
            if ($("#areaSelected1").is(":hidden")) {
                $("#areaSelected1").slideToggle('fast');
            }
        },
        
        /**
         * 
         */
        draw_rect: function() {
            var self = this;
            var ne = self.bounds.getNorthEast();
            var sw = self.bounds.getSouthWest();
            // need to double check on this
            var se = new google.maps.LatLng(sw.lat(), ne.lng());
            var nw = new google.maps.LatLng(ne.lat(), sw.lng());
            var points = [ne, se, sw, nw];
            self.poly = new google.maps.Polygon({
                paths: points,
                fillColor: "#0055ff",
                fillOpacity: 0.35,
                strokeWeight: 2
            });
            self.poly.setMap(self.map);
            self.map.fitBounds(self.bounds);	
        },
        
        /**
         * 
         */
        redraw_circle: function () {
            var self = this;
            //alert('redraw circle');
            if (self.poly) {
                self.poly.setMap(null);
            }
            self.centroidRadius = $("input[name='radius']").val();
            quality = $("input[name='quality']").val();
            self.drawCircle(self.markerGroup[0], self.centroidRadius, quality);
        },
        
        /**
         * 
         * @param marker
         * @param radius
         * @param nodes
         */
        drawCircle: function (marker, radius, nodes) {
            var self = this;
            var i = null;
            self.centroidCenter = marker.getPosition();  
            var latConv = self.dist(self.centroidCenter, new google.maps.LatLng(self.centroidCenter.lat() + 0.1, self.centroidCenter.lng()))*10;
            var lngConv = self.dist(self.centroidCenter, new google.maps.LatLng(self.centroidCenter.lat(), self.centroidCenter.lng() + 0.1))*10;
            // loop
            var points = [];
            var step = parseInt(360/nodes,10) || 40;
            self.cbounds = new google.maps.LatLngBounds();
        
            for (i = 0; i <= 360; i+= step) {
                var pint = new google.maps.LatLng( self.centroidCenter.lat() + (radius/latConv * Math.cos(i * Math.PI/180)),
                		self.centroidCenter.lng() + (radius/lngConv * Math.sin(i * Math.PI/180)));
                points.push(pint);
                self.cbounds.extend(pint);
            }
            points.push(points[0]); // close circle
            self.poly = new google.maps.Polygon({
                paths: points,
                fillColor: "#0055ff",
                fillOpacity: 0.35,
                strokeWeight: 2
            });
            if (self.infowindow) {
                self.infowindow.close();
            }
            self.poly.setMap(self.map);
            self.map.fitBounds(self.cbounds);
            //since the circle can be resized, Im placing the call to this method here
            //setGeographicRadiusConstraint();
        },
        
        /**
         * 
         * @param p1
         * @param p2
         * @returns
         */
        dist: function (p1, p2) {
            var self = this;
            var R = 6371; // earth's mean radius in km
            var dLat  = self.rad(p2.lat() - p1.lat());
            var dLong = self.rad(p2.lng() - p1.lng());
            var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(self.rad(p1.lat())) * Math.cos(self.rad(p2.lat())) * Math.sin(dLong/2) * Math.sin(dLong/2);
            var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            var d = R * c;
            return d.toFixed(3);
        },
        
//need to space from here down        
        /**
         * 
         */
        rad: function(x) {
        	return x*Math.PI/180;
        },
    
    
        /**
         * 
         */
        executeGeospatialQuery: function () {
    	
	        var self = this;
	        var geoSearchType = $("input[name='searchType']:checked").val();
	        var geoShape = $("input[name='areaGroup']:checked").val();
	
	        if(geoSearchType === 'Encloses') {
	            self.encloses(geoShape);
	        }
	        else {
	            self.overlaps(geoShape);
	        }
        

        },
    
    
    
    /**
     * 
     * @param geoSearchType
     */
    encloses: function (geoSearchType) {
    	
        var self = this;
        var geoQueryString = '';
        var sw = null;
        var ne = null;
        var west_degrees, east_degrees, north_degrees, south_degrees,
        west_degreesFQ, east_degreesFQ, north_degreesFQ, south_degreesFQ; 
        
        // geosearch type is a circle
        if(geoSearchType === 'circle') { 
        	/*
             * Note that the code for bounding box and centroid are the same here
             * The reason is that centroid searches are trimmed after a solr query has been processed
             * It is actually handled in Results
             * This may be changed due to pagination issues
             */
            self.centroidCenter = self.markerGroup[0].getPosition();
        	//sw extreme point of the bounding box
            sw = self.cbounds.getSouthWest();
        	//ne extreme point of the bounding box
            ne = self.cbounds.getNorthEast();
            //west_degrees limit
            self.boundingboxWD = sw.lng();
            //this is the min lat
            //south_degrees limit
            self.boundingboxSD = sw.lat();
            //this is the max long
            //east_degrees limit
            self.boundingboxED = ne.lng();
            //this is the max lat
            //north_degrees limit
            self.boundingboxND = ne.lat();
            
            geoQueryString += self.getEnclosesBBQuery(self.boundingboxSD,self.boundingboxWD,self.boundingboxND,self.boundingboxED);
            
        }else { // geosearch type is a bounding box
        	
        	//sw extreme point of the bounding box
            sw = self.bounds.getSouthWest();
            //ne extreme point of the bounding box
            ne = self.bounds.getNorthEast();

            //get the the extremes for each direction
            self.boundingboxWD = sw.lng();
            self.boundingboxSD = sw.lat();
            self.boundingboxED = ne.lng();
            self.boundingboxND = ne.lat();

            var searchAPIQueryStr = 'bbox=[' + self.boundingboxWD + ',' + self.boundingboxSD + ',' + self.boundingboxED + ',' + self.boundingboxND + ']'; 
            
            geoQueryString += self.getEnclosesBBQuery(self.boundingboxSD,self.boundingboxWD,self.boundingboxND,self.boundingboxED);
            
            ESGF.localStorage.put('esgf_queryString',geoQueryString,searchAPIQueryStr);
            
        }
        
        //attact the geo to the query string
        if(ESGF.setting.storage) {
        	ESGF.localStorage.put('esgf_fq', geoQueryString, geoQueryString);
        }
        
        
        
        Manager.store.addByValue('fq',geoQueryString);
    },
    
    
   
    
    
    
    /**
     * 
     * @param geoSearchType
     */
	overlaps: function(geoSearchType) {
		
        var self = this;
        
        geoQueryString = '';
        var sw = null;
        var ne = null;
        if(geoSearchType === 'circle') { // geosearch type is a circle

            var theCenter = self.markerGroup[0];
            self.centroidCenter = self.markerGroup[0].getPosition();
            sw = self.cbounds.getSouthWest();
            ne = self.cbounds.getNorthEast();
            //this is the min long 
            //west_degrees limit
            self.boundingboxWD = sw.lng();
            self.boundingboxSD = sw.lat();
            self.boundingboxED = ne.lng();
            self.boundingboxND = ne.lat();
            
            geoQueryString += self.getOverlapsBBQuery(self.boundingboxSD,self.boundingboxWD,self.boundingboxND,self.boundingboxED);
            
            
            
        } else { // geosearch type is a bounding box

            sw = self.bounds.getSouthWest();
            ne = self.bounds.getNorthEast();
            //this is the min long 
            //west_degrees limit
            self.boundingboxWD = sw.lng();
            //this is the min lat
            //south_degrees limit
            self.boundingboxSD = sw.lat();
            //this is the max long
            //east_degrees limit
            self.boundingboxED = ne.lng();
            //this is the max lat
            //north_degrees limit
            self.boundingboxND = ne.lat();

            geoQueryString += self.getOverlapsBBQuery(self.boundingboxSD,self.boundingboxWD,self.boundingboxND,self.boundingboxED);
            
        }

        if(ESGF.setting.storage) {
        	ESGF.localStorage.put('esgf_fq', geoQueryString, geoQueryString);
        }
        
        //add the geo string to the querystring
        Manager.store.addByValue('fq',geoQueryString);
    }
    
    
});


    
}(jQuery));

