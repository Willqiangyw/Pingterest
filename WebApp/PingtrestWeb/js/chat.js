var chatslists = {};
var activeChat;
//const navHeight = $('#chatNav').height();
const navHeight = 1.5 * $('#chatNav')[0].offsetHeight + $('#chatNav').height();
document.getElementById('chatContentEle').style.height = String($(window).height() - navHeight) + "px";
document.getElementById('chatListEle').style.height = String($(window).height() - navHeight) + "px";
firebase.auth().onAuthStateChanged(function(user){
    if(user){
        firebase.database().ref('/Chats/').once('value').then(
            function(snapshot) {
            var keys = Object.keys(snapshot.val());
            var lastChats = [];
            var destKeys = [];
            for (i in keys){
                users = keys[i].split("+");
                const useridx = users.indexOf(userName);
                if (useridx != -1){
                    const destidx = (useridx + 1) % 2;
                    const data = snapshot.val()[keys[i]];
//                    chatslists["chatidx" + index] = data;
                    chatslists[users[destidx]] = keys[i];
//                    console.log(data);
                    chatKeys = Object.keys(data);
//                    console.log(data[chatKeys[chatKeys.length - 1]]);
                    lastChats.push(data[chatKeys[chatKeys.length - 1]]);
                    destKeys.push(users[destidx]);                    
                
                }
            }
            firebase.database().ref('/Users/').once('value').then(function(snapshot1){
                for (j in destKeys){
                    document.getElementById('chatList').innerHTML += constructListItem(destKeys[j], snapshot1.child(destKeys[j]).child("name").val(), lastChats[j].time, lastChats[j].content);
                }
                
                
                
            });    
                
        });
    } else {
        alert("You've signed out");
        window.location.href = "login.html"
    }
});

document.getElementById('msgBox').onkeydown = function(event){
    if (event.key == "Enter"){
        sendChat();
        document.getElementById('msgBox').value = "";
    }
}

var constructListItem = function(id, heading, lastSeen, detail) {
    var htmlStr = `<a href="#" id="` + id + `" class="list-group-item list-group-item-action flex-column align-items-start" onclick="return selectChat(this)">
                        <div class="d-flex w-100 justify-content-between">
                          <h5 class="mb-1">`;
    htmlStr += heading + `</h5>
                          <small>`;
    htmlStr += lastSeen + `</small>
                        </div>
                        <p class="mb-1">`;
    htmlStr += detail + `</p>
                      </a>`;
    return htmlStr;
}

var selectChat = function(htmlElement){
    if(activeChat){
        document.getElementById(activeChat).classList.remove("active");
        firebase.database().ref("/Chats/" + chatslists[activeChat+"key"]).off("child_added");
        document.getElementById('chatContent').innerHTML = ``;
    }
    htmlElement.classList.add("active");
    activeChat = htmlElement.id;
    var destName = htmlElement.children[0].children[0].innerHTML;
    document.getElementById('chatContentCol').classList.remove("d-none");
    const chatData = chatslists[htmlElement.id]
    const activeChatKey = activeChat+"key";


    firebase.database().ref("/Chats/" + chatslists[activeChat]).on("child_added", function(snapshot) {
        var newPost = snapshot.val();
        if (newPost.user){
            if (newPost.user == userName){
            document.getElementById('chatContent').innerHTML += constructMyChat(newPost.content, newPost.time);
            }else{
                document.getElementById('chatContent').innerHTML += constructOtherChat(newPost.content, newPost.time, destName);
            }
        }
        scrollBottom();
    });
    
}

var scrollBottom = function(){
    const objDiv = document.getElementById('chatContentScroll');
    objDiv.scrollTop = objDiv.scrollHeight;
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
var sendChat = function(){
    const msg = document.getElementById('msgBox').value;
    console.log(msg);
    if (msg){
        const date = new Date();
        const time = date.getMonth() + "/" + date.getDate() + "/" +
          date.getFullYear().toString().substr(-2) + " " + date.getHours() + ":" + date.getMinutes();
//        console.log(time);
        firebase.database().ref('/Chats/' + chatslists[activeChat]).push({
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