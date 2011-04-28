<%@ page isELIgnored="true" %>

<script id="facetTemplate" type="text/x-jquery-tmpl">
	<ul class="ul_class" >
		
		<li class="li_facet_class">
	   			<a class="showFacetValues">${Facet_name}</a>
		</li>

		{{each Facet_values}}
			<li class="li_facet_value_class" id="${$value}" style="display:none">
        		<a href="#" id="${$value}" class="alink" >${$value}</a>

			</li>
    	{{/each}}
	</ul>
</script>






<script id="facetTemplate2" type="text/x-jquery-tmpl">
	<ul class="ul_class2" >
		<li class="li_facet_class2">
	   			<a class="showFacetValues2">${Facet_name}</a>
		</li>
		<li class="facet_container_class2" id="${Facet_name}_facet_container_class2" style="display:none">
			{{each Facet_values}}
				<a>${$value}</a><br>
    		{{/each}}
		</li>
	</ul>
</script>


<script id="facetTemplate3" type="text/x-jquery-tmpl"><ul class="ul_class3" >
		<li class="li_facet_class3">
	   			<a class="showFacetValues3">${Facet_name}</a>
		</li>
		
	</ul>
</script>


<script id="facetTemplate4" type="text/x-jquery-tmpl">
	
</script>





