function toggle() {
  var usr = document.getElementById("username");
  if(usr.style.display == "block") {
    usr.style.display = "none";
    $("div .success").hide();
    $("div .error").hide();
  }
  else {
    usr.style.display = "block";
    $("div .success").hide();
    $("div .error").hide();
  }
} 

function roggle() {
  var pwd = document.getElementById("password");
  if(pwd.style.display == "block") {
    pwd.style.display = "none";
    $("div .success").hide();
    $("div .error").hide();    
  }
  else {
    pwd.style.display = "block";
    $("div .success").hide();
    $("div .error").hide();
  }
}
