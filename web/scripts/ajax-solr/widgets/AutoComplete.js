/*****************************************************************************
 * Copyright � 2011 , UT-Battelle, LLC All rights reserved
 *
 * OPEN SOURCE LICENSE
 *
 * Subject to the conditions of this License, UT-Battelle, LLC (the
 * �Licensor�) hereby grants to any person (the �Licensee�) obtaining a copy
 * of this software and associated documentation files (the "Software"), a
 * perpetual, worldwide, non-exclusive, irrevocable copyright license to use,
 * copy, modify, merge, publish, distribute, and/or sublicense copies of the
 * Software.
 *
 * 1. Redistributions of Software must retain the above open source license
 * grant, copyright and license notices, this list of conditions, and the
 * disclaimer listed below.  Changes or modifications to, or derivative works
 * of the Software must be noted with comments and the contributor and
 * organization�s name.  If the Software is protected by a proprietary
 * trademark owned by Licensor or the Department of Energy, then derivative
 * works of the Software may not be distributed using the trademark without
 * the prior written approval of the trademark owner.
 *
 * 2. Neither the names of Licensor nor the Department of Energy may be used
 * to endorse or promote products derived from this Software without their
 * specific prior written permission.
 *
 * 3. The Software, with or without modification, must include the following
 * acknowledgment:
 *
 *    "This product includes software produced by UT-Battelle, LLC under
 *    Contract No. DE-AC05-00OR22725 with the Department of Energy.�
 *
 * 4. Licensee is authorized to commercialize its derivative works of the
 * Software.  All derivative works of the Software must include paragraphs 1,
 * 2, and 3 above, and the DISCLAIMER below.
 *
 *
 * DISCLAIMER
 *
 * UT-Battelle, LLC AND THE GOVERNMENT MAKE NO REPRESENTATIONS AND DISCLAIM
 * ALL WARRANTIES, BOTH EXPRESSED AND IMPLIED.  THERE ARE NO EXPRESS OR
 * IMPLIED WARRANTIES OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE,
 * OR THAT THE USE OF THE SOFTWARE WILL NOT INFRINGE ANY PATENT, COPYRIGHT,
 * TRADEMARK, OR OTHER PROPRIETARY RIGHTS, OR THAT THE SOFTWARE WILL
 * ACCOMPLISH THE INTENDED RESULTS OR THAT THE SOFTWARE OR ITS USE WILL NOT
 * RESULT IN INJURY OR DAMAGE.  The user assumes responsibility for all
 * liabilities, penalties, fines, claims, causes of action, and costs and
 * expenses, caused by, resulting from or arising out of, in whole or in part
 * the use, storage or disposal of the SOFTWARE.
 *
 *
 ******************************************************************************/

/**
 * Experimental Solr Front for ESG
 *
 * fwang2@ornl.gov
 * harneyjf@ornl.gov
 */


(function ($) {

AjaxSolr.AutocompleteWidget = AjaxSolr.AbstractFacetWidget.extend({

	init: function() {
		var self = this;
		
		/**
		 * DOCUMENT ME
		 */
		$('#search-button').live('click',function(){
			var value = $('input#query').val();

			if(value.length > 0) {
				
				/*
				var multiString = value.split('\+');
				
				for(var i=0;i<multiString.length;i++) {
					self.add(multiString[i]);
					
					// If the field value has a space or a colon in it, wrap it in quotes,
				    // unless it is a range query.
				    if (multiString[i].match(/[ :]/) && !multiString[i].match(/[\[\{]\S+ TO \S+[\]\}]/)) {
				    	multiString[i] = '"' + multiString[i] + '"';
				    }
					ESGF.localStorage.put('esgf_fq','text:'+multiString[i],'text:'+multiString[i]);
					ESGF.localStorage.put('esgf_queryString','text:'+multiString[i],'query='+multiString[i]);
				}
				*/
				//remove previous text
				var queryStringMap = ESGF.localStorage.getAll('esgf_queryString');
				
				for(var key in queryStringMap) {
					var keyStr = key;
					if(keyStr.search('text') > -1) {
						//alert('remove ' + key + ' ' + queryStringMap[key]);
						ESGF.localStorage.remove('esgf_queryString',keyStr);
					}
				}
				
				ESGF.localStorage.put('esgf_queryString','text:'+value,'query='+value);
				var mapStr = ESGF.localStorage.toString('esgf_queryString');
				
				//alert(mapStr);
				self.manager.doRequest(0);
			}
			
		});
		
		
		/**
		 * DOCUMENT ME
		 */
		$(this.target).find('input').bind('keydown', function(e) {
			if(self.requestSent === false && e.which === 13) {
				var value = $('input#query').val();
				if(value.length > 0) {
					/*
					var multiString = value.split('\+');
					
					for(var i=0;i<multiString.length;i++) {
						self.add(multiString[i]);
						
						// If the field value has a space or a colon in it, wrap it in quotes,
					    // unless it is a range query.
					    if (multiString[i].match(/[ :]/) && !multiString[i].match(/[\[\{]\S+ TO \S+[\]\}]/)) {
					    	multiString[i] = '"' + multiString[i] + '"';
					    }
						ESGF.localStorage.put('esgf_fq','text:'+multiString[i],'text:'+multiString[i]);
						
						ESGF.localStorage.put('esgf_queryString','text:'+multiString[i],'query='+multiString[i]);
						
					}
					*/
					
					//remove previous text
					var queryStringMap = ESGF.localStorage.getAll('esgf_queryString');
					
					for(var key in queryStringMap) {
						var keyStr = key;
						if(keyStr.search('text') > -1) {
							//alert('remove ' + keyStr + ' ' + queryStringMap[key]);
							ESGF.localStorage.remove('esgf_queryString',keyStr);
						}
					}
					
					
					
					ESGF.localStorage.put('esgf_queryString','text:'+value,'query='+value);
					var mapStr = ESGF.localStorage.toString('esgf_queryString');
					
					//alert(mapStr);
					
					self.manager.doRequest(0);
				}
			}
		});
		
		
	},

	/**
	 * DOCUMENT ME
	 */
  afterRequest: function () {
	  
	  $(this.target).find('input').val('');
	  
	  var self = this;
	  
	  
	  var list = [];
	  for (var i=0;i<this.fields.length;i++) {
		  var field = this.fields[i];
		  for(var facet in this.manager.response.facet_counts.facet_fields[field]) {
			  list.push({
				  field: field,
				  value: facet,
				  text: facet + ' (' + this.manager.response.facet_counts.facet_fields [field][facet] + ') - ' + field
			  });
		  }
	  }
	  
	  this.requestSent = false;
	  
	  /* disable autocomplete for now
	   * 
	  $(this.target).find('input').autocomplete(list, {
		  formatItem: function (facet) {
			  return facet.text;
		  }
	  }).result(function(e, facet) {
		  self.requestSent = true;
		  alert('result');
		  if(self.manager.store.addByValue('fq',facet.field + ':' + facet.value)){
			  

				alert('localstorage: ' + ESGF.localStorage.toString('esgf_fq'));

				ESGF.localStorage.put('esgf_fq','text:'+value,'text:'+value);
				
				
				alert('localstorage: ' + ESGF.localStorage.toString('esgf_fq'));
			  self.manager.doRequest(0);
		  }
	  });
	  */
	  
	
  }
  



});




}(jQuery));


