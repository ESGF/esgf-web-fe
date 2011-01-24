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
    	var fqString = new String(fq[i]);
    	
    	//check to see if this is a geospatial query (assuming 'east_degrees' is in every geo query)
    	//if it is -> need to change the current selection string
    	
    	if(fqString.search('east_degrees') != -1)
    	{
    		//if there is no OR, it is an enclosed search
    		if(fqString.search('OR') == -1)
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
    	
    	//check to see if this is a temporal query (assuming 'datetime_start' is in every geo query)
    	/*
    	alert('the string: ' + fqString + ' ' + fqString.search('start') != -1);
    	if(fqString.search('start') != -1) {
    		var start = fqString.search('\:') + 2;
    		var stop = fqString.search(' TO');
    		var substr = fqString.substring(start,stop);
    		//alert(substr + ' ' + substr.length);
    		if(substr == '*') {
    			fqString = 'From: ' + 'ALL';
    		}
    		else {
    			fqString = 'From: ' + substr;
    		}
    		//get the time
    		//fqString = 'From: ' + 
    	}
    	//fqString = fq[i];
    	
    	if(fqString.search('stop') != -1) {
    		var start = fqString.search('\:') + 2;
    		var stop = fqString.search(' TO');
    		var substr = fqString.substring(start,stop);
    		alert(substr);
    		if(substr == '*') {
    			fqString = 'To: ' + 'ALL';
    		}
    		else {
    			fqString = 'To: ' + substr;
    		}
    		//get the time
    		//fqString = 'From: ' + 
    	}
    	*/
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