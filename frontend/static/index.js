var inmemory = [];

async function loadShortedUrls() {
  var shortedUrlsTable = document.getElementById("table-urls-shorted");
  var shortedUrlsTableBody = document.getElementById("table-urls-shorted-body");
	var hasData = false;

	shortedUrlsTableBody.innerHTML = null;
	for (i = 0; i < inmemory.length; i++) {
		console.log(inmemory[i]);
		var row = shortedUrlsTableBody.insertRow(i);
		var cell1 = row.insertCell(0);
		var cell2 = row.insertCell(1);
		var cell3 = row.insertCell(2);
		cell1.innerHTML = inmemory[i].originalUrl;
		cell2.innerHTML = inmemory[i].shortedUrl;
		cell3.innerHTML = inmemory[i].datetime.toISOString();
		cell3.style.textAlign = 'right';

		hasData = true;
	}

  shortedUrlsTable.style.visibility = hasData ? 'visible' : 'hidden';
};

function formSubmit() {
	var originalUrlInput = document.getElementById("input-original-url");
	var originalUrl = originalUrlInput.value;
	originalUrlInput.value = "";

	inmemory.push({
		originalUrl: originalUrl,
		shortedUrl: 'http://aaa',
		datetime: new Date()
	});
	loadShortedUrls();

  return false; //do not reload page
};

document.getElementById("url-to-short-form").onsubmit = formSubmit;
window.onload = loadShortedUrls();
