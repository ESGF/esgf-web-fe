<%@ include file="/WEB-INF/views/common/include.jsp" %>

<!--  header info -->
<div class="container">
	<div class="prepend-3 span-18 append-3">
		<fieldset style="background: #F5F5E0">
			<legend>Administration Home</legend>
			<p>
        <ul style="list-style-type: none;">                    
	        <li><a href="<c:url value='/usermanagement'/> ">Manage Users</a></li>  
	        <li><a href="<c:url value='/creategroups'/> ">Manage Groups</a></li>
        </ul>
      </p>
    </fieldset>
  </div>
</div>
