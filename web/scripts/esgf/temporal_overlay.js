var popupStatusTemporalSearch=0;

var temporalString = '<div id="popupTemporalSearch" > ' +
					 '	<a id="popupTemporalSearchClose">x</a>' +
					 '		<h1>Temporal Search</h1>' +
 					 '		<div id="temporal_helper">' +
					 '			<p>' +
					 '				Help: first define the range of dates either with text or by clicking the calendar icon; select advanced to extract data at particular times...' +
					 '			</p>' +
					 '	</div>' +
					 
					 
					 '<div id="temporalbox1">				' +

					 '	<div id="lb1">' +
					 '	<div id="rb1">' +
					 '	<div id="bb1"><div id="blc1"><div id="brc1">' +
'						<div id="tb1"><div id="tlc1"><div id="trc1">' +
'							<div id="temporal_range_title">' +
'							Date Range' +
'							</div>' +
'							<div id="temporal_content1">' +	
'								<div id="time_range">' +
'								<p class=legend> ' +
'										Search from: <input type="text" id="from" name="from" size="12" /> ' +
'										to: <input type="text" id="to" name="to" size="12" /> ' +
'									</p>'+
'									</div>'+
'									<br />' +
'									<div id="adv_temp1">' +
'										<p class="legend">Advanced...</p>	' + 
'									</div>'+
'							</div>' +
'						</div></div></div></div>'+
'						</div></div></div></div>' +
'					</div> <!-- temporal box -->' +
					 '</div> ' +
					 '<div id="backgroundPopupTemporalSearch"></div>    ';



//CONTROLLING EVENTS IN jQuery
$(document).ready(function(){
	
	$('div#temporal_overlay').after(temporalString);
	
	
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
				//alert(date);
				dates.not(this).datepicker("option",option,date);
			}
		});
	});			
	
	/*
	 * Date picker object created when the temporal search is activated 
	 */
	$('#adv_temp1').click(function() {
		  $("div#monthlist1").toggle();
		  $("div#timelist1").toggle();
			
	});
	
	//loading temporal search popup!
	function loadPopupTemporalSearch(){
		//loads popup only if it is disabled
		if(popupStatusTemporalSearch==0){
			$("#backgroundPopupTemporalSearch").css({
				"opacity": "0.8"
			});
			$("#backgroundPopupTemporalSearch").fadeIn("slow");
			$("#popupTemporalSearch").fadeIn("slow");
			popupStatusTemporalSearch = 1;
		}
	}


	//disabling geospatial search popup!
	function disablePopupTemporalSearch(){
		//disables popup only if it is enabled
		if(popupStatusTemporalSearch==1){
			$("#backgroundPopupTemporalSearch").fadeOut("slow");
			$("#popupTemporalSearch").fadeOut("slow");
			popupStatusTemporalSearch = 0;
		}
	}

	//centering popup
	function centerPopupTemporalSearch(){
		//request data for centering
		var windowWidth = document.documentElement.clientWidth;
		var windowHeight = document.documentElement.clientHeight;
		var popupHeight = $("#popupTemporalSearch").height();
		var popupWidth = $("#popupTemporalSearch").width();
		//centering
		$("#popupTemporalSearch").css({
			"position": "absolute",
			"top": windowHeight/2-popupHeight/2,
			"left": windowWidth/2-popupWidth/2
		});
		//only need force for IE6
		
		$("#backgroundPopupTemporalSearch").css({
			"height": windowHeight
		});
		
	}

	
	
	//Click the x event for geospatial!
	$("#popupTemporalSearchClose").click(function(){
		disablePopupTemporalSearch();
	});
	
	//Click out event!
	$("#backgroundPopupTemporalSearch").click(function(){
		disablePopupTemporalSearch();
	});
	
	$('div#temporal').click(function(){
		if(popupStatusTemporalSearch==0){
			$("#backgroundPopupTemporalSearch").css({
				"opacity": "0.8"
			});
			$("#backgroundPopupTemporalSearch").fadeIn("slow");
			$("#popupTemporalSearch").fadeIn("slow");
			popupStatusTemporalSearch = 1;
		}
	});
	
	
	
	
	
	
});




function temporal_link()
{
	alert('temporal');
	if(popupStatusTemporalSearch==0){
		$("#backgroundPopupTemporalSearch").css({
			"opacity": "0.8"
		});
		$("#backgroundPopupTemporalSearch").fadeIn("slow");
		$("#popupTemporalSearch").fadeIn("slow");
		popupStatusTemporalSearch = 1;
	}
}




/*
<div id="popupTemporalSearch" >
<a id="popupTemporalSearchClose">x</a>
<h1>Temporal Search</h1>
<div id="temporal_helper">
<p>
Help: first define the range of dates either with text or by clicking the calendar icon; select advanced to extract data at particular times...
</p>
</div>    


<div id="temporalbox1">				

	<div id="lb1">
	<div id="rb1">
	<div id="bb1"><div id="blc1"><div id="brc1">
	<div id="tb1"><div id="tlc1"><div id="trc1">
		
		<div id="temporal_range_title">
		Date Range
		</div>
		
		<div id="temporal_content1">	
	
			<div id="time_range">
			
			<p class=legend> 
					Search from: <input type="text" id="from1" name="from" size="12" /> 
					to: <input type="text" id="to1" name="to" size="12" /> 
				</p>
				</div>
				<br />
				 
				<div id="adv_temp1">
					<p class="legend">Advanced...</p>	 
				</div>
		
		</div>

	</div></div></div></div>
	</div></div></div></div>

</div> <!-- temporal box -->





</div>   


<div id="backgroundPopupTemporalSearch"></div>
*/
