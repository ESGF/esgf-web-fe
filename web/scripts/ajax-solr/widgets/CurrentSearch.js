/**
 * 
 */

(function ($) {

AjaxSolr.CurrentSearchWidget = AjaxSolr.AbstractWidget.extend({
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
                //if there is no OR, it is an enclosed search
                if(fqString.search('OR') === -1)
                {
                    fqString = 'encloses centroid (N,W,S,E): (' + Math.round(parseFloat(boundingboxND).toFixed(2)) + ',' +
                    Math.round(parseFloat(boundingboxWD).toFixed(2)) + ',' +
                    Math.round(parseFloat(boundingboxSD).toFixed(2)) + ',' +
                    Math.round(parseFloat(boundingboxED).toFixed(2)) + ')';
                }
                //otherwise it is an overlaps search
                else {
                    fqString = 'overlaps centroid (N,W,S,E): (' + Math.round(parseFloat(boundingboxND).toFixed(2)) + ',' +
                    Math.round(parseFloat(boundingboxWD).toFixed(2)) + ',' +
                    Math.round(parseFloat(boundingboxSD).toFixed(2)) + ',' +
                    Math.round(parseFloat(boundingboxED).toFixed(2)) + ')';
                }
            }
            else {
                //if there is no OR, it is an enclosed search
                if(fqString.search('OR') === -1)
                {
                    fqString = 'encloses bounding (N,W,S,E): (' + Math.round(parseFloat(boundingboxND).toFixed(2)) + ',' +
                    Math.round(parseFloat(boundingboxWD).toFixed(2)) + ',' +
                    Math.round(parseFloat(boundingboxSD).toFixed(2)) + ',' +
                    Math.round(parseFloat(boundingboxED).toFixed(2)) + ')';
                }
                //otherwise it is an overlaps search
                else {
                    fqString = 'overlaps bounding (N,W,S,E): (' + Math.round(parseFloat(boundingboxND).toFixed(2)) + ',' +
                    Math.round(parseFloat(boundingboxWD).toFixed(2)) + ',' +
                    Math.round(parseFloat(boundingboxSD).toFixed(2)) + ',' +
                    Math.round(parseFloat(boundingboxED).toFixed(2)) + ')';
                }
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
  }
  
  
  
});

}(jQuery));