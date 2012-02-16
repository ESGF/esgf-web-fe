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
 * Results component for ESG
 *
 * fwang2@ornl.gov
 * harneyjf@ornl.gov
 */

/**
 * Results.js
 */

(function($) {
    AjaxSolr.ResultWidget = AjaxSolr.AbstractWidget.extend({

        beforeRequest: function () {
            $(this.target).html($('<img/>').attr('src', 'images/ajax-loader.gif'));
            $('a.selections').die('click');
        },
        
        
        distanceInKm : function (point1,point2) {
            var seLat1 = point1.lat()*Math.PI/180.0;
            var seLat2 = point2.lat()*Math.PI/180.0;
            var lngDiff = (point2.lng()-point1.lng())*Math.PI/180.0;

            //note this is in feet
            var a = Math.acos(Math.sin(seLat1) * Math.sin(seLat2) +
                    Math.cos(seLat1) * Math.cos(seLat2) * Math.cos(lngDiff)) * 20902231.0029;

            //convert feet to km
            var aKm = a/3280.84;
            return aKm;
        },
        
        
        postSolrProcessing: function (doc) {
            var self = this;
            var isInRange = true;
            var radius,center,se,ne,sw,nw,dist=null;
            var i = null;
            if($("input[name='areaGroup']:checked").val() === 'circle') {
                //get the radius
                //radius = $("input[name='radius']").val();
                radius = Manager.widgets['geo_browse'].centroidRadius;
                
                center = Manager.widgets['geo_browse'].centroidCenter;
                
                if(center != null && radius !=null) {
                	//get extreme points - they must ALL be within range
                    se = new google.maps.LatLng(doc.south_degrees, doc.east_degrees);
                    ne = new google.maps.LatLng(doc.north_degrees, doc.east_degrees);
                    sw = new google.maps.LatLng(doc.south_degrees, doc.west_degrees);
                    nw = new google.maps.LatLng(doc.north_degrees, doc.west_degrees);

                    dist = [];
                    dist[0] = self.distanceInKm(se,center);
                    dist[1] = self.distanceInKm(ne,center);
                    dist[2] = self.distanceInKm(sw,center);
                    dist[3] = self.distanceInKm(nw,center);

                    for (i = 0; i < dist.length; i++) {
                        if(dist[i] > radius) {
                            isInRange = false;
                        }
                    }
                }
                
                
            }
            return isInRange;
        },
        
        facetLinks: function (facet_field, facet_values) {
            var links = [];
            var i = null;
            if (facet_values) {
                for (i = 0, l = facet_values.length; i < l; i++) {
                    links.push(AjaxSolr.theme('facet_link', facet_values[i], this.facetHandler(facet_field, facet_values[i])));
                }
            }
            return links;
        },
        facetHandler: function (facet_field, facet_value) {
            var self = this;
            return function () {
                self.manager.store.remove('fq');
                self.manager.store.addByValue('fq', facet_field + ':' + facet_value);
                self.manager.doRequest(0);
                return false;
            };
        },

        afterRequest: function () {
        	
	        var i = null;
	        var self = this;
            $(this.target).empty();

            
            if(ESGF.setting.storage) {
            	
            	//extract from localStorage
            	var esgf_fq = ESGF.localStorage.getAll('esgf_fq');
            	
            	var searchConstraint = '';
            	
            	//loop through all the fqs
            	for(var key in esgf_fq) {
            		var value = esgf_fq[key];
            	}
            	
            	//need to check the following to see if there are any USER ADDED search constraints
            	var userAddedConstraint = 'false';
            	
            	
            	var esgf_queryString = ESGF.localStorage.getAll('esgf_queryString');
            	for(var key in esgf_queryString) {
            		if(key != '' && key != ' ') {
            			searchConstraint += key + ',';
            		}
            		//alert('key: ' + key + ' value: ' + esgf_queryString[key]);
            		//have to ignore the following
            		if(key.search('offset') > -1) {
            			//alert('offset found');
            		} else if(key.search('type:') > -1) {
            			//alert('type found');
            		} else if(key.search('replica:') > -1) {
            			//alert('replica found');
            		} else if(key.search('latest:') > -1) {
            			//alert('latest found');
            		} else if(key.search('distrib') > -1) {
            			//alert('distrib found');
            		} else if(key == '' || key == ' ') {
            			//alert('blank found');
            		} else {
            			userAddedConstraint = 'true';
            		}
            	}
            	
            	
            	var fq = localStorage['fq'];

            	
                /* only display results if there is a search */
            	/* for now that means the localStorage ONLY has type:Dataset;,replica:false */
            	//if(Manager.store.values('fq') != 'type:Dataset,replica:false') {	
                //if(searchConstraint != 'type:Dataset,replica:false,distrib,') {
            	if(userAddedConstraint == 'true') {
                //if(searchConstraint != 'offset,type:Dataset,replica:false,latest:true,distrib,') {
            	    		//alert ('i should not display this if ' + fq);
                	for (i = 0, l = this.manager.response.response.docs.length; i < l; i++) {
                        var doc = this.manager.response.response.docs[i];
                        
                    		if(self.postSolrProcessing(doc)) {
                    			

                                //$("a[id=" + selectID + "]").die('click');

                    			//remove the live events
                    		    selectID = 'ai_select_'+ doc.id.replace(/\./g, "_");
                    		    
                    		    selectID = selectID.replace("|","_");
                    		    
                    		    /*
                    		    alert('selectedId: ' + selectID);
                    		    
                                //alert('killing live link for ' + selectID);
                    		    if(doc['version'] == '20111119') {
                    		    	alert('data doc that breaks ' + doc['id']);
                    		    }
                    		    */
                    		    
                    		    $("a#" + selectID).die('click');
                    			
                                //console.log('keep doc: ' + doc.title);
                            	//alert('doc: ' + doc.title);
                                $(this.target).append(
                                    AjaxSolr.theme('result', doc,
                                            AjaxSolr.theme('snippetReplica', doc),
                                            AjaxSolr.theme('snippetVersion', doc),
                                            AjaxSolr.theme('snippet', doc),
                                    AjaxSolr.theme('actions', doc)));
                            } 
                        }
                } 
            } else {
            	if(this.manager.store.values('fq') != 0)
            		for (i = 0, l = this.manager.response.response.docs.length; i < l; i++) {
                        var doc = this.manager.response.response.docs[i];
                		if(self.postSolrProcessing(doc)) {
                            //console.log('keep doc: ' + doc.title);
                        	//alert('doc: ' + doc.title);
                            $(this.target).append(
                                AjaxSolr.theme('result', doc,
                                AjaxSolr.theme('snippet', doc),
                                AjaxSolr.theme('actions', doc)));
                        } 
                    }
            	
            }
                
        },


        init: function () {
            $('a.more').livequery(function () {
                $(this).toggle(function () {
                    $(this).parent().find('span').show();
                    $(this).text('... less');
                    return false;
                }, function () {
                    $(this).parent().find('span').hide();
                    $(this).text('... more');
                    return false;
                });
            });
        }

    });

}(jQuery));


