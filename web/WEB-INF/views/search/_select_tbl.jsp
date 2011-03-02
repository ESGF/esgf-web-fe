<%@ page isELIgnored="true" %>


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

