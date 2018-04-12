const navHeight = 1.5 * $('#chatNav')[0].offsetHeight + $('#chatNav').height();
//document.getElementById('searchContent').style.height = String($(window).height() - navHeight) + "px";
document.getElementById('resultContent').style.height = String($(window).height() - navHeight) + "px";
document.getElementById('chatContentEle').style.height = String($(window).height() - navHeight) + "px";
var userDB = {};

$(function () {
    console.log("setting");
    $('#searchDatePicker').datetimepicker({
        format: 'L'
    });
    $('#searchTimePicker').datetimepicker({
        format: 'LT'
    });
    
    $('#newDatePicker').datetimepicker({
        format: 'L'
    });
    $('#newTimePicker').datetimepicker({
        format: 'LT'
    });
});

var search = function(){
    document.getElementById("searchCol").classList.remove("d-none");
    document.getElementById("resultList").innerHTML = "";
    var userZip = document.getElementById("userZip").value;
    var userDistance = document.getElementById("userDistance").value;
//    var userDate = document.getElementById("userDate").value;
//    var userHour = document.getElementById("userHour").value;
//    var userMinute = document.getElementById("userMinute").value;
    var userDateEle = $('#searchDatePicker').datetimepicker("viewDate");
    var userTimeEle = $('#searchTimePicker').datetimepicker("viewDate");
    var userDate = userDateEle.year() + "/" + (userDateEle.month() + 1) + "/" + userDateEle.date();
    var userHour = userTimeEle.hour();
    var userMinute = userTimeEle.minute();
    console.log(userDate, userHour, userMinute);
    var userRange = document.getElementById("userRange").value;
    var userTime = userDate + "/" + userHour + "/" + userMinute;
    console.log(userDate);
    if (userZip && userDistance && userDate && userHour && userMinute && userRange){
        $.get("https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyCdgNpTlxZbvtB8_SK186QUqp-5Y72IIN4&address=" + userZip, function(data, status){
            console.log(data);
            var userLong = data.results[0].geometry.location.lng;
            var userLat = data.results[0].geometry.location.lat;
            var url = "https://us-central1-pingterest-ffca7.cloudfunctions.net/findEvent?userLong=";
            url += userLong + "&userLat=";
            url += userLat+ "&targetDist=";
            url += userDistance + "&targetDate=";
            url += userTime + "&targetTime="+ userRange;
            console.log(url);
            $.get(url, function(data, status){
                userDB = {};
                console.log(data);

                var events = data.Events;
                firebase.database().ref('/Events/').once('value').then(function(snapshot) {
                    for (key in events){
                        var ud = snapshot.child(events[key]).val();
                        userDB[events[key]] = ud;
                        console.log(events[key]);
                        document.getElementById("resultList").innerHTML += 
                            constructListHtml(
                            events[key],
                            ud.key,
                            ud.holder,
                            ud.description,
                            ud.time
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
//    document.getElementById("searchCol").classList.add("d-none");
    closeChat();
    console.log(coachKey)
    document.getElementById("detailCol").classList.remove("d-none");
    document.getElementById(coachKey).classList.add("active");
    prevActive = coachKey;
    document.getElementById("detailCol").innerHTML = constructDetailHtml(
        coachKey,
        userDB[coachKey].holder,
        userDB[coachKey].participant,
        userDB[coachKey].location,
        userDB[coachKey].description
    )
}

var constructListHtml = function(key, name, level, description, time){
    return `<a href="#" class="list-group-item list-group-item-action flex-column align-items-start" id="` + key + `" onclick="selectCoach(this.id)">
                        <div class="row">
                            <div class="col-sm-4 col-xs-4 text-center d-flex">
                                <img class="mx-auto my-auto" src="http://placehold.it/100">
                            </div>
                            <div class="col-sm-8 col-xs-8">
                                <div class="d-flex w-100 justify-content-between">
                                  <h5 class="mb-1 ">` + name +`</h5>
                                  <large class="">By: `+ level +`</large>
                                </div>
                                <p class="mb-1 text-dark">` + description +`</p>
                                <small>Time: `+ time +`</small>
                            </div>   
                        </div>
                      </a>`;
}

var constructDetailHtml = function(key, name, participant, location, description){
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
                <li class="list-group-item">Participants &emsp; &emsp; `+ participant +`</li>
                <li class="list-group-item ">Address &emsp; `+ location + `</li>
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
                    <a class="btn btn-success text-white btn-sm" style="padding-left: 30px;padding-right: 30px;" onclick="participate('` + key + `')">
                        Participate!
                    </a>
                </li>
                
              </ul>
              
                                
            </div>`
    
}

var participate = function(coachkey){
    var eventKey = userDB[coachkey].key;
    firebase.database().ref('Events/' + eventKey + "/participant").set(
            userData.name
        );
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


var postEvent = function(){
    var eventTitle = document.getElementById("newEventName").value;
    var eventDes = document.getElementById("newEventDes").value;
    var userDateEle = $('#newDatePicker').datetimepicker("viewDate");
    var userTimeEle = $('#newTimePicker').datetimepicker("viewDate");
    var userZip = document.getElementById("newEventZip").value;
    var userDate = userDateEle.date() + "/" + (userDateEle.month() + 1) + "/" + userDateEle.year()
    var userTime = userTimeEle.hour() + ":" +userTimeEle.minute();
    var userTime2= userTimeEle.hour() + "/" +userTimeEle.minute();
    console.log(userZip)
    if(eventTitle && eventDes && userZip){
        $.get("https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyCdgNpTlxZbvtB8_SK186QUqp-5Y72IIN4&address=" + userZip, function(data, status){
            var userLong = data.results[0].geometry.location.lng;
            var userLat = data.results[0].geometry.location.lat;
            var userAddr = data.results[0].formatted_address;
            console.log(userAddr);
            firebase.database().ref("Events/" + eventTitle).set(
                {
                    "description" : eventDes,
                    "holder" : userData.name,
                    "key" : eventTitle,
                    "latitude" : userLat,
                    "location" : userAddr,
                    "longitude" : userLong,
                    "participant" : "",
                    "time" : userDate + "  " + userTime,
                    "time2" : userDate + userTime2
                }
            );
        });
        
    }
    
    
}