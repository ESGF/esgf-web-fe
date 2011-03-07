<%@ include file="/WEB-INF/views/common/include.jsp" %>

<ti:insertDefinition name="main-layout" >

	<ti:putAttribute name="extrascript">
  
	  	<script type="text/javascript" src="http://view.jquery.com/trunk/plugins/autocomplete/lib/jquery.js"></script>
		<script type='text/javascript' src='http://view.jquery.com/trunk/plugins/autocomplete/lib/jquery.bgiframe.min.js'></script>
		<script type='text/javascript' src='.http://view.jquery.com/trunk/plugins/autocomplete/lib/jquery.ajaxQueue.js'></script>
		<script type='text/javascript' src='http://view.jquery.com/trunk/plugins/autocomplete/lib/thickbox-compressed.js'></script>
		<script type='text/javascript' src='http://view.jquery.com/trunk/plugins/autocomplete/jquery.autocomplete.js'></script>
	
		<%@ include file="createAccountData.jsp" %>
	
		<script type='text/javascript' src='./createAccountData.js'></script>
	
		<script>
	  		$(document).ready(function() {
				$("#state").autocomplete(states);
				$("#country").autocomplete(countries);
	 	 	});
	  	</script>
	
	</ti:putAttribute>
	
	<ti:putAttribute name="extrastyle">
		<link rel="stylesheet" href='<c:url value="/styles/base.css" />' type="text/css">
		<link rel="stylesheet" type="text/css" href="http://view.jquery.com/trunk/plugins/autocomplete/jquery.autocomplete.css" />
	</ti:putAttribute>

	<ti:putAttribute name="main">
	
	
		<div style="margin:0 auto; width:600px">
		
			<h1>ESGF User Registration</h1>
												
			<div class="panel">
				<form:form method="post" commandName="user">
				
					<table>
						<tr>
							<th class="right" nowrap="nowrap">First Name</th>
							<td class="left">
								<form:input path="firstName" size="60" />
								<br/><form:errors path="firstName" cssClass="myerror" />
							</td>
						</tr>
						<tr>
							<td class="right" nowrap="nowrap">Middle Name</td>
							<td class="left">
								<form:input path="middleName" size="60" />
								<br/><form:errors path="middleName" cssClass="myerror" />
							</td>
						</tr>
						<tr>
							<th class="right" nowrap="nowrap">Last Name</th>
							<td class="left">
								<form:input path="lastName" size="60" />
								<br/><form:errors path="lastName" cssClass="myerror" />
							</td>
						</tr>
						<tr>
							<th class="right">Email</th>
							<td class="left">
								<form:input path="email" size="60" />
								<br/><form:errors path="email" cssClass="myerror" />
							</td>
						</tr>
						<tr>
							<th class="right" nowrap="nowrap">User Name</th>
							<td class="left">
								<form:input path="userName" size="60" />
								<br/><i>Username can contain only digits and numbers.</i>
								<br/><form:errors path="userName" cssClass="myerror" />
							</td>
						</tr>
						<tr>
							<th class="right">Password<br/>&nbsp;<br/>&nbsp;</th>
							<td class="left">
								<form:password path="password1" size="60" />
				                <br/><i>Password must contain at least one letter and one number,<br/>and be at least 6 characters long.</i>
				                <br/><form:errors path="password1" cssClass="myerror" />
							</td>
						</tr>
						<tr>
							<th class="right" nowrap="nowrap">Confirm Password</th>
							<td class="left">
								<form:password path="password2" size="60" />
								<br/><form:errors path="password2" cssClass="myerror" />
							</td>
						</tr>	
						<tr>
							<td class="right">Organization</td>
							<td class="left">
								<form:input path="organization" size="60" />
								<br/><form:errors path="organization" cssClass="myerror" />
							</td>
						</tr>
						<tr>
							<td class="right">City</td>
							<td class="left">
								<form:input path="city" size="60" />
								<br/><form:errors path="city" cssClass="myerror" />
							</td>
						</tr>
						<tr>
							<td class="right">State</td>
							<td class="left">
								<form:input path="State" size="60" id="state" />
								<br/><form:errors path="state" cssClass="myerror" />
							</td>
						</tr>
						<tr>
							<td class="right">Country</td>
							<td class="left">
								<form:input path="country" size="60" id="country"/>
								<br/><form:errors path="country" cssClass="myerror" />
							</td>
						</tr>
					</table>
					
					<input type="submit" value="submit" class="button"/>
				
				</form:form> 
			</div>
		
		</div>
	</ti:putAttribute>
</ti:insertDefinition>