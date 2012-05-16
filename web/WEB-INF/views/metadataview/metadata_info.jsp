<%@ include file="/WEB-INF/views/common/include.jsp" %>

<div class="prepend-2 span-20 append-1 last" id="metadata_section" style="margin-top:10px;">
	<span style="text-align:left;margin-bottom:10px;font-size:26px;">
		Metadata
	</span>
		
	<span style="float:right;margin-bottom:20px;margin-top:10px">
		<select id="s3" multiple="multiple">
			<c:set var="j" value="0"/>
			<c:forEach var="group" items="${MetadataView_KeyArr}">
				<option selected="selected">${MetadataView_KeyArr[j]}</option>
				<c:set var="j" value="${j+1}"/>
			</c:forEach>
	    </select>

	</span>
	<span style="font-size:18px;float:right;margin-top:10px;margin-left:10px;margin-right:10px;">
		Show/Hide Properties
	</span>
	
	<c:set var="j" value="0"/>

	<table style="border: 1px solid black;">
		<tr>
		<th style="border: 1px solid black;"></th>
		<th style="border: 1px solid black;">Property</th>
		<th style="border: 1px solid black;">Value</th>
		</tr>
		<c:forEach var="group" items="${MetadataView_KeyArr}">
			<tr class="meta_rows" id="row_${MetadataView_KeyArr[j]}" style="display:none;border: 1px solid black;">
				<td style="width:20px;font-size:12px;vertical-align:top;border: 1px solid black;"><a class="meta_link" style="cursor:pointer;display:none">expand</a></td>
				
				<td style="width:125px;word-wrap: break-word;font-size:12px;vertical-align:top;border: 1px solid black;">${MetadataView_KeyArr[j]}</td>
			
			
				<td class="cellshort" style="width:250px;word-wrap: break-word;font-size:12px;border: 1px solid black;"><span class="textshort">${MetadataView_ValueArr[j]}</span></td>
				<td class="celllong" style="width:250px;word-wrap: break-word;font-size:12px;display:none;border: 1px solid black;"><span class="textlong">${MetadataView_ValueArr[j]}</span></td>
			
				
			</tr>
        	
        	<c:set var="j" value="${j+1}"/>
		</c:forEach>
		
	</table>
	
</div>

