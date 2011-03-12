<%@ include file="/WEB-INF/views/common/include.jsp" %>


<div id="adminTabs">

    <ul>

    <li><a href="#headlines">Headlines</a></li>

    <li><a href="#searchsettings"> Search Settings </a></li>

    </ul>


    <div id="headlines">

    <span class="actions">
    <a href="#"> Create New Headlines </a> or,
    <a href="#"> List/Edit/Remove Headlines</a>
    </span>

    <div id="hlcontent">

    </div>

    </div>

    <div id="searchsettings">

        To be done

    </div>


</div>


<script type="text/javascript">

    $(function(){
        $('#adminTabs').tabs();
    });


</script>

