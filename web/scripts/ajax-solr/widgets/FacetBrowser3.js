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
 * Facet Browser component for ESG
 *
 * fwang2@ornl.gov
 * harneyjf@ornl.gov
 */


(function ($) {

AjaxSolr.FacetBrowserWidget = AjaxSolr.AbstractFacetWidget.extend({
    

	capFirstLetter: function (str) {
		return str.charAt(0).toUpperCase() + str.slice(1);
	},
	
	
	
	afterRequest: function () {

		var self = this;
		
		$('a#facet_overlay').click(function() {
            
			
        	$( "#facetdialog" ).dialog({
        		width: 700,
        		maxWidth: 700,
        		height: 500,
        		maxHeight: 550,
        		show: 'blind',
        		
        		
        		open: function() {
        			//alert(self.manager.response.facet_counts);
        			
        			for(var i=0;i<self.longNames.length;i++) {
        				
        				var title = $('<div class="facet_title" id="title_' + self.longNames[i] + '" style="margin-left:10px;margin-top:15px;margin-right:10px"><h3>' + self.longNames[i] + '</h3></div>');
        			
        				var values = $('<div id="' + self.longNames[i] + '"></div>');
        				
        				
        				$('#start').append(title);
            			$('#title_'+ self.longNames[i]).append(values);
        				
        				//if(self.shortNames[i] == 'project') {
        					//alert(self.shortNames[i] + ' ' + self.manager.response.facet_counts.facet_fields[self.shortNames[i]].length);
            				for(var j=0;j<self.manager.response.facet_counts.facet_fields[self.shortNames[i]].length;j++) {
            					var facet = self.manager.response.facet_counts.facet_fields[self.shortNames[i]];
            					//alert('facet: ' + facet + ' value: ' + facet[j]);
            					
            					
            					var facet_value = facet[j];
            		    		j = j + 1;
            		    		var count = facet[j];

            		    		var shortName = self.shortNames[i];
            		    		//alert('shortName: ' + shortName + ' ' + self.shortNames[i]);
            		    		//alert('rep: ' + replacePeriod(facet_value));
            		    		
            		    		var attr = ' facetname="' + shortName + '" facetValue="' + facet_value.replace(' ','_') + '" ';
            		    		
            		    		//alert('facet value: ' + facet_value.replace(' ','_'));
            		    		
            		    		
            					var $facet_value_element = $('<a href="#" id="' + facet_value.replace(' ','_')  +'" class="tag_item" ' + attr + 'style="margin-right:10px;color:blue;font-size:10px">' + facet_value + '(' + count + ') </a>' + ' ').click(function(shortName){

            						var facetV = $(this).html().split('(')[0];
            						//alert(facetV.replace(' ','_'));
            						
            						var facetN = $(this).parent().find('#' + facetV.replace(' ','_')).attr('facetname');
            						var facetVa = $(this).parent().find('#' + facetV.replace(' ','_')).attr('facetValue');
            						
            						
            						var key = facetN + ':' + facetV;

            						//for now duplicate val
            				        ESGF.localStorage.put('esgf_fq',key,key);
            				          
            						//add key= category:value value= category=value to the esgf_queryString map
            				        ESGF.localStorage.put('esgf_queryString',key,key.replace(":", "="));
            				          
            				        Manager.store.addByValue('fq', facetN + ':' + facetV );
            						
            				        $("#facetdialog").dialog("close");  
            						Manager.doRequest(0);
            					});
            					
            					$('#'+self.longNames[i]).append($facet_value_element);
            		    		
            				}
        			}
        			
        		},
        		
        		close: function() {
        			$('#start').empty();
        		}
                
        		
        	});
		});
		
		/*
	    $(self.target).empty();
	    $(self.target).append(AjaxSolr.theme('facet_title',self.capFirstLetter(self.field)));
	    this.displayFacetValues();
	    */
	    
	    
	    
	}

	

});

/**
 * This function is used primarily to avoid annoying errors that occur with strings that have periods in them
 */
/**
 * Obviously needs revision
 */
function replacePeriod(word)
{
    var newWord = word.replace(/\./g,"_");
    var newNewWord = newWord.replace(":","_");
    var newNewNewWord = newWord.replace("|","_");
    return newNewNewWord;
}

}(jQuery));


