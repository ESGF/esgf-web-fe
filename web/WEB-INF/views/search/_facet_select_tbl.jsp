<%@ page isELIgnored="true" %>

<script id="facetTemplate" type="text/x-jquery-tmpl">
	<ul class="ul_class" >
		
		<li class="li_facet_class">
	   			<a class="showFacetValues">${Facet_label} </a>
		</li>

		{{each Facet_values}}
			<!-- <li class="li_facet_value_class ${Facet_name}" id="${Facet_name}_${$value}" style="display:none">  -->
			<li class="li_facet_value_class ${Facet_name}" style="display:none"> 

        		<!-- <a href="#" id="${$value}" class="alink" >${$value} (${Facet_counts[$index]}) </a> -->
<!-- <a href="#" id="${$value}" class="alink" >${$item.truncate($value,20)}</a> -->
<div style="word-wrap: break-word;font-weight:bold;"><a style="padding-right:10px;" href="#" id="${$value}" class="alink" >${$item.truncate($value,20)} (${Facet_counts[$index]})</a></div>
			</li>
    	{{/each}}
	</ul>
</script>


<script id="facetTemplate1" type="text/x-jquery-tmpl">
 <ul class="ul_class" >
		
		<li class="li_facet_class">
			{{if Search_counts < 3}}
				{{if Facet_label == 'Variable' || Facet_label == 'Ensemble' || Facet_label == 'Variable Long Name' || Facet_label == 'CF Standard Name'}}
					<a class="showFacetValues" style="background-color:#808080" id="${Facet_label}">${Facet_label}</a>	
				{{else}}
					<a class="showFacetValues" style="" id="${Facet_label}">${Facet_label}</a>
				{{/if}}
			{{else}}
					<a class="showFacetValues" style="" id="${Facet_label}">${Facet_label}</a>					
			{{/if}}
		</li>

		{{each Facet_values}}
			<li class="li_facet_value_class ${Facet_name}" style="display:none"> 

<div style="word-wrap: break-word;font-weight:bold;"><a style="padding-right:10px;" href="#" id="${$value}" class="alink" >${$item.truncate($value,20)} (${Facet_counts[$index]})</a></div>
			</li>
    	{{/each}}
	</ul>
</script>






