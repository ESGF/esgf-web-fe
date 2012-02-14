<%@ page isELIgnored="true" %>




<script id="cartTemplateStyledNew" type="text/x-jquery-tmpl">
	
	<%-- dataset header display --%>
	{{tmpl($data) "#datasetInfo"}}

	<tr>
		<td></td>
	</tr>

	<%-- file display (still need to put this in its own subtemplate--%>
	{{each(i) file}}

			{{if i < 12}}
        		{{if i > 1}}
					<tr class="rows_${$item.replacePeriods(datasetId)}" style="display:none">
						<td style="width: 40px;"><input style="margin-left: 10px;" class="fileLevel" type="checkbox" class="fileId" id="${fileId}" checked="true" value="${urls.url[1]}"/></td>

						<td style="width: 325px;padding-left:10px;font-size:11px;">
							<div style="word-wrap: break-word;"> 
							<span style="font-weight:bold"> ${$item.abbreviate(fileId)} (${$item.sizeConversion(size)}) </span>
							<br /> 
							<span style="font-style:italic">Tracking Id: ${tracking_id}</span>
							<br />
							<span style="font-style:italic">Checksum: ${checksum} (${checksum_type})</span>
							</div>
						</td>

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
			{{/if}}
			
		{{/each}}
	<tr>
		<td></td>
	</tr>	

	
	<%-- more files display --%>
	{{tmpl($data) "#view_more_files"}}
		

	
</script>


<script id="datasetInfo" type="text/x-query-tmpl">
<tr style="margin-top:50px;" class="top_level_data_item ${$item.replacePeriods(datasetId)}" id="${$item.replacePeriods(datasetId)}" >
		{{if count > 0}}
			<td style="width: 40px;"><input class="topLevel" type="checkbox" id="${datasetId}" name="${datasetId}" checked="true" /> </td>
		{{else}}
			<td style="width: 40px;"><input class="topLevel" disabled="true" type="checkbox" id="${datasetId}" name="${datasetId}" checked="true" /> </td>
		{{/if}}
		<td style="width: 300px;font-size:13px">
			{{if count > 0}}
			<div style="word-wrap: break-word;font-weight:bold">${datasetId} 
				 (${count} files) 
			</div>
			{{else}}
        	<div style="word-wrap: break-word;font-weight:bold;color:gray">${datasetId} 
				 
			</div>
			<div style="word-wrap: break-word;font-size:10px;font-style:italic;font-weight:bold;color:gray;margin-top:5px">
				NOTE: There are no files in this dataset that match the search criteria 
			</div>
			{{/if}}

		</td>		
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
</script>


<script id="fileInfo" type="text/x-query-tmpl">
	<%--
	{{each(i) file}}
		{{if i < 12}}
        		{{if i > 1}}
					<tr><td></td><td>aaa</td></tr>
					<tr class="rows_${replacePeriodGlobal(datasetId)}"  >
						<td style="width: 40px;"><input style="margin-left: 10px;" class="fileLevel" type="checkbox" class="fileId" id="${fileId}" checked="true" value="${urls.url[1]}"/></td>

						<td style="width: 325px;padding-left:10px;font-size:11px;">
							<div style="word-wrap: break-word;"> 
							<span style="font-weight:bold"> ${replacePeriodGlobal(fileId)} (${replacePeriodGlobal(size)}) </span>
							<br /> 
							<span style="font-style:italic">Tracking Id: ${tracking_id}</span>
							<br />
							<span style="font-style:italic">Checksum: ${checksum} (${checksum_type})</span>
							</div>
						</td>

					</tr>
				{{/if}}
		{{/if}}
	{{/each}}
	--%>
--</script>
        
<script id="view_more_files" type="text/x-jquery-tmpl">


    {{if count > 12}}

		<tr class="view_more_results_${replacePeriodGlobal(datasetId)}" style="display:none">
			<td></td><td><a href="#" class="view_more_results" >View more files</a></td>
		</tr>
		<tr class="appendRow_${replacePeriodGlobal(datasetId)}" >
			<td></td>
		</tr>
	
	{{/if}}
</script>



