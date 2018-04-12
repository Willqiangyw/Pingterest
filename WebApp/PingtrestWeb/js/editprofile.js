firebase.auth().onAuthStateChanged(function(user){
//    console.log(user)
    if(user){
        
        firebase.database().ref('/Users/' + userName).once('value').then(function(snapshot) {
            var data = snapshot.val();
            console.log(data);
            var age = data.age;
            var city = data.city;
            var gender = data.gender;
            var Level = data.level;
            var name = data.name;
            document.getElementById('newage').value = age;
//            document.getElementById('newcity').value = city;
            document.getElementById('newgender').value = gender;
            document.getElementById('newlevel').value = Level;
            document.getElementById('newname').value = name;
            document.getElementById('newRating').value = data.rating;
            document.getElementById('newCoach').value = data.isCoach;
            document.getElementById('newDes').value = data.description;
//            document.getElementById('newState').value = data.state;
            document.getElementById('newZip').value = data.zip;
            
        });
    } else {
        alert("You've signed out");
        window.location.href = "login.html"
    }

});
//console.log(user)
var back = function(){
    window.location.href = "dashboard.html"
    return false
}

var save = function(){
    if(userName != ""){
        console.log(userName)
        var age = document.getElementById('newage').value;
//        var city = document.getElementById('newcity').value;
        var gender = document.getElementById('newgender').value;
        var level = document.getElementById('newlevel').value;
        var name = document.getElementById('newname').value;
        var rating = document.getElementById('newRating').value ;
        var coach = document.getElementById('newCoach').value;
        var des = document.getElementById('newDes').value;
//        var state = document.getElementById('newState').value;

        var zip = document.getElementById('newZip').value;
        $.get("https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyCdgNpTlxZbvtB8_SK186QUqp-5Y72IIN4&address=" + zip, function(data, status){
            var userLong = data.results[0].geometry.location.lng;
            var userLat = data.results[0].geometry.location.lat;
            var userAddr = data.results[0].address_components;
            var city = userAddr[1].long_name;
            var state = userAddr[3].long_name;
            console.log(userAddr);
            firebase.database().ref('Users/' + userName).set({
              "age" : age,
              "city" : city,
              "description" : des,
              "gender" : gender,
              "isCoach" : coach,
              "key" : userName,
              "latitude" : userLat,
              "level" : level,
              "longitude" : userLong,
              "name" : name,
              "rating" : rating,
              "state" : state,
              "zip" : zip
            });
        });
        
    } 
                                                         
}