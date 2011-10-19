/*****************************************************************************
 * Copyright © 2011 , UT-Battelle, LLC All rights reserved
 *
 * OPEN SOURCE LICENSE
 *
 * Subject to the conditions of this License, UT-Battelle, LLC (the
 * ÒLicensorÓ) hereby grants to any person (the ÒLicenseeÓ) obtaining a copy
 * of this software and associated documentation files (the "Software"), a
 * perpetual, worldwide, non-exclusive, irrevocable copyright license to use,
 * copy, modify, merge, publish, distribute, and/or sublicense copies of the
 * Software.
 *
 * 1. Redistributions of Software must retain the above open source license
 * grant, copyright and license notices, this list of conditions, and the
 * disclaimer listed below.  Changes or modifications to, or derivative works
 * of the Software must be noted with comments and the contributor and
 * organizationÕs name.  If the Software is protected by a proprietary
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
 *    Contract No. DE-AC05-00OR22725 with the Department of Energy.Ó
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
 * Facet Sidebar component for ESG
 *
 * fwang2@ornl.gov
 * harneyjf@ornl.gov
 */


(function ($) {
	
	AjaxSolr.FacetSideBarWidget = AjaxSolr.AbstractFacetWidget.extend({
	
		
		afterRequest: function () {
		

            
			var self = this;
			var facet = '';
			
			var facet_arr = new Array();
			
			
			
		    for (facet in self.manager.response.facet_counts.facet_fields) {
		    	var facet_obj = new Object();
		    	var facet_val_arr = new Array();
		    	var facet_val_counts = new Array();
		    	var facet_max_count = 0;
		    	for(var facet_value in self.manager.response.facet_counts.facet_fields[facet]) {
		    		if(facet == 'project') {
		    			var count = parseInt(self.manager.response.facet_counts.facet_fields[facet][facet_value]);
					    
		    			if(count > facet_max_count) {
		    				facet_max_count = count;
		    			}
		    		
		    			facet_val_counts.push(count);
		    			facet_val_arr.push(facet_value);
		    			
		    			
		    		}else if(!isConstraint(facet)) {
		    			var count = parseInt(self.manager.response.facet_counts.facet_fields[facet][facet_value]);
					    
		    			if(count > facet_max_count) {
		    				facet_max_count = count;
		    			}
		    		
		    			facet_val_counts.push(count);
		    			facet_val_arr.push(facet_value);
		    			
		    		} else if(matchesFacetValue(facet,facet_value)) {
		    			var count = parseInt(self.manager.response.facet_counts.facet_fields[facet][facet_value]);
					    
		    			if(count > facet_max_count) {
		    				facet_max_count = count;
		    			}
		    		
		    			facet_val_counts.push(count);
		    			facet_val_arr.push(facet_value);
		    		}
		    		
		    	}
		    	facet_obj.Facet_name = facet;
		    	facet_obj.Facet_values = facet_val_arr;
		    	facet_obj.Facet_counts = facet_val_counts;
		    	facet_obj.Facet_max_count = facet_max_count;
		    	facet_arr.push(facet_obj);
		    	
	    	}
		    
		    
		    
		    if($( "#facetTemplate").html() != null) {

	            $("#facetList").empty();
		    	$( "#facetTemplate").tmpl(facet_arr, {
		    		replaceWhiteSpaces : function (word) {
	                    return replaceWhiteSpace(word);
	                }
		        })
		    	.appendTo("#facetList")
		    	.find( "a.showFacetValues" ).click(function() {
	                var selectedItem = $.tmplItem(this);
	                for(var i = 0;i<selectedItem.data.Facet_values.length;i++) {
	                	var convertedStr = replaceWhiteSpace(selectedItem.data.Facet_values[i]);
	                    $('li#' + selectedItem.data.Facet_name + '_' + selectedItem.data.Facet_values[i]).toggle();
	                }
	                
		   		});
		    }
		    
		    $('a.alink').click( function () {
		    	
				var facet_value = $(this).html();
				
				var facet = $(this).parent().parent().find('a.showFacetValues').html();
				
				/* NEED TO COME BACK - IT ONLY MATCHES whitespace */
				var index = facet_value.search(' ');
				var trimmedFacetValue = facet_value.substr(0,index);
				index = facet.search(' ');
				var trimmedFacet = facet.substr(0,index);
				Manager.store.addByValue('fq', trimmedFacet + ':' + trimmedFacetValue );
				
				//add to esgf_fq localstore
				var key = trimmedFacet + ':' + trimmedFacetValue;
				var value = trimmedFacet + ':' + trimmedFacetValue;
				ESGF.localStorage.put('esgf_fq', key, value);
				
				//add to the old localstore
				if(ESGF.setting.storage) {
					var fq = localStorage['fq'];
		     	   	if(fq == null) {
		     	   		fq = trimmedFacet + ':' + trimmedFacetValue + ';';
		     	   		localStorage['fq'] = fq;
		     	   	} else {
			     		  fq += trimmedFacet + ':' + trimmedFacetValue + ';';
			              localStorage['fq'] = fq;
			     	}
				}
	     	   	Manager.doRequest(0);
				
			});
	    	
		}
	
	});

	$(document).ready( function() {
		$.fx.speeds._default = 1000;
	});
	
	function replaceWhiteSpace(word) {
		var convertedStr = word.split(' ').join('_'); 
		return convertedStr;
	}
	
	
	function matchesFacetValue(facet,value) {
		var matchesFacetValue = false;

		
		//make sure project still displays all values
		if(facet == 'project') {
			matchesFacetValue = true;
			
		} else {
			var fq = Manager.store.get('fq');
			
			for(var i=0;i<fq.length;i++) {
				var constraint = fq[i];
				var facet_constraint = constraint['value'].split(":")[0];
				if(facet == facet_constraint) {
					var facet_value = constraint['value'].split(":")[1];
					if(facet_value == value) {
						matchesFacetValue = true;
					}
				}
			}
		}

		return matchesFacetValue;
		
	}
	
	function isConstraint(facet) {
		var isConstraint = false;
		
		var fq = Manager.store.get('fq');
		
		for(var i=0;i<fq.length;i++) {
			var constraint = fq[i];
			var facet_constraint = constraint['value'].split(":")[0];
			if(facet == facet_constraint) {
				isConstraint = true;
			}
		}
		return isConstraint;
	}
	
}(jQuery));
