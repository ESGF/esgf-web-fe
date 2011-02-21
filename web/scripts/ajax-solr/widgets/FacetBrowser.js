/**
 * 
 */

(function ($) {

AjaxSolr.FacetBrowserWidget = AjaxSolr.AbstractFacetWidget.extend({
  	
	afterRequest: function () {
	  var self = this;
	  $(self.target).empty();
	  $(self.target).append(AjaxSolr.theme('facet_title',capitalizeFirstLetter(self.field)));
	  this.displayFacetValues();
	}

});

})(jQuery);

/*
 * Utility function to captilize (just wanted to reduce the length of code on a single line)...Can probably be taken out at a later date
 * 
 */
function capitalizeFirstLetter(string)
{
    return string.charAt(0).toUpperCase() + string.slice(1);
}