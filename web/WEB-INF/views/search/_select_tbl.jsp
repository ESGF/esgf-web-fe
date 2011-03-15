<%@ page isELIgnored="true" %>




<script id="cartTemplate" type="text/x-jquery-tmpl">
 	<tr class="top_level_data_item">
		<td class="left_table_header">${doc.id} </td><td id="${doc.id}" > <a href="#" class="showAllChildren">Expand</a> | <a href="#" class="wgetAllChildren">Download wget (${$item.sizeConversion(doc.size)})</a></td>
	</tr>
	{{each(i) doc.file_url}}
		<tr class="rows_${$item.replacePeriods(doc.id)}" style="display:none"> 
		{{if doc.service_type[i] == 'HTTPServer'}}
			<td class="left_download">  <div class="child" id="${doc.file_id[i]}">${$item.abbreviate(doc.file_id[i])} </div></td> <td class="right_download">  <a href="${doc.file_url[i]}">Download (${$item.sizeConversion(doc.file_size[i])})</a>  </td>
		   
		{{/if}}   
		</tr>
	{{/each}}

</script>
