<%@ include file="/WEB-INF/views/common/include.jsp" %>
<%@ include file="/WEB-INF/views/uiproperties/uiproperties_script_search.jsp" %>

<style type="text/css">

<style type="text/css">
            h1{text-align: center;}
            p{text-align: left;}
            img{}
            #log {
                width: 420px;
                height: 280px;
                position: fixed;
                bottom: 0;
                right: 0;
                background: #000;
                color: lightgreen;
                font-family: monospace;
                font-weight: bold;
                border: 2px solid #c0c0c0;
                overflow: auto;
            }
</style>

        <script type="text/javascript">
            hljs.tabReplace = '    '; // 4 spaces
            hljs.initHighlightingOnLoad();
            
            
            $(document).ready(function(){
                $('#t1').dynoTable();               
                
            });
            
            
        </script>
      
<style type="text/css">
            body {
                font-family: arial, verdana, sans-serif;
            }

            table {
                background: #D0E5F5;
                border: 1px solid #c0c0c0; 
                width: 520px;
            }

            table td {
                vertical-align: middle;

            }

            table td input {
                width: 250px;
                margin: 0;
                background: #fff;
                height: 18px;
            }  

            table td select {
                background: #fff;
                height: 18px;
                width: 90px;
            }

            #msg {
                width: 420px;
                float:left;
            }
            
            button {
                width: 100px;
                height: 25px; 
                float:left;                
            }
            
            .clr {
                clear: both;
            }

            .row-cloner, .row-remover {
                cursor: pointer;
            } 

            pre, code {
                margin: 0 !important;
                padding: 0 !important;
            }
        </style>      
      
      
      
      
<ti:insertDefinition name="main-layout" >

    <ti:putAttribute name="extrastyle">
        <link rel="stylesheet" href='<c:url value="/styles/security.css" />' type="text/css">
    </ti:putAttribute>

    <ti:putAttribute name="main">
		<div class="span-22 prepend-1 last" style="min-height:500px;">
		
			<p/>
		
			<h1 style="margin-bottom:15px">ESGF UI Properties</h1>
		
			<div style="margin-bottom:15px">
			Welcome to the ESGF UI Properties Page!  ${Facet_Short_Names[0] }
			</div>
		
		
		<div class="wrapper">
            <table id="t1" class="example">
            	<thead>
            		<td>
            		</td>
            		<td>Short Name
            		</td>
            		<td>Long Name
            		</td>
            		<td>Description
            		</td>
            	</thead>
            	<c:set var="j" value="0"/>

 
		        <c:forEach var="user" items="${Facet_Short_Names}">
					 <tr  
					 	 style="">  
					 	<td >
                        	<img class="drag-handle" src="images/drag.png" alt="click and drag to rearrange" /> 
                        </td>
                    	<td class="short_names">
                        	<input class="drag-handle" id="tf1" style="width:100px" type="text" value="${Facet_Short_Names[j]}" /> 
                    	</td>
                    	<td class="long_names">
                        	<input class="drag-handle" id="tf2" style="width:125px" type="text" value="${Facet_Long_Names[j]}" /> 
                    	</td>
                    	<td class="descriptions">
                        	<input class="drag-handle" id="tf3" style="width:150px" type="text" value="${Facet_Descriptions[j]}" /> 
                    	</td>
                    	<td style="width:50px">
                       		 <img class="row-remover" src="images/deleterow.gif" alt="Remove Row" />
                    	</td>
		            </tr> 
					<c:set var="j" value="${j+1}"/>
				</c:forEach>
            	
            	
                <tr id="add-template">
                    <td style="">
                        	<img class="drag-handle" src="images/drag.png" alt="click and drag to rearrange" /> 
                        </td>
                    	<td class="short_names">
                        	<input class="drag-handle" id="tf1" style="width:100px" type="text" value="${Facet_Short_Names[j]}" /> 
                    	</td>
                    	<td class="long_names">
                        	<input class="drag-handle" id="tf2" style="width:125px" type="text" value="${Facet_Long_Names[j]}" /> 
                    	</td>
                    	<td class="descriptions">
                        	<input class="drag-handle" id="tf3" style="width:150px" type="text" value="${Facet_Descriptions[j]}" /> 
                    	</td>
                    	<td style="width:50px" >
                       		 <img class="row-remover" src="images/deleterow.gif" alt="Remove Row" />
                    	</td>
                </tr>
            </table>               
        </div>
        <p></p>
         
        <div class="clr"></div>
		
		
     	<input style="" id="add-row" class="" type="submit" value="Add New Facet">
     	<input id="update_facets" style="" class="" type="submit" value="Update Facet Sidebar">
     	<input style="" class="" type="submit" value="Reset">
     	<%--  <input style="" class="" type="submit" value="Preview"> --%>
		</div>
		
		
		
		


		
		
		
	</ti:putAttribute>
