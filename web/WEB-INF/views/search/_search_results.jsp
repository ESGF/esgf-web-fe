<%@ include file="/WEB-INF/views/common/include.jsp" %>


<div id="search_summary"> Search summary: ${search_output.counts} records returned.</div>

<table class="data search_data">

	<c:set var="j" value="0" />
	<c:forEach var="record" items="${search_output.results}">
		<c:set var="j" value="${j+1}"/>
		<tr>
			<td>
			 <div class="dlink">
				<a href="${record.fields['url'][0]}"><c:out value="${record.fields['title'][0]}"/></a>
			 	<a href="javascript:metadata_link()">metadata</a>
			 </div>
				
				<c:if test="${record.fields['description'][0]}"><br/>
				<c:out value="${record.fields['description'][0]}"/>
				</c:if>
				<br/>
				
				<c:forEach var="field" items="${record.fields}">
					<c:if test="${field.key != 'title'  && field.key != 'url' && 
					       field.key != 'type' && field.key != 'score'}">
						<c:out value="${field.key}"/>=<c:out value="${field.value}"/>
					</c:if>
				</c:forEach>
			</td>
		</tr>
	</c:forEach>
</table>

<div id="pagination">


        <c:set var="last" value="${search_input.offset+fn:length(search_output.results)}"/>
        Total # of results: ${search_output.counts}
        <c:if test="${search_input.offset>0}">
            | <a href="javascript:search(<c:out value="${search_input.offset - search_input.limit}"/>)"> &#171; Back</a>
        </c:if>
        <c:if test="${fn:length(search_output.results)>0}">
            | Showing results: <c:out value="${search_input.offset+1}"/>-<c:out value="${last}"/>
        </c:if>
        <c:if test="${last < search_output.counts}">
            | <a href="javascript:search(<c:out value="${last}"/>)">Next &#187;</a>
        </c:if>

</div>


