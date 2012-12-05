<%@ page isELIgnored="true" %>

<script id="cartTemplateStyledNew2" type="text/x-jquery-tmpl">


{{each(i) doc}}
	{{if count > 0}}
		<tr style="margin-top:50px;" class="top_level_data_item"  >
			{{if count > 0}}
				<td style="width: 40px;"><input class="topLevel" type="checkbox" checked="true" alt="checkbox" /> </td>
			{{else}}
				<td style="width: 40px;"><input class="topLevel" disabled="true" type="checkbox" checked="true" alt="checkbox" /> </td>
			{{/if}}	

			<td style="width: 300px;font-size:13px">
				{{if count > 0}}
					<div style="word-wrap: break-word;font-weight:bold"  ><span class="datasetId">${datasetId}</span>
						(<span class="datasetCount_${$item.replaceChars(datasetId)}">${count}</span> files) 
					</div>
				{{else}}
        			<div style="word-wrap: break-word;font-weight:bold;color:gray" class="datasetId"><span class="datasetId">${datasetId}</span>
				 
					</div>
					<div style="word-wrap: break-word;font-size:10px;font-style:italic;font-weight:bold;color:gray;margin-top:5px">
						NOTE: There are no files in this dataset that match the search criteria 
					</div>
				{{/if}}

			</td>
			<td style="font-size:13px;float:right" > 
				{{if count > 0}}
					<a href="#" class="showAllFiles" id="${$item.replacePeriods(datasetId)}">Expand</a> | 
					{{if hasHttp > 0}}
						<a href="#" class="wgetAllFiles"> WGET </a> |  
					{{/if}}
					{{if hasGridFTP > 0}}
						<a href="#" class="globusOnlineAllFiles" >Globus Online</a> |
					{{/if}}
				{{/if}}
				<a href="#" class="remove_dataset">Remove</a> 
			</td>
		</tr>
		
	{{else}}
		<tr style="margin-top:50px;" class="top_level_data_item"  >
			<td style="width: 40px;"><input class="topLevel" disabled="true" type="checkbox" checked="true" alt="checkbox" /> </td>
			<td style="width: 300px;font-size:13px">
				
				<div style="word-wrap: break-word;font-weight:bold;color:gray"><span class="datasetId">${datasetId}</span> 
				</div>
				<div style="word-wrap: break-word;font-size:10px;font-style:italic;font-weight:bold;color:gray;margin-top:5px">
					NOTE: There are no files in this dataset that match the search criteria 
				</div>
			</td>
			<td style="font-size:13px;float:right" > 
				<a href="#" class="remove_dataset">Remove</a> 
			</td>
		</tr>


	{{/if}}

	{{if count > 0}}

		{{each(j) files.file}}
			<tr class="file_rows_${$item.replaceChars(datasetId)}" style="display:none">
				<td style="width: 40px;">
					<input style="margin-left: 10px;display:none" 
						   class="fileLevel" 
						   type="checkbox" 
						   alt="checkbox"
                           class="fileId" 
                           id="${fileId}" 
                           checked="true" 
                           value="${fileId}"/>
                </td>
				<td style="width: 325px;padding-left:10px;font-size:11px;">
					<div style="word-wrap: break-word;"> 
						<span style="font-weight:bold"> ${fileId} (${$item.sizeConversion(size)}) </span>
						<br /> 
						<span style="font-style:italic">Tracking Id: ${tracking_id}</span>
						<br />
						<span style="font-style:italic">Checksum: ${checksum} (${checksum_type})</span>
					</div>
				</td>
				{{each(k) services.service}}
					{{if services.service[k]  == 'HTTPServer'}}
						<td style="float:right;font-size:11px;">
							<div style="word-wrap: break-word;vertical-align:middle">
								<a href="${urls.url[k]}">HTTP </a>
							</div>
						</td>
					{{/if}}
					{{if services.service[k]  == 'OPENDAP'}}
						<td style="float:right;font-size:11px;">
							<span style="display:none" >${urls.url[k]}</span>
							<div style="word-wrap: break-word;vertical-align:middle">
								<a href="${urls.url[k]}">OPENDAP </a>
							</div>
						</td>
					{{/if}}
					{{if services.service[k]  == 'SRM'}}
						<td style="float:right;font-size:11px;">
							<span style="display:none" >${urls.url[k]}</span>
							<div style="word-wrap: break-word;vertical-align:middle">
								<a href="#">SRM </a>
							</div>
						</td>
					{{/if}}
					{{if services.service[k]  == 'GridFTP'}}
						<td style="float:right;font-size:11px;">
							<span style="display:none" class="gridftp">${urls.url[k]}</span>
							<div style="word-wrap: break-word;vertical-align:middle">
								<a href="#" class="go_individual_gridftp" >GridFTP </a>
							</div>
						</td>
					{{/if}}
				{{/each}}
				{{if technote  != 'NA'}}
					<td style="float:right;font-size:11px;">
						<div style="word-wrap: break-word;vertical-align:middle">
							<a href="${technote}" target="_blank">TECHNOTE </a>
						</div>
					</td>
				{{/if}}
				
			</tr>

		{{/each}}

	{{/if}}
	{{if count > 10}}
		<tr>
			<td></td>
		</tr>
		<tr class="view_files_${$item.replaceChars(datasetId)}" style="display:none">
			<td></td>
			<td style="display:none"><span class="datasetId">${datasetId}</span></td>
			<td><a href="#" class="view_more_files" >View more files</a></td>
		</tr>
		<tr class="file_append_${$item.replaceChars(datasetId)}">
			<td></td>
		</tr>
	{{/if}}

