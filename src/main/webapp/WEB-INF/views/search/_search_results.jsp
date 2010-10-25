<!-- Search results -->
<%@ include file="/WEB-INF/views/common/include.jsp" %>



<c:if test="${param['search_model'] != null}">

<div id="search_summary"> Search summary: ${search_output.counts} records returned.</div>
		
<table class="data">

	<c:set var="j" value="0" />
	<c:forEach var="record" items="${search_output.results}">
		<c:set var="j" value="${j+1}"/>
		<tr>
			<%-- <td><c:out value="${j}"/>.</td> --%>
			<td>
			 <div class="dlink">
				<a href="${record.fields['url'][0]}"><c:out value="${record.fields['title'][0]}"/></a>
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

</c:if>


