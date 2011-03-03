<%@ include file="/WEB-INF/views/common/include.jsp" %>

<ti:insertDefinition name="main-layout" >

    <ti:putAttribute name="main">
     <div class="push-1 span-15 last">

    <h2> Create News </h2>
       <form:form modelAttribute="news" action="news" method="post">
            <fieldset>
                <legend>News Fields</legend>
                <p>
                    <form:label for="title" path="title" cssErrorClass="error">Title</form:label><br/>
                    <form:input path="title" size="75" /> <form:errors path="title" />
                </p>

                <p>
                    <form:label for="body" path="body" cssErrorClass="error"> Contents (500 limit) </form:label><br/>
                    <form:textarea path="body" rows="10" cols="50" />"
                </p>
               <p>
                    <input type="submit" />
                </p>
            </fieldset>
        </form:form>
   </div>

    </ti:putAttribute>

</ti:insertDefinition>

