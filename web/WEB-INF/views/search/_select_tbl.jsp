<%@ page isELIgnored="true" %>


<!--  
<script id="cartTemplate" type="text/x-jquery-tmpl">
    <tr>
	<td >${doc.id}</td><td id="${doc.id}" > <a href="#" class="showChildren">Select</a> | <a href="#">Download all ${doc.child_dataset_url.length} datasets</a></td>
	</tr>
	{{each(i) doc.child_dataset_url}}
		<tr class="rows_${$item.replacePeriods(doc.id)}" style="display:none">
			<td class="left_download"> ${i + 1}: ${doc.child_dataset_id[i]}</td> <td class="right_download">  <a href="${doc.child_dataset_url[i]}">Download</a>  </td>
		</tr>
	{{/each}}
		
</script>
-->

<script id="cartTemplate" type="text/x-jquery-tmpl">

    <tr class="top_level_data_item">
<!--
	<td >${doc.id} </td><td id="${doc.id}" class="right_table_header"> <a href="#" class="showAllChildren">Expand</a> | <a href="#" class="wgetAllChildren">Download all ${doc.child_dataset_url.length} datasets (${$item.sizeConversion(doc.dataset_size)})</a></td>
-->
	<td class="left_table_header">${doc.id} </td><td id="${doc.id}" > <a href="#" class="showAllChildren">Expand</a> | <a href="#" class="wgetAllChildren">Download wget (${$item.sizeConversion(doc.dataset_size)})</a></td>
	</tr>
	{{each(i) doc.child_dataset_url}}
		<tr class="rows_${$item.replacePeriods(doc.id)}" style="display:none">
			<td class="left_download"> <!-- ${i + 1}: --> <div class="child" id="${doc.child_dataset_id[i]}">${$item.abbreviate(doc.child_dataset_id[i])}<!-- $item.abbreviate(${doc.child_dataset_id[i]}) --></div></td> <td class="right_download">  <a href="${doc.child_dataset_url[i]}">Download (${$item.sizeConversion(doc.child_dataset_size[i])})</a>  </td>
		    
		</tr>
	{{/each}}
</script>