{{/each}}

</script>


<script id="fileTemplate" type="text/x-jquery-tmpl">
	{{each(j) files.file}}
		<tr>
<td style="width: 40px;">td ${j}</td>
<td style="width: 340px;">td ${j}</td>
<td style="width: 40px;">td ${j}</td>
		</tr>
	{{/each}}
</script>


<%--
			<tr class="rows_${$item.replacePeriods(datasetId)}" style="display:none">
				<td style="width: 40px;"><input style="margin-left: 10px;" class="fileLevel" type="checkbox" alt="checkbox" class="fileId" id="${fileId}" checked="true" value="${urls.url[1]}"/></td>
				
				<td style="width: 325px;padding-left:10px;font-size:11px;">
					<div style="word-wrap: break-word;"> 
						<span style="font-weight:bold"> ${$item.abbreviate(fileId)} (${$item.sizeConversion(size)}) </span>
						<br /> 
						<span style="font-style:italic">Tracking Id: ${tracking_id}</span>
						<br />
						<span style="font-style:italic">Checksum: ${checksum} (${checksum_type})</span>
					</div>
				</td>
				{{each(j) services.service}}
						{{if services.service[j] == 'HTTPServer'}}
							<td id="${$item.replacePeriods(datasetId)}_http" style="float:right;font-size:11px;"><div id="${urls.url[j]}" style="word-wrap: break-word;vertical-align:middle"><a href="${urls.url[j]}">HTTP </a></div></td>
						{{/if}}
						{{if services.service[j] == 'GridFTP'}}
							<td id="${$item.replacePeriods(datasetId)}_gridftp" id="${urls.url[j]}" style="float:right;font-size:11px;"><div id="${urls.url[j]}" style="word-wrap: break-word;vertical-align:middle"><a id="${fileId}" class="go_individual_gridftp" href="#">GridFTP </a></div></td>
	   					{{/if}}
						{{if services.service[j] == 'OPENDAP'}}
							<td id="${$item.replacePeriods(datasetId)}_openid" style="float:right;font-size:11px;"><div id="${urls.url[j]}" style="word-wrap: break-word;vertical-align:middle"><a href="${urls.url[j]}">OPENDAP </a></div></td>
	   					{{/if}}
						{{if services.service[j] == 'SRM'}}
							<td id="${$item.replacePeriods(datasetId)}_srm" style="float:right;font-size:11px;"><div id="${urls.url[j]}" style="word-wrap: break-word;vertical-align:middle"><a href="${urls.url[j]}">SRM </a></div></td>
	   					{{/if}}
				{{/each}}
			<tr>
--%>

