const navHeight = 1.5 * $('#chatNav')[0].offsetHeight + $('#chatNav').height();
document.getElementById('itemListCard').style.height = String($(window).height() - navHeight) + "px";
document.getElementById('chatContentEle').style.height = String($(window).height() - navHeight) + "px";
//document.getElementById('itemDetailCard').style.height = String($(window).height() - navHeight) + "px";

$(document).ready(function(){
  $("#itemFilter").on("keyup", function() {
    var value = $(this).val().toLowerCase();
    $("#equipmentList a").filter(function() {
      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
    });
  });
  $("#oldPrice").on("keyup", function() {
      var value = $(this).val();
      if (value){
          if ($.isNumeric(value)){
              $(this).val('$' + value);
          }else if (value.substring(0,1) == "$" && $.isNumeric(value.substring(1, value.length))){
              if (value.length == 1){
                  $(this).val("");
              }
          }else{
              $(this).val(value.substring(0, value.length-1));
          }   
      }
  });
  $("#newPrice").on("keyup", function() {
      var value = $(this).val();
      if (value){
          if ($.isNumeric(value)){
              $(this).val('$' + value);
          }else if (value.substring(0,1) == "$" && $.isNumeric(value.substring(1, value.length))){
              
          }else{
              $(this).val(value.substring(0, value.length-1));
          }   
      }
  });
});

var ItemData;
var prevActive;
var activeListenerKey;
firebase.auth().onAuthStateChanged(function(user){
    if(user){
        cuUser = user;
        firebase.database().ref('/Equipments/').once('value').then(
            function(snapshot) {
                var listHtml = "";
                ItemData = snapshot.val();
                for (key in ItemData){
                    listHtml += constructItemList(
                        key,
                        "//placehold.it/100",
                        ItemData[key].itemName,
                        ItemData[key].newPrice,
                        ItemData[key].itemDescription
                    );
                }
                document.getElementById("equipmentList").innerHTML = listHtml;
        });
    } else {
        alert("You've signed out");
        window.location.href = "login.html"
    }
});


var constructItemList = function(id, imagesrc, itemName, price, itemDescription){
    return `<a id="` + id + `" href="#" class="list-group-item list-group-item-action flex-column align-items-start" onclick="selectItem(this.id)">
                        <div class="row">
                            <div class="col-sm-4 col-xs-4 text-center d-flex">
                                <img class="img-thumbnail mx-auto my-auto" src="`+ imagesrc + `">
                            </div>
                            <div class="col-sm-8 col-xs-8">
                                <div class="d-flex w-100 justify-content-between">
                                  <h5 class="mb-1 ">` + itemName + `</h5>
                                  <h5 class="text-danger">$` + price + `</h5>
                                </div>
                                <p class="mb-1 text-dark">` + itemDescription + `</p>
                            </div>   
                        </div>
                      </a>`;
}

var selectItem = function(listID){
    if (prevActive){
        document.getElementById(prevActive).classList.remove("active");
        closeChat();
    }
    document.getElementById(listID).classList.add("active");
    document.getElementById("equipmentDetail").classList.remove("d-none");
    const detailView = document.getElementById("itemDetailCard");
    detailView.classList.remove("d-none");
    detailView.innerHTML = constructDetailHTML(
        ItemData[listID].sellTime,
        ItemData[listID].sellerInfo,
        ItemData[listID].sellAddr,
        ItemData[listID].itemName,
        ItemData[listID].newPrice,
        ItemData[listID].usedPrice,
        ItemData[listID].itemDescription,
        ItemData[listID].sellerKey
    )
    prevActive = listID;
    
}

