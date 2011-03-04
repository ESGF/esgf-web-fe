/**
 * Temporal Search Overlay Widget
 */
/*
 * 
 */
(function ($) {
    AjaxSolr.TemporalWidget = AjaxSolr.AbstractWidget.extend({
        afterRequest: function () {
            var self = this;
            $("div#temporal a[rel]").overlay({
                mask: {opacity: 0.5, color: '#000'},
                effect: 'apple',
                onBeforeLoad: function() {
                    $('.apple_overlay').css({'width' : '440px'});	
			        var wrap = this.getOverlay().find(".contentWrap");
                    wrap.load(this.getTrigger().attr("href"));			
                },
        
                onLoad: function() {
                    var dateFrom = '';
                    var dateTo ='';
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
                                var option = this.id === "from" ? "minDate" : "maxDate",
                                instance = $( this ).data( "datepicker");
                                date = $.datepicker.parseDate(
                                instance.settings.dateFormat,
                                selectedDate, instance.settings );
                                dateFrom = document.getElementById('from');
                                dateTo = document.getElementById('to');
                                dates.not(this).datepicker("option",option,date);
                            }
                        });
                    }); //end datepicker
                    $("button#submitTemporal").button({ });
                    $(".overlay_header").show();
                    $(".overlay_content").show();
                    $(".overlay_footer").show();
                    $(".overlay_border").show();
                    $('div#tButton').click(function() {
                        //call the helper method to assemble the fq and execute it
                        self.executeDateQuery(dateFrom,dateTo);
                    });
                },//end onLoad
                
                onClose: function() {
                    $(".overlay_header").hide();
                    $(".overlay_content").hide();
                    $(".overlay_footer").hide();
                    $(".overlay_border").hide();
                }//end onClose
           });
    
       },//end afterRequest
  
    
       /*
        * Function for creating the temporal filter query
        * Utilizes both the datetime_start and datetime_stop fields from solr
        * The default searches each from [* TO *]
        */
	   executeDateQuery: function (dateFrom,dateTo){
		   var datetime_start, datetime_startFQ,
           datetime_stop, datetime_stopFQ; 
        
           //datetime_start
           if(dateFrom.value) {
               datetime_start = dateFrom.value + 'T00:00:00Z';
           } else {
               datetime_start = '*';
           }
           //datetime_stop
           if(dateTo.value) {
               datetime_stop = dateTo.value + 'T00:00:00Z';
           } else {
               datetime_stop = '*';
           }
           datetime_startFQ = 'datetime_start:[' + datetime_start + ' TO *]';
           datetime_stopFQ = 'datetime_stop:[* TO ' + datetime_stop + ']';
           Manager.store.addByValue('fq', datetime_startFQ );	
           Manager.store.addByValue('fq', datetime_stopFQ );	
           Manager.doRequest(0);
	  }//end executeDateQuery
	
    });

}(jQuery));