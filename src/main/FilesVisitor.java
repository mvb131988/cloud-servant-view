package main;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FilesVisitor implements FileVisitor<Path> {

  private List<FileDescriptor> list = new ArrayList<FileDescriptor>();
  private Path inputPath;
  
  public FilesVisitor(Path inputPath) {
    this.inputPath = inputPath;
    //init with back to parent dir
    list.add(new FileDescriptor("back", FileType.DIR));
  }
  
  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    String fileName = dir.getFileName().toString();
    if(!dir.equals(inputPath) && fileName.lastIndexOf(".") == -1) {
      list.add(new FileDescriptor(dir.getFileName().toString(), FileType.DIR));
      //if directory is found don't visit it
      return FileVisitResult.SKIP_SUBTREE;
    }
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    String fileName = file.getFileName().toString();
    String extension = null; 
    if(fileName.lastIndexOf(".") != -1) {
      extension = fileName.substring(fileName.lastIndexOf(".")+1);
      if(extension.equals("bmp")) {
        list.add(new FileDescriptor(file.getFileName().toString(), FileType.FILE));
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

  public List<FileDescriptor> getList() {
    return list;
  }
  
}
