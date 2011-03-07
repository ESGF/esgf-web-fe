
<%@ include file="/WEB-INF/views/common/include.jsp" %>

<ti:insertDefinition name="main-layout" >

    <ti:putAttribute name="main">

    <div class="push-1 span-22 last">

    <div class="newstitle"> </div>

    <div class="thumbnail"> 
    
    <img src="view?ARTDATA_ID=${curEntity.thumbnailPicture.id}"></div>
    
    </div>


    <div class="newsbody"> </div>
    
    

    </ti:putAttribute>

</ti:insertDefinition>
