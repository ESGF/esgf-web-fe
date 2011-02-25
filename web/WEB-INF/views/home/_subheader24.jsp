<%@ include file="/WEB-INF/views/common/include.jsp" %>

<div class="push-1 span-3">
    <img src="<c:url value="/images/search2.png" />" >
</div>

<div class="span-20 last"">

    <span id="search-box">
    <input id="query" name="text" type="text" value="" />
    </span>

    <input id="search-button" type="submit" value="Search" />

    <div class="span-20 last" id="search-summary">

    <div class="span-7" id="search-speed">
    <div id="search-help">(press ESC to close suggestions)</div>
    </div>

    <div id="page-navigation" class="span-13 last">
      <ul id="pager"></ul>
      <div id="pager-header"></div>
    </div>

    </div> <!--  search summary -->

</div>

