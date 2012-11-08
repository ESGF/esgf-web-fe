<%@ include file="/WEB-INF/views/common/include.jsp" %>
<%@ include file="/WEB-INF/views/uiproperties/common/uiproperties_script_search.jsp" %>



        <script type="text/javascript">
            hljs.tabReplace = '    '; // 4 spaces
            hljs.initHighlightingOnLoad();
            
            
            $(document).ready(function(){
                $('#t1').dynoTable();               
                
            });
            
            
        </script>
      
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

            body {
                font-family: arial, verdana, sans-serif;
            }

            table {
                background: #D0E5F5;
                border: 1px solid black; 
                width: 520px;
            }

            table td {
                vertical-align: middle;
                /*border-right: 1px solid black;*/
    			border-bottom: 1px solid black;

            }
            
            table th {
                vertical-align: middle;
                border: 1px solid black;

            }

            table td input {
                width: 200px;
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
			Add, Delete, Reorder, and Update the facet sidebar here.
			</div>
		
		
		<div class="wrapper">
            <table id="t1" class="example">
            	<thead>
            		<td style="text-align:left;font-size:16px;border-top: 1px solid black;border-left: 1px solid black;">
            		</td>
            		<td style="text-align:left;font-size:16px;border-top: 1px solid black;">Short Name
            		</td>
            		<td style="text-align:left;font-size:16px;border-top: 1px solid black;">Long Name
            		</td>
            		<td style="text-align:left;font-size:16px;border-top: 1px solid black;">Description
            		</td>
            		<td style="text-align:left;font-size:16px;border-top: 1px solid black;border-right: 1px solid black;">
            		</td>
            	</thead>
            	<c:set var="j" value="0"/>

 
		        <c:forEach var="user" items="${Facet_Short_Names}">
					 <tr  
					 	 style="">  
					 	<td style="border-left: 1px solid black;">
                        	<img class="drag-handle" src="images/drag.png" alt="click and drag to rearrange" /> 
                        </td>
                    	<td class="short_names">
                        	<input class="drag-handle" id="tf1" style="width:100px" type="text" value="${Facet_Short_Names[j]}" /> 
                    	</td>
                    	<td class="long_names">
                        	<input class="drag-handle" id="tf2" style="width:150px" type="text" value="${Facet_Long_Names[j]}" /> 
                    	</td>
                    	<td class="descriptions">
                        	<input class="drag-handle" id="tf3" style="width:300px" type="text" value="${Facet_Descriptions[j]}" /> 
                    	</td>
                    	<td  style="border-right: 1px solid black;" style="width:50px">
                       		 <img class="row-remover" src="images/deleterow.gif" alt="Remove Row" />
                    	</td>
		            </tr> 
					<c:set var="j" value="${j+1}"/>
				</c:forEach>
            	
            	
                <tr id="add-template">
                    <td style="border-left: 1px solid black;">
                        	<img class="drag-handle" src="images/drag.png" alt="click and drag to rearrange" /> 
                        </td>
                    	<td class="short_names">
                        	<input class="drag-handle" id="tf1" style="width:100px" type="text" value="${Facet_Short_Names[j]}" /> 
                    	</td>
                    	<td class="long_names">
                        	<input class="drag-handle" id="tf2" style="width:150px" type="text" value="${Facet_Long_Names[j]}" /> 
                    	</td>
                    	<td class="descriptions">
                        	<input class="drag-handle" id="tf3" style="width:300px" type="text" value="${Facet_Descriptions[j]}" /> 
                    	</td>
                    	<td style="width:50px;border-right: 1px solid black;" >
                       		 <img class="row-remover" src="images/deleterow.gif" alt="Remove Row" />
                    	</td>
                </tr>
            </table>               
        </div>
        <p></p>
         
        <div class="clr"></div>
        
		<div class="restore_disclaimer" style="display:none;color:red;font-style:italic;text-align:center;margin-bottom:10px;">
		Facets Restored
		</div>
		<div class="reset_disclaimer" style="display:none;color:red;font-style:italic;text-align:center;margin-bottom:10px;">
		Facets Reset
		</div>
		<div class="update_disclaimer" style="display:none;color:red;font-style:italic;text-align:center;margin-bottom:10px;">
		Facets Updated
		</div>
		
     	<input style="" id="add-row" class="" type="submit" value="Add New Facet">
     	<input id="update_facets" style="" class="" type="submit" value="Update Facet Sidebar">
     	<input id="reset_facets"  style="" class="" type="submit" value="Reset">
     	<input id="restore_facets"  style="margin-bottom:20px" class="" type="submit" value="Restore Default Facets">
     	<%--  <input style="" class="" type="submit" value="Preview"> --%>
		</div>
		
		
		
		


		
		
		
	</ti:putAttribute>
</ti:insertDefinition>

<script type="text/javascript">

	$(function(){
		
		
		$('input#reset_facets').click(function() {

			location.href=window.location;
		}),
		
		$('input#restore_facets').click(function() {

			$('div#restore_disclaimer').show();
			
			
		}),
		
		
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
			
			
			var queryStr = {'short_names' : short_names, 'long_names' : long_names, 'descriptions' : descriptions};
			
			
			
			$.ajax({
				data: queryStr, 
			    url: "facetproperties/update",
			    type: "POST",
			    //async: false,
			    success: function() {
			    	alert('success');
					$('div.update_disclaimer').show();
			    },
				error: function() {
					alert('error in updating facets');
				}

			});
			
			
		});
		/**/
		
	 	
		
	});
	
	
	


</script>


	