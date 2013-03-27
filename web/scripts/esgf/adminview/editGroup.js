function editGroup(id, name, desc, vis, auto){
  $("div .error").hide();
  $("div .success").hide();
  
  //empty table first then fill
  var Parent = document.getElementById('edit_table');
  for(var i = Parent.rows.length - 1; i > 0; i--){
    Parent.deleteRow(i);
  }

  $('.editTable').append('<tr><td>Group Id</td><td>' + id + '</td></tr>' +
                                     '<tr><td>Group Name:</td><td><input alt="" type="text" class="text" id="groupName" value="' + name + '"/></td></tr>' +
                                     '<tr><td>Group Description:</td><td><input alt="" type="text" class="text" id="groupDesc" value="' + desc + '"/></td></tr>' +
                                     '<tr><td>Visable:</td><td><input alt="" type="text" class="text" id="visable" value="' + vis + '"/> (t or f)</td></tr>' +
                                     '<tr><td>Auto Approve:</td><td><input alt="" type="text" class="text" id="approve" value="' + auto + '"/> (t or f)</td></tr>' +
                                     '<tr><td></td><td><div class="submit"><input alt="" id="userUpdate" type="submit" value="Submit" class="button" onclick="javascript:groupInfo(\'' + id + '\')"/>' +
                                     '<input alt="" id="cancelUpdate" type="submit" value="Cancel" class="button" onclick="javascript:cancelUserInfo()"/>' +
                                     '<input alt="" id="userDelete" type="submit" value="Delete" class="button" onclick="javascript:areYouSure()"/></div>' +
                                     '<div class="doubleCheck" style="display: none"><font color="red"><strong>Are you sure you want to delete this group?</strong></font> ' +
                                     '<input alt="" id="canceldelete" type="submit" value="Cancel" class="button" onclick="javascript:cancelDelete()"/>' +
                                     '<input alt="" id="delete" type="submit" value="Delete" class="button" onclick="javascript:deleteGroupInfo(\'' + name + '\')"/></div>' +
                                     '</td></tr>');
  $("div .edit_field").show();
}
