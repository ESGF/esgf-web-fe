/**
 * 
 */

(function ($) {

AjaxSolr.FacetBrowserWidget = AjaxSolr.AbstractFacetWidget.extend({
    

	capFirstLetter: function (str) {
		return str.charAt(0).toUpperCase() + str.slice(1);
	},
	
	afterRequest: function () {
        var self = this;
	    $(self.target).empty();
	    $(self.target).append(AjaxSolr.theme('facet_title',self.capFirstLetter(self.field)));
	    this.displayFacetValues();
	}


});


}(jQuery));


