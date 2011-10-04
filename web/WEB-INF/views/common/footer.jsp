<%@ include file="/WEB-INF/views/common/include.jsp" %>
<%@ include file="/WEB-INF/views/search/script_search.jsp" %>

<div class="span-24 info last" >
	<div class="span-2"><p></p></div>
	<div id="disclaimer" class="span-20" style="padding-top:10px;margin-bottom:5px;text-align:center">
	</div>	
	<div class="span-2 last" ><p></p></div>
</div>


<div class="span-24 last footer">

	<div class="span-15 left">
		<div class="footerleft">
			<p>
			<sec:authentication property="principal" var="principal"/>
			<c:choose>
		    	<c:when test="${principal=='anonymousUser'}">Guest User</c:when>
		   		<c:otherwise>User: <c:out value="${principal.username}"/></c:otherwise>
		   	</c:choose>
		   	| ESGF P2P Version <spring:message code="esgf.footer.version" />
		   	</p> 
		</div>
	</div>
	
	<div class="prepend-3 span-6 last">
		<div class="footerright">
        	<a href="#">Privacy Policy</a> | <a href="#">Terms of Use </a> 
        </div>
	</div>
</div>  

	
<script type="text/javascript">
$(function(){
	if($.browser.msie) {
		alert("ESGF currently does not support Microsoft Internet Explorer.  Please try the Mozilla Firefox, Apple Safari, or Google Chrome browser.");
		var disclaimer = '<p style="color:red;font-weight:bold;border-top:1px dashed #e8ddcf;margin-top:10px">' +
						 'IMPORTANT: The ESGF P2P system interface may be viewed using the Mozilla Firefox, Google Chrome, or Apple Safari browsers. ' +
						 'Support for the Microsoft Internet Explorer browser will be included in subsequent releases.' +
						 '</p>';
		$('div#disclaimer').append(disclaimer);
	}
});

</script>
