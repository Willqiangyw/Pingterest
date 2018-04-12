const navHeight = 1.5 * $('#chatNav')[0].offsetHeight + $('#chatNav').height();
//document.getElementById('searchContent').style.height = String($(window).height() - navHeight) + "px";
document.getElementById('resultContent').style.height = String($(window).height() - navHeight) + "px";
document.getElementById('chatContentEle').style.height = String($(window).height() - navHeight) + "px";
var userDB = {};

var search = function(){
    document.getElementById("searchCol").classList.remove("d-none");
    document.getElementById("resultList").innerHTML = "";
    var targetAge = document.getElementById("userAgeInput").value;
    var userZip = document.getElementById("userZip").value
    var userGenderList = document.getElementsByName("optionsRadios");
    var targetRating = document.getElementById("userRating").value;
    var targetLevel = document.getElementById("userLevel").value
    
    var userGender;
    for (var i = 0, length = userGenderList.length; i < length; i++){
        if (userGenderList[i].checked){
            userGender = userGenderList[i].value;
        }
    }
    
    if (targetAge && userZip && userGender && targetRating && targetLevel){
        $.get("http://maps.googleapis.com/maps/api/geocode/json?address=" + userZip, function(data, status){
            var userLong = data.results[0].geometry.location.lng;
            var userLat = data.results[0].geometry.location.lat;
            var url = "https://us-central1-pingterest-ffca7.cloudfunctions.net/findCoach?userLong=";
            url += userLong + "&userLat=";
            url += userLat+ "&userGender=";
            url += userGender + "&targetRating=";
            url += targetRating + "&targetlevel="+ targetLevel;
            console.log(url);
            $.get(url, function(data, status){
                userDB = {};
                var coaches = data.coaches;
//                for (key in coaches){
//                    firebase.database().ref('/Users/' + coaches[key]).once('value').then(function(snapshot) {
//                        var ud = snapshot.val();
//                        userDB[coaches[key]] = ud;
//                        console.log(coaches[key]);
//                        document.getElementById("resultList").innerHTML += 
//                            constructListHtml(
//                            coaches[key],
//                            ud.name,
//                            ud.level,
//                            ud.description,
//                            ud.rating
//                        );
//                    });
//                }
                firebase.database().ref('/Users/').once('value').then(function(snapshot) {
                    for (key in coaches){
                        var ud = snapshot.child(coaches[key]).val();
                        userDB[coaches[key]] = ud;
                        console.log(coaches[key]);
                        document.getElementById("resultList").innerHTML += 
                            constructListHtml(
                            coaches[key],
                            ud.name,
                            ud.level,
                            ud.description,
                            ud.rating
                        );
                    }
                });
            });
        });
    }
    
    return false;
//    document.getElementById("resultList").innerHTML = 
}

var prevActive;

var selectCoach = function(coachKey){
    
    if(prevActive){
        document.getElementById(prevActive).classList.remove("active")
    }
    document.getElementById(coachKey).classList.add("active");
    prevActive = coachKey;
//    document.getElementById("searchCol").classList.add("d-none");
    closeChat();
    console.log("selected")
    document.getElementById("detailCol").classList.remove("d-none");
    console.log(coachKey,
        userDB[coachKey].name,
        userDB[coachKey].level,
        userDB[coachKey].city,
        userDB[coachKey].state,
        userDB[coachKey].description)
    document.getElementById("detailCol").innerHTML = constructDetailHtml(
        coachKey,
        userDB[coachKey].name,
        userDB[coachKey].level,
        userDB[coachKey].city,
        userDB[coachKey].state,
        userDB[coachKey].description
    )
}

var constructListHtml = function(key, name, level, description, rating){
    return `<a href="#" class="list-group-item list-group-item-action flex-column align-items-start" id="` + key + `" onclick="selectCoach(this.id)">
                        <div class="row">
                            <div class="col-sm-4 col-xs-4 text-center d-flex">
                                <img class="mx-auto my-auto" src="http://placehold.it/100">
                            </div>
                            <div class="col-sm-8 col-xs-8">
                                <div class="d-flex w-100 justify-content-between">
                                  <h5 class="mb-1 ">` + name +`</h5>
                                  <large class="">Level: `+ level +`</large>
                                </div>
                                <p class="mb-1 text-dark">` + description +`</p>
                                <small>Rating: `+ rating +`</small>
                            </div>   
                        </div>
                      </a>`;
}

