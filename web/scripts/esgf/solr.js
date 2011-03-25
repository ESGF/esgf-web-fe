/**
 * Experimental Solr Front for ESG
 *
 * fwang2@ornl.gov
 *
 */



(function ($) {

	$(function () {

		 Manager = new AjaxSolr.Manager({
		        proxyUrl: '/esgf-web-fe/solrproxy',
		        //proxyUrl: 'http://esg-gw.ornl.teragrid.org:8080/esgf-web-fe/solrproxy'
		        metadataProxyUrl: '/esgf-web-fe/metadataproxy'
		    });
		
		 Manager.addWidget(new AjaxSolr.ResultWidget({
	          id: 'result',
	          target: '#search-results'
	        }));
		 
		 Manager.addWidget(new AjaxSolr.PagerWidget({
	          id: 'pager',
	          target: '#pager',
	          prevLabel: '&lt;',
	          nextLabel: '&gt;',
	          innerWindow: 1,
	          renderHeader: function (perPage, offset, total) {
	            $('#pager-header').html($('<span/>').text('displaying ' +
	                    Math.min(total, offset + 1) + ' to ' + Math.min(total, offset + perPage) +
	                    ' of ' + total + " search results"));
	          }
	        }));
		 
		 Manager.addWidget(new AjaxSolr.CurrentSearchWidget({
	          id: 'currentsearch',
	          target: '#current-selection'
	        }));

		 Manager.addWidget(new AjaxSolr.AutocompleteWidget({
	          id: 'text',
	          target: '#search-box',
	          field: 'text',
	          fields: [ 'project', 'model' , 'experiment']
	        }));
		 
		 
		 Manager.init();
		 Manager.store.addByValue('q','*:*');
		 
		 var fields = ['project', 'model', 'experiment', 'frequency', 'realm', 'instrument', 'variable', 'cf_variable', 'gcmd_variable']; //['project' , 'model', 'experiment', 'frequency', 'realm', 'instrument', 'variable', 'cf_variable', 'gcmd_variable'];

		 var params = {
	              'facet': true,
	              'facet.field': fields,
	              'facet.limit': 20,
	              'facet.mincount': 1,
	              'f.topics.facet.limit': 50,
	              'json.nl': 'map'
	            };
		 
		 for (var name in params) {
	    	if(name == 'facet.field') {
	    		for(var i=0;i<fields.length;i++) {
	    			Manager.store.addByValue(name,fields[i]);
	    		}
	    	}
	    	else {
	    		Manager.store.addByValue(name, params[name]);
	    	}
	     }
		 
		 Manager.addWidget(new AjaxSolr.GeospatialSearchWidget({
	          id: 'geo_browse'
	        }));

		 
		 Manager.addWidget(new AjaxSolr.TemporalWidget({
	          id: 'temp-browse'
	        }));
		
		 Manager.addWidget(new AjaxSolr.MetadataWidget({
		        id: 'metadata-browse'
		      }));
		 
		 for (var i = 0, l = fields.length; i < l; i++) {
	          Manager.addWidget(new AjaxSolr.FacetBrowserWidget({
	            id: fields[i],
	            target: '#' + fields[i],
	            field: fields[i]
	          }));
	        }

		

		 Manager.doRequest();
	});
	
	
	
})(jQuery);

