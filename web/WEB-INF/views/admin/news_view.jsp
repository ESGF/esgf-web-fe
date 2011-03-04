
<%@ include file="/WEB-INF/views/common/include.jsp" %>

<ti:insertDefinition name="main-layout" >

    <ti:putAttribute name="main">

    <div class="push-1 span-22 last">

    <h2> Published News </h2>

    <table>

    <c:forEach var="news" items="${newsList}" varStatus="newsLoopCount" >

        <tr>
            <td> ${news.title} </td>
            <td> Edit </td>
            <td> Remove </td>
        </tr>

    </c:forEach>

    </table>
   </div>

    </ti:putAttribute>

</ti:insertDefinition>