var constructDetailHtml = function(key, name, level, city, state, description){
    return `<div class="card border-primary" style="">
            <div class="bg-primary text-white" style="padding: 15px;">
                    <div class="row">
                        <div class="col-xl-1 col-sm-2 col-xs-2 text-left d-flex">
                            <img class="mx-auto my-auto img-round" src="http://placehold.it/50">
                        </div>
                        <div class="col-xl-11 col-sm-10 col-xs-10 text-left">
                            <div class="w-100 justify-content-right">
                              <h5 class="mb-1" style="padding-left:15px;">`+ name +`</h5>
                            </div>
                        </div>   
                    </div>
            </div>  
         <ul class="list-group list-group-flush">
                <li class="list-group-item">Level &emsp; &emsp; `+ level +`</li>
                <li class="list-group-item ">Address &emsp; `+ city + `&nbsp;` + state + `</li>
                <li class="list-group-item">
                    <div>
                        Rating
                        <p></p>
                        <div class="rating ">
                            <span>☆</span><span>☆</span><span>☆</span><span>☆</span><span>☆</span>
                        </div>  
                    </div>
                    <p></p>
                    <a class="btn btn-success text-white btn-sm" style="padding-left: 30px;padding-right: 30px;" >
                        Rate!
                    </a>
                  
                </li>
                <li class="list-group-item">Description <br><br>` + description + `</li>
                  
                <li class="list-group-item">
                    <a class="btn btn-success text-white btn-sm" style="padding-left: 30px;padding-right: 30px;" onclick="chatWithCoach('` + key + `')">
                        Chat
                    </a>
                    
                </li>
                
              </ul>
              
                                
            </div>`
    
}
var activeListenerKey;
var chatWithCoach = function(coachKey){
//    const epuipmentElement = document.getElementById("equipmentDetail");
//    epuipmentElement.classList.add("d-none");
//    epuipmentElement.classList.add("d-lg-block");
//    document.getElementById("chatView").classList.remove("d-none");
    document.getElementById("search").classList.add("d-none");
    document.getElementById("chatView").classList.remove("d-none");
    firebase.database().ref("/Chats").once("value").then(function(snapshot){
       if (snapshot.hasChild(userData.key + "+" + coachKey)){
           activeListenerKey = userData.key + "+" + coachKey;
           setListener();
       }else if (snapshot.hasChild(coachKey + "+" + userData.key)){
           activeListenerKey = coachKey + "+" + userData.key;
           setListener();
       }else{
           activeListenerKey = userData.key + "+" + coachKey;
           setListener();
       }
    });
    
}

var setListener = function(){
    firebase.database().ref("Chats/" + activeListenerKey).on("child_added", function(snapshot) {
        var newPost = snapshot.val();
        if (newPost.user){
            if (newPost.user == userName){
            document.getElementById('chatContent').innerHTML += constructMyChat(newPost.content, newPost.time);
            }else{
                document.getElementById('chatContent').innerHTML += constructOtherChat(newPost.content, newPost.time, ItemData[prevActive].sellerInfo);
            }
        }
    scrollBottom();
    });
    
}

var constructMyChat = function(content, time){
    var htmlStr = `<div class="row" style="padding-top: 5px;">
                           <div class="col-lg-6 col-sm-3">
                       </div>
                        
                       <div class="col-lg-6 col-sm-9">
                           <h5 align="right" style="line-height: 5px;">You</h5>                 
                           <a class="list-group-item list-group-item-success">`
                               + content +
                            `</a>
                           <small class="float-right text-muted">` + time + `</small>
                       </div>
                       </div>`
    return htmlStr
}

var sendChat = function(){
    const msg = document.getElementById('msgBox').value;
    console.log(msg);
    if (msg){
        const date = new Date();
        const time = date.getMonth() + "/" + date.getDate() + "/" +
          date.getFullYear().toString().substr(-2) + " " + date.getHours() + ":" + date.getMinutes();
//        console.log(time);
        firebase.database().ref('/Chats/' + activeListenerKey).push({
            content: msg,
            time: time,
            user : userName
        });
        document.getElementById('msgBox').value = "";
    }else{
        console.log("msg is none");
    }
    document.getElementById('msgBox').value = "";
}

var scrollBottom = function(){
    const objDiv = document.getElementById('chatContentScroll');
    objDiv.scrollTop = objDiv.scrollHeight;
}
    

var constructOtherChat = function(content, time, username){
    var htmlStr = `<h5 align="left" style=" padding-left: 5px; padding-top:15px;line-height: 8px;">`+username+`</h5>  
<div class="row" style="padding-top: -5px;">
                            
                       <div class="col-lg-6 col-sm-9">         
                           <a class="list-group-item list-group-item-action list-group-item .text-dark">
                               `+content+`
                            </a>
                           <small class="float-left text-muted">`+time+`</small>
                       </div>
                       <div class="col-lg-6 col-sm-3">
                       </div>
                       </div>`
    return htmlStr
}

var closeChat = function(){
    document.getElementById("search").classList.remove("d-none");
    document.getElementById("chatView").classList.add("d-none");
    firebase.database().ref("/Chats/" + activeListenerKey).off("child_added");
    document.getElementById('chatContent').innerHTML = "";
}

document.getElementById('msgBox').onkeydown = function(event){
//    console.log(event);
    if (event.key == "Enter"){
        sendChat();
        document.getElementById('msgBox').value = "";
    }
}