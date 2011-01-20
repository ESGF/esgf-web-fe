//CONTROLLING EVENTS IN jQuery
var dateFrom = '';
var dateTo ='';

$(document).ready(function(){
	
	
	/*
	 * Date picker object created when the temporal search is activated 
	 */
	$(function() {
		dates = $( "#from, #to" ).datepicker({
			defaultDate: "+1w",
			dateFormat: "yy-mm-dd",
			changeMonth: true,
			changeYear: true,
			showOn: "button",
			buttonImage: "images/temporal_overlay/calendar.png",
			buttonImageOnly: true,
			onSelect: function( selectedDate ) {
				
				var option = this.id == "from" ? "minDate" : "maxDate",
						instance = $( this ).data( "datepicker");
				date = $.datepicker.parseDate(
						instance.settings.dateFormat,
						selectedDate, instance.settings );

				dateFrom = document.getElementById('from');
				dateTo = document.getElementById('to');
				
				//delete the original fq (if it existed)
				//Manager.store.remove('fq');
				/*
				var fq = Manager.store.values('fq');
			   
			    for (var i = 0, l = fq.length; i < l; i++) {
			    	
			    	if(fqString.search('start') != -1) {
			    		alert('removing start');
			    		Manager.store.removeFacet(fq[i]);
			    	}
			    	if(fqString.search('stop') != -1) {
			    		alert('removing start');
			    		Manager.store.removeFacet(fq[i]);
			    	}
			    }
			    */
				dates.not(this).datepicker("option",option,date);

				
			}
		});
		
		
		
		
	
	});

	
	$('div#tButton').click(function() {
		//call the helper method to assemble the fq and execute it
		executeDateQuery(dateFrom,dateTo);
		//alert('submitted: ' + geoQueryString);
		
	});

	/*
	 * Function for creating the temporal filter query
	 * Utilizes both the datetime_start and datetime_stop fields from solr
	 * The default searches each from [* TO *]
	 */
	function executeDateQuery(dateFrom,dateTo)
	{

		

		var datetime_start, datetime_startFQ,
			datetime_stop, datetime_stopFQ; 
		
		
		//datetime_start
		if(dateFrom.value)
		{
			datetime_start = dateFrom.value + 'T00:00:00Z';
		}
		else
		{
			datetime_start = '*';
		}
		
		
		//datetime_stop
		if(dateTo.value)
		{
			datetime_stop = dateTo.value + 'T00:00:00Z';
		}	
		else
		{
			datetime_stop = '*';
		}
			
		datetime_startFQ = 'datetime_start:[' + datetime_start + ' TO *]';
		datetime_stopFQ = 'datetime_stop:[* TO ' + datetime_stop + ']';
		
		Manager.store.addByValue('fq', datetime_startFQ );	
		Manager.store.addByValue('fq', datetime_stopFQ );	
		
		Manager.doRequest(0);
		
	}	
	
});		
