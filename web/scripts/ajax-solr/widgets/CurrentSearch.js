/**
 * 
 */

(function ($) {

AjaxSolr.CurrentSearchWidget = AjaxSolr.AbstractWidget.extend({
  afterRequest: function () {
    var self = this;
    var links = [];

    var fq = this.manager.store.values('fq');
    
    
    for (var i = 0, l = fq.length; i < l; i++) {
    	var fqString = fq[i];
    	/*
    	alert('s: ' + boundingboxSD + 
    		  ' n: ' + boundingboxND +
    		  ' e: ' + boundingboxED +
    		  ' w: ' + boundingboxWD);
    	*/
    	if(fqString.search('OR') != -1)
    	{
    		//alert('pushing ' + fq[i] + ' on encloses current search');
    		//alert('Center for ' + fq[i]);
    		fqString = 'Center (Lat, Lon): (' + 
    					(boundingboxND+boundingboxSD)/2 + ',' + (boundingboxED+boundingboxWD)/2 + ')' + 
    					'\nRadius: '; 
    	}
    	else if (fqString.search('east_degrees'))
    	{
    		//alert('Bounding box');
    		fqString = 'Bounding: (N,W,S,E): (' + Math.round(parseFloat(boundingboxND).toFixed(2)) + ',' +
    											     Math.round(parseFloat(boundingboxWD).toFixed(2)) + ',' +
    											     Math.round(parseFloat(boundingboxSD).toFixed(2)) + ',' +
    											     Math.round(parseFloat(boundingboxED).toFixed(2)) + ')';
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

})(jQuery);