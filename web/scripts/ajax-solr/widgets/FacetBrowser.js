/**
 * 
 */

(function ($) {

AjaxSolr.FacetBrowserWidget = AjaxSolr.AbstractFacetWidget.extend({
  


	
	
	afterRequest: function () {
	  

	  var self = this;

	  $(this.target).empty();

	  $(self.target).append(AjaxSolr.theme('facet_title',capitalizeFirstLetter(self.field)));
		
	  this.displayFacetValues();
	  
	  
	  /*
	  //DONT KNOW HOW TO FIND THE LENGTH - TAKE THIS PART OUT WHEN THE LENGTH CAN BE DETERMINED IN AN EASIER MANNER
	  var numFields = 0;
	  for (var facet in this.manager.response.facet_counts.facet_fields[this.field]) {
		    numFields += 1;
	  }
	  
	  var maxCount = 0;
	  var objectedItems = [];
	  for (var facet in this.manager.response.facet_counts.facet_fields[this.field]) {
	      var count = parseInt(this.manager.response.facet_counts.facet_fields[this.field][facet]);
	      if (count > maxCount) {
	        maxCount = count;
	      }
	      objectedItems.push({ facet: facet, count: count });
	  }
	  objectedItems.sort(function (a, b) {
		  if(Manager.sortType == 'sortbycount') {
			  return a.count > b.count ? -1 : 1;
		  } else {
			  return a.facet.value < b.facet.value ? -1 : 1;
		  }
	  });
	  
	  	var divFieldID = 'facet_value_' + this.field;
	  	var start = this.startingValue;
	  	var increment = this.incrementValue;
		
		$(this.target).append(AjaxSolr.theme('facet_title',capitalizeFirstLetter(this.field)));
		var stopValue = objectedItems.length;
		
		if(objectedItems.length > (this.startingValue + this.incrementValue)) {
			stopValue = (this.startingValue + this.incrementValue);
		}
		
		if(numFields > 0) {
			//print the "prev..." if this is not the first value in the category
			if(this.startingValue != 0){
				$(this.target).append(AjaxSolr.theme('prevLink',stopValue,objectedItems,divFieldID,this));
			}
			
			$(this.target).append('<div id="' + divFieldID + '"></div>');
			
			

			$('div#'+divFieldID).append(AjaxSolr.theme('facet_content',stopValue,objectedItems,divFieldID,self));
				
			
			if(stopValue == (this.startingValue + this.incrementValue)) {
				//$(this.target).append(AjaxSolr.theme('nextLink',stopValue,objectedItems,divFieldID,this));
				$(this.target).append(AjaxSolr.theme('nextLink',divFieldID,this));
			}
			
	  }
		*/
	  
  },
  
  
  setStartingValue: function (value) {
	  alert('value: ' + value);
  },

  removeFacet: function (facet) {
    var self = this;
    
  },
  
  init: function () {
	  $('a.moreFacets').livequery(function () {
		  $(this).toggle(function () {
		        $(this).parent().find('span').show();
		        $(this).text('... less');
		        return false;
		  }, function () {
		        $(this).parent().find('span').hide();
		        $(this).text('... more');
		        return false;
		  });
		  
		  
		  
	  });
	  

	  

  }
  
  
	
});



})(jQuery);

function capitalizeFirstLetter(string)
{
    return string.charAt(0).toUpperCase() + string.slice(1);
}