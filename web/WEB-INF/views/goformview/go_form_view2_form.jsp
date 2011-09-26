<form id="new_user_form" action="goformview3" method="post">
		<h3 style="" id="form_title">Form 2 Info</h3>
			
			<table>
				
				<tr id="">
					<td>
						<div class="" style="">Destination Endpoint</div> 
					</td>
					<td>
						<select name="endpointdropdown">
							<c:set var="j" value="0"/>
        					<c:forEach var="group" items="${GoFormView_Endpoints}">
        						<option value="${GoFormView_Endpoints[j]}">${GoFormView_Endpoints[j]}</option>
            					<c:set var="j" value="${j+1}"/>
       						</c:forEach>
						</select>
					</td>
				</tr>
				<tr id="">
					<td>
						<div class="" style="">Target Endpoint Path Location</div> 
					</td>
					<td>
						<input type="text" class="text" id="target" name="target" value=""> 
					</td>
				</tr>
			</table>
			<p>
			<%-- Input params to go_form3 
		      	endpointdropdown -> dropdown box for available endpoints
		      	target -> text box representing the target endpoint path
		      	id -> DatasetName
		      	child_id[] -> FileNames
		      	child_url[] -> FileUrls
      		--%>
			<input type="hidden" name="id" id="${GoFormView_Dataset_Name}" value="${GoFormView_Dataset_Name}" />
			<c:set var="j" value="0"/>
			<c:forEach var="group" items="${GoFormView_File_Names}">
				<input type="hidden" name="child_id" id="${GoFormView_File_Names[j]}" value="${GoFormView_File_Names[j]}" />
				<input type="hidden" name="child_url" id="${GoFormView_File_Urls[j]}" value="${GoFormView_File_Urls[j]}" />
				<c:set var="j" value="${j+1}"/>
			</c:forEach>
	      		<input style="" class="adminbutton" type="submit" value="next">
	      	</p>
		</form>