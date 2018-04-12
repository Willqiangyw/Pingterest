
//$.cookie("firebase");
var userEmail = sessionStorage.getItem("userEmail");
var userPwd = sessionStorage.getItem("userPwd");
var userName = sessionStorage.getItem("userName");
var userData;
firebase.auth().signInWithEmailAndPassword(userEmail, userPwd).then(function(user){

    }).catch(function(error) {
        // Handle Errors here.
        var errorCode = error.code;
        var errorMessage = error.message;
        console.error("Login error", errorCode, errorMessage);
    });
console.log(userEmail, userPwd)
//
//$('#search').load("searchCoach.html");
//var findcoach = function(){
//    window.location.href = "searchCoach.html"
//}

//var findplayer = function(){
//    window.location.href = "searchPlayer.html"
//}
//
//var buysell = function(){
//    
//}
//
//var me = function(){
//    window.location.href = "me.html"
//}

var signout = function(){
    firebase.auth().signOut().then(function() {
      console.log('Signed Out');
    }, function(error) {
      console.error('Sign Out Error', error);
    });
}

firebase.auth().onAuthStateChanged(function(user){
    if(user){
        console.log("logged in");
        firebase.database().ref('/Users/' + userName).once('value').then(function(snapshot) {
            var data = snapshot.val();
            userData = snapshot.val();
            var InfoName = data.name;
            document.getElementById('InfoName').innerHTML = InfoName;
            document.getElementById('InfoEmail').innerHTML = userEmail;
            document.getElementById('usrInfoLevel').innerHTML = "Level: " + data.level;
            document.getElementById('usrInfoAddr').innerHTML = "Address: <br>" + data.city + ", " + data.state + ", " + data.zip;
            document.getElementById('usrInfoDes').innerHTML = "Description: <br> &emsp;" + data.description;
        });
    } else {
        alert("You've signed out");
        window.location.href = "login.html"
    }
});