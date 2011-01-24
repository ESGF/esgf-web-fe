/**
 * 
 */

(function ($) {

AjaxSolr.GeospatialSearchWidget = AjaxSolr.AbstractWidget.extend({
  afterRequest: function () {
    var self = this;

    $("div#geo a[rel]").overlay({
    	
		mask: 'darkred',
		effect: 'apple',

		onBeforeLoad: function() {
			
			$('.apple_overlay').css({'width' : '640px'});
			
			// grab wrapper element inside content
			var wrap = this.getOverlay().find(".contentWrap");

			// load the page specified in the trigger
			wrap.load(this.getTrigger().attr("href"));
			
		},
    
    	onLoad: function() {
    		alert(loaded);
    	}

	});
  },

  removeFacet: function (facet) {
    var self = this;
    
  },
  
  init: function () {
	  
	 
	
	  
  }
  
   
	
});

})(jQuery);