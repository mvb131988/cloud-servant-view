package main;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.servlet.ServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

	private static Logger logger = LogManager.getRootLogger();

	private String minPathRoot = ResourceBundle.getBundle("app").getString("min-path");
	
	private String bigPathRoot = ResourceBundle.getBundle("app").getString("big-path");
	
	private String imgResourcesPathRoot = ResourceBundle.getBundle("app").getString("img-resources-path");
	
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
	@GetMapping("/list")
	public @ResponseBody ListResponse list(@RequestParam("path") String path) throws IOException {
	  Path p = Paths.get(minPathRoot.toString(), path);
		List<FileDescriptor> l0 = allPaths(p);
		
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		ObjectMapper mapper = new ObjectMapper();
//		
//		mapper.writeValue(out, l0);
//
//		final byte[] data = out.toByteArray();
		
		List<String> files = l0.stream()
		                       .filter(e -> FileType.FILE.equals(e.getType()))
		                       .map(e -> e.getName())
		                       .collect(Collectors.toList());
		
    List<String> dirs = l0.stream()
                          .filter(e -> FileType.DIR.equals(e.getType()))
                          .map(e -> e.getName())
                          .collect(Collectors.toList());
    
		return new ListResponse(files, dirs);
	}
	
	@GetMapping("/imgs")
	public String greeting(Model model, ServletRequest request) throws IOException {
		List<List<String>> l = new ArrayList<>();
		
		List<String> r1 = new ArrayList<String>();
		r1.add("1");
		r1.add("2");
		r1.add("3");
		
		List<String> r2 = new ArrayList<String>();
		r2.add("4");
		r2.add("5");
		r2.add("6");
		
		List<String> r3 = new ArrayList<String>();
		r3.add("7");
		r3.add("8");
		r3.add("9");
		
		l.add(r1);
		l.add(r2);
		l.add(r3);
		
		model.addAttribute("imgs", l);
		return "gallery";
	}
	
	private List<FileDescriptor> allPaths(Path inputPath) throws IOException {
		FilesVisitor fv = new FilesVisitor(inputPath);
		Files.walkFileTree(inputPath, fv);
		return fv.getList();
	}
	
	@PostMapping("/img")
	public @ResponseBody byte[] getImage(@RequestBody String imgPath) throws IOException {
		logger.info(imgPath);
		
		Path p = Paths.get(minPathRoot.toString(), imgPath);
		InputStream is = new BufferedInputStream(Files.newInputStream(p, StandardOpenOption.READ), 262_144);
		
		byte[] inarr = new byte[is.available()];
		is.read(inarr);
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
		
		InputStream is = new BufferedInputStream(Files.newInputStream(p, StandardOpenOption.READ), 262_144);
		
		byte[] inarr = new byte[is.available()];
		is.read(inarr);
		return inarr;
	}
	
	@PostMapping("/resource-img")
	public @ResponseBody byte[] getResourceImg(@RequestBody String imgName) throws IOException {
		Path p = Paths.get(imgResourcesPathRoot).resolve(Paths.get(imgName));
		InputStream is = new BufferedInputStream(Files.newInputStream(p, StandardOpenOption.READ), 262_144);
		
		byte[] inarr = new byte[is.available()];
		is.read(inarr);
		return inarr;
	}
	
}
