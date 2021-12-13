////////////////////////////////////////////////////////////////
///// bmp-gallery, jpg-galleries + dirs navigation /////////////
////////////////////////////////////////////////////////////////

function initBmpGallery(bigImageControl, bmpImgs) {
	//Both maps contain .bmp image names and they are used in case when
	//.jpg has paired .bmp image
    var bmpNextImgs = new Map();
	var bmpPrevImgs = new Map();
    initImgMaps(bmpImgs, bmpNextImgs, bmpPrevImgs); 
    
    loadBmpImg(0,0,0, 
    		   bmpImgs, 
    		   bmpNextImgs, 
    		   bmpPrevImgs, 
    		   bigImageControl, 
    		   bmpGalleryTable);
}
	
function initJpgGallery(bigImageControl, jpgImgs) {
	//Both maps contain .jpg image names and they are used in case when
	//.jpg is not paired to .bmp image
    var jpgNextImgs = new Map();
	var jpgPrevImgs = new Map();
    initImgMaps(jpgImgs, jpgNextImgs, jpgPrevImgs); 
    
	loadJpgImg(0,0,0, 
			   jpgImgs, 
			   jpgNextImgs, 
			   jpgPrevImgs, 
			   bigImageControl, 
			   jpgGalleryTable,
			   jpegIconImgDescriptor.obj.src);
}

function initOtherGallery(others) {
	loadOther(0,0,0,
    		  others,
    		  otherGalleryTable,
    		  otherIconImgDescriptor.obj.src);
}

//Loads single img, creates img element and inserts it
//and then recursively invokes itself to load next image from
//the bmp-imgs list.
function loadBmpImg(index, 
					rowIndex, 
					cellIndex, 
					bmpImgs, 
					bmpNextImgs, 
					bmpPrevImgs, 
					bigImageControl,
					galleryTableContainer) 
{
	if(index == bmpImgs.length) {return;}
	var imgId = bmpImgs[index];

	var oReq = new XMLHttpRequest();
	oReq.open("POST", loadBmpImgUrl, true);
	oReq.responseType = "arraybuffer";
	oReq.onload = function(oEvent) {
	  	var blob = new Blob([oReq.response], {type: "image/bmp"});
		var objectURL = URL.createObjectURL(blob);
		  
	  	// select or create row		  
	  	var row = null;
  		if(cellIndex == 0) {
	  		//new row is started
	  		row = galleryTableContainer.insertRow(rowIndex);
	  	} 
	  	else {row = galleryTableContainer.rows[rowIndex];}
		  
	  	// create image object
	  	var img = new Image();
	  	img.id = bmpImgs[index];
	  	img.row = row;
	  	img.cellIndex = cellIndex;
	  	img.onload  = cellIndex == nHc-1 || index == bmpImgs.length-1 ? resizeRow : null;
	  	img.src = objectURL;
	  	img.addEventListener("click", bigImageControl.smallImageClickHandler);
	  	img.nextImgs = bmpNextImgs;
	  	img.prevImgs = bmpPrevImgs;
	  	img.imgType = 'bmp';
		  
	  	//create cell and pass control further down the recursion chain			  
	  	var cell = row.insertCell(cellIndex);
	  	cell.appendChild(img);
	  	if(cellIndex < nHc-1) {
	  		loadBmpImg(++index, 
		  			   rowIndex, 
		  			   ++cellIndex, 
		  			   bmpImgs, 
		  			   bmpNextImgs, 
		  			   bmpPrevImgs, 
		  			   bigImageControl,
		  			   galleryTableContainer);
	  	}
	  	else {
		  	loadBmpImg(++index, 
		  			   ++rowIndex, 
		  			   0, 
		  			   bmpImgs, 
		  			   bmpNextImgs, 
		  			   bmpPrevImgs, 
		  			   bigImageControl,
		  			   galleryTableContainer);
	  	}
	};
	
	oReq.send(currentPath + imgId);
}

//Uses jpgIconSrc, as default jpg small image for each file in the list
//and then recursively invokes itself to display next jpg image from
//the others jpg-imgs list.
function loadJpgImg(index, 
					rowIndex, 
					cellIndex, 
					jpgImgs, 
					jpgNextImgs, 
					jpgPrevImgs, 
					bigImageControl,
					galleryTableContainer,
					jpgIconSrc) 
{
	if(index == jpgImgs.length) {return;}
	
	// select or create row		  
	var row = null;
	if(cellIndex == 0) {
		//new row is started
	  	row = galleryTableContainer.insertRow(rowIndex);
	} 
	else {row = galleryTableContainer.rows[rowIndex];}
	  
	// create image object
	var img = new Image();
	img.id = jpgImgs[index];
	img.row = row;
	img.cellIndex = cellIndex;
	img.onload  = cellIndex == nHc-1 || index == jpgImgs.length-1 ? resizeRow : null;
	img.src = jpgIconSrc;
	img.addEventListener("click", bigImageControl.smallImageClickHandler);
	img.nextImgs = jpgNextImgs;
	img.prevImgs = jpgPrevImgs;
	img.imgType = 'jpg';
	
	var imgName = jpgImgs[index];
	var imgNameLabel = document.createTextNode(imgName);
	var imgNameSpan = document.createElement("span");
	imgNameSpan.align = 'center';
	imgNameSpan.style = 'width: 200px; display:block; word-break: break-all;'
	imgNameSpan.append(imgNameLabel);  
	  
	//create cell and pass control further down the recursion chain			  
	var cell = row.insertCell(cellIndex);
	cell.align = 'center';
	cell.style = 'vertical-align: top;'
	cell.appendChild(img);
	cell.appendChild(imgNameSpan);
	
	if(cellIndex < nHc-1) {
		loadJpgImg(++index, 
	  			   rowIndex, 
	  			   ++cellIndex, 
	  			   jpgImgs, 
	  			   jpgNextImgs, 
	  			   jpgPrevImgs, 
	  			   bigImageControl,
	  			   galleryTableContainer,
	  			   jpgIconSrc);
	}
	else {
		loadJpgImg(++index, 
	  			   ++rowIndex, 
	  			   0, 
	  			   jpgImgs, 
	  			   jpgNextImgs, 
	  			   jpgPrevImgs, 
	  			   bigImageControl,
	  			   galleryTableContainer,
	  			   jpgIconSrc);
	}
}

//Uses otherIconSrc, as default non-image icon for each file in the list
//and then recursively invokes itself to display next file from
//the others list.
function loadOther(index, 
				   rowIndex, 
				   cellIndex, 
				   others, 
				   galleryTableContainer,
				   otherIconSrc)
{
	if(index == others.length) {return;}
	
	// select or create row		  
	var row = null;
	if(cellIndex == 0) {
		//new row is started
	  	row = galleryTableContainer.insertRow(rowIndex);
	} 
	else {row = galleryTableContainer.rows[rowIndex];}
	  
	// create image object
	var img = new Image();
	img.id = others[index];
	img.row = row;
	img.cellIndex = cellIndex;
	img.onload  = cellIndex == nHc-1 || index == others.length-1 ? resizeRow : null;
	img.src = otherIconSrc;
	img.path = currentPath + others[index];
	img.onclick = function() {
		downloadLink.href = 'javascript:void(0)';
	
		var oReq = new XMLHttpRequest();
		oReq.open("POST", otherUrl, true);
		oReq.responseType = "arraybuffer";
		
		oReq.onload = function(oEvent) {
		  	var blob = new Blob([oReq.response], 
		  						{type: oReq.getResponseHeader('Content-Type')});
		  	var objectURL = URL.createObjectURL(blob);
		  	downloadLink.href = objectURL;
		  	downloadLink.click();
		};
		oReq.onprogress = function(oEvent) {
			//TODO: implement loader
		};
		oReq.send(this.path);
	}
	
	var imgName = others[index];
	var imgNameLabel = document.createTextNode(imgName);
	var imgNameSpan = document.createElement("span");
	imgNameSpan.align = 'center';
	imgNameSpan.style = 'width: 200px; display:block; word-break: break-all;'
	imgNameSpan.append(imgNameLabel);  
	  
	var downloadLink = document.createElement('a');
	downloadLink.setAttribute("download", others[index]);
	downloadLink.appendChild(img);
	  
	//create cell and pass control further down the recursion chain			  
	var cell = row.insertCell(cellIndex);
	cell.align = 'center';
	cell.style = 'vertical-align: top;'
	cell.appendChild(downloadLink);
	cell.appendChild(imgNameSpan);
	
	if(cellIndex < nHc-1) {
		loadOther(++index, 
	  			   rowIndex, 
	  			   ++cellIndex, 
	  			   others,
	  			   galleryTableContainer,
	  			   otherIconSrc);
	}
	else {
		loadOther(++index, 
	  			   ++rowIndex, 
	  			   0, 
	  			   others, 
	  			   galleryTableContainer,
	  			   otherIconSrc);
	}
}

//when a single row is completely filled with the images,
//find the minimum height amongst them and change their height
//to this minimum value
function resizeRow() {
  var i;
  var min = 2147483647;
  for(i=0; i<this.row.cells.length; i++) {
  	var h = this.row.cells[i].childNodes[0].height;
  	min = min >= h ? h : min;
  }
  for(i=0; i<this.row.cells.length; i++) {
    this.row.cells[i].childNodes[0].height = min;		  
  }
}

//init maps of <img name> -> <next img name>, 
//			   <img name> -> <prev img name>
function initImgMaps(imgs, nextImgs, prevImgs) {
	for(var i=0; i<imgs.length; i++) {
		var next = i < imgs.length - 1 ? imgs[i+1] : null;
		nextImgs.set(imgs[i], next);
		var prev = i > 0 ? imgs[i-1] : null;
		prevImgs.set(imgs[i], prev);
	}
}

///////////////////////////////////////////////////////

/////////////////// Helpers  //////////////////////////
function findById(id, elements) {
	var i;
	for(i=0; i<elements.length; i++) {
		if(elements[i].id == id) {return elements[i];}
	}
	return null;
}
///////////////////////////////////////////////////////