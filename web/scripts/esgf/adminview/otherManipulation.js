$(document).ready(function() {
  $("#manipulationRoles").multiselect({
    click: function(event, ui){
    //just incase
    }
  });
});

function help(){
  var help =  "select action: <br/>" +
              "  Add - add User X to Group Y with Roles Z <br/>" +
              "  Edit - update User Xs Roles Z in Group Y <br/>" +
              "  Remove - remove User X from Group Y with Role Z (if role is unknown select \"Check all\" <br/>" +
              "select User: username, first last, email<br/>" +
              "select Group:<br/>" +
              "select Role:<br/>" +
              "press submit:"; 
  $("div .error").html(help);
  $("div .error").show();
}