var constructDetailHTML = function(time, name, location, title, newPrice,
                                   oldPrice, detail, sellerKey){
    return `<div class="bg-primary text-white" style="padding: 15px;">
                    <div class="row">
                        <div class="col-xl-1 col-lg-2 col-md-2 col-sm-2 col-xs-2 text-left d-flex" >
                            <img class="mx-auto my-auto img-round" src="http://placehold.it/50">
                        </div>
                        <div class="col-xl-10 col-lg-10 col-md-10 col-sm-10 col-xs-10 text-left">
                            <div class="w-100 justify-content-right">
                              <h5 class="mb-1" style="padding-left:15px;">`+ name +`</h5>
                            </div>
                            <small style="padding-left:15px;">`+ time +`</small>
                            &nbsp;
                            <small >`+ location +`</small>
                        </div>   
                    </div>
                </div>
                <div class="card-body" style="padding-left: 25px;">
                  

                   <div class="d-flex w-100 justify-content-between">
                      <h5 class="mb-1 ">` + title +`</h5>
                       <div class="row">
                        <h4 class="text-danger">$`+ newPrice +`</h4>
                           &nbsp;
                        <small><strike>$`+ oldPrice +`</strike></small>
                           &emsp;
                       </div>
                    </div>
                <p class="mb-1 text-dark">`+ detail +`</p>
                <div class="col-sm-12 col-xs-12 text-center border d-flex">
                    <img class="img-thumbnail mx-auto my-auto" src="http://placehold.it/300">
                </div>
              </div>
            <ul class="list-group list-group-flush">
                <li class="list-group-item">
                    <a class="btn btn-success text-white btn-sm" style="padding-left: 30px;padding-right: 30px;" onclick="chatWithSeller('`+sellerKey+`')">
                        Chat
                    </a>
                </li>
                
              </ul>

         `
    
//    `            <a class="btn  btn-success text-white" style="padding: 7px;" onclick="chatWithSeller('`+sellerKey+`')">
//                    Chat
//                </a>`
}

var sellEquipment = function() {
    const title = $('#itemTitle').val();
    const description = $('#itemDescription').val();
    const originalPrice = $('#oldPrice').val();
    const newPrice = $('#newPrice').val();
    const date = new Date();
    var time = date.getMonth() + "/" + date.getDate() + "/" + date.getYear().toString().substring(1,3);
    console.log(time);
    if (title && description && originalPrice && newPrice){
        firebase.database().ref('Equipments/' + title).set(
            {
              "itemDescription" : description,
              "itemName" : title,
              "key" : title,
              "newPrice" : newPrice.substring(1, newPrice.length),
              "sellAddr" : userData.city + " " + userData.state,
              "sellTime" : time,
              "sellerInfo" : userData.name,
              "sellerKey" : userData.key,
              "usedPrice" : originalPrice.substring(1, originalPrice.length)
            }
        );
    }
}

var chatWithSeller = function(sellerKey){
    const epuipmentElement = document.getElementById("equipmentDetail");
    epuipmentElement.classList.add("d-none");
    epuipmentElement.classList.add("d-lg-block");
    document.getElementById("chatView").classList.remove("d-none");
    console.log(sellerKey);
    firebase.database().ref("/Chats").once("value").then(function(snapshot){
       if (snapshot.hasChild(userData.key + "+" + sellerKey)){
           console.log(1);
           activeListenerKey = userData.key + "+" + sellerKey;
           setListener();
       }else if (snapshot.hasChild(sellerKey + "+" + userData.key)){
           console.log(2);
           activeListenerKey = sellerKey + "+" + userData.key;
           setListener();
       }else{
           console.log(3);
           activeListenerKey = userData.key + "+" + sellerKey;
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
    document.getElementById("equipmentDetail").classList.remove("d-none");
    document.getElementById("chatView").classList.add("d-none");
    firebase.database().ref("/Chats/" + activeListenerKey).off("child_added");
    document.getElementById('chatContent').innerHTML = "";
}

document.getElementById('msgBox').onkeydown = function(event){
//    console.log(event);
    if (event.key == "Enter"){
        console.log("pressed Enter");
        sendChat();
        document.getElementById('msgBox').value = "";
    }
}