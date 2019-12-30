package main;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class MainController {

	private static Logger logger = LogManager.getRootLogger();

	private String minPathRoot = ResourceBundle.getBundle("app").getString("min-path");
	
	private String bigPathRoot = ResourceBundle.getBundle("app").getString("big-path");
	
	@GetMapping("/list")
	public @ResponseBody List<String> list() throws IOException {
		List<String> l0 = allPaths(Paths.get(minPathRoot));
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.writeValue(out, l0);

		final byte[] data = out.toByteArray();
		return l0;
	}
	
	@GetMapping("/imgs")
	public String greeting(Model model) throws IOException {
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
	
	private List<String> allPaths(Path inputPath) throws IOException {
		FilesVisitor fv = new FilesVisitor();
		Files.walkFileTree(inputPath, fv);
		return fv.list;
	}
	
	private static class FilesVisitor implements FileVisitor<Path> {

		private List<String> list = new ArrayList<String>();
		
		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			String fileName = file.getFileName().toString();
			String extension = null; 
			if(fileName.lastIndexOf(".") != -1) {
				extension = fileName.substring(fileName.lastIndexOf(".")+1);
				if(extension.equals("bmp")) {
					list.add(file.getFileName().toString());
				}
			}
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			return FileVisitResult.CONTINUE;
		}
		
	}
	
	@PostMapping("/img")
	public @ResponseBody byte[] getImage(@RequestBody String imgName) throws IOException {
		logger.info(imgName);
		
		Path p = Paths.get(minPathRoot).resolve(Paths.get(imgName));
		InputStream is = new BufferedInputStream(Files.newInputStream(p, StandardOpenOption.READ), 262_144);
		
		byte[] inarr = new byte[is.available()];
		is.read(inarr);
		return inarr;
	}
	
	@PostMapping("/big-img")
	public @ResponseBody byte[] getBigImage(@RequestBody String imgName) throws IOException {
		logger.info(imgName);
		
		Path p = Paths.get(bigPathRoot).resolve(Paths.get(imgName));
		InputStream is = new BufferedInputStream(Files.newInputStream(p, StandardOpenOption.READ), 262_144);
		
		byte[] inarr = new byte[is.available()];
		is.read(inarr);
		return inarr;
	}
	
}
