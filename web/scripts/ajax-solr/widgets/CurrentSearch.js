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
        
        alert('fqString: ' + fqString);
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
        links.push($('<a href="#"/>').text('(x) ' + fqString).click(self.removeFacet(fq[i])));
    }

    if (links.length > 1) {
      links.unshift($('<a href="#"/>').text('remove all').click(function () {
        self.manager.store.remove('fq');
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
      if (self.manager.store.removeByValue('fq', facet)) {
        self.manager.doRequest(0);
      }
      return false;
    };
  },
  
  outputBoundingBoxString: function (fqString) {
	 alert('centroid string');
	//if there is no OR, it is an enclosed search
      if(fqString.search('OR') === -1)
      {
          fqString = 'encloses bounding (N,W,S,E):\n (' + Math.round(parseFloat(Manager.widgets['geo_browse'].boundingboxND).toFixed(self.floatPrecision)) + ',' +
          Math.round(parseFloat(Manager.widgets['geo_browse'].boundingboxWD).toFixed(self.floatPrecision)) + ',' +
          Math.round(parseFloat(Manager.widgets['geo_browse'].boundingboxSD).toFixed(self.floatPrecision)) + ',' +
          Math.round(parseFloat(Manager.widgets['geo_browse'].boundingboxED).toFixed(self.floatPrecision)) + ')';
      }
      //otherwise it is an overlaps search
      else {
          fqString = 'overlaps bounding (N,W,S,E): (' + Math.round(parseFloat(Manager.widgets['geo_browse'].boundingboxND).toFixed(self.floatPrecision)) + ',' +
          Math.round(parseFloat(Manager.widgets['geo_browse'].boundingboxWD).toFixed(self.floatPrecision)) + ',' +
          Math.round(parseFloat(Manager.widgets['geo_browse'].boundingboxSD).toFixed(self.floatPrecision)) + ',' +
          Math.round(parseFloat(Manager.widgets['geo_browse'].boundingboxED).toFixed(self.floatPrecision)) + ')';
      }
	  return fqString;
  },
  
  outputCentroidString: function (fqString) {
	  var self = this;
	//if there is no OR, it is an enclosed search
	  alert('radius: ' + Manager.widgets['geo_browse'].centroidRadius);
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
  }
  
});

}(jQuery));