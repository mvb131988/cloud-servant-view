package main;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SharingController {

  private String minPathRoot = ResourceBundle.getBundle("app").getString("min-path");
  
  private String bigPathRoot = ResourceBundle.getBundle("app").getString("big-path");
  
  private String imgResourcesPathRoot = ResourceBundle.getBundle("app").getString("img-resources-path");
  
  @Autowired
  private SharingRepository shareRepository;
  
  @GetMapping("/sharing/imgs")
  public String mainPage(@RequestParam("path-id") String pathId, HttpServletResponse response) throws IOException {
    Cookie cookie = new Cookie("path-id", pathId);
    response.addCookie(cookie);
    return "sharing-gallery";
  }
  
  @GetMapping("/sharing/list")
  public @ResponseBody ListResponse list(@CookieValue("path-id") String pathId) throws IOException {
    ListResponse response = new ListResponse(null, null, null, null);
    
    //find a value(dir relative path) for the corresponding path-id
    String path = shareRepository.getPath(pathId);
    if(path != null) {
      List<FileDescriptor> l0 = allPaths(Paths.get(minPathRoot), Paths.get(bigPathRoot));
      
      List<String> bmps = l0.stream()
          .filter(e -> FileType.BMP.equals(e.getType()))
          .map(e -> e.getName())
          .collect(Collectors.toList());
      
      List<String> jpgs = l0.stream()
          .filter(e -> FileType.JPG.equals(e.getType()))
          .map(e -> e.getName())
          .collect(Collectors.toList());
      
      List<String> others = l0.stream()
          .filter(e -> FileType.OTHER.equals(e.getType()))
          .map(e -> e.getName())
          .collect(Collectors.toList());
      
      response.setBmps(bmps);
      response.setJpgs(jpgs);
      response.setOthers(others);
    }
    return response;
  }
  
  private List<FileDescriptor> allPaths(Path minPathRoot, Path bigPathRoot) throws IOException {
    FilesVisitor fv = new FilesVisitor(minPathRoot, bigPathRoot);
    Files.walkFileTree(bigPathRoot, fv);
    return fv.getList();
  }
  
  @PostMapping("/sharing/img")
  public @ResponseBody byte[] getImage(@CookieValue("path-id") String pathId, 
                                       @RequestBody String imgName) throws IOException {
    //imgName must be in presented in the format <file-name>.<file-extension>
    //cases when:
    // + <path>/<file-name>.<file-extension>
    // + ../<path>/<file-name>.<file-extension>
    //are unacceptable
    imgName = Paths.get(imgName).getFileName().toString();
    
    byte[] inarr = null;
    
    //find a value(dir relative path) for the corresponding path-id
    String path = shareRepository.getPath(pathId);
    if(path != null) {
      String dirRelativePath = path;
      Path p = Paths.get(minPathRoot.toString(), dirRelativePath, imgName);
      InputStream is = new BufferedInputStream(Files.newInputStream(p, StandardOpenOption.READ), 262_144);
      
      inarr = new byte[is.available()];
      is.read(inarr);
    }
    return inarr;
  }
  
  @PostMapping("/sharing/big-img")
  public @ResponseBody byte[] getBigImage(@CookieValue("path-id") String pathId,
                                          @RequestBody String imgName) throws IOException {
    //imgName must be in presented in the format <file-name>.<file-extension>
    //cases when:
    // + <path>/<file-name>.<file-extension>
    // + ../<path>/<file-name>.<file-extension>
    //are unacceptable
    imgName = Paths.get(imgName).getFileName().toString();
    
    byte[] inarr = null;
    
    //find a value(dir relative path) for the corresponding path-id
    String path = shareRepository.getPath(pathId);
    if(path != null) {
      String dirRelativePath = path;
      
      //cut trailing .jpg
      imgName = imgName.substring(0, imgName.lastIndexOf("."));
      
      Path p = null;
      if(Files.exists(Paths.get(bigPathRoot.toString(), dirRelativePath, imgName.concat(".jpg")))) {
        p = Paths.get(bigPathRoot.toString(), dirRelativePath, imgName.concat(".jpg"));
      }
      if(Files.exists(Paths.get(bigPathRoot.toString(), dirRelativePath, imgName.concat(".JPG")))) {
        p = Paths.get(bigPathRoot.toString(), dirRelativePath, imgName.concat(".JPG"));
      }
      
      assert(p != null);
      
      InputStream is = new BufferedInputStream(Files.newInputStream(p, StandardOpenOption.READ), 262_144);
      
      inarr = new byte[is.available()];
      is.read(inarr);
    }
    return inarr;
  }
  
  @PostMapping("/sharing/resource-img")
  public @ResponseBody byte[] getResourceImg(@CookieValue("path-id") String pathId,
                                             @RequestBody String imgName) throws IOException {
    //imgName must be in presented in the format <file-name>.<file-extension>
    //cases when:
    // + <path>/<file-name>.<file-extension>
    // + ../<path>/<file-name>.<file-extension>
    //are unacceptable
    imgName = Paths.get(imgName).getFileName().toString();
    
    byte[] inarr = null;
    
    String path = shareRepository.getPath(pathId);
    if(path != null) {
      Path p = Paths.get(imgResourcesPathRoot).resolve(Paths.get(imgName));
      InputStream is = new BufferedInputStream(Files.newInputStream(p, StandardOpenOption.READ), 262_144);
      
      inarr = new byte[is.available()];
      is.read(inarr);
    }
    return inarr;
  }
  
  @PostMapping("/sharing/create")
  public @ResponseBody String sharingCreate(@RequestBody String path) {
    String hash = shareRepository.getOrCreateHash(path);
    return hash;
  }
  
  @PostMapping("/sharing/get")
  public @ResponseBody String sharingGet(@RequestBody String path) {
    String hash = shareRepository.getHash(path);
    return hash;
  }
  
}
