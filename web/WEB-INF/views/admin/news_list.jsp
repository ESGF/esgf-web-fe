
<%@ include file="/WEB-INF/views/common/include.jsp" %>


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

