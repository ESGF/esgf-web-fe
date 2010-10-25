function getSearchForm() {
	//alert("getSearchForm");
	return document.getElementById("search-form");
}

function setFacet(facetKey, facetValue) {
	//alert("setFacet");
	var searchForm = getSearchForm();
	var input = searchForm[facetKey];
	alert("input2 value: " + input2.value);
	alert("facetKey: " + facetKey + " facetValue: " + facetValue);
	facetValue = "ESIP";
	alert("facetKey: " + facetKey + " facetValue: " + facetValue);
	input.value = facetValue;
	search(0);
}



function resetFacet(facetKey) {
	alert("resetFacet");
	var searchForm = getSearchForm();
	var input = searchForm[facetKey];
	input.value = "";
	search(0);
}



function search(offset) {
	alert("search");
	var searchForm = getSearchForm();
	searchForm.offset.value = offset;
	alert("before submit");
	alert("searchForm limit: " + searchForm.project.value);
	searchForm.submit();
	alert("after submit");
}

