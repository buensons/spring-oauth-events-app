'use strict';

let eventsToday = 0;
let eventsThisWeek = 0;


document.getElementById("addEvent").addEventListener('click', addEvent);

fetch("https://events-dot-outstanding-box-250210.appspot.com/api/me")
    .then(resp => {
        return resp.json();
    })
    .then(user => {
        loadProfile(user);
    });

fetch("https://events-dot-outstanding-box-250210.appspot.com/api/events")
    .then(resp => {
        return resp.json();
    })
    .then(resp => {
        resp.forEach(addTile);
        document.getElementById("stat-col").innerHTML =
            '<p>Events today: ' + eventsToday
            + '</p><p>Events this week: ' + eventsThisWeek + '</p>';
    })
    .catch(error => {
        console.log(error);
    });


function loadProfile(currentUser) {
    document.getElementById("pic-col").innerHTML = '<img src="' + currentUser.photo
        + '" alt="pic" id="avatar" class="mr-3 mt-3 align-self-start rounded-circle" style="width:100px;">\
        <p><a href="https://events-dot-outstanding-box-250210.appspot.com/logout">[Logout]</a></p>';

    document.getElementById("name-col").innerHTML = '<div id="name"><h2>'
        + currentUser.username + '</h2></div><small><p>('+ currentUser.email +')</p></small>';

    document.getElementById("btn-col").innerHTML =
        '<button type="button" id="btn" class="btn btn-primary" data-toggle="modal" data-target="#myModal">\
        Add Event <b>+</b></button>';
}

function addTile(event) {
    let dateTimeStart = event.startDate;
    let dateTimeFinish = event.finishDate;

    if(dateTimeStart != null) {
        dateTimeStart = dateTimeStart.split("T");
        dateTimeStart = dateTimeStart[0] + "  " + dateTimeStart[1];
    }

    if(dateTimeFinish != null) {
        dateTimeFinish = dateTimeFinish.split("T");
        dateTimeFinish = dateTimeFinish[0] + "  " + dateTimeFinish[1];
    }

    let tileTemplate = '<div class="card col-lg-5"><div class="card-header text-center">'
        + event.title + '</div><div class="card-body">'
        + '<table class="table table-borderless"><tbody>'
        + '<tr><td><span class="badge badge-primary">Description: </span></td><td>'
        + event.description + '</td></tr>'
        + '<tr><td><span class="badge badge-primary">Start Date: </span></td><td>'
        + dateTimeStart + '</td></tr>'
        + '<tr><td><span class="badge badge-primary">End Date: </span></td><td>'
        + dateTimeFinish + '</td></tr>'
        + '<tr><td><span class="badge badge-primary">Location: </span></td><td>'
        + event.location + '</td></tr></tbody></table>'
        + '<br><button type="button" onclick="deleteEvent(' + event.id + ')" class="btn btn-danger btn-block" id="'
        + event.id + '">Delete</button></div></div>';

    document.getElementById("events").innerHTML += tileTemplate;


}

function deleteEvent(id) {
    fetch("https://events-dot-outstanding-box-250210.appspot.com/api/events/" + id, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(resp => {
        if(resp.status === 200) {
            refresh();
        }
    })
}

function refresh() {
    fetch("https://events-dot-outstanding-box-250210.appspot.com/api/events")
        .then(resp => {
            return resp.json();
        })
        .then(resp => {
            document.getElementById("events").innerHTML = "";
            resp.forEach(addTile);
        });
}

function addEvent() {
    let eventTitle = document.getElementById("title");
    let eventDescription = document.getElementById("description");
    let eventLocation = document.getElementById("location");
    let eventFinishDate = document.getElementById("start");
    let eventStartDate = document.getElementById("end");

    let data = { title : eventTitle.value,
        description : eventDescription.value,
        startDate: eventFinishDate.value,
        finishDate: eventStartDate.value,
        location : eventLocation.value };

    eventTitle.value = "";
    eventDescription.value = "";
    eventLocation.value = "";
    eventStartDate.value = "";
    eventFinishDate.value = "";

    fetch("https://events-dot-outstanding-box-250210.appspot.com/api/events", {
        method: 'POST',
        headers:{
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then(resp => {
        if (resp.status === 200) {
            return resp.json();
        } else {
            throw new Error("bad data");
        }
    }).then(json => {
        addTile(json);
    }).catch(error => console.error('Error:', error));
}

