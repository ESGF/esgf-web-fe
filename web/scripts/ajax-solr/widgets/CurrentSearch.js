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
        //alert('fqStr: ' + i + ' ' + fqString);
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
        /*
        links.push($('<a href="#"/>').text('(x) ' + fqString).click( 
        		function () {
        			alert('remove facet');
        			
        			// include code to remove from cookie 
        				self.removeFacet(fq[i]);
        		})); */
        
        links.push($('<a href="#"/>').text('(x) ' + fqString).click( self.removeFacet(fq[i])));
    }

    if (links.length > 1) {
      links.unshift($('<a href="#"/>').text('remove all').click(function () {
        self.manager.store.remove('fq');
        
        // alert('clear out the fq store');
        
        localStorage['fq'] = "";
        
        var facet = null;
        //self.removeGeospatialConstraints(facet);  
        
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
    
    /* insert code to delete the fq and/or q parameters */
    
    return function () {
      if (self.manager.store.removeByValue('fq', facet)) {
    	  //alert('remove: ' + facet + ' from the fq store');
    	  var fq = localStorage['fq'].replace(facet+";","");
    	  localStorage['fq'] = fq;
    	  //replace("");
    	self.removeGeospatialConstraints(facet);  
        self.manager.doRequest(0);
      }
      return false;
    };
  },
  
  outputBoundingBoxString: function (fqString) {
	  var self = this;
	//if there is no OR, it is an enclosed search
      if(fqString.search('OR') === -1)
      {
          fqString = 'encloses bounding (N,W,S,E):\n (' + self.roundToPrecision(parseFloat(Manager.widgets['geo_browse'].boundingboxND),self.floatPrecision) + ',' +
          self.roundToPrecision(parseFloat(Manager.widgets['geo_browse'].boundingboxND),self.floatPrecision) + ',' +
          self.roundToPrecision(parseFloat(Manager.widgets['geo_browse'].boundingboxND),self.floatPrecision) + ',' +
          self.roundToPrecision(parseFloat(Manager.widgets['geo_browse'].boundingboxND),self.floatPrecision) + ')';
      }
      //otherwise it is an overlaps search
      else {
          fqString = 'overlaps bounding (N,W,S,E): (' + self.roundToPrecision(parseFloat(Manager.widgets['geo_browse'].boundingboxND),self.floatPrecision) + ',' +
          self.roundToPrecision(parseFloat(Manager.widgets['geo_browse'].boundingboxND),self.floatPrecision) + ',' +
          self.roundToPrecision(parseFloat(Manager.widgets['geo_browse'].boundingboxND),self.floatPrecision) + ',' +
          self.roundToPrecision(parseFloat(Manager.widgets['geo_browse'].boundingboxND),self.floatPrecision) + ')';
      }
	  return fqString;
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
	  if(facet.search('east_degrees') !== -1 || facet == null) {
  	//reset ALL temporal and geospatial paramters to null
        Manager.widgets['geo_browse'].boundingboxND = null;
        Manager.widgets['geo_browse'].boundingboxSD = null;
        Manager.widgets['geo_browse'].boundingboxED = null;
        Manager.widgets['geo_browse'].boundingboxWD = null;
        console.log('\t\tSetting radius and center to null');
        Manager.widgets['geo_browse'].centroidRadius = null;
        Manager.widgets['geo_browse'].centroidCenter = null;  
       }
  },
  
  removeTemporalConstraints: function (facet) {
	  
  }
  
});

}(jQuery));