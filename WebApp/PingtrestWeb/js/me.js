console.log("loading");
var userEmail = sessionStorage.getItem("userEmail");
var userPwd = sessionStorage.getItem("userPwd");
firebase.auth().signInWithEmailAndPassword(userEmail, userPwd).then(function(user){
//        sessionStorage.setItem("userEmail", userEmail);
//        sessionStorage.setItem("userPwd", userPwd);
//         console.log(firebase.apps);
//        alert("you have signed in");
    console.log("loaded");
    }).catch(function(error) {
        // Handle Errors here.
        var errorCode = error.code;
        var errorMessage = error.message;
        console.error("Login error", errorCode, errorMessage);
        // ...
    });
firebase.auth().onAuthStateChanged(function(user){
//    console.log(user)
    if(user){
        var re = new RegExp("^(.+?)@");
        var matched = re.exec(user.email);
        var userName = "";
        if (matched[0]){
            userName = matched[0].slice(0,-1);
            document.getElementById('usrname').innerHTML = userName
        }
        var userId = firebase.auth().currentUser.uid;
        console.log(userName)
        return firebase.database().ref('/Users/' + userName).once('value').then(function(snapshot) {
//            var data = snapshot.val();
//            console.log(data)
//            var age = data.age;
//            var city = data.city;
//            var gender = data.gender;
//            var Level = data.level;
//            var name = data.name;
//            document.getElementById('age').innerHTML = "Age: " + age;
//            document.getElementById('city').innerHTML = "city: " + city;
//            document.getElementById('gender').innerHTML = "gender: " + gender;
//            document.getElementById('level').innerHTML = "Level: " + Level;
//            document.getElementById('name').innerHTML = "name: " + name;
        });
    } else {
        alert("You've signed out");
        window.location.href = "login.html"
    }
});

var signout = function(){
    firebase.auth().signOut().then(function() {
      console.log('Signed Out');
    }, function(error) {
      console.error('Sign Out Error', error);
    });
}

var back = function(){
    window.location.href = "dashboard.html"
    return false
}

var editProfile = function(){
    window.location.href = "editprofile.html"
    return false
}