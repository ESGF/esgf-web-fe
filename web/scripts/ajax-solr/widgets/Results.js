/**
 * Results.js
 */






(function ($) {

AjaxSolr.ResultWidget = AjaxSolr.AbstractWidget.extend({
	
  beforeRequest: function () {
    $(this.target).html($('<img/>').attr('src', 'images/ajax-loader.gif'));
  },

  facetLinks: function (facet_field, facet_values) {
    var links = [];
    if (facet_values) {
      for (var i = 0, l = facet_values.length; i < l; i++) {
        links.push(AjaxSolr.theme('facet_link', facet_values[i], this.facetHandler(facet_field, facet_values[i])));
      }
    }
    return links;
  },

  facetHandler: function (facet_field, facet_value) {
	    alert('in facethandler...');
	    var self = this;
	    return function () {
	      self.manager.store.remove('fq');
	      self.manager.store.addByValue('fq', facet_field + ':' + facet_value);
	      self.manager.doRequest(0);
	      return false;
	    };
  },
  
  
  afterRequest: function () {
	  
		
	    $(this.target).empty();
	    for (var i = 0, l = this.manager.response.response.docs.length; i < l; i++) {
	    //if(i < 1) //for debugging the metadata report (remove when done)
	    	//{
	      var doc = this.manager.response.response.docs[i];
	      $(this.target).append(AjaxSolr.theme('result', doc, AjaxSolr.theme('snippet', doc)));
	      //$(this.target).append(AjaxSolr.theme('result2', doc, AjaxSolr.theme('snippet2', doc)));
	      var items = [];
	      //items = items.concat(this.facetLinks('topics', doc.topics));
	      //items = items.concat(this.facetLinks('organisations', doc.organisations));
	      //items = items.concat(this.facetLinks('exchanges', doc.exchanges));
	       //AjaxSolr.theme('list_items', '#links_' + doc.id, items);
	    	//}
	    }
	    
  },
  
  
  init: function () {
	  $('a.more').livequery(function () {
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
	  
	 
	 
	  
	  $('a.met').livequery(function () {

		  $(this).click(function () {
			
			  
			  	//need to gather the following here...
			  	//metadatafilename
			  	//metadataformat
			  	//id
			  	//title
			  	var id = $(this).next().attr('id');
			  	var title = $(this).next().attr('title');
			  	//these still need to be added
			  	var metadatafilename = 'ORNL-oai_dif.json';
			  	var metadatafileformat = 'oai';
			  	
			  	//send the info to the metadata_report
			  	metadata_report(id,title,metadatafilename,metadatafileformat);
			
			
		  });
		  });
	  
	  
	  $("a[rel]").overlay({

			mask: 'darkred',
			effect: 'apple',

			onBeforeLoad: function() {

				// grab wrapper element inside content
				var wrap = this.getOverlay().find(".contentWrap");

				// load the page specified in the trigger
				wrap.load(this.getTrigger().attr("href"));
			}

		});
	  
  }
  
   
	
});




	


})(jQuery);



