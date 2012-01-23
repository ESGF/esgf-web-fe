<%@ page isELIgnored="true" %>




<script id="cartTemplateStyledNew" type="text/x-jquery-tmpl">
	<tr style="margin-top:50px;" class="top_level_data_item ${$item.replacePeriods(datasetId)}" id="${$item.replacePeriods(datasetId)}" >
		<td style="width: 40px;"><input class="topLevel" type="checkbox" id="${datasetId}" name="${datasetId}" checked="true" /> </td>
		<td style="width: 300px;font-size:13px"><div style="word-wrap: break-word;font-weight:bold">${datasetId} (${count} files)</div></td>
		<td style="font-size:13px;float:right" id="${datasetId}"> 
			{{if count > 0}}
				<a href="#" class="showAllChildren">Expand</a> | 
				{{if hasHttp > 0}}
				<a href="#" class="wgetAllChildren"> WGET </a> |  
				{{/if}}
				{{if hasGridFTP > 0}}
				<a href="#" class="globusOnlineAllChildren" >Globus Online</a> |
				{{/if}}
			{{/if}}
			<a href="#" class="remove_dataset_from_datacart">Remove</a> 
		</td>
	</tr>
	<tr>
		<td></td>
	</tr>

<!--
{{if technotes.technotes.technote.length > 2}}
	<tr >
		<td></td><td style="cursor:pointer"><a href="#" class="technotes">Show Technotes</a></td>
	</tr>
	{{each(k) technotes.technotes.technote}}
		{{if technotes.technotes.technote[k].name != 'technoteName'}}
			<tr class="rows_${$item.replacePeriods(datasetId)}_technotes" style="display:none">
				<td></td><td><a style="margin-left:20px" href="${technotes.technotes.technote[k].location}" target="_blank" >${technotes.technotes.technote[k].name}</a> </td>
			</tr>
		{{/if}}
	{{/each}}
{{/if}}
-->
   		{{each(i) file}}
        	{{if i > 1}}
				<tr class="rows_${$item.replacePeriods(datasetId)}" style="display:none">
					<td style="width: 40px;"><input style="margin-left: 10px;" class="fileLevel" type="checkbox" class="fileId" id="${fileId}" checked="true" value="${urls.url[1]}"/></td>

					<td style="width: 325px;padding-left:10px;font-size:11px;"><div style="word-wrap: break-word;"> ${$item.abbreviate(fileId)} (${$item.sizeConversion(size)}) </div></td>

					{{each(j) urls.url}}
						{{if services.service[j] == 'HTTPServer'}}
							<td id="${$item.replacePeriods(datasetId)}_http" style="float:right;font-size:11px;"><div id="${urls.url[j]}" style="word-wrap: break-word;vertical-align:middle"><a href="${urls.url[j]}">HTTP </a></div></td>
	   					{{/if}}
						{{if services.service[j] == 'GridFTP'}}
							<td id="${$item.replacePeriods(datasetId)}_gridftp" id="${urls.url[j]}" style="float:right;font-size:11px;"><div id="${urls.url[j]}" style="word-wrap: break-word;vertical-align:middle"><a id="${fileId}" class="go_individual_gridftp" href="#">GridFTP </a></div></td>
	   					{{/if}}
						{{if services.service[j] == 'OPENDAP'}}
							<td id="${$item.replacePeriods(datasetId)}_openid" style="float:right;font-size:11px;"><div id="${urls.url[j]}" style="word-wrap: break-word;vertical-align:middle"><a href="${urls.url[j]}">OPENDAP </a></div></td>
	   					{{/if}}
					{{/each}}

					{{if technotes.technote.length > 2}}
						<td id="${$item.replacePeriods(datasetId)}_openid" style="float:right;font-size:11px;"><div id="d" style="word-wrap: break-word;vertical-align:middle"><a href="${technotes.technote[2].location}" title="${technotes.technote[2].name}" target="_blank">TECHNOTE </a></div></td>
	   				{{/if}}	

					
				</tr>		
			{{/if}}
		{{/each}}
	<tr>
		<td></td>
	</tr>	
	<tr>
		<td></td>
	</tr>
	<tr>
		<td></td>
	</tr>
</script>


<script id="addedCartTemplate" type="text/x-jquery-tmpl">
	<tr class="top_level_data_item" id="${$item.replacePeriods(doc.id)}">
		<td class="left_table_header"><input class="topLevel" type="checkbox" id="${doc.id}" name="${doc.id}" checked="true" />Datasets:  ${doc.id}</td>
		<td id="${doc.id}" class="right_table_header">  <a href="#" class="showAllFiles">Expand</a> |  <a href="#" class="wgetAllChildren"> WGET </a> | <%-- <a href="#" class="globusOnlineAllChildren">Globus Online</a> | --%> <a href="#" class="remove_dataset_from_datacart">Remove</a> </td>
    </tr>
</script>

