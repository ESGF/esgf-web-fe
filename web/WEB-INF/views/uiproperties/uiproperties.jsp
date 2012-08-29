<%@ include file="/WEB-INF/views/common/include.jsp" %>
<%@ include file="/WEB-INF/views/uiproperties/uiproperties_style_search.jsp" %>
<%@ include file="/WEB-INF/views/uiproperties/uiproperties_script_search.jsp" %>

		
		
		
<ti:insertDefinition name="main-layout" >

    <ti:putAttribute name="extrastyle">
    <!--  
        <link rel="stylesheet" href='<c:url value="/styles/security.css" />' type="text/css">
    -->
    </ti:putAttribute>


    <ti:putAttribute name="main">
    
    	<div class="span-15 prepend-6 last" style="margin-top:10px">
    		<h2>UI Configuration</h2>
		</div>
		
		<div class="span-18 prepend-3 last">
		
		<p/>
		
		<%-- 
		if(keyStr.equals("enableGlobusOnline")) {
                    uiproperties.setEnableGlobusOnline(value);
                } else if(keyStr.equals("lasrestrictions")) {
                    System.out.println(value.split(";").length);
                    String [] valueArr = value.split(";");
                    List<String> lasRestrictionList = new ArrayList<String>();
                    for(int i=0;i<valueArr.length;i++) {
                        lasRestrictionList.add(valueArr[i]);
                    }
                    uiproperties.setLasExclusions(lasRestrictionList);
                } else if(keyStr.equals("datacartMax")) {
                    uiproperties.setDatacartMax(value);
                } else if(keyStr.equals("defaultFileCounter")) {
                    uiproperties.setDefaultFileCounter(value);
                } else if(keyStr.equals("defaultDatasetCounter")) {
                    uiproperties.setDefaultDatasetCounter(value);
                }
		--%>
		
		<input type="radio" name="go" value="goTrue" id="goTrue" /> Turn on globus online  
		<input type="radio" name="go" value="goFalse" id="goFalse" /> Turn off globus online
		
		
		
		
		</div>
		
		<div class="span-18 prepend-3 last">
		
		Max datasets in data cart: 
			<select class="datasetCounter" name="datasetC" style="display:inline">
  				<option id="" value="5">5</option>
  				<option>10</option>
  				<option>25</option>
  				<option>50</option>
  				<option>100</option>
			</select>
		</div>
		
		<div class="span-18 prepend-3 last">
		
		Default dataset counter: 
			<select>
  				<option>5</option>
  				<option>10</option>
  				<option>25</option>
  				<option>50</option>
  				<option>100</option>
			</select>
		</div>
		
		<div class="span-18 prepend-3 last">
		
		Default file counter: 
			<select>
  				<option>5</option>
  				<option>10</option>
  				<option>25</option>
  				<option>50</option>
  				<option>100</option>
			</select>
		</div>
		
		<div class="span-18 prepend-3 last">
			Max datasets in data cart: <input type="text" size="5" name="datacartMax" id="datacartMax" />
		</div>
		
		<div class="span-18 prepend-3 last">
			<a href="#">Add LAS Restriction</a><br />
			<span>
				LAS Restriction: <input type="text" name="firstname" />
			</span>
			<br />
		</div>
		
		<script>
		(function ($) {
			
			alert('changing buttons');
			
			//toggle the "checked" attribute of the showAllContents radio button
			//$("input[id='maleButton']").attr("checked","true");
			$("input[id='goFalse']").attr("checked","true");
			
			
			$("input[id='datacartMax']").val('10');
			
	    	//if(ESGF.setting.showAllContents == 'true') {
	    	//	$("input[id='datacart_filtered']").attr("checked","false");
	    	//	$("input[id='datacart_filter_all']").attr("checked","true");
	    	//} else {
	    	//	$("input[id='datacart_filter_all']").attr("checked","false");
	    	//	$("input[id='datacart_filtered']").attr("checked","true");
	    	//}
	    	
			
			
		}(jQuery));
		</script>
		
	</ti:putAttribute>

</ti:insertDefinition>