
<%@ include file="/WEB-INF/views/common/include.jsp" %>


    <span class="headline_tabs">
    <a id="create" href="#">
    <img src='<c:url value="/images/add-24.png" />' width="16" alt='create news'>
     Create New Headlines </a>
    </span>

    <div id="headline_list">



    <table>
    <thead>
        <tr>
        <th> Titles </th>
        <th> Edit </th>
        <th> Remove </th>
    </thead>
    <c:forEach var="news" items="${newsList}" varStatus="newsLoopCount" >

        <tr>
            <td> ${news.title} </td>
            <td> <a class="edit" href="#"><img src='<c:url value="/images/edit-32.png" />' width="24" alt='edit news'> </a></td>
            <td> <a class="remove" href="<c:out value="${news.id}" />">
            <img src='<c:url value="/images/delete-24.png" />' width="24" alt='delete news'></a></td>
        </tr>

    </c:forEach>

    </table>

    </div>


    <div id="dialog-message" style="display:none" title="Not implemented yet">
    <p> Just delete it first, then create an new entry
    </p>
    </div>
