<%@ include file="/WEB-INF/views/common/include.jsp" %>

<ti:insertDefinition name="main-layout" >

    <ti:putAttribute name="extrascript">

		<link href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>
	  	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.5/jquery.min.js"></script>
	  	<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"></script>

        <%@ include file="createAccountData.jsp" %>

        <script>
              $(document).ready(function() {
                $("input#state").autocomplete({ source: states});
                $("input#country").autocomplete({source: countries});
              });
          </script>

    </ti:putAttribute>

    <ti:putAttribute name="extrastyle">
        <link rel="stylesheet" href='<c:url value="/styles/security.css" />' type="text/css">
        <link rel="stylesheet" type="text/css" href="/styles/jquery.autocomplete.css" />
    </ti:putAttribute>

    <ti:putAttribute name="main">


        <div style="margin:0 auto; width:600px">

			<p/>
            <h1>ESGF User Registration</h1>

            <div class="panel">
                <form method="post" commandName="user">

                    <table>
                        <tr>
                            <th class="right_text" nowrap="nowrap">First Name</th>
                            <td class="left_text">
                                <input type="text" name="firstName" size="60" value="${param.firstName}" alt="firstName" />
                                <br/>
                                <c:forEach items="${errors}" var="error">
								    <c:if test="${error.field == 'firstName'}">
								    	<span class="myerror"><c:out value="${error.defaultMessage}" /></span><br/>
								    </c:if>
								</c:forEach>                               
                            </td>
                        </tr>
                        <tr>
                            <td class="right_text" nowrap="nowrap">Middle Name</td>
                            <td class="left_text">
                                <input type="text" name="middleName" size="60" value="${param.middleName}" alt="middleName" />
                                <br/>
                                <c:forEach items="${errors}" var="error">
								    <c:if test="${error.field == 'middleName'}">
								    	<span class="myerror"><c:out value="${error.defaultMessage}" /></span><br/>
								    </c:if>
								</c:forEach>                               
                            </td>
                        </tr>
                        <tr>
                            <th class="right_text" nowrap="nowrap">Last Name</th>
                            <td class="left_text">
                                <input type="text" name="lastName" size="60" value="${param.lastName}" alt="lastName" />
                                <br/>
                                <c:forEach items="${errors}" var="error">
								    <c:if test="${error.field == 'lastName'}">
								    	<span class="myerror"><c:out value="${error.defaultMessage}" /></span><br/>
								    </c:if>
								</c:forEach>                                                               
                            </td>
                        </tr>
                        <tr>
                            <th class="right_text">Email</th>
                            <td class="left_text">
                                <input type="text" name="email" size="60" value="${param.email}" alt="email" />
                                <br/>
                                <c:forEach items="${errors}" var="error">
								    <c:if test="${error.field == 'email'}">
								    	<span class="myerror"><c:out value="${error.defaultMessage}" /></span><br/>
								    </c:if>
								</c:forEach>                                                               
                            </td>
                        </tr>
                        <tr>
                            <th class="right_text" nowrap="nowrap">User Name</th>
                            <td class="left_text">
                                <input type="text" name="userName" size="60" value="${param.userName}" alt="userName" />
                                <br/><i>Username can contain only letters and numbers.</i>
                                <br/>
                                <c:forEach items="${errors}" var="error">
								    <c:if test="${error.field == 'userName'}">
								    	<span class="myerror"><c:out value="${error.defaultMessage}" /></span><br/>
								    </c:if>
								</c:forEach>                                                               
                            </td>
                        </tr>
                        <tr>
                            <th class="right_text">Password<br/>&nbsp;<br/>&nbsp;</th>
                            <td class="left_text">
                                <input type="password" name="password1" size="60" alt="password1" />
                                <br/><i>Password must contain at least one letter and one number,<br/>and be at least 6 characters long.</i>
                                <br/>
                                <c:forEach items="${errors}" var="error">
								    <c:if test="${error.field == 'password1'}">
								    	<span class="myerror"><c:out value="${error.defaultMessage}" /></span><br/>
								    </c:if>
								</c:forEach>                                                               
                            </td>
                        </tr>
                        <tr>
                            <th class="right_text" nowrap="nowrap">Confirm Password</th>
                            <td class="left_text">
                                <input type="password" name="password2" size="60" alt="password2" />
                                <br/>
                                <c:forEach items="${errors}" var="error">
								    <c:if test="${error.field == 'password2'}">
								    	<span class="myerror"><c:out value="${error.defaultMessage}" /></span><br/>
								    </c:if>
								</c:forEach>                                                               
                            </td>
                        </tr>
                        <tr>
                            <td class="right_text">Organization</td>
                            <td class="left_text">
                                <input type="text" name="organization" size="60" value="${param.organization}" alt="organization" />
                                <br/>
                                <c:forEach items="${errors}" var="error">
								    <c:if test="${error.field == 'organization'}">
								    	<span class="myerror"><c:out value="${error.defaultMessage}" /></span><br/>
								    </c:if>
								</c:forEach>                                                               
                            </td>
                        </tr>
                        <tr>
                            <td class="right_text">City</td>
                            <td class="left_text">
                                <input type="text" name="city" size="60" value="${param.city}" alt="city" />
                                <br/>
                                <c:forEach items="${errors}" var="error">
								    <c:if test="${error.field == 'city'}">
								    	<span class="myerror"><c:out value="${error.defaultMessage}" /></span><br/>
								    </c:if>
								</c:forEach>                                                               
                            </td>
                        </tr>
                        <tr>
                            <td class="right_text">State</td>
                            <td class="left_text">
                                <input type="text" name="State" size="60" id="state" value="${param.State}" alt="state" />
                                <br/>
                                <c:forEach items="${errors}" var="error">
								    <c:if test="${error.field == 'State'}">
								    	<span class="myerror"><c:out value="${error.defaultMessage}" /></span><br/>
								    </c:if>
								</c:forEach>                                
                            </td>
                        </tr>
                        <tr>
                            <td class="right_text">Country</td>
                            <td class="left_text">
                                <input type="text" name="country" size="60" id="country" value="${param.country}" alt="country" />
                                <br/>
                                <c:forEach items="${errors}" var="error">
								    <c:if test="${error.field == 'country'}">
								    	<span class="myerror"><c:out value="${error.defaultMessage}" /></span><br/>
								    	<br/>
								    </c:if>
								</c:forEach>                                                               
                            </td>
                        </tr>
                    </table>

					<!-- no-spam fields -->
					<input type="hidden" name="blank" />
					<input type="hidden" name="uuid" value="<c:out value="${user.uuid}"/>" id="uuid" />
					
                    <input type="submit" value="Submit" class="button"/>

                </form>
            </div>

        </div>
        <p/>
        
    </ti:putAttribute>
</ti:insertDefinition>