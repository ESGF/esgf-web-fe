<%@ include file="/WEB-INF/views/common/include.jsp" %>


<div class="push-3 span-21 last">
<!--
<a class="backward">prev</a>
-->

<div class="images">


<c:forEach var="news" items="${newsList}">

<div>
    <h3>${news.title}</h3>
    <c:if test="${news.imageFile != null}" >
        <img src="news/image/${news.id}" style="float:left;margin:0 30px 20px 0" alt="news" />
    </c:if>

    <p> ${news.body} </p>

</div>

</c:forEach>




</div> <!--  end of "image" -->


<!--  <a class="forward">next</a>  -->

<div class="slidetabs">
    <c:forEach var="news" items="${newsList}">
        <a href="#"></a>
    </c:forEach>
</div>



</div>