</ti:insertDefinition>

<script type="text/javascript">

	$(function(){
		

		
		$('input#update_facets').click(function() {

			var table = $(this).parent().find('#t1');

			var short_names = new Array();
			var long_names = new Array();
			var descriptions = new Array();
			
			table.find('td.short_names').each( function(index) {
				var shortName = $(this).find('input').val();
				short_names.push(shortName);
			});
			
			table.find('td.long_names').each( function(index) {
				var longName = $(this).find('input').val();
				long_names.push(longName);
			});
			
			table.find('td.descriptions').each( function(index) {
				var description = $(this).find('input').val();
				descriptions.push(description);
			});
			
			alert('short_names: ' + short_names);
			
			var queryStr = {'short_names' : short_names, 'long_names' : long_names, 'descriptions' : descriptions};
			
			$.ajax({
				data: queryStr, 
			    url: "uiproperties/update",
			    type: "POST",
			    //async: false,
			    success: function(data) {
			    	alert('success');
			    },
				error: function() {
					alert('error in updating');
				}

			});
			
			
		});
		
		
		/*
		// Initialise the table
	    $("#table-1").tableDnD();
		
	 // Initialise the second table specifying a dragClass and an onDrop function that will display an alert
		$("#table-2").tableDnD({
		    onDragClass: "myDragClass",
		    onDrop: function(table, row) {
	            var rows = table.tBodies[0].rows;
	            var debugStr = "Row dropped was "+row.id+". New order: ";
	            for (var i=0; i<rows.length; i++) {
	                debugStr += rows[i].id+" ";
	            }
		    	//alert(debugStr);
		    	alert($.tableDnD.serialize());
		    },
			onDragStart: function(table, row) {
				//$(#debugArea).html("Started dragging row "+row.id);
			}
		});
	 
	 
	 	$('input#update_facets').click(function() {

			
			var table = $(this).parent().find('#table-2');
			
			
			
			alert(table.html());
			
			//alert($(this).parent().html().find('#table-2'));
			table.append('<tr id="2.114"><td>114</td><td>Fourteen</td><td><input type="text" name="fourteen1" value="114"/></td></tr>');

			
	 	});
	*/		
	 	
	 	
		
	});
	
	
	


</script>


		
<%-- --%>
<%-- 
<table id="table-2" cellspacing="0" cellpadding="2">

   <tr id="2.1"><td>1</td><td>One</td><td><input type="text" name="one" value="one"/></td></tr>
    <tr id="2.2"><td>2</td><td>Two</td><td><input type="text" name="two" value="two"/></td></tr>
    <tr id="2.3"><td>3</td><td>Three</td><td><input type="text" name="three" value="three"/></td></tr>
    <tr id="2.4"><td>4</td><td>Four</td><td><input type="text" name="four" value="four"/></td></tr>
    <tr id="2.5"><td>5</td><td>Five</td><td><input type="text" name="five" value="five"/></td></tr>
    <tr id="2.6"><td>6</td><td>Six</td><td><input type="text" name="six" value="six"/></td></tr>
    <tr id="2.7"><td>7</td><td>Seven</td><td><input type="text" name="seven" value="7"/></td></tr>
    <tr id="2.8"><td>8</td><td>Eight</td><td><input type="text" name="eight" value="8"/></td></tr>
    <tr id="2.9"><td>9</td><td>Nine</td><td><input type="text" name="nine" value="9"/></td></tr>
    <tr id="2.10"><td>10</td><td>Ten</td><td><input type="text" name="ten" value="10"/></td></tr>
    <tr id="2.11"><td>11</td><td>Eleven</td><td><input type="text" name="eleven" value="11"/></td></tr>
    <tr id="2.12"><td>12</td><td>Twelve</td><td><input type="text" name="twelve" value="12"/></td></tr>
    <tr id="2.13"><td>13</td><td>Thirteen</td><td><input type="text" name="thirteen" value="13"/></td></tr>
    <tr id="2.14"><td>14</td><td>Fourteen</td><td><input type="text" name="fourteen" value="14"/></td></tr>
</table>
--%>
