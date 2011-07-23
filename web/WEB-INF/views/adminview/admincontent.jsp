<%@ include file="/WEB-INF/views/common/include.jsp" %>

<div>
<h2>Administration Home</h2>
<h4>Users and Groups Administration</h4>
<ul style="list-style-type: none;">                    
	<li><a href="<c:url value='/usermanagement'/> ">Manage All Users</a></li>  
	<li><a href="<c:url value='/creategroups'/> ">Create New Group</a></li>
</ul>

<h4>Search Settings</h4>
<ul style="list-style-type: none;">                    
	<li><a href="<c:url value='/changeFacets'/> ">Add/Remove Facets</a></li>  
	<li><a href="<c:url value='/newsGroup'/> ">News</a></li>
</ul>
</div>