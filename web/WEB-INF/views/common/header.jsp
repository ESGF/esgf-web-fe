<%@ include file="/WEB-INF/views/common/include.jsp" %>
	
<!-- left tabs -->	   
<div class="span-14 left">
	    <span class="tabitem"> <a href="<c:url value='/'/> ">Home</a></span>    
	    <span class="tabitem" id="facet"> <a href="#" rel="#facet_overlay">Browse</a></span>
	    <span class="tabitem"><a href="#">Analysis</a></span>
	    
</div>
	
<!-- right tabs -->
<div class="prepend-5 span-5 last">
	    <span class="tabitem"> <a href="#">Search Settings</a></span>
	    <span class="tabitem"> <a href="<c:url value='/login'/>" >Login</a></span>
</div>
	
<hr/>