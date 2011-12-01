    <%@ include file="/WEB-INF/views/common/include.jsp" %>

    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/jquery-1.4.4.min.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/esgf/esgf-search.js" />'> </script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/underscore-min.js" />'> </script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/jquery-ui-1.8.12.custom.min.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/overlay.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/overlay.apple.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/toolbox.mousewheel.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/scrollable.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/scrollable.navigator.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/scrollable.autoscroll.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/tooltip.js" /> '></script>

    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/jquery.tmpl.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/jquery.livequery.js" /> '></script>
    <script type="text/javascript" src='<c:url value="/scripts/jquery-1.4.2/jquery.autocomplete.js" /> '></script>

	<!--  
    <script type="text/javascript" src='<c:url value="/scripts/esgf/esgf-download.js" /> '></script>
	-->
    <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>

    <script type="text/javascript" src='<c:url value="/scripts/esgf/solr.js" />'> </script>
    <script type="text/javascript" src='<c:url value="/scripts/ajax-solr/core/Core.js" />'> </script>
    <script type="text/javascript" src='<c:url value="/scripts/ajax-solr/core/AbstractManager.js" />'></script>
    <script type="text/javascript" src='<c:url value="/scripts/ajax-solr/managers/Manager.jquery.js" />'> </script>
    <script type="text/javascript" src='<c:url value="/scripts/ajax-solr/core/Parameter.js" />'></script>
    <script type="text/javascript" src='<c:url value="/scripts/ajax-solr/core/ParameterStore.js" />'></script>
    <script type="text/javascript" src='<c:url value="/scripts/ajax-solr/core/AbstractWidget.js" />'></script>
    <script type="text/javascript" src='<c:url value="/scripts/ajax-solr/core/AbstractFacetWidget.js" />'></script>
    <script type="text/javascript" src='<c:url value="/scripts/ajax-solr/core/AbstractOverlayWidget.js" />'></script>
    <script type="text/javascript" src='<c:url value="/scripts/ajax-solr/core/AbstractGeospatialWidget.js" />'></script>
    <script type="text/javascript" src='<c:url value="/scripts/ajax-solr/helpers/jquery/ajaxsolr.theme.js" />'></script>


    <script type="text/javascript" src='<c:url value="/scripts/ajax-solr/widgets/Results.js" />'> </script>
    <script type="text/javascript" src='<c:url value="/scripts/ajax-solr/widgets/Pager.js" />'> </script>
    <script type="text/javascript" src='<c:url value="/scripts/ajax-solr/widgets/CurrentSearch.js" />'> </script>
    <script type="text/javascript" src='<c:url value="/scripts/ajax-solr/widgets/Text.js" />'> </script>
    <script type="text/javascript" src='<c:url value="/scripts/ajax-solr/widgets/AutoComplete.js" />'> </script>
    <script type="text/javascript" src='<c:url value="/scripts/ajax-solr/widgets/FacetBrowser.js" />'> </script>
    <script type="text/javascript" src='<c:url value="/scripts/ajax-solr/widgets/TagClouds.js" />'> </script>

    <script type="text/javascript" src='<c:url value="/scripts/ajax-solr/widgets/Geospatial.js" />'> </script>

    <script type="text/javascript" src='<c:url value="/scripts/ajax-solr/widgets/Temporal2.js" />'> </script>
    <script type="text/javascript" src='<c:url value="/scripts/ajax-solr/widgets/Metadata.js" />'> </script>
    <script type="text/javascript" src='<c:url value="/scripts/ajax-solr/widgets/FacetSidebar.js" />'> </script>
    <script type="text/javascript" src='<c:url value="/scripts/ajax-solr/widgets/Annotator.js" />'> </script>
	<script type="text/javascript" src='<c:url value="/scripts/ajax-solr/widgets/DistributedSearch.js" />'> </script>
	<script type="text/javascript" src='<c:url value="/scripts/ajax-solr/widgets/DataCart.js" />'> </script>
    

    <script type="text/javascript" src='<c:url value="/scripts/esgf/solr.theme.js" />'> </script>


	<script type="text/javascript" src='<c:url value="/scripts/cim/cim-viewer-core.js" />'> </script>
	<script type="text/javascript" src='<c:url value="/scripts/cim/cim-viewer-renderer.js" />'> </script>
	<script type="text/javascript" src='<c:url value="/scripts/cim/cim-services-proxy.js" />'> </script>

<script type="text/javascript">
            (function () {
            	
            	
                // CIM viewer.
                var viewer = cim.viewer;

                // CIM viewer options by cim document type.
                var viewerOptions = {
                    'MODEL' : function() {
                        var result = viewer.options.forModel.defaults;
                        return result;
                    },

                    'DATAOBJECT' : function() {
                        var result = viewer.options.forDataObject.defaults;
                        return result;
                    },

                    'EXPERIMENT' : function() {
                        var result = viewer.options.forExperiment.defaults;
                        return result;
                    },
                    
                    'SIMULATION' : function() {
                        var result = viewer.options.forSimulation.defaults;
                        return result;
                    }
                };

                // Renders a CIM instance based upon type and name.
                var render = function(cim_type, name) {
                    var options = viewerOptions[cim_type.toUpperCase()]();
                    viewer.renderFromName('cmip5', cim_type, name, options);
                };

                // Event handler for document ready event.
                $jq(document).ready(function() {
                    // Model metadata selectors.
                    $jq('.cim-model').live('click', function() {
                        render('model', $jq(this).text());
                    });
                    
                });
            }());
        </script>
