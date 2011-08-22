<%@ page isELIgnored="true" %>

<script id="facetTemplate" type="text/x-jquery-tmpl">
	<ul class="ul_class" >
		
		<li class="li_facet_class">
	   			<a class="showFacetValues">${Facet_name} (${Facet_max_count})</a>
		</li>

		{{each Facet_values}}
			<li class="li_facet_value_class" id="${Facet_name}_${$value}" style="display:none">
        		<a href="#" id="${$value}" class="alink" >${$value} (${Facet_counts[$index]}) </a>

			</li>
    	{{/each}}
	</ul>
</script>










