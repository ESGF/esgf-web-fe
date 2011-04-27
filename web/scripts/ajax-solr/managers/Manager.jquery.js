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
	              {}, function (data) { self.handleResponse(data); });
          }
      },
  
  	loadExistingQueries: function () {
  		var self = this;
  		
  		//compare the parameter store with the local store
  		//if the local store contains something that is not in the parameter store, then add to parameter store
  		var fq = localStorage['fq'];
  		
  		if(fq != undefined) {
  			var allFqs = fq.split(";");
  			//alert('allFqs: ' + allFqs);
  			for(var i=0;i<allFqs.length-1;i++)
  			{
  				
  				//alert('i: ' + escape(allFqs[i]));
  				//alert(self.store.string().search(escape(allFqs[i])));
  				if(self.store.string().search(escape(allFqs[i])) == -1) {
  					//alert('   I need to add ' +  allFqs[i] + ' to the parameter store');
  					Manager.store.addByValue('fq',allFqs[i]);
  				}
  			}
  	  		//alert('Parameter Store: ' + self.store.string() + ' localStore: ' + escape(localStorage['fq'].substring(0,localStorage['fq'].length-1)));
  		}
  		else {
  			//alert('undefined doesnt matter');
  		}
  		
  		

  		
  	}
});
