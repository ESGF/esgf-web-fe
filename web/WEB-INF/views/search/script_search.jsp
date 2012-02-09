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
	<script type="text/javascript" src='<c:url value="/scripts/cim/cim-viewer-core.js" />'> </script>
	 
	<script type="text/javascript" src='<c:url value="/scripts/cim/cim-viewer-core.js" />'> </script>
	<script type="text/javascript" src='<c:url value="/scripts/cim/cim-viewer-renderer.js" />'> </script>
	<script type="text/javascript" src='<c:url value="/scripts/cim/cim-services-proxy.js" />'> </script>
	-->
	
	<script type="text/javascript" src='<c:url value="/scripts/cim/cim-viewer.js" />'> </script>
	
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
	<script type="text/javascript" src='<c:url value="/scripts/ajax-solr/widgets/Versions.js" />'> </script>
	<script type="text/javascript" src='<c:url value="/scripts/ajax-solr/widgets/Replicas.js" />'> </script>
	<script type="text/javascript" src='<c:url value="/scripts/ajax-solr/widgets/DataCart.js" />'> </script>
    

    <script type="text/javascript" src='<c:url value="/scripts/esgf/solr.theme.js" />'> </script>


	
<script type="text/javascript">
            (function () {
            	
            	
            	/*
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
                var render = function(project, cim_type, name) {
                    var options = viewerOptions[cim_type.toUpperCase()]();
                    viewer.renderFromName(project, cim_type, name, options);
                };

                */
                
                // Event handler for document ready event.
                $jq(document).ready(function() {
                    // Model metadata selectors.
                    
                    $jq('.cim-model').live('click', function() {

                    	var project = $(this).parent().find("a").attr("project");
                    	
                    	var model = $(this).parent().find("a").attr("model");

                    	var institute = $(this).parent().find("a").attr("institute");
                    	
                    	var experiment = $(this).parent().find("a").attr("experiment");
                    	
                    	var drs_components = {'project' : project, 'model' : model, 'institute' : institute, 'experiment' : experiment};
                    	
                    	
                    	try {
                        	//calls the cim service for the give dict
                        	cim.viewer.renderFromDRS(drs_components);
                    	} catch(e) {
                    		alert('There was error in rendering the CIM viewer. Contact your administrator');
                    	}
                    	
                    	
                    });
                    
                    
                    
                });
                
                function onCIMLinkClicked(doc) {
                	
                	var drs_components = get_drs_components(doc);
                	
                	alert('drs_components: ' + JSON.stringify(drs_components));
                	
                	try {
                    	//calls the cim service for the give dict
                    	cim.viewer.renderFromDRS(drs_components);
                	} catch(e) {
                		alert('There was error in rendering the CIM viewer. Contact your administrator');
                	}
                	
                	
                	//render('cmip5', 'model', '$jq(this).text()');
                }


                function get_drs_components(doc) {
                	
                	//do something from the doc obj to get the dict
                	//perhaps loop over all keys and add to the dict?
                	//var dict = {};
                	//for(var key in doc) {
                	//	dict[key] = doc[key];
                	//}
                	
                	var dict = {'project' : 'cmip5', 'institute' : 'ipsl', 'model' : 'ipsl-cm5-lr', 'experiment' : 'amip' };
                    
                	return dict;
                }
                
                
            }());
        </script>
