/////////////////// Directories list ///////////////////////////
// Directory control (initialization + events handling + disposal) 
////////////////////////////////////////////////////////////////

//exposes list of existed directories for the current directory.
function initDirectories(dirs, bigImageControl) {
	var i = 0;
	var rowIndex = 0;
	while(i < dirs.length) {
		row = dirTable.insertRow(rowIndex);
		
		for(var cellIndex = 0; cellIndex<nHc; cellIndex++) {
			if(i == dirs.length) break;
			
			var dirName;
			//Special case for the first element.
			//First element is folder back to the previous directory
			if(cellIndex == 0 && rowIndex == 0) {
				if(currentPath.localeCompare("/") != 0) {
					//"back" value
					dirName = dirs[i]; 
				} else {
					i++;
					dirName = dirs[i];
				}
			} else {
				dirName = dirs[i];
			}				
			i++;
			
			//<div style="position: relative;">
			//	<div style="text-align: center;">
			//		<img src="blob:http://92.115.183.17:53131/6d0a2264-0150-41a7-b865-9d66346a6fcb">
			//	</div>
			//	<span>nastia_fotoset_03</span>
			//</div>
			
			var div0 = document.createElement("div");
			div0.style = "position: relative;"
			var div1 = document.createElement("div");
			div1.style = "text-align: center;";
			var span = document.createElement("span");
			span.textContent = dirName;
			
			div0.appendChild(div1);
			div0.appendChild(span);
			
			var localDirImg = new Image();
			localDirImg.src = dirImg.src;
			div1.append(localDirImg);
			changeDirHandler(div1, dirName, bigImageControl);
			
			var cell = row.insertCell(cellIndex);
	  		cell.appendChild(div0);
		}
		rowIndex++;
	}
}

//Allows directory navigation(in depth traversal). When directory changes, change current path
//and dispose bmp-gallery and dirs objects
function changeDirHandler(element, dirName, bigImageControl) {
	element.addEventListener('click', function() {
		if(dirName.localeCompare("back") == 0) {
			//special case of back path (move to parent directory)
			//calculate path
			currentPath = currentPath.substring(0, currentPath.lastIndexOf("/"));
			currentPath = currentPath.substring(0, currentPath.lastIndexOf("/")+1);
		} else {
			currentPath += dirName + "/";
		}
		
		disposeDirs();
		disposeGalleryTable(bmpGalleryTable);
		disposeGalleryTable(jpgGalleryTable);
		disposeGalleryTable(otherGalleryTable);
		
		//remove sharing url
		shareUrlDiv.innerHTML = "";
		
		initGalleries(bigImageControl);
	});
}

//Dispose dir table
function disposeDirs() {
	for (var i = dirTable.rows.length-1; i > -1; i--) {
		dirTable.deleteRow(i);
	}
}

//Dispose gallery tables (bmp, jpg galleries)
function disposeGalleryTable(galleryTable) {
	for (var i = galleryTable.rows.length-1; i > -1; i--) {
		galleryTable.deleteRow(i);
	}
}
///////////////////////////////////////////////////////