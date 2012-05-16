<table style="margin-left:30px;">
	<c:set var="i" value="0"/>
		<c:forEach var="metadata_items" items="${record['fields']}">
      	<tr>
			<td class="leftAttr">${record['fields'][i].key}:</td>
			<td class="rightAttr">
				<c:set var="j" value="0"/>
				<c:forEach var="metadata_values" items="${record['fields']}">
      	
					<div>${record['fields'][i].values[j]} </div>
	  				<c:set var="j" value="${j+1}"/>
	  				
				</c:forEach>
			</td>
		</tr>
	  		<c:set var="i" value="${i+1}"/>
     	</c:forEach>
	</table>