<%@ include file="/WEB-INF/views/common/include.jsp" %>

<ti:insertDefinition name="main-layout" >

	 <ti:putAttribute name="extrastyle">
        <link rel="stylesheet" href='<c:url value="/styles/security.css" />' type="text/css">
    </ti:putAttribute>
    
    <ti:putAttribute name="main">
    	    		
   		<h1>Disable Account - Confirmation Required</h1>    	
   	
   		<div class="panel">
   			<span class="myerror">Are you sure you want to disable the account for user <c:out value="${user.openid}"/> ?</span>
   			<br/>
   			Once disabled, an account can only be re-enabled by the site administrator.
   			<form method="post" action="">
   			
   				<input type="hidden" value="${user.openid}" />
   				<input type="hidden" value="${user.token}" />
   			
   				<input type="button" value="NO" class="button" onclick="window.location='<c:url value="/"/>'"/>
   				&nbsp;
   				<input type="submit" value="YES" class="button"/>
   			
   			</form>
   		
   		</div>
    
    </ti:putAttribute>

</ti:insertDefinition>