/**
 * 
 */

(function ($) {

AjaxSolr.AutocompleteWidget = AjaxSolr.AbstractFacetWidget.extend({
  afterRequest: function () {
    $(this.target).find('input').val('');

    var self = this;
    var i,facet = null;
    var list = [];
    for (i = 0; i < this.fields.length; i++) {
        var field = this.fields[i];
        for (facet in this.manager.response.facet_counts.facet_fields[field]) {
            if(this.manager.response.facet_counts.facet_fields[field].hasOwnProperty(facet)) {
                list.push({
                    field: field,
                    value: facet,
                    text: facet + ' (' + this.manager.response.facet_counts.facet_fields[field][facet] + ') - ' + field
                });
            }
        }
    }

    this.requestSent = false;
    $(this.target).find('input').autocomplete(list, {
      formatItem: function(facet) {
        return facet.text;
      }
    }).result(function(e, facet) {
      self.requestSent = true;
      if (self.manager.store.addByValue('fq', facet.field + ':' + facet.value)) {
        self.manager.doRequest(0);
      }
    }).bind('keydown', function(e) {
      if (self.requestSent === false && e.which === 13) {
        var value = $(this).val();
        
        //alert('adding text:' + value + '; to fq storage');
        
        var fq = localStorage['fq'];
  	  	if(fq == null) {
  	  		//alert('add ' + value + '; to fq storage ' + self.fq(value));
  	  		fq = self.fq(value) + ';';
  	  		localStorage['fq'] = fq;
  	  	} else {
  	  		//if(fq.search(self.fq(value)) != -1) {
  	  		fq += self.fq(value) + ';';
  	  		localStorage['fq'] = fq;
  	  		//}
  	  	}
        
        if (value && self.add(value)) {
          self.manager.doRequest(0);
        }
      }
    });
  }
});

}(jQuery));
