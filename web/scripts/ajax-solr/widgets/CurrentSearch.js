/**
 * 
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
        delete localStorage['fq'];
        
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
    		var fq = localStorage['fq'].replace((facet+';'),"");
      	  	localStorage['fq'] = fq;
      	  
      	  	if(fq == '') {
      		  delete localStorage['fq'];
      	  	} 
    		self.manager.doRequest(0);
        }
    }
    
    	
    /*
    return function() {
    	self.manager.store.removeByValue('fq', facet);
    	if (self.manager.store.removeByValue('fq', facet)) {
    		
    	}
    	return false;
    };
    */
    /* insert code to delete the fq and/or q parameters */
    /*
    return function () {
      if (self.manager.store.removeByValue('fq', facet)) {
    	  var fq = localStorage['fq'].replace((facet+';'),"");
    	  localStorage['fq'] = fq;
    	  
    	  if(fq == '') {
    		  delete localStorage['fq'];
    	  } 
    	  
    	  self.removeGeospatialConstraints(facet);  
          self.manager.doRequest(0);
      }
      return false;
    };
    */
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
	  var printedND = self.roundToPrecision(parseFloat(localStorage['ND']),self.floatPrecision);
	  var printedSD = self.roundToPrecision(parseFloat(localStorage['SD']),self.floatPrecision);
	  var printedED = self.roundToPrecision(parseFloat(localStorage['ED']),self.floatPrecision);
	  var printedWD = self.roundToPrecision(parseFloat(localStorage['WD']),self.floatPrecision);
	  
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

	  delete localStorage['ED'];
	  delete localStorage['WD'];
	  delete localStorage['SD'];
	  delete localStorage['ND'];
	  
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