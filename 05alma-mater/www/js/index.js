var selectedButton = 'selected';

function log(msg) {
  console.log(msg);
}

function updateSelected(id) {
  var buttons = document.getElementsByClassName(selectedButton);

  for (var i = 0; i < buttons.length; i++) {
    buttons[i].classList.toggle(selectedButton, false);
  }

  var newButton = document.getElementById(id);
  newButton.classList.toggle(selectedButton, true);
}

function instrumental() {
  updateSelected('instrumental');
}

function lowVocal() {
  updateSelected('low');
}

function highVocal() {
  updateSelected('high');
}

function vocal() {
  updateSelected('vocal');
}

function trackPlay() {
  log('play');
}

function trackPause() {
  log('pause');
}

function trackStop() {
  log('stop');
}

function setupEventListeners() {
  document.getElementById('instrumental').addEventListener('click', instrumental);
  document.getElementById('low').addEventListener('click', lowVocal);
  document.getElementById('high').addEventListener('click', highVocal);
  document.getElementById('vocal').addEventListener('click', vocal);

  document.getElementById('play').addEventListener('click', trackPlay);
  document.getElementById('pause').addEventListener('click', trackPause);
  document.getElementById('stop').addEventListener('click', trackStop);
  instrumental();
}

var app = {
    // Application Constructor
    initialize: function() {
      setupEventListeners();
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
