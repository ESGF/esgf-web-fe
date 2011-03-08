 <%@ include file="/WEB-INF/views/common/include.jsp" %>

 <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/jquery-1.4.2.min.js" /> '></script>


 <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/tabs.min.js" /> '></script>

 <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/tabs.slideshow.js" /> '></script>


 <script type="text/javascript">

 $(function() {

 $(".slidetabs").tabs(".images > div", {

        // enable "cross-fading" effect
        effect: 'fade',
        fadeOutSpeed: "slow",

        // start from the beginning after the last tab
        rotate: true

    // use the slideshow plugin. It accepts its own configuration
    }).slideshow();
 });

 </script>