<%@ include file="/WEB-INF/views/common/include.jsp" %>

<div class="span-24 last" >
	<div class="span-2"></div>
	<div id="disclaimer" class="span-20" style="text-align:center;margin-left:50px"> 
	</div>
	<div class="span-2 last" ></div>
</div>

<%-- 
<div class="span-24 info last" >
	<div class="span-2"><p></p></div>
	<div id="disclaimer" class="span-20" style="text-align:center"><p></p>
	</div>	
	<div class="span-2 last" ><p></p></div>
</div>
--%>

<div class="span-24 last footer">

	<div class="span-12 append-3 left">
		<div class="footerleft">
			<p>
			<sec:authentication property="principal" var="principal"/>
			<c:choose>
		    	<c:when test="${principal=='anonymousUser'}"><span class="footer_openid">Guest User</span></c:when>
		   		<c:otherwise>User: <span class="footer_openid"><c:out value="${principal.username}"/></span></c:otherwise>
		   	</c:choose>
		   	<br /> <span style="font-style:italics;font-size:10px">ESGF P2P Version <spring:message code="esgf.footer.version" /> </span>
		   	</p> 
		</div>
	</div>
	
	
	<div class="prepend-1 span-8 last">
		<div class="footerright">
        	<a href="https://www.llnl.gov/disclaimer.html">Privacy Policy & Legal Notice</a> | <a href="mailto:esgf-user@lists.llnl.gov?subject=ESGF Portal Feedback">Contact ESGF</a>
        </div>
	</div>
</div>  

	
<script type="text/javascript">
$(function(){
	/*
	if($.browser.msie) {
=======
	
	$('div#disclaimer').empty();
	
	if($.browser.mozilla) {
		var versionPad = 'v' + $.browser.version;
		if(versionPad.search('v3') != -1) {
			alert("ESGF currently does not support Mozilla Firefox 3.x.  Please try the Mozilla Firefox (versions 9 and above), Apple Safari, or Google Chrome browser.");
			
			var disclaimer = '<p style="color:red;font-weight:bold;border:1px dashed #e8ddcf;margin-top:10px">' +
			 'IMPORTANT: The ESGF P2P system interface may be viewed using the Mozilla Firefox (versions 9 and above), Google Chrome, or Apple Safari browsers. ' +
			 '</p>';
			$('div#disclaimer').append(disclaimer);
		}
	}
	else if($.browser.msie) {
>>>>>>> devel
		alert("ESGF currently does not support Microsoft Internet Explorer.  Please try the Mozilla Firefox, Apple Safari, or Google Chrome browser.");
		var disclaimer = '<p style="color:red;font-weight:bold;border:1px dashed #e8ddcf;margin-top:10px">' +
						 'IMPORTANT: The ESGF P2P system interface may be viewed using the Mozilla Firefox (versions 9 and above), Google Chrome, or Apple Safari browsers. ' +
						 'Support for the Microsoft Internet Explorer browser will be included in subsequent releases.' +
						 '</p>';
		$('div#disclaimer').append(disclaimer);
	}
	*/
});

</script>
