/**
 * Presentation theme for Solr search results
 * 
 * fwang2@ornl.gov
 * 
 */

(function ($) {

AjaxSolr.theme.prototype.result = function (doc, snippet) {
		
	if (doc.title.length > 78) {
		doc.title = doc.title.substring(0,100) + "...";
	}
	
  	var output = '<div class="search-entry"><h4 class="desc"><a href="javascript:metadata_link()">' + doc.title + '</a></h4>' ;
  	output += '<p id="links_' + doc.id + '" class="links"></p>';
  	output += '<p>' + snippet + '</p></div>';
  	return output;
};

AjaxSolr.theme.prototype.snippet = function (doc) {
  
	var output = '';
 
	if (doc.description != undefined)
		doc.text = doc.description[0];
	if (doc.text != undefined) {
		if (doc.text.length > 500) {
		    output += doc.text.substring(0, 500);
		    output += '<span style="display:none;">' + doc.text.substring(500);
		    output += '</span> <a href="#" class="more"> ... more</a>';
		} else {
			output += doc.text;
		}
	} else {
	    output = "No description available.";
	}
	
	return output;
};

AjaxSolr.theme.prototype.tag = function (value, weight, handler) {
  return $('<a href="#" class="tagcloud_item"/>').text(value).addClass('tagcloud_size_' + weight).click(handler);
};

AjaxSolr.theme.prototype.facet_link = function (value, handler) {
  return $('<a href="#"/>').text(value).click(handler);
};

AjaxSolr.theme.prototype.no_items_found = function () {
  return 'no items found in current selection';
};

})(jQuery);
