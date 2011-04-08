// $Id$

/**
 * @see http://wiki.apache.org/solr/SolJSON#JSON_specific_parameters
 * @class Manager
 * @augments AjaxSolr.AbstractManager
 */
AjaxSolr.Manager = AjaxSolr.AbstractManager.extend(
  /** @lends AjaxSolr.Manager.prototype */
  {
      executeRequest: function (servlet) {
          var self = this;
          self.loadExistingQueries();
          if (this.proxyUrl) {
          // jQuery.post(this.proxyUrl, 
          // { query: "I am something" }, 
          // function (data) { self.handleResponse(data); }, 
          // 'json');
          jQuery.ajax({
              url: this.proxyUrl,
              data: this.store.string(),
              type: 'GET',
              success: function(data) {self.handleResponse(data);},
              error: function() {alert("error");},
              dataType: 'json'
          });
          }
          else {
	          jQuery.getJSON(this.solrUrl + servlet + 
	              '?' + this.store.string() + '&wt=json&json.wrf=?', 
	              {}, function (data) { alert(self.store.string()); self.handleResponse(data); });
          }
      },
  
  	loadExistingQueries: function () {
  		var self = this;
  		
  		//alert(self.store.string());
  		
  		//load the cookies in the fq and q arrays from the store
  		var fq = localStorage['fq'];
  		var q = localStorage['q'];
  		
  		
  		//add them to the store and the current selection
  		//first, we must check if such a local store has been created
  		if(fq != null || fq != undefined) {
  			/* debug */
  	  		//alert('fq: ' + fq + '\nq: ' + q);
  			
  			var allFqs = fq.split(";");
  			//loop over all existing queries - note 'length-1' was used to ignore the trailing whitespace of the last split
  	  		for(var i=0;i<allFqs.length-1;i++)
  			{
  				Manager.store.addByValue('fq',allFqs[i]);
  			}
  		}
  		
  		
  	}
});
