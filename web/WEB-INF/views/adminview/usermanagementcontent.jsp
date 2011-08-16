<%@ include file="/WEB-INF/views/common/include.jsp" %>

<sec:authentication property="principal" var="principal"/>

<div style="margin-top:20px;margin-bottom:20px;">
	<c:choose>
		<c:when test="${principal=='anonymousUser'}">
    		<div> <c:out value="${principal}"/> IS NOT AUTHORIZED TO VIEW THIS PAGE</div>
  		</c:when>
  		<c:otherwise>
      		<c:choose>
      			<c:when test="${principal.username=='https://pcmdi3.llnl.gov/esgcet/myopenid/jfharney'}">
  					<div style="margin-top:20px">
						<h2>
						usermanagment
						<c:out value="${ManageUsers_USER}"/>
						</h2>
					</div>
  				</c:when>
  				<c:otherwise>
  					<div> <c:out value="${principal.username}"/> IS NOT AUTHORIZED TO VIEW THIS PAGE</div>
  				</c:otherwise>
      		</c:choose>
  		</c:otherwise>
	</c:choose>      
</div>




<!--  
									<p>
										<label class="formLabels" for="userName">User Name:</label>
					      		  		<input type="text" class="text" id="form_userName" name="userName" value=""> 
					      		  		<br />
					      		  	
					      		  		<label class="formLabels" id="lastName" for="lastName" style="">Last Name:</label>
						      		 	<input type="text" class="text" name="lastName" id="form_lastName" value=""> 
						      		 	<br />
						      		 	
						          		<label class="formLabels" for="firstName">First Name:</label>
						      			<input type="text" class="text" id="form_firstName" name="firstName" value=""> 
						      			<br />	
						      											
										<label class="formLabels" for="emailAddress" style="">Email:</label>
						      		 	<input type="text" class="text" name="emailAddress" id="form_emailAddress" value=""> 
						      		 	<br />
						      		 	
						          		<label class="formLabels" for="status">Status:</label>
						      			<input type="text" class="text" id="form_status" name="status" value=""> 
						      			<br />	
								      			
								      	<label class="formLabels" for="organization">Organization:</label>
								      	<input type="text" class="text" id="organization" name="organization" value=""> 
								      	<br />
								      		  	
								    	<label class="formLabels" for="city" style="">City:</label>
								    	<input type="text" class="text" name="city" id="city" value=""> 
								    	<br />
								      		 	
								    	<label class="formLabels" for="country">Country:</label>
								    	<input type="text" class="text" id="country" name="country" value=""> 
								    	<br />	
								      											
										<label class="formLabels" for="openId">OpenId:</label>
								      	<input type="text" class="text" id="openId" name="openId" value=""> 
								      	<br />
								      		  	
										<label class="formLabels" for="DN">DN:</label>
								      	<input type="text" class="text" id="DN" name="DN" value=""> 
								      	<br />
								      	
								      	<input type="hidden" name="type" id="type" value="add"/>
									</p>
									<p>
						      			<input style="margin-left: 15px" class="adminbutton" type="submit" value="Submit">
						      			
					      			</p>
					      			-->
					      			
					      			
					      			
<!--  toggle menu that might come in handy later
	<div id="toggleMenu"> 
			<h4>Account Summary</h4> 
							<div class="togglebox"> 
 								<div class="block"> 
 								Account Summary
 								</div> 
						</div>
						<h4>Group Roles</h4> 
							<div class="togglebox"> 
 								<div class="block"> 
 								Group Roles
 								</div> 
						</div>
	</div>
-->