<script type="text/javascript">


    $(function(){
        $('#myTabs').tabs();
    });


</script>

<%@ include file="/WEB-INF/views/search/_overlay.jsp" %>


<div id="myTabs">

    <ul>

    <li><a href="#search-results"> Results</a></li>

    <li><a href="#carts"> Selections </a></li>

    </ul>

    <div id="search-results"> </div>

    <div id="carts">
    <table id="datasetList"></table>
    </div>

    <script id="cartTemplate" type="text/x-jquery-tmpl">
        <tr>
               <td> ${ID} </td>
        </tr>
    </script>


</div>