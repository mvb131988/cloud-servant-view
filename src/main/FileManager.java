package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class FileManager {

  public List<FileDescriptor> allPaths(Path minPath, Path bigPath) throws IOException {
    FilesVisitor fv = new FilesVisitor(minPath, bigPath);
    Files.walkFileTree(bigPath, fv);
    return fv.getList();
  }
  
}
