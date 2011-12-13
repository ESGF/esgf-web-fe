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
/**
 * 
 */
AjaxSolr.CurrentSearchWidget = AjaxSolr.AbstractWidget.extend({
	
  floatPrecision: 2,	
	
  /**
   * DOCUMENT ME
   */
  afterRequest: function () {
	  
    var self = this;
    var links = [];
    var i = null;
    var fq = this.manager.store.values('fq');
    for (i = 0, l = fq.length; i < l; i++) {
        var fqString = fq[i];
        //leave out the 'type:Dataset' constraint 
        if(fqString.search('Dataset') == -1 && fqString.search('replica:false')) {
        	
        	//check to see if this is a geospatial query (assuming 'east_degrees' is in every geo query)
            //if it is -> need to change the current selection string
        	//this is mainly to make it human readable (as opposed to only solr readable)
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
        
    }

    if (links.length > 1) {
      links.unshift($('<a href="#"/>').text('remove all').click(function () {
        
    	//remove everything from ajax-solr parameter store  
    	self.manager.store.remove('fq');
        
    	//remove everything from the localstorage esgf_fq store
        var esgf_fq = ESGF.localStorage.getAll('esgf_fq');        
        for(var key in esgf_fq) {
        	ESGF.localStorage.remove('esgf_fq',key);
        }

    	//remove everything from the localstorage esgf_queryString store
        var esgf_queryString = ESGF.localStorage.getAll('esgf_queryString');  
        for(var key in esgf_queryString) {
        	ESGF.localStorage.remove('esgf_queryString',key);
        }
        
        self.manager.doRequest(0);
        return false;
      }));
      
    }
    if (links.length) {
      AjaxSolr.theme('list_items', this.target, links);
    }
    else {
      $(this.target).html('<div>No search criteria selected</div>');
    }
  },

  /**
   * 
   * @param facet
   * @returns {Function}
   */
  removeFacet: function (facet) {
    var self = this;
    return function () {
    	if(self.manager.store.removeByValue('fq',facet)) {
    		if(ESGF.setting.storage) {
          	  	ESGF.localStorage.remove('esgf_fq', facet);
          	  	ESGF.localStorage.remove('esgf_queryString', facet);
    		}
    		self.manager.doRequest(0);
        }
    }
    
    
  },
  
  /**
   * 
   * @param fqString
   * @returns {String}
   */
  outputBoundingBoxString: function (fqString) {
	  var self = this;

	  var n = Manager.widgets['geo_browse'].boundingboxND;
	  var s = Manager.widgets['geo_browse'].boundingboxSD;
	  var e = Manager.widgets['geo_browse'].boundingboxED;
	  var w = Manager.widgets['geo_browse'].boundingboxWD;
	  
	  
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
	  
	  printedND = self.roundToPrecision(parseFloat(n),self.floatPrecision);
	  printedSD = self.roundToPrecision(parseFloat(s),self.floatPrecision);
	  printedED = self.roundToPrecision(parseFloat(e),self.floatPrecision);
	  printedWD = self.roundToPrecision(parseFloat(w),self.floatPrecision);
  
	  
	  newFqString += '(' + printedND + ',' + printedWD + ',' + printedSD + ',' + printedWD + ')';
	  
	  return newFqString;
  },
  
  /**
   * 
   * @param fqString
   * @returns
   */
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
  
  /**
   * 
   * @param inputNum
   * @param desiredPrecision
   * @returns {Number}
   */
  roundToPrecision: function (inputNum,desiredPrecision) {
	  var precisionGuide = Math.pow(10, desiredPrecision);
	  return( Math.round(inputNum * precisionGuide) / precisionGuide );
  },
  
  /**
   * 
   * @param facet
   */
  removeGeospatialConstraints: function (facet) {
	  
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
  
  /**
   * 
   * @param facet
   */
  removeTemporalConstraints: function (facet) {
	  
  }
  
});

}(jQuery));