<script id="cartTemplateStyled" type="text/x-jquery-tmpl">
	<tr class="top_level_data_item" id="${$item.replacePeriods(dataset_id)}" style="text-align:center">
		<td style="width: 40px;"><input class="topLevel" type="checkbox" id="${dataset_id}" name="${dataset_id}" checked="true" /> </td>
		<td style="width: 375px;font-size:13px"><div style="word-wrap: break-word;font-weight:bold">${dataset_id} (${file.length-1} files)</div></td>
    	<td style="font-size:13px;float:right" id="${dataset_id}"> <a href="#" class="showAllChildren">Expand</a> | <a href="#" class="wgetAllChildren"> WGET </a> |  <a href="#" class="remove_dataset_from_datacart">Remove</a> </td>
    </tr>
	{{each(i) file}}
    	{{if i != 0}}
        	<tr class="rows_${$item.replacePeriods(dataset_id)}" style="display:none">
				<td style="width: 40px;"><input type="checkbox" id="${$item.replacePeriods(file_id)}" checked="true" value="${url}"/></td>
				<td style="width: 425px;padding-left:10px;font-size:11px;"><div style="word-wrap: break-word;">${$item.abbreviate(file_id)}</div></td>
				<td style="float:right;font-size:11px;"><div style="word-wrap: break-word;vertical-align:middle"><a href="${url}">Download (${$item.sizeConversion(size)})</a></div></td>
	   		</tr>
		{{/if}}
	{{/each}}
</script>


<%-- 
<script id="cartTemplate" type="text/x-jquery-tmpl">
	<tr class="top_level_data_item" id="${$item.replacePeriods(dataset_id)}">
		<td class="left_table_header"><input class="topLevel" type="checkbox" id="${dataset_id}" name="${dataset_id}" checked="true" /> Dataset: ${dataset_id} (${file.length-1} files)</td>
    	<td id="${dataset_id}" class="right_table_header"> <a href="#" class="showAllChildren">Expand</a> | <a href="#" class="wgetAllChildren"> WGET </a> | <a href="#" class="remove_dataset_from_datacart">Remove</a> </td>
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
--%>

<%-- 
=======
<%@ page isELIgnored="true" %>


<script id="addedCartTemplate" type="text/x-jquery-tmpl">
	<tr class="top_level_data_item" id="${$item.replacePeriods(doc.id)}">
		<td class="left_table_header">
			<input class="topLevel" type="checkbox" id="${doc.id}" name="${doc.id}" checked="true" />Dataset:  ${doc.id}
		</td>
		<td id="${doc.id}" class="right_table_header">  
			<a href="#" class="showAllFiles">Expand</a> |  
			<a href="#" class="wgetAllChildren"> WGET </a> | 
			<a href="#" class="remove_dataset_from_datacart">Remove</a> </td>
    </tr>
</script>

<script id="cartTemplate" type="text/x-jquery-tmpl">
	<tr class="top_level_data_item" id="${$item.replacePeriods(dataset_id)}">
		<td class="left_table_header">
			<input class="topLevel" type="checkbox" id="${dataset_id}" name="${dataset_id}" checked="true" /> Dataset: ${dataset_id} (${file.length-1} files)</td>
    	<td id="${dataset_id}" class="right_table_header" > 
			<a href="#" class="showAllChildren" style="float:right">Expand</a> 
			<span style="float:right;margin-left:5px;margin-right:5px"> | </span> 
			<a href="#" class="wgetAllChildren" style="float:right;"> WGET </a> 
			<span style="float:right;margin-left:5px;margin-right:5px"> | </span> 
			{{if GridFTP == true}}
				<a href="#" class="globusOnlineAllChildren" style="float:right" >Globus Online</a> 
				<span style="float:right;margin-left:5px;margin-right:5px"> | </span>
			{{/if}} 
			<a href="#" class="remove_dataset_from_datacart" style="float:right">Remove</a> 
		</td>
    </tr>


	{{each(i) file}}
        {{if i != 0}}
				
				{{each(j) urls.url}}
					
					{{if services.service[j] == 'HTTPServer'}}
						<tr class="rows_${$item.replacePeriods(dataset_id)}_http">
							<td class="left_download">
								<div class="child" id="${file_id}" title="${file_id}" ><input type="checkbox" id="${$item.replacePeriods(file_id)}" checked="true" value="${url}"/>${$item.abbreviate(file_id)} (Size: ${$item.sizeConversion(size)})</div>
							</td>
							<td class="right_download">
								<a href="${urls.url[j]}">Download (HTTP)</a> |
							</td>
						</tr>
					{{/if}}
					{{if services.service[j] == 'OPENDAP'}}
						<tr class="rows_${$item.replacePeriods(dataset_id)}_opendap">
							<td class="left_download">
								<div class="child" id="${file_id}" title="${file_id}" ><input type="checkbox" id="${$item.replacePeriods(file_id)}" checked="true" value="${url}"/>${$item.abbreviate(file_id)} (Size: ${$item.sizeConversion(size)})</div>
							</td>
							<td class="right_download">
								<a href="#">Download (OPENDAP)</a> |
							</td>
						</tr>
					{{/if}}
					{{if services.service[j] == 'GridFTP'}}
						<tr class="rows_${$item.replacePeriods(dataset_id)}_gridftp">
							<td class="left_download">
								<div class="child" id="${file_id}" title="${file_id}" ><input type="checkbox" id="${$item.replacePeriods(file_id)}" checked="true" value="${urls.url[j]}"/>${$item.abbreviate(file_id)} (Size: ${$item.sizeConversion(size)})</div>
							</td>
							<td class="right_download">
								<a class="go_individual_gridftp" href="#" id="${urls.url[j]}" >Download (GridFTP)</a> |
							</td>
						</tr>
					{{/if}}
					</td>
					</tr>
            	{{/each}}
		{{/if}}
	{{/each}}

</script>
--%>
