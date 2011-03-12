<%@ include file="/WEB-INF/views/common/include.jsp" %>

<style>
img {
    vertical-align:text-bottom;
}
</style>

<div id="adminTabs">

    <ul>

    <li><a href="#headline">Headlines</a></li>

    <li><a href="#searchsetting"> Search Settings </a></li>

    </ul>


    <div id="headline">

    <span class="headline_tabs">
    <a href="#">
    <img src='<c:url value="/images/add-24.png" />' width="16">
     Create New Headlines </a>
    </span>

    <div id="headline_list">

    </div>

    </div>

    <div id="searchsetting">

        To be done

    </div>


</div>


<script type="text/javascript">

    $(function(){
        $('#adminTabs').tabs();
    });


</script>

