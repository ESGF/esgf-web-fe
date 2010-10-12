<%@ include file="/WEB-INF/views/common/include.jsp" %>

<!-- Search Text widget-->
<div class="box-colored">
	<div style="text-align: center">
		Enter text &nbsp; <input type="text" name="text" size="30" value="${search_input.text}"/> 
		&nbsp; <input type="submit" value="Search"/>
		&nbsp; <input type="button" value="Reset" onclick="window.location='.'"/>
	</div>
</div>

<!-- Form errors -->
<c:if test="${error_message!=null}">
	<div class="error"><c:out value="${error_message}"/></div>
</c:if>
