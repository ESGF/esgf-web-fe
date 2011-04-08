<%@ page isELIgnored="true" %>




<script id="cartTemplate" type="text/x-jquery-tmpl">
 	<tr class="top_level_data_item">
		<td class="left_table_header"><input class="topLevel" type="checkbox" id="${doc.id}" name="${doc.id}" checked="true" style="display:none" />${doc.id} </td><td id="${doc.id}" class="right_table_header"> <a href="#" class="showAllChildren">Expand</a> | <a href="#" class="wgetAllChildren">Download wget (${$item.sizeConversion(doc.size)})</a></td>
	</tr>
	{{each(i) doc.file_url}}
		<tr class="rows_${$item.replacePeriods(doc.id)}" style="display:none"> 
		{{if doc.service_type[i] == 'HTTPServer'}}
			<td class="left_download">  <div class="child" id="${doc.file_id[i]}"  title="${doc.file_id[i]}"> <input type="checkbox" id="${$item.replacePeriods(doc.file_id[i])}" checked="true" value="${doc.file_url[i]}"/> ${$item.abbreviate(doc.file_id[i])} </div></td> <td class="right_download">  <a href="${doc.file_url[i]}">Download (${$item.sizeConversion(doc.file_size[i])})</a>  </td>
		   
		{{/if}}   
		</tr>
	{{/each}}

</script>
