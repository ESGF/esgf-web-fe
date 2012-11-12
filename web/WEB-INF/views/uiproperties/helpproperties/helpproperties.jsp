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
		
			<h1 style="margin-bottom:15px">ESGF UI Properties: Help Page</h1>
		
			<div style="margin-bottom:15px">
			Add, Delete, Reorder, and Update the help page here.
			</div>
		
		
		<div class="wrapper">
            <table id="t1" class="example">
            	<thead>
            		<td style="text-align:left;font-size:16px;border-top: 1px solid black;border-left: 1px solid black;">
            		</td>
            		<td style="text-align:left;font-size:16px;border-top: 1px solid black;">Subject Id 
            		</td>
            		<td style="text-align:left;font-size:16px;border-top: 1px solid black;">Header Name 
            		</td>
            		<td style="text-align:left;font-size:16px;border-top: 1px solid black;">Header Link
            		</td>
            		<td style="text-align:left;font-size:16px;border-top: 1px solid black;">Email
            		</td>
            		<td style="text-align:left;font-size:16px;border-top: 1px solid black;">Description
            		</td>
            		<td style="text-align:left;font-size:16px;border-top: 1px solid black;border-right: 1px solid black;">
            		</td>
            	</thead>
            	<c:set var="j" value="0"/>

 
		        <c:forEach var="user" items="${Help_Subject_Ids}"> 
					 <tr  
					 	 style="">  
					 	<td style="border-left: 1px solid black;">
                        	<img class="drag-handle" src="images/drag.png" alt="click and drag to rearrange" /> 
                        </td>
                    	<td class="subjects">
                        	<input class="drag-handle" id="tf1" style="width:100px" type="text" value="${Help_Subject_Ids[j]}" /> 
                    	</td>
                    	<td class="header_names">
                        	<input class="drag-handle" id="tf2" style="width:100px" type="text" value="${Help_Header_Names[j]}" /> 
                    	</td>
                    	<td class="header_links">
                        	<input class="drag-handle" id="tf3" style="width:100px" type="text" value="${Help_Header_Links[j]}" /> 
                    	</td>
                    	<td class="emails">
                        	<input class="drag-handle" id="tf4" style="width:100px" type="text" value="${Help_Emails[j]}" /> 
                    	</td>
                    	<td class="descriptions">
                        	<input class="drag-handle" id="tf5" style="width:100px" type="text" value="${Help_Descriptions[j]}" /> 
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
                    	<td class="subjects">
                        	<input class="drag-handle" id="tf1" style="width:100px" type="text" value="" /> 
                    	</td>
                    	<td class="header_names">
                        	<input class="drag-handle" id="tf2" style="width:100px" type="text" value="" /> 
                    	</td>
                    	<td class="header_links">
                        	<input class="drag-handle" id="tf3" style="width:100px" type="text" value="" /> 
                    	</td>
                    	<td class="emails">
                        	<input class="drag-handle" id="tf4" style="width:100px" type="text" value="" /> 
                    	</td>
                    	<td class="descriptions">
                        	<input class="drag-handle" id="tf5" style="width:100px" type="text" value="" /> 
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

			var retVal = confirm("Restoring default help page will undo any changes you have done previously.  Do you want to continue Restore?");
			if( retVal == true ){
	          $.ajax({
			    url: "helpproperties/restore",
			    type: "POST",
			    //async: false,
			    success: function() {
			      location.href=window.location;
			    },
			    error: function(request, status, error) {
			      alert(request.responseText);
			      alert('error in updating facets');				
			    }
			  });
		   	}else{
			    alert("Restoration cancelled");
				return false;
			}
			
			
			
			
		}),
		
		
		$('input#update_facets').click(function() {

			var help_subject_ids = new Array();
			var help_header_names = new Array();
			var help_header_links = new Array();
			var help_emails = new Array();
			var help_descriptions = new Array();

			
			
			var table = $(this).parent().find('#t1');

			table.find('td.subjects').each( function(index) {
				var subject = $(this).find('input').val();
				help_subject_ids.push(subject);
			});

			table.find('td.header_names').each( function(index) {
				var header_name = $(this).find('input').val();
				help_header_names.push(header_name);
			});

			table.find('td.header_links').each( function(index) {
				var header_link = $(this).find('input').val();
				help_header_links.push(header_link);
			});

			table.find('td.emails').each( function(index) {
				var email = $(this).find('input').val();
				help_emails.push(email);
			});

			table.find('td.descriptions').each( function(index) {
				var description = $(this).find('input').val();
				help_descriptions.push(description);
			});
			
			
			
			
			var queryStr = {'help_subject_ids' : help_subject_ids, 
							'help_header_names' : help_header_names,
							'help_header_links' : help_header_links,
							'help_emails' : help_emails,
							'help_descriptions' : help_descriptions};
			
			alert('quer: ' + queryStr.help_subject_ids);
			
			
			$.ajax({
				data: queryStr, 
			    url: "helpproperties/update",
			    type: "POST",
			    //async: false,
			    success: function() {
			    	alert('success');
					$('div.update_disclaimer').show();
			    },
				error: function(request, status, error) {
			        alert(request.responseText);
					alert('error in updating facets');
				}

			});
			
			
		});
		/**/
		
	 	
		
	});
	
	
	


</script>


	