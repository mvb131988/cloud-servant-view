/////////////////// Share functions  //////////////////
function shareLinkContext(shareButton, shareUrlDiv, host) {
	shareButton.addEventListener("click", function(){
	
		shareUrlDiv.innerHTML  = "";
	
		var oReq = new XMLHttpRequest();
		oReq.open("POST", "/sharing/create", true);
		oReq.onload = function(oEvent) {
			var id = oReq.responseText;
			var href = 'http://' + host + '/sharing/imgs?path-id=' + id;
			shareUrlDiv.innerHTML = 'Sharing url: <a href="' + href + '">' + href + '</a>';
		};
		oReq.send(currentPath);
	
	});
}	

function initShareUrl() {
	var oReq = new XMLHttpRequest();
	oReq.open("POST", "/sharing/get", true);
	oReq.onload = function(oEvent) {
		var id = oReq.responseText;
		if(id) shareUrlDiv.innerHTML = 'Sharing url: ' + 'sharing/imgs?path-id=' + id;
	};
	oReq.send(currentPath);
}	
///////////////////////////////////////////////////////