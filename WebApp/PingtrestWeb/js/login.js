(firebase.auth().signOut().then(function() {
  console.log('Signed Out');
}, function(error) {
  console.error('Sign Out Error', error);
}));
document.getElementById('userPwd').onkeydown = function(event){
//    console.log(event);
    if (event.key == "Enter"){
        console.log("pressed Enter");
        login();
    }
}

firebase.auth().onAuthStateChanged(function(user){
    console.log("onstatechange");
    console.log(user)
    if(user){
        var re = new RegExp("^(.+?)@");
        var matched = re.exec(user.email);
        if (matched[0]){
            userName = matched[0].slice(0,-1);
            console.log(userName);
            alert("Hello, " + userName);
            sessionStorage.setItem("userName", userName);
        }
        console.log(firebase.apps)
        window.location.href = "/dashboard.html"

    } else {
        
    }
});

var login = function(){
    console.log("logged in");
    userEmail = document.getElementById('userEmail').value;
    userPwd = document.getElementById('userPwd').value;
    console.log(userEmail);
    console.log(userPwd);
    firebase.auth().signInWithEmailAndPassword(userEmail, userPwd).then(function(user){
        sessionStorage.setItem("userEmail", userEmail);
        sessionStorage.setItem("userPwd", userPwd);
        
    }).catch(function(error) {
        // Handle Errors here.
        var errorCode = error.code;
        var errorMessage = error.message;
        console.error("Login error", errorCode, errorMessage);
        // ...
    });
};

var register = function(){
//    console.log("logged in");
    userEmail = document.getElementById('userEmail').value;
    userPwd = document.getElementById('userPwd').value;
    console.log(userEmail);
    console.log(userPwd);
    if(userEmail, userPwd){
        firebase.auth().createUserWithEmailAndPassword(userEmail, userPwd).then(function(user){
            sessionStorage.setItem("userEmail", userEmail);
            sessionStorage.setItem("userPwd", userPwd);
            window.location.href = "/editprofile.html"
        }).catch(function(error) {
          // Handle Errors here.
          var errorCode = error.code;
          var errorMessage = error.message;
          // ...
        });
        
    }

}