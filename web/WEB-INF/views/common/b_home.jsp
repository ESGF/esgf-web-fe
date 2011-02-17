<%@ include file="../common/include.jsp" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>


<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

    <script type="text/javascript" src="<c:url value="/scripts/jquery-1.4.2/jquery-1.4.2.min.js" /> "></script>
    <script type="text/javascript" src="<c:url value="/scripts/jquery-1.4.2/jquery-ui-1.8.5.min.js" /> "></script>
    <script type="text/javascript" src="<c:url value="/scripts/jquery-1.4.2/overlay.js" /> "></script>
    <script type="text/javascript" src="<c:url value="/scripts/jquery-1.4.2/overlay.apple.js" /> "></script>
    <script type="text/javascript" src="<c:url value="/scripts/jquery-1.4.2/scrollable.js" /> "></script>
    <script type="text/javascript" src="<c:url value="/scripts/jquery-1.4.2/scrollable.navigator.js" /> "></script>
    <script type="text/javascript" src="<c:url value="/scripts/jquery-1.4.2/scrollable.autoscroll.js" /> "></script>

    <script type="text/javascript" src="<c:url value="/scripts/jquery-1.4.2/jquery.tmpl.min.js" /> "></script>
    <script type="text/javascript" src="<c:url value="/scripts/jquery-1.4.2/jquery.livequery.js" /> "></script>
    <script type="text/javascript" src="<c:url value="/scripts/jquery-1.4.2/jquery.autocomplete.js" /> "></script>

    <script type="text/javascript" src="<c:url value="/scripts/esgf/esgf-core.js" /> "></script>
    <script type="text/javascript" src="<c:url value="/scripts/esgf/logger_1.0.0.js" /> "></script>


   
    
    <script type="text/javascript"
        src="http://maps.google.com/maps/api/js?sensor=false">
    </script>


    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/core/Core.js" />"> </script>
    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/core/Parameter.js" />"></script>
    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/core/ParameterStore.js" />"></script>
    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/core/AbstractManager.js" />"></script>
    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/core/AbstractWidget.js" />"></script>
    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/core/AbstractFacetWidget.js" />"></script>

    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/helpers/jquery/ajaxsolr.theme.js" />"></script>
    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/managers/Manager.jquery.js" />"> </script>


    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/widgets/Results.js" />"> </script>
    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/widgets/Pager.js" />"> </script>
    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/widgets/TagClouds.js" />"> </script>
    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/widgets/CurrentSearch.js" />"> </script>
    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/widgets/Text.js" />"> </script>
    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/widgets/AutoComplete.js" />"> </script>
    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/widgets/FacetBrowser.js" />"> </script>
    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/widgets/Geospatial.js" />"> </script>
    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/widgets/Metadata.js" />"> </script>
    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/widgets/Temporal.js" />"> </script>
    <script type="text/javascript" src="<c:url value="/scripts/ajax-solr/widgets/Annotator.js" />"> </script>
    
    <!--  
    <script type="text/javascript" src="<c:url value="/scripts/esgf/Geospatial.js" />"> </script>
    -->
    
    <!--  
	-->
    
    <script type="text/javascript" src="<c:url value="/scripts/esgf/solr.js" />"> </script>
    <script type="text/javascript" src="<c:url value="/scripts/esgf/solr.theme.js" />"> </script>



    <!--
    <link href='http://fonts.googleapis.com/css?family=Droid+Sans&subset=latin'
        rel='stylesheet' type='text/css'>
    -->

    <link rel="stylesheet"
        href="<c:url value="/styles/cupertino/jquery-ui-1.8.5.custom.css" />"
        type="text/css" media="screen">

    <link rel="stylesheet"
        href="<c:url value="/styles/blueprint/screen.css" />"
        type="text/css" media="screen, projection">

    <link rel="stylesheet"
        href="<c:url value="/styles/blueprint/print.css" />"
        type="text/css" media="print">

    <!--[if lt IE 8]><link rel="stylesheet" href="<c:url value="styles/blueprint/ie.css" />"
        type="text/css" media="screen, projection"><![endif]-->

    <link rel="stylesheet"
        href="<c:url value="/styles/blueprint/plugins/fancy-type/screen.css" />"
        type="text/css" media="screen, projection">

    <link rel="stylesheet"
        href="<c:url value="/styles/jquery.autocomplete.css" />"
        type="text/css" media="screen, projection">

    <link rel="stylesheet"
        href="<c:url value="/styles/esg-simple.css" />"
        type="text/css" media="screen, projection">

    <link rel="stylesheet"
        href="<c:url value="/styles/overlay.css" />"
        type="text/css" media="screen, projection">

    <link rel="stylesheet"
        href="<c:url value="/styles/geospatial_overlay.css" />"
        type="text/css" media="screen, projection">
  
    <link rel="stylesheet"
        href="<c:url value="/styles/facet_overlay.css" />"
        type="text/css" media="screen, projection">


    <link rel="stylesheet"
        href="<c:url value="/styles/metadata_overlay.css" />"
        type="text/css" media="screen, projection">

    <link rel="stylesheet"
        href="<c:url value="/styles/temporal_overlay.css" />"
        type="text/css" media="screen, projection">
        
     <link rel="stylesheet"
        href="<c:url value="/styles/annotator_overlay.css" />"
        type="text/css" media="screen, projection">
        
    <title> ESGF Portal</title>

</head>

<body>


<div class="container">

    <!-- overlays -->
    <div class="apple_overlay" id="temporal_overlay"><div class="contentWrap"></div></div>
    
    <div class="apple_overlay" id="geospatial_overlay"><div class="contentWrap"></div></div>
    
    <div class="apple_overlay" id="metadata_overlay"><div class="contentWrap"></div></div>
	
	<div class="apple_overlay" id="annotator_overlay"><div class="contentWrap"></div></div>
	
	<!--  
	<div class="apple_overlay" id="facet_overlay"><div class="contentWrap"></div></div>
	-->
	
    <!-- facet overlay -->
    <!-- need to replace the following with a separate html -->
      
    <div class="apple_overlay" id="facet_overlay">
        <div class="overlay_header" style="display:none">
            <div class="overlay_header_title">
                Category Browser
            </div>
              
            <div class="overlay_header_buttons">
                <div id="facetSort">
                    <input type="radio" id="sortbycount" name="sorter" checked="checked" value="sortbycount" /><label for="sortbycount">Sort By Count</label>
                    <input type="radio" id="sortbyabc" name="sorter" value="sortbyabc" /><label for="sortbyabc">Sort By ABC</label>
                </div>
            </div>
            
        </div>
         
        <div class="content" style="display:none">
         
	       	<div class="scrollable facet_verticalscroll">
	            <div class="facet_items">
	                <div id="project"></div>
	                <div id="model"></div>
	                <div id="experiment"></div>
	                  
	                <div id="frequency"></div>
	                <div id="realm"></div>
	                <div id="instrument"></div>
	                <div id="variable"></div>
	                
	                <div id="cf_variable"></div>
	                <div id="gcmd_variable"></div>
	                
	            </div>
	        </div>
        </div>
          
    </div>
	


    <!-- end facet overlay -->



    <div id="temp-browse"></div>
    <div id="metadata-browse"></div>

<div id="header" class="span-24 last">
    <div class="span-16 left">
    <span class="tabitem"> <a href="#"> Home </a></span>
     
    <span class="tabitem" id="facet"> <a href="#" rel="#facet_overlay"> Browse </a> </span>
    <!--  
    <span class="tabitem" id="facet"><a href="<c:url value="/scripts/esgf/facet_overlay.html" />" rel="#facet_overlay" > Browse</a> </span>
    -->
    <span class="tabitem"> <a href="#"> Analysis</a></span>
    </div>

    <div class="prepend-4 span-4 last">
    <span class="tabitem"> <a href="#"> Search settings</a></span>
    <span class="tabitem"> <a href="#"> Sign in </a></span>
    </div>

    <hr/>
</div> <!--  top header -->


<!--  subheader-24 -->
<div id="subheader" class="span-24 last">
    <ti:insertAttribute name="subheader-24" />
</div>

<div class="span-24 last" id="contents">

<!--  sidebar-4 -->
<div class="span-4" id="sidebar">
       <ti:insertAttribute name="sidebar-4" />
</div>

<!--  main-19 -->
<div class="span-19 last" id="main"> </div>
     <ti:insertAttribute name="main-19" />

</div>




<div id="footer">
     <div class="prepend-top span-8 last">
       <span class="quiet">ESGF Copyright &copy; 2011</span> |
       <a href="#" class="quiet">Disclaimer</a>
       </div>
</div> <!--  end of footer div -->

</div> <!--  end of container -->

</body>
</html>
