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
 * Experimental Solr Front for ESG
 *
 * fwang2@ornl.gov
 * harneyjf@ornl.gov
 */


(function ($) {

AjaxSolr.CurrentSearchWidget = AjaxSolr.AbstractWidget.extend({
	
  floatPrecision: 2,	
	
  afterRequest: function () {
    var self = this;
    var links = [];
    var i = null;
    var fq = this.manager.store.values('fq');
    for (i = 0, l = fq.length; i < l; i++) {
        var fqString = fq[i];
        
        //check to see if this is a geospatial query (assuming 'east_degrees' is in every geo query)
        //if it is -> need to change the current selection string
        if(fqString.search('east_degrees') !== -1)
        {
            if($("input[name='areaGroup']:checked").val() === 'circle') {
            	fqString = self.outputCentroidString(fqString);
            }
            else {
            	fqString = self.outputBoundingBoxString(fqString);
            }
        }
        links.push($('<a href="#"/>').text('(x) ' + fqString).click( //function () {
        		
        	self.removeFacet(fq[i]))
        );
    }

    if (links.length > 1) {
      links.unshift($('<a href="#"/>').text('remove all').click(function () {
        self.manager.store.remove('fq');
         //delete the localStorage
        if(ESGF.setting.storage) {
            delete localStorage['fq'];
        }
        
        var facet = null;
        self.removeGeospatialConstraints(facet);  
        
        self.manager.doRequest(0);
        return false;
      }));
    }
    if (links.length) {
      AjaxSolr.theme('list_items', this.target, links);
    }
    else {
      $(this.target).html('<div>Viewing all documents!</div>');
    }
  },

  removeFacet: function (facet) {
    var self = this;
    return function () {
    	if(self.manager.store.removeByValue('fq',facet)) {
    		if(ESGF.setting.storage) {
    			var fq = localStorage['fq'].replace((facet+';'),"");
          	  	localStorage['fq'] = fq;
          	  
          	  	if(fq == '') {
          		  delete localStorage['fq'];
          	  	} 
    		}
    		
    		self.manager.doRequest(0);
        }
    }
    
    
  },
  
  outputBoundingBoxString: function (fqString) {
	  var self = this;
	  
	//if there is no OR, it is an enclosed search
	  var newFqString = "";
	  if(fqString.search('OR') === -1)
      {
		  newFqString += 'encloses '; 
      }
      //otherwise it is an overlaps search
      else {
    	  newFqString += 'overlaps '; 
      }
	  newFqString += 'bounding (N,W,S,E):\n';
	  //var printedND = self.roundToPrecision(parseFloat(Manager.widgets['geo_browse'].boundingboxND),self.floatPrecision);
	  
	  
	  printedND = self.roundToPrecision(parseFloat(Manager.widgets['geo_browse'].boundingboxND),self.floatPrecision);
	  printedSD = self.roundToPrecision(parseFloat(Manager.widgets['geo_browse'].boundingboxSD),self.floatPrecision);
	  printedED = self.roundToPrecision(parseFloat(Manager.widgets['geo_browse'].boundingboxED),self.floatPrecision);
	  printedWD = self.roundToPrecision(parseFloat(Manager.widgets['geo_browse'].boundingboxWD),self.floatPrecision);
  
	  
	  if(ESGF.setting.storage) {
		  printedND = self.roundToPrecision(parseFloat(localStorage['ND']),self.floatPrecision);
		  printedSD = self.roundToPrecision(parseFloat(localStorage['SD']),self.floatPrecision);
		  printedED = self.roundToPrecision(parseFloat(localStorage['ED']),self.floatPrecision);
		  printedWD = self.roundToPrecision(parseFloat(localStorage['WD']),self.floatPrecision);
	  }
	  
	  /*
	  var printedSD = self.roundToPrecision(parseFloat(Manager.widgets['geo_browse'].boundingboxSD),self.floatPrecision);
	  var printedED = self.roundToPrecision(parseFloat(Manager.widgets['geo_browse'].boundingboxED),self.floatPrecision);
	  var printedWD = self.roundToPrecision(parseFloat(Manager.widgets['geo_browse'].boundingboxWD),self.floatPrecision);
	  */
	  newFqString += '(' + printedND + ',' + printedWD + ',' + printedSD + ',' + printedWD + ')';
	  
	  return newFqString;
  },
  
  outputCentroidString: function (fqString) {
	  var self = this;
	//if there is no OR, it is an enclosed search
      if(fqString.search('OR') === -1)
      {
          fqString = 'encloses centroid center(Lat,Long): (' + self.roundToPrecision(parseFloat(Manager.widgets['geo_browse'].boundingboxND),self.floatPrecision) + ',' +
          self.roundToPrecision(parseFloat(Manager.widgets['geo_browse'].boundingboxED),self.floatPrecision) + ')';
      }
      //otherwise it is an overlaps search
      else {
          fqString = 'overlaps centroid center(Lat,Long): (' + self.roundToPrecision(parseFloat(Manager.widgets['geo_browse'].boundingboxND),self.floatPrecision) + ',' +
          self.roundToPrecision(parseFloat(Manager.widgets['geo_browse'].boundingboxED),self.floatPrecision) + ')';
      }
      fqString += '\nradius: ' + Manager.widgets['geo_browse'].centroidRadius;
	  return fqString;
  },
  
  roundToPrecision: function (inputNum,desiredPrecision) {
	  var precisionGuide = Math.pow(10, desiredPrecision);
	  return( Math.round(inputNum * precisionGuide) / precisionGuide );
  },
  
  removeGeospatialConstraints: function (facet) {
	  if(ESGF.setting.storage) {
		  delete localStorage['ED'];
		  delete localStorage['WD'];
		  delete localStorage['SD'];
		  delete localStorage['ND'];
	  }
	  
	  
	  if(facet != null) {
		  if(facet.search('east_degrees') !== -1 || facet == null) {
			  	//reset ALL temporal and geospatial paramters to null
			        Manager.widgets['geo_browse'].boundingboxND = null;
			        Manager.widgets['geo_browse'].boundingboxSD = null;
			        Manager.widgets['geo_browse'].boundingboxED = null;
			        Manager.widgets['geo_browse'].boundingboxWD = null;
			        Manager.widgets['geo_browse'].centroidRadius = null;
			        Manager.widgets['geo_browse'].centroidCenter = null;  
			       }
	  }
	  
  },
  
  removeTemporalConstraints: function (facet) {
	  
  }
  
});

}(jQuery));