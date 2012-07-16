(function ($) {

AjaxSolr.DatasetSelectionWidget = AjaxSolr.AbstractWidget.extend({
	init: function() {},
	
	beforeRequest: function() {

		$('a#add_all').die('click');
		$('a#remove_all').die('click');
		
		$("select[name='datasetC']").die('change');
		
	},
	
	afterRequest: function() {
		
		$('a#add_all').live('click', function() {
			
			//ajax call?
			
			//if(this.innerHTML == 'Add All to Datacart') {
				//this.innerHTML = 'Remove All From Datacart';
				
				//
            	//ESGF.localStorage.put('dataCart',evt.data.doc.id,datasetInfo);
			
			var canAdd = true;
			
			
			
				for(var i=0;i<Manager.response.response.docs.length;i++) {
					var doc = Manager.response.response.docs[i];
					
					var datasetInfo = {'numFiles' : doc['number_of_files'], 'peer' : doc['index_node'] , 'xlink' : doc['xlink']};
	        		
					if(canAdd) {
						//alert(' in can add for: ' + doc.id);
						if(ESGF.setting.datacartMax <= ESGF.localStorage.toKeyArr('dataCart').length) {
			        		alert('Data cart contents exceeded.  Please remove a dataset from the datacart before adding a new one. ' +
			        			  'Note that the last dataset added was ' + ESGF.localStorage.toKeyArr('dataCart')[ESGF.localStorage.toKeyArr('dataCart').length-1]);
			        		canAdd = false;
						} else {

							ESGF.localStorage.put('dataCart',doc.id,datasetInfo);
						}
						
					}
					
				}
			
			
            	Manager.doRequest(0);
				
			
		});
		
		$('a#remove_all').live('click',function(){
			//this.innerHTML = 'Add All to Datacart';
			
			for(var i=0;i<Manager.response.response.docs.length;i++) {
				var doc = Manager.response.response.docs[i];

				ESGF.localStorage.remove('dataCart',doc.id);
			}
			

        	Manager.doRequest(0);
		});
		
		
		$("select[name='datasetC']").live('change', function() {
			var selectedIndex = $("select[name='datasetC']").attr("selectedIndex");
			
			if(selectedIndex == 0) {
				ESGF.setting.datasetCounter = 5;
			} else if(selectedIndex == 1) {
				ESGF.setting.datasetCounter = 10;
			} else if(selectedIndex == 2) { 
				ESGF.setting.datasetCounter = 25;
			} else if(selectedIndex == 3) {
				ESGF.setting.datasetCounter = 50;
			} else if(selectedIndex == 4) {
				ESGF.setting.datasetCounter = 100;
			} else if(selectedIndex == 5) {
				ESGF.setting.datasetCounter = 200;
			} else {
				ESGF.setting.datasetCounter = 500;
			}

			Manager.doRequest(0);
			
		});
	
		
		
	}
	
});

})(jQuery);