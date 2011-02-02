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
	  
	      var doc = this.manager.response.response.docs[i];
	      //if( i < 2)
		   // { 
	      if(postSolrProcessing(doc)) {
	    	  console.log('\t\t\t\tKeep this record');
	    	  $(this.target).append(AjaxSolr.theme('result', doc, AjaxSolr.theme('snippet', doc)));
		      //$(this.target).append(AjaxSolr.theme('result2', doc, AjaxSolr.theme('snippet2', doc)));
		      var items = [];
	      } else {
	    	  console.log('\t\t\t\tDo not keep this record ' + doc.title);
	      }
	      
		   // }
	      /*
	      $(this.target).append(AjaxSolr.theme('result', doc, AjaxSolr.theme('snippet', doc)));
	      //$(this.target).append(AjaxSolr.theme('result2', doc, AjaxSolr.theme('snippet2', doc)));
	      var items = [];
	      */
	      //items = items.concat(this.facetLinks('topics', doc.topics));
	      //items = items.concat(this.facetLinks('organisations', doc.organisations));
	      //items = items.concat(this.facetLinks('exchanges', doc.exchanges));
	       //AjaxSolr.theme('list_items', '#links_' + doc.id, items);
		   
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
	  
	  

	  
  }
  
   
	
});




})(jQuery);




function postSolrProcessing(doc)
{
	var isInRange = true;
	if($("input[name='areaGroup']:checked").val() == 'circle') {
		
		//get the radius
		var radius = $("input[name='radius']").val();
		
		var center = centroidCenter;
		
		//get extreme points - they must ALL be within range
		var se = new google.maps.LatLng(doc.south_degrees, doc.east_degrees);
		var ne = new google.maps.LatLng(doc.north_degrees, doc.east_degrees);
		var sw = new google.maps.LatLng(doc.south_degrees, doc.west_degrees);
		var nw = new google.maps.LatLng(doc.north_degrees, doc.west_degrees);
		
		var dist = new Array();
		dist[0] = distanceInKm(se,center);
		dist[1] = distanceInKm(ne,center);
		dist[2] = distanceInKm(sw,center);
		dist[3] = distanceInKm(nw,center);
		
		for (var i = 0, l = dist.length; i < l; i++) {
			if(dist[i] > radius) {
				isInRange = false;
			}
		}
		
	}
	
	
	return isInRange;
	
}

function distanceInKm(point1,point2) {
	
	var seLat1 = point1.lat()*Math.PI/180.0;
	var seLat2 = point2.lat()*Math.PI/180.0;
	var lngDiff = (point2.lng()-point1.lng())*Math.PI/180.0;
	
	//note this is in feet
	var a = Math.acos(Math.sin(seLat1) * Math.sin(seLat2) + Math.cos(seLat1)
			* Math.cos(seLat2) * Math.cos(lngDiff)) * 20902231.0029; 
	
	//convert feet to km
	var aKm = a/3280.84;
	
	
	return aKm;
}