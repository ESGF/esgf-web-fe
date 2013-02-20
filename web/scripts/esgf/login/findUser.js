function findusername() {
  $("div .success").hide();
  $("div .error").hide();
  var email = document.getElementById("usnemail").value;
  var jsonObj = new Object;
  jsonObj.email = email;			
  var jsonStr = JSON.stringify(jsonObj);
  var userinfo_url = '/esgf-web-fe/getopenidsproxy';
  $.ajax({
    type: "POST",
    url: userinfo_url,
    async: true,
    cache: false,
    data: {query:jsonStr},
    dataType: 'json',
    statusCode: {
    404: function(){
      alert("page note found");
    }},
    success: function(data) {
      if (data.EditOutput.status == "success") {
        var split = data.EditOutput.comment.split('][');
        var size = split.length;
        if(size > 2){
          var print = "<u>These are all the Openids you have on this node:</u> <br/>";
          for(i = 1; i < (size - 1); i++){
            print = print + split[i] + "<br/>";
          }
        }
        else{
          var print = "<font color='red'>This email: " + email + " is not registered on this node</font>"; 
        }
        document.getElementById("usnemail").value="";
        $("div .success").html(print);
        $("div .success").show();
        $("div .error").hide();
      } 
      else {
        $("div .error").html("Error");
        $("div .error").show();
        $("div .success").hide();
      }
    },
    error: function(request, status, error) {
      alert('request: ' + request + ' status: ' + status + ' error: ' + error);
      $("div .error").html("Error Three");
      $("div .error").show();
      $("div .success").hide();
    }
  });
}
