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
                <form:form method="post" commandName="user">

                    <table>
                        <tr>
                            <th class="right_text" nowrap="nowrap">First Name</th>
                            <td class="left_text">
                                <form:input path="firstName" size="60" />
                                <br/><form:errors path="firstName" cssClass="myerror" />
                            </td>
                        </tr>
                        <tr>
                            <td class="right_text" nowrap="nowrap">Middle Name</td>
                            <td class="left_text">
                                <form:input path="middleName" size="60" />
                                <br/><form:errors path="middleName" cssClass="myerror" />
                            </td>
                        </tr>
                        <tr>
                            <th class="right_text" nowrap="nowrap">Last Name</th>
                            <td class="left_text">
                                <form:input path="lastName" size="60" />
                                <br/><form:errors path="lastName" cssClass="myerror" />
                            </td>
                        </tr>
                        <tr>
                            <th class="right_text">Email</th>
                            <td class="left_text">
                                <form:input path="email" size="60" />
                                <br/><form:errors path="email" cssClass="myerror" />
                            </td>
                        </tr>
                        <tr>
                            <th class="right_text" nowrap="nowrap">User Name</th>
                            <td class="left_text">
                                <form:input path="userName" size="60" />
                                <br/><i>Username can contain only digits and numbers.</i>
                                <br/><form:errors path="userName" cssClass="myerror" />
                            </td>
                        </tr>
                        <tr>
                            <th class="right_text">Password<br/>&nbsp;<br/>&nbsp;</th>
                            <td class="left_text">
                                <form:password path="password1" size="60" />
                                <br/><i>Password must contain at least one letter and one number,<br/>and be at least 6 characters long.</i>
                                <br/><form:errors path="password1" cssClass="myerror" />
                            </td>
                        </tr>
                        <tr>
                            <th class="right_text" nowrap="nowrap">Confirm Password</th>
                            <td class="left_text">
                                <form:password path="password2" size="60" />
                                <br/><form:errors path="password2" cssClass="myerror" />
                            </td>
                        </tr>
                        <tr>
                            <td class="right_text">Organization</td>
                            <td class="left_text">
                                <form:input path="organization" size="60" />
                                <br/><form:errors path="organization" cssClass="myerror" />
                            </td>
                        </tr>
                        <tr>
                            <td class="right_text">City</td>
                            <td class="left_text">
                                <form:input path="city" size="60" />
                                <br/><form:errors path="city" cssClass="myerror" />
                            </td>
                        </tr>
                        <tr>
                            <td class="right_text">State</td>
                            <td class="left_text">
                                <form:input path="State" size="60" id="state" />
                                <br/><form:errors path="state" cssClass="myerror" />
                            </td>
                        </tr>
                        <tr>
                            <td class="right_text">Country</td>
                            <td class="left_text">
                                <form:input path="country" size="60" id="country"/>
                                <br/><form:errors path="country" cssClass="myerror" />
                            </td>
                        </tr>
                    </table>

					<!-- no-spam fields -->
					<form:hidden path="blank"/>
					<input type="hidden" name="uuid" value="<c:out value="${user.uuid}"/>" id="uuid" />
					
                    <input type="submit" value="Submit" class="button"/>

                </form:form>
            </div>

        </div>
        <p/>
        
    </ti:putAttribute>
</ti:insertDefinition>