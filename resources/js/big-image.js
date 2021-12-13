/////////////////// big image control  ////////////////
	
function BigImageControl(bmpGalleryTable, 
						 jpgGalleryTable, 
						 otherGalleryTable,
						 dirTable,
						 controlDiv,
						 shareUrlDiv,
						 iw,
						 ih) 
{
	
	//current small img object
	var currentSmallImg = null;
	
	var bigImageWidth = iw-100;
	var bigImageHeight = ih - bigImageWidth/10 - 100;
	var cancelImgWidth = bigImageWidth/10;
	var nextImgWidth = bigImageWidth/10;
	var prevImgWidth = bigImageWidth/10;
	
	//loading/progress image container
	var loadingImgTable = document.getElementById("loadingImgTable");
	document.body.removeChild(loadingImgTable.parentNode);
	loadingImgTable.parentNode.style.marginTop = Math.floor(ih/3) + "px";
	
	//before big image is loaded loading image is shown
	this.loadingImg = loadingImgTable.getElementsByTagName("img")[0];
	
	//container for bigImg and control(left, right, cancel buttons) elements
	var bigImgTable = document.getElementById("bigImgTable");
	document.body.removeChild(bigImgTable);
	
	//bigImg object
	var bigImg = findById("bigImg", bigImgTable.getElementsByTagName("img"));
	
	//width is defined on page loading, height must be recalculated based on 
	//current image width. Implementation contains redundant width oversize branch.
	//Keep it as it suits the general case. 
	bigImg.onload = function() {
		var w = this.width;
		var h = this.height;
		
		if(w > bigImageWidth) {
			wRation = w/bigImageWidth;
			w = bigImageWidth;
			h = h/wRatio;
			if(h > bigImageHeight) {
				hRation = h/bigImageHeight;
				h = bigImageHeight;
				w = w/hRation;
			}
		}
		if(h > bigImageHeight) {
			hRation = h/bigImageHeight;
			h = bigImageHeight;
			w = w/hRation;
			if(w > bigImageWidth) {
				wRation = w/bigImageWidth;
				w = bigImageWidth;
				h = h/wRatio;
			}
		}
		
		this.width = w;
		this.height = h;
	} 
	
	//cancel button(cancelImg) on click handler
	var closeBigImgClickHandler = function() {
		bigImgTable.parentNode.removeChild(bigImgTable);
		if(controlDiv) {document.body.appendChild(controlDiv);}
		if(shareUrlDiv) {document.body.appendChild(shareUrlDiv);}
		if(dirTable) {document.body.appendChild(dirTable);}
		document.body.appendChild(bmpGalleryTable);
		document.body.appendChild(jpgGalleryTable);
		document.body.appendChild(otherGalleryTable);
	}
	
	//loading cancel button object
	this.cancelImg = findById("cancelImg", bigImgTable.getElementsByTagName("img"));
	this.cancelImg.width = Math.floor(cancelImgWidth);
	this.cancelImg.addEventListener("click", closeBigImgClickHandler);

	//Loads big .jpg image by small image(.bmp) name		
	var loadBigImage = function(smallImgName) {
		//reset to screen width
		bigImg.width = bigImageWidth;
		//remove height to allow height auto scaling
		bigImg.removeAttribute('height');
		
		//replace bmp extension to jpg
		var bigImgName = smallImgName.substring(0,smallImgName.indexOf(".")) + ".jpg";
	
		var oReq = new XMLHttpRequest();
		oReq.open("POST", loadBigJpgUrl, true);
		oReq.responseType = "arraybuffer";
		
		oReq.onload = function(oEvent) {
		  var blob = new Blob([oReq.response], {type: "image/jpg"});
		  var objectURL = URL.createObjectURL(blob);
		  
		  bigImg.src = objectURL;
		  bigImg.name = smallImgName.substring(0,smallImgName.indexOf("."));
		   
		  document.body.removeChild(loadingImgTable.parentNode);
		  document.body.appendChild(bigImgTable);
		};
		oReq.send(currentPath + bigImgName);
	}
	
	//next button on click handler
	var loadNextBigImgClickHandler = function() {
		var nextImgs = currentSmallImg.nextImgs;
		var next = nextImgs.get(bigImg.name + "." + currentSmallImg.imgType);
		if(next != null) {
			bigImgTable.parentNode.removeChild(bigImgTable);
			document.body.appendChild(loadingImgTable.parentNode);
			loadBigImage(next);
		}
	}
	
	//loading nextImg button object
	this.nextImg = findById("nextImg", bigImgTable.getElementsByTagName("img"));
	this.nextImg.width = Math.floor(nextImgWidth);
	this.nextImg.addEventListener("click", loadNextBigImgClickHandler);
	
	//prev button on click handler
	var loadPrevBigImgClickHandler = function() {
		var prevImgs = currentSmallImg.prevImgs;
		var prev = prevImgs.get(bigImg.name + "." + currentSmallImg.imgType);
		if(prev != null) {
			bigImgTable.parentNode.removeChild(bigImgTable);
			document.body.appendChild(loadingImgTable.parentNode);
			loadBigImage(prev);
		}
	}
	
	//loading prevImg button object
	this.prevImg = findById("prevImg", bigImgTable.getElementsByTagName("img"));
	this.prevImg.width = Math.floor(prevImgWidth);
	this.prevImg.addEventListener("click", loadPrevBigImgClickHandler);
		
	//Sets currently chosen image. Prev and next navigation is carried out based
	//on this value				
	this.smallImageClickHandler = function() {
		currentSmallImg = this;
		if(controlDiv) {controlDiv.parentNode.removeChild(controlDiv);}
		if(shareUrlDiv) {shareUrlDiv.parentNode.removeChild(shareUrlDiv);}
		if(dirTable) {dirTable.parentNode.removeChild(dirTable);}
		bmpGalleryTable.parentNode.removeChild(bmpGalleryTable);
		jpgGalleryTable.parentNode.removeChild(jpgGalleryTable);
		otherGalleryTable.parentNode.removeChild(otherGalleryTable);
		document.body.appendChild(loadingImgTable.parentNode);
		loadBigImage(this.id);
	}
	
}

///////////////////////////////////////////////////////