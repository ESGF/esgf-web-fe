<%@ page isELIgnored="true" %>

<script id="metadata_summaryTemplate" type="text/x-jquery-tmpl">
    
</script>

<script id="cartTemplate" type="text/x-jquery-tmpl">
	<tr class="top_level_data_item" id="${$item.replacePeriods(dataset_id)}">
		<td class="left_table_header"><input class="topLevel" type="checkbox" id="${dataset_id}" name="${dataset_id}" checked="true" /> Dataset: ${dataset_id} (${file.length-1} file)</td>
    	<td id="${dataset_id}" class="right_table_header"> <a href="#" class="showAllChildren">Expand</a> | <a href="#" class="wgetAllChildren"> WGET </a> | <a href="#" class="globusOnlineAllChildren">Globus Online</a> |  <a href="#" class="remove_dataset_from_datacart">Remove</a> </td>
    </tr>
	{{each(i) file}}
        {{if i != 0}}
        <tr class="rows_${$item.replacePeriods(dataset_id)}" style="display:none">
            <td class="left_download"> <div class="child" id="${file_id}"  title="${file_id}"> <input type="checkbox" id="${$item.replacePeriods(file_id)}" checked="true" value="${url}"/> ${$item.abbreviate(file_id)} </div></td>
        	<td class="right_download"> <a href="${url}">Download (${$item.sizeConversion(size)})</a> </td> 
	   </tr>
		{{/if}}
	{{/each}}
</script>

<script id="cartTemplate1" type="text/x-jquery-tmpl">
 	<tr class="top_level_data_item" id="${$item.replacePeriods(id)}">
       <td class="left_table_header"><input class="topLevel" type="checkbox" id="${id}" name="${id}" checked="true" /> ${id} </td>
       <td id="${id}" class="right_table_header"> <a href="#" class="showAllChildren">Expand</a> | <a href="#" class="wgetAllChildren"> wget </a> | <a href="#" class="remove_dataset_from_datacart">Remove</a> </td>
    </tr>
	{{each(i) file}}
       <tr class="rows_${$item.replacePeriods(dataset_id)}" style="display:none">
          <td class="left_download"> <div class="child" id="${file_id}"  title="${file_id}">  <input type="checkbox" id="${$item.replacePeriods(file_id)}" checked="true" value="${url}"/> ${$item.abbreviate(file_id)} </div></td>
          <td class="right_download"> <a href="${url}">Download (${$item.sizeConversion(size)})</a> </td> 
	   </tr>
    {{/each}}

</script>

<script id="newcartTemplate" type="text/x-jquery-tmpl">
 	<tr class="top_level_data_item" id="${$item.replacePeriods(dataset_id)}">
		<td class="left_table_header"><input class="topLevel" type="checkbox" id="${dataset_id}" name="${dataset_id}" checked="true" style="display:none" />${dataset_id}</td>
		<td id="${dataset_id}" class="right_table_header"> <a href="#" class="showAllChildren">Expand</a> | <a href="#" class="wgetAllChildren">wget </a> | <a href="#" class="remove_dataset_from_datacart">Remove</a></td>
	</tr>
	{{each(i) file}} 
		<tr class="rows_${$item.replacePeriods(dataset_id)}" style="display:none"> 
			<td class="left_download"> <div class="child" id="${file_id}"  title="${file_id}"> <input type="checkbox" id="${$item.replacePeriods(file_id)}" checked="true" value="${url}"/> ${$item.abbreviate(file_id)} </td> 
			<td class="right_download">  <a href="${url}">Download (${$item.sizeConversion(size)})</a>  </td>
		</tr>
	{{/each}}
</script>

<script id="oldcartTemplate" type="text/x-jquery-tmpl">
 	<tr class="top_level_data_item" id="${$item.replacePeriods(doc.id)}">
		<td class="left_table_header"><input class="topLevel" type="checkbox" id="${doc.id}" name="${doc.id}" checked="true" style="display:none" />${doc.id} </td><td id="${doc.id}" class="right_table_header"> <a href="#" class="showAllChildren">Expand</a> | <a href="#" class="wgetAllChildren">wget (${$item.sizeConversion(doc.size)})</a> | <a href="#" class="remove_dataset_from_datacart">Remove</a></td>
	</tr>
	{{each(i) doc.file_url}}
		<tr class="rows_${$item.replacePeriods(doc.id)}" style="display:none"> 
		{{if doc.service_type[i] == 'HTTPServer'}}
			<td class="left_download">  <div class="child" id="${doc.file_id[i]}"  title="${doc.file_id[i]}"> <input type="checkbox" id="${$item.replacePeriods(doc.file_id[i])}" checked="true" value="${doc.file_url[i]}"/> ${$item.abbreviate(doc.file_id[i])} </div></td> <td class="right_download">  <a href="${doc.file_url[i]}">Download (${$item.sizeConversion(doc.file_size[i])})</a>  </td>
		   
		{{/if}}   
		</tr>
	{{/each}}

</script>
