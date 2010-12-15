/**
 * Solr for ESG
 * 
 */
(function ($) {
	
	$("div.search-entry").live('mouseover',  function() {
		$(this).css('background-color', '#ffccff');
	})
	
	$("div.search-entry").live('mouseout', function() {
		$(this).css("background-color", '#ffffff');
	})
	
	Manager = new AjaxSolr.Manager({
		proxyUrl: 'http://localhost:8080/esg-web/solrproxy'
	});
	
	Manager.addWidget(new AjaxSolr.ResultWidget({
		  id: 'result',
		  target: '#search-results'
		}));
	
	Manager.init();
	Manager.store.addByValue('q', '*:*');	
	Manager.doRequest();
	
})(jQuery);