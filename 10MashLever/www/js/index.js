// Placeholder test data
var firstName = 'Jim';
var lastName = 'Bob';
var email = 'test@example.com';
var demographic = 'Conservative';

function register() {
  firstName = document.getElementById('firstname').value;
  lastName = document.getElementById('lastname').value;
  email = document.getElementById('email').value;
  demGroup = document.getElementsByName('demographic');

  for (var i = 0; i < demGroup.length; i++) {
    if (demGroup[i].checked) {
      demographic = demGroup[i].value;
    }
  }
}

function vote(status) {
  var date = new Date();
  alert(firstName + ' ' + lastName + ' ' + email + ' | entered ' + status + ' at ' + date);
}

var app = {
    // Application Constructor
    initialize: function() {

    },

    // deviceready Event Handler
    //
    // Bind any cordova events here. Common events are:
    // 'pause', 'resume', etc.
    onDeviceReady: function() {
    },

    // Update DOM on a Received Event
    receivedEvent: function(id) {
    }
};

app.initialize();
