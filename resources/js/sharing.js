/////////////////// Share functions  //////////////////
function shareLinkContext(shareButton, shareUrlDiv) {
	shareButton.addEventListener("click", function(){
	
		var oReq = new XMLHttpRequest();
		oReq.open("POST", "/sharing/create", true);
		oReq.onload = function(oEvent) {
			var id = oReq.responseText;
			shareUrlDiv.innerHTML = 'Sharing url: ' + 'sharing/imgs?path-id=' + id;
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