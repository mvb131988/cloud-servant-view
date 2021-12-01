package main;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FilesVisitor implements FileVisitor<Path> {

  private List<FileDescriptor> list = new ArrayList<FileDescriptor>();
  private Path repoMin;
  private Path repo;
  
  public FilesVisitor(Path minPath, Path bigPath) {
    this.repoMin = minPath;
    this.repo = bigPath;
    //init with back to parent dir
    list.add(new FileDescriptor("back", FileType.DIR));
  }
  
  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    if(!dir.equals(repo)) {
      list.add(new FileDescriptor(dir.getFileName().toString(), FileType.DIR));
      //if directory is found don't visit it
      return FileVisitResult.SKIP_SUBTREE;
    }
    return FileVisitResult.CONTINUE;
  }
  
  /**
   * Visits each file/folder in start directory (defined on FilesVisitor invocation). Each file 
   * could end up in the following state:
   * (1) This .jpeg file(in repo) that matches with .bmp file(in repo-min). Save in .bmp list.
   * (2) This .jpeg file(in repo) that doesn't match with .bmp file(in repo-min). Save in .jpg list.
   *     This would be the list with failed .jpg. Failed .jpg are those files that couldn't be 
   *     processed by jpeg-decoder. All of them would have the same special icon.
   * (3) Non .jpeg files. All of them would have the same special icon.    
   */
  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    String fileName = file.getFileName().toString();
    String ext = extension(fileName); 
    
    if(ext == null) {
      return FileVisitResult.CONTINUE;
    }
    
    if(ext.equals("jpg")) {
      String bmpFileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".bmp";
      boolean exist = Files.exists(Paths.get(repoMin.toString(), bmpFileName));
      if(exist) {
        list.add(new FileDescriptor(bmpFileName, FileType.BMP));        
      } else {
        list.add(new FileDescriptor(file.getFileName().toString(), FileType.JPG));
      }
    } else {
      list.add(new FileDescriptor(file.getFileName().toString(), FileType.OTHER));
    }
    
    return FileVisitResult.CONTINUE;
  }
  
  private String extension(String fileName) {
    String extension = fileName.lastIndexOf(".") != -1 ? 
                       fileName.substring(fileName.lastIndexOf(".")+1) : 
                       null; 
    return extension;
  }
  
  @Override
  public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  public List<FileDescriptor> getList() {
    return list;
  }
  
}
