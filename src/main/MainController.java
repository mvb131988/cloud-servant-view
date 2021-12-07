package main;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.servlet.ServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

	private static Logger logger = LogManager.getRootLogger();

	private String minPathRoot = ResourceBundle.getBundle("app").getString("min-path");
	
	private String bigPathRoot = ResourceBundle.getBundle("app").getString("big-path");
	
	private String imgResourcesPathRoot = ResourceBundle.getBundle("app").getString("img-resources-path");
	
	@Autowired
	private FileManager fm;
	
	/**
	 * For the current directory return list of structures of the following representation:
	 * + file name
	 * + file type (bmp, directory, other(for video file create dedicated icon, relates to
	 * missing functionality of the jpeg-decoder))
	 * 
	 * Add method parameter relative path. If empty then it's root, if not empty build
	 * the full path relative to the root. 
	 * 
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/list")
	public @ResponseBody ListResponse list(@RequestBody String path) throws IOException {	  
	  Path minPath = Paths.get(minPathRoot.toString(), path);
	  Path bigPath = Paths.get(bigPathRoot.toString(), path);
		
	  List<FileDescriptor> l0 = fm.allPaths(minPath, bigPath);
		
		List<String> bmps = l0.stream()
		                      .filter(e -> FileType.BMP.equals(e.getType()))
		                      .map(e -> e.getName())
		                      .collect(Collectors.toList());
		
		Collections.sort(bmps);
		
		List<String> jpgs = l0.stream()
                          .filter(e -> FileType.JPG.equals(e.getType()))
                          .map(e -> e.getName())
                          .collect(Collectors.toList());

		Collections.sort(jpgs);
		
		List<String> others = l0.stream()
                            .filter(e -> FileType.OTHER.equals(e.getType()))
                            .map(e -> e.getName())
                            .collect(Collectors.toList());
		
		Collections.sort(others);
		
    List<String> dirs = l0.stream()
                          .filter(e -> FileType.DIR.equals(e.getType()))
                          .map(e -> e.getName())
                          .collect(Collectors.toList());
    
    Collections.sort(dirs);
    //back to parent dir
    dirs.add(0, "back");
    
		return new ListResponse(bmps, jpgs, others, dirs);
	}
	
	@GetMapping("/imgs")
	public String greeting(Model model, ServletRequest request) throws IOException {
		return "gallery";
	}
	
	@PostMapping("/img")
	public @ResponseBody byte[] getImage(@RequestBody String imgPath) throws IOException {
		logger.info(imgPath);
		
		Path p = Paths.get(minPathRoot.toString(), imgPath);
		
		byte[] inarr;
		try(InputStream is = new BufferedInputStream(Files.newInputStream(p, StandardOpenOption.READ), 262_144)) {
		  inarr = new byte[is.available()];
		  is.read(inarr);
		}
		return inarr;
	}
	
	@PostMapping("/big-img")
	public @ResponseBody byte[] getBigImage(@RequestBody String imgPath) throws IOException {
		logger.info(imgPath);
		
		//cut trailing .jpg
		imgPath = imgPath.substring(0, imgPath.lastIndexOf("."));
		
		Path p = null;
		if(Files.exists(Paths.get(bigPathRoot.toString(), imgPath.concat(".jpg")))) {
		  p = Paths.get(bigPathRoot.toString(), imgPath.concat(".jpg"));
		}
		if(Files.exists(Paths.get(bigPathRoot.toString(), imgPath.concat(".JPG")))) {
		  p = Paths.get(bigPathRoot.toString(), imgPath.concat(".JPG"));
    }
		
		assert(p != null);
		
		byte[] inarr;
		try(InputStream is = new BufferedInputStream(Files.newInputStream(p, StandardOpenOption.READ), 262_144)) {
		  inarr = new byte[is.available()];
		  is.read(inarr);
		}
		return inarr;
	}
	
	@PostMapping("/resource-img")
	public @ResponseBody byte[] getResourceImg(@RequestBody String imgName) throws IOException {
		Path p = Paths.get(imgResourcesPathRoot).resolve(Paths.get(imgName));
		byte[] inarr;
		try(InputStream is = new BufferedInputStream(Files.newInputStream(p, StandardOpenOption.READ), 262_144);) {
  		inarr = new byte[is.available()];
  		is.read(inarr);
		}
		return inarr;
	}

  public void setFm(FileManager fm) {
    this.fm = fm;
  }

  public void setMinPathRoot(String minPathRoot) {
    this.minPathRoot = minPathRoot;
  }

  public void setBigPathRoot(String bigPathRoot) {
    this.bigPathRoot = bigPathRoot;
  }
	
}
