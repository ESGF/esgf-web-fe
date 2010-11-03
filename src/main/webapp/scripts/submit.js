/**
 * Ajax submit
 * 
 * - fwang2@ornl.gov
 * 
 */
$(document).ready(function(){
	
	var opt_facets = {			
			type: 'GET',
			//beforeSubmit: showRequest,
			//success: showResponse,
			complete: styleFacets,
			target: '#facets',
			url: 'search/facets'
				
	};
	
	var opt_results = {
			type: 'GET',
			//beforeSubmit: showRequest,
			success: restoreOpacity,
			target: '#search_results',
			url: 'search/results'
	}
	
	$('#search-form').bind('submit', function() {
		// inside event callback, 'this' is the DOM element
		// so we first warp it in a jQuery object and then
		// invoke ajaxSubmit
		$("#search_results").css('opacity', 0.4);
		$(this).ajaxSubmit(opt_results);
		
		// must return false to prevent standard browser submit
		return false;
	});

	$('#search-form').bind('submit', function() {
		//$(this).ajaxSubmit(opt_facets);

		return false;
	});
	
});


function showRequest(formData, jqForm, options) {
	var queryString = $.param(formData);
	
	alert('About to submit:\n\n' + queryString);
}

function showResponse(responseText, statusText, xhr, $form) {
//    alert('status: ' + statusText + '\n\nresponseText: \n' + responseText + 
//    '\n\nThe output div should have already been updated with the responseText.'); 
	
	$('ul.acitem > li').trigger('click');

}

/**
 * This is where we clean up the mess (facets data transferred back)
 * 
 * @param XMLHttpRequest
 * @param textStatus
 */
function styleFacets(XMLHttpRequest, textStatus) {
	$('.acitem > li > a').trigger('dbclick');
}

function restoreOpacity() {
	$('#search_results').css("opacity", 1);
}
function getSearchForm() {
	return $("#search-form")[0];
}

function setFacet(facetKey, facetValue) {
	var searchForm = getSearchForm();
	var input = searchForm[facetKey];
	input.value = facetValue;
	search(0);
}

function resetFacet(facetKey) {
	var searchForm = getSearchForm();
	var input = searchForm[facetKey];
	input.value = "";
	search(0);
}
	

function search(offset) {
		var searchForm = getSearchForm();
		searchForm.offset.value = offset;
		// this is the tricky part
		// I have to manually trigger this event
		// and ajax submit must be done within the ready handler
		$("#search-form").trigger("submit");
		//searchForm.submit();
}