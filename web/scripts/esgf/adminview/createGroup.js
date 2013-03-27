function createGroup(){
  $("div .error").hide();
  $("div .success").hide();
  
  //empty table first then fill
  var Parent = document.getElementById('edit_table');
  for(var i = Parent.rows.length - 1; i > 0; i--){
    Parent.deleteRow(i);
  }

  $('.editTable').append('<tr><td>Group Id</td><td>N/A</td></tr>' +
    '<tr><td>Group Name:</td><td><input alt="" type="text" class="text" id="groupName" value=""/></td></tr>' +
    '<tr><td>Group Description:</td><td><input alt="" type="text" class="text" id="groupDesc" value=""/></td></tr>' +
    '<tr><td>Visable:</td><td><input alt="" type="text" class="text" id="visable" value=""/> (t or f)</td></tr>' +
    '<tr><td>Auto Approve:</td><td><input alt="" type="text" class="text" id="approve" value=""/> (t or f)</td></tr>' +
    '<tr><td></td><td><input alt="" id="userUpdate" type="submit" value="Submit" class="button" onclick="javascript:submitNewGroup()"/>' +
    '<input alt="" id="cancelUpdate" type="submit" value="Cancel" class="button" onclick="javascript:cancelNewGroup()"/>' +
    '</td></tr>');

  $("div .edit_field").show();
}

function cancelNewGroup(){
  $("div .edit_field").hide();
}
