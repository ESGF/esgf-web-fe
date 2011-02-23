<%@ include file="/WEB-INF/views/search/_overlay.jsp" %>
<%@ include file="/WEB-INF/views/search/_select_tbl.jsp" %>

<div id="myTabs">

    <ul>

    <li><a href="#search-results"> Results</a></li>

    <li><a href="#carts"> Selections </a></li>

    </ul>

    <div id="search-results"> </div>

    <div id="carts"> <ul id="datasetList"></ul> </div>

</div>

<script type="text/javascript">

    $(function(){
        $('#myTabs').tabs();
    });

</script>
