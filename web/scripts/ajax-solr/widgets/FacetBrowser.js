/**
 * 
 */

(function ($) {

AjaxSolr.FacetBrowserWidget = AjaxSolr.AbstractFacetWidget.extend({
  afterRequest: function () {
	  $("span#facet a[rel]").overlay({
			 
	  		mask: {opacity: 0.5, color: '#000'},
	  		effect: 'apple',
	  		//left: "5%",
			//top: "5%",
	  		onBeforeLoad: function() {
			
	  			$('.apple_overlay').css({'width' : '640px'});
			
			
				// grab wrapper element inside content
				var wrap = this.getOverlay().find(".contentWrap");
	
				// load the page specified in the trigger
				wrap.load(this.getTrigger().attr("href"));
	  		}

	  		
		
	  });  
	  

	  

	  $(this.target).empty();

	  
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
	  
	  if(numFields > 0) {
			$(this.target).append('<p>');
			$(this.target).append(AjaxSolr.theme('facet_title',capitalizeFirstLetter(this.field)));
			
			for (var i = 0, l = objectedItems.length; i < l; i++) {
				  var facetTextValue = objectedItems[i].facet + ' (' + objectedItems[i].count + ') ';
				  if(i != objectedItems.length-1)
					  facetTextValue += '|'; 
			      var facet = objectedItems[i].facet;
			      //console.log('i: ' + i + ' textLength: ' + textLength);
			      //facet += " (" + objectedItems[i].count + ") |";
			      $(this.target).append($('<a href="#" class="tag_item" />').text(facetTextValue).click(this.clickHandler(facet)));
			    };
			$(this.target).append('<\p>');
	  }
	  
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