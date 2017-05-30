var selectedButton = 'selected';
var length = 0;
var alma;
var mediaTimer;

function getLyrics() {
  var elem = document.getElementsByClassName(selectedButton)[0];
  var lyrics;
  switch (elem.id) {
    case 'instrumental':
      lyrics = [
        {delta: 0, lyric: '--'},
        {delta: 3, lyric: '[Introduction]'},
        {delta: 8, lyric: 'We have gained wisdom and honor'},
        {delta: 13, lyric: 'From our home of green and grey'},
        {delta: 17, lyric: 'We will go forth and remember'},
        {delta: 21, lyric: 'All we\'ve learned along the way'},
        {delta: 25, lyric: 'And with knowledge and compassion'},
        {delta: 29, lyric: 'We will build communities'},
        {delta: 34, lyric: 'Leading by example'},
        {delta: 39, lyric: 'And with dignity'},
        {delta: 42, lyric: 'Georgia Gwinnett, we\'ll never forget'},
        {delta: 51, lyric: 'How we have grown, and those that we\'ve met'},
        {delta: 59, lyric: 'Georgia Gwinnett, with love and respect'},
        {delta: 67, lyric: 'Our alma mater, Georgia Gwinnett'},
        {delta: 75, lyric: 'Our alma mater, Georgia Gwinnett'}
      ];
      break;
    case 'low':
      lyrics = [
        {delta: 0, lyric: '--'},
        {delta: 3, lyric: '[Introduction]'},
        {delta: 8, lyric: 'We have gained wisdom and honor'},
        {delta: 13, lyric: 'From our home of green and grey'},
        {delta: 17, lyric: 'We will go forth and remember'},
        {delta: 21, lyric: 'All we\'ve learned along the way'},
        {delta: 25, lyric: 'And with knowledge and compassion'},
        {delta: 29, lyric: 'We will build communities'},
        {delta: 34, lyric: 'Leading by example'},
        {delta: 39, lyric: 'And with dignity'},
        {delta: 42, lyric: 'Georgia Gwinnett, we\'ll never forget'},
        {delta: 51, lyric: 'How we have grown, and those that we\'ve met'},
        {delta: 59, lyric: 'Georgia Gwinnett, with love and respect'},
        {delta: 67, lyric: 'Our alma mater, Georgia Gwinnett'},
        {delta: 75, lyric: 'Our alma mater, Georgia Gwinnett'}
      ];
      break;
    case 'high':
      lyrics = [
        {delta: 0, lyric: '--'},
        {delta: 3, lyric: '[Introduction]'},
        {delta: 11, lyric: 'We have gained wisdom and honor'},
        {delta: 14, lyric: 'From our home of green and grey'},
        {delta: 19, lyric: 'We will go forth and remember'},
        {delta: 22, lyric: 'All we\'ve learned along the way'},
        {delta: 27, lyric: 'And with knowledge and compassion'},
        {delta: 31, lyric: 'We will build communities'},
        {delta: 35, lyric: 'Leading by example'},
        {delta: 40, lyric: 'And with dignity'},
        {delta: 44, lyric: 'Georgia Gwinnett, we\'ll never forget'},
        {delta: 51, lyric: 'How we have grown, and those that we\'ve met'},
        {delta: 59, lyric: 'Georgia Gwinnett, with love and respect'},
        {delta: 67, lyric: 'Our alma mater, Georgia Gwinnett'},
        {delta: 75, lyric: 'Our alma mater, Georgia Gwinnett'}
      ];
      break;
    case 'vocal':
      lyrics = [
        {delta: 0, lyric: '--'},
        {delta: 3, lyric: '[Introduction]'},
        {delta: 12, lyric: 'We have gained wisdom and honor'},
        {delta: 16, lyric: 'From our home of green and grey'},
        {delta: 20, lyric: 'We will go forth and remember'},
        {delta: 25, lyric: 'All we\'ve learned along the way'},
        {delta: 29, lyric: 'And with knowledge and compassion'},
        {delta: 34, lyric: 'We will build communities'},
        {delta: 38, lyric: 'Leading by example'},
        {delta: 42, lyric: 'And with dignity'},
        {delta: 46, lyric: 'Georgia Gwinnett, we\'ll never forget'},
        {delta: 54, lyric: 'How we have grown, and those that we\'ve met'},
        {delta: 63, lyric: 'Georgia Gwinnett, with love and respect'},
        {delta: 70, lyric: 'Our alma mater, Georgia Gwinnett'},
        {delta: 78, lyric: 'Our alma mater, Georgia Gwinnett'}
      ];
      break;
    default:
      lyrics = [
        {delta: 0, lyric: '--'},
        {delta: 3, lyric: '[Introduction]'},
        {delta: 8, lyric: 'We have gained wisdom and honor'},
        {delta: 13, lyric: 'From our home of green and grey'},
        {delta: 17, lyric: 'We will go forth and remember'},
        {delta: 22, lyric: 'All we\'ve learned along the way'},
        {delta: 25, lyric: 'And with knowledge and compassion'},
        {delta: 29, lyric: 'We will build communities'},
        {delta: 34, lyric: 'Leading by example'},
        {delta: 39, lyric: 'And with dignity'},
        {delta: 42, lyric: 'Georgia Gwinnett, we\'ll never forget'},
        {delta: 51, lyric: 'How we have grown, and those that we\'ve met'},
        {delta: 59, lyric: 'Georgia Gwinnett, with love and respect'},
        {delta: 67, lyric: 'Our alma mater, Georgia Gwinnett'},
        {delta: 75, lyric: 'Our alma mater, Georgia Gwinnett'}
      ];
  }

  return lyrics;
}

function getMediaURL(s) {
    return "/android_asset/www/" + s;
}

function updateSelected(id) {
  var buttons = document.getElementsByClassName(selectedButton);

  for (var i = 0; i < buttons.length; i++) {
    buttons[i].classList.toggle(selectedButton, false);
  }

  var newButton = document.getElementById(id);
  newButton.classList.toggle(selectedButton, true);
}

function updatePos(delta, state) {
  var pos = document.getElementById('pos');
  pos.innerHTML = '<span>Position:</span> ' + delta + '/' + length + ' (' + state + ')';

  var tick = document.getElementsByClassName('tick')[0];
  tick.style.width = (delta / length) * 100 + '%';

  var lyrics = getLyrics();
  var curLyric = 0;
  for (var i = 0; i < lyrics.length; i++) {
    if (lyrics[i].delta <= delta) {
      curLyric = i;
    }
  }

  var previous = document.getElementById('previous');
  var current = document.getElementById('current');
  var next = document.getElementById('next');

  current.innerHTML = lyrics[curLyric].lyric;
  if (curLyric > 0) {
    previous.innerHTML = lyrics[curLyric-1].lyric;
    if (curLyric >= lyrics.length - 1) {
      console.log('==');
      next.innerHTML = '--';
    }
    else {
      next.innerHTML = lyrics[curLyric + 1].lyric;
    }
  }
  else {
    previous.innerHTML = '--';
    next.innerHTML = '--';
  }
}

function instrumental() {
  updateSelected('instrumental');
  var music = getMediaURL('lib/music/Alma\ Mater\ PIano.mp3');
  alma.release();
  alma = new Media(
    music,
    function () { console.log('media success'); },
    function (err) { console.log('media failure: ' + err.code); }
  );
  length = 93;
  updatePos(0, '-');
}

function lowVocal() {
  updateSelected('low');
  var music = getMediaURL('lib/music/Alma\ Mater\ Complete\ Low.mp3');
  alma.release();
  alma = new Media(
    music,
    function () { console.log('media success'); },
    function (err) { console.log('media failure: ' + err.code); }
  );
  length = 89;

  updatePos(0, '-');
}

function highVocal() {
  updateSelected('high');
  var music = getMediaURL('lib/music/Alma\ Mater\ Complete\ High.mp3');
  alma.release();
  alma = new Media(
    music,
    function () { console.log('media success'); },
    function (err) { console.log('media failure: ' + err.code); }
  );
  length = 89;

  updatePos(0, '-');
}

function vocal() {
  updateSelected('vocal');
  var music = getMediaURL('lib/music/Alma\ Mater\ Melody.mp3');
  alma.release();
  alma = new Media(
    music,
    function () { console.log('media success'); },
    function (err) { console.log('media failure: ' + err.code); }
  );
  length = 91;

  updatePos(0, '-');
}

function trackPlay() {
  alma.play();
  mediaTimer = setInterval( function () {
    alma.getCurrentPosition(
      function (pos) {
        if (pos > -1) {
          updatePos(Math.floor(pos), 'playing');
        }
      },
      function (e) {
        console.log('Error getting pos=' + e);
      }
    );
  }, 1000);
}

function trackPause() {
  alma.pause();
  clearInterval(mediaTimer);
  alma.getCurrentPosition(
    function (pos) {
      if (pos > -1) {
        updatePos(Math.floor(pos), 'paused');
      }
    },
    function (e) {
      console.log('Error getting pos=' + e);
    }
  );
}

function trackStop() {
  alma.stop();
  alma.release();
  clearInterval(mediaTimer);
  updatePos(0, 'stopped');
}

function setupEventListeners() {
  document.getElementById('instrumental').addEventListener('click', instrumental);
  document.getElementById('low').addEventListener('click', lowVocal);
  document.getElementById('high').addEventListener('click', highVocal);
  document.getElementById('vocal').addEventListener('click', vocal);

  document.getElementById('play').addEventListener('click', trackPlay);
  document.getElementById('pause').addEventListener('click', trackPause);
  document.getElementById('stop').addEventListener('click', trackStop);
  alma = new Media(
    getMediaURL('lib/music/Alma\ Mater\ PIano.mp3'),
    function () { console.log('media success'); },
    function (err) { console.log('media failure: ' + err.code); }
  );
  instrumental();
}

var app = {
    // Application Constructor
    initialize: function() {
      document.addEventListener('deviceready', this.onDeviceReady.bind(this), false);
    },

    // deviceready Event Handler
    //
    // Bind any cordova events here. Common events are:
    // 'pause', 'resume', etc.
    onDeviceReady: function() {
      setupEventListeners();
    },

    // Update DOM on a Received Event
    receivedEvent: function(id) {
    }
};

app.initialize();
