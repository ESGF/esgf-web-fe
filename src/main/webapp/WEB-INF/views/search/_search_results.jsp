<!-- Search results -->
<%@ include file="/WEB-INF/views/common/include.jsp" %>


<div id="search-results">

<c:if test="${param['search_model'] != null}">
		
	<c:out value="results"/>
    <c:out value="${search_input.text}"/>
	<c:out value="${search_input.constraints}"/>		
	<c:forEach var="text2" items="${search_input.text}">
    	<c:out value="javascript:set(text)"/>
	</c:forEach>
	 <!-- 
	<a href='javascript:setFacet("${entry.key}","${subFacet.label}")'> 
		 -->
		 <script type="text/javascript">
document.write("This message is written by JavaScript");
</script>

<!-- Table of search results -->
<table class="datatable">
	<c:set var="j" value="0" />
	<c:forEach var="record" items="${search_output.results}">
		<c:set var="j" value="${j+1}"/>
		<tr>
			<td><c:out value="${j}"/>.</td>
			<td>
				<a href="${record.fields['url'][0]}"><c:out value="${record.fields['title'][0]}"/></a>
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

</c:if>

</div>	