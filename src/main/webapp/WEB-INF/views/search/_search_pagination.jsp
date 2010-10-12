<!-- Pagination -->
<%@ include file="/WEB-INF/views/common/include.jsp" %>


<c:if test="${param['search_model'] != null}">
	<div align="center">
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
</c:if>