const tracedUrlsDiv = document.getElementById("my-traced-urls");

function addMyTracedUrl(myTracedUrl) {
  const li = document.createElement("li");
  const tracedUrlLink = document.createElement("a");
  const originaUrlLink = document.createElement("a");
  const copyButton = document.createElement("button");
  const metrics = document.createElement("span");

  li.classList.add("li-tracedurl");
  li.classList.add("card");
  tracedUrlLink.className = "a-tracedurl";
  originaUrlLink.className = "a-tracedurl";
  metrics.className = "span-tracedurl";

  tracedUrlLink.innerHTML = myTracedUrl.shortedUrl;
  tracedUrlLink.href = myTracedUrl.shortedUrl;

  originaUrlLink.innerHTML = myTracedUrl.originalUrl;
  originaUrlLink.href = myTracedUrl.originalUrl;

  metrics.innerHTML = JSON.stringify({
    click_count: { total: 123123, last_30_mins: 33 }
  });

  li.append(copyButton);
  li.append(tracedUrlLink);
  li.append(originaUrlLink);
  li.append(metrics);

  tracedUrlsDiv.prepend(li);
  tracedUrlsDiv.style.visibility = "visible";

  copyButton.appendChild(document.createTextNode("copy link"));
  copyButton.addEventListener("click", e => copyToClipboard(myTracedUrl.shortedUrl));

}

function refreshTable(myTracedUrlList) {
  for (var i = 0; i < myTracedUrlList.length; i++) {
    addMyTracedUrl(myTracedUrlList[i]);
  }
}

function getMyTrackedUrls() {
  const xhttp = new XMLHttpRequest();
  xhttp.open("get", "/anonapi/urls", true);
  xhttp.onload = function() {
    const msg = {
      status: xhttp.status,
      responseBody: JSON.parse(xhttp.response)
    };
    console.log(msg);
    if (xhttp.status == 200) {
      refreshTable(JSON.parse(xhttp.response));
    }
  };
  xhttp.send();
};

function addNewUrlToTrackClick(event) {
  event.preventDefault();

  const originalUrlToTrack = document.getElementById("original-url-to-track");

  const xhttp = new XMLHttpRequest();
  xhttp.open("post", "/anonapi/urls", true);
  xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
  xhttp.onload = function() {
    const msg = {
      status: xhttp.status,
      responseBody: JSON.parse(xhttp.response)
    };
    console.log(msg);
    if (xhttp.status == 200) {
      addMyTrackedUrl(msg.responseBody);
      originalUrlToTrack.value = "";
    } else {
      alert(msg);
    }
  };
  xhttp.send(JSON.stringify({
    originalUrl: originalUrlToTrack.value
  }));

  return false;
};

function copyToClipboard(textToCopy) {
  var textArea;

  function isOS() {
    //can use a better detection logic here
    return navigator.userAgent.match(/ipad|iphone/i);
  }

  function createTextArea(text) {
    textArea = document.createElement('textArea');
    textArea.readOnly = true;
    textArea.contentEditable = true;
    textArea.value = text;
    document.body.appendChild(textArea);
  }

  function selectText() {
    var range, selection;

    if (isOS()) {
      range = document.createRange();
      range.selectNodeContents(textArea);
      selection = window.getSelection();
      selection.removeAllRanges();
      selection.addRange(range);
      textArea.setSelectionRange(0, 999999);
    } else {
      textArea.select();
    }
  }

  function copyTo() {
    document.execCommand('copy');
    document.body.removeChild(textArea);
  }

  createTextArea(textToCopy);
  selectText();
  copyTo();
}

document.getElementById("url-to-track-form").addEventListener("submit", addNewUrlToTrackClick);
window.onload = getMyTrackedUrls();
