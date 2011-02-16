<script type="text/javascript">


    $(function(){
        $('#myTabs').tabs();
    });


</script>



<div class="span-19 last">

<div id="myTabs">

    <ul>

    <li><a href="#search-results"> Results</a></li>

    <li><a href="#carts"> Selections </a></li>

    </ul>

    <div id="search-results"> </div>

    <div id="carts">
    <table><tbody id="datasetList"></tbody></table>
    </div>

    <script id="cartTemplate" type="text/x-jquery-tmpl">
        <tr>
               <td> ${Id} </td>
        </tr>
    </script>


</div>

</div>

