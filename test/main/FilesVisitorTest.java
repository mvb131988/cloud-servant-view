package main;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;

import org.junit.Test;

public class FilesVisitorTest {
  
  @Test
  public void test() throws IOException {
    String repo = "test-resources/main/file-visitor/repo";
    String repoMin = "test-resources/main/file-visitor/repo-min";
    
    FilesVisitor fv = new FilesVisitor(Paths.get(repoMin), Paths.get(repo));
    Files.walkFileTree(Paths.get(repo), fv);
    List<FileDescriptor> res = fv.getList();
    
    Map<FileType, List<String>> map = 
        res.stream()
           .collect(
               Collectors.groupingBy(e -> e.getType(), 
                                     Collectors.mapping(e -> e.getName(), Collectors.toList()))
           );
    
    assertEquals(1, map.get(FileType.BMP).size()); 
    assertEquals(2, map.get(FileType.JPG).size());
    assertEquals(1, map.get(FileType.OTHER).size());
    assertEquals(2, map.get(FileType.DIR).size());
    
    assertEquals("2.bmp", map.get(FileType.BMP).get(0));
    assertEquals("3.txt", map.get(FileType.OTHER).get(0));
    
    Collections.sort(map.get(FileType.JPG));
    assertEquals("1.jpg", map.get(FileType.JPG).get(0));
    assertEquals("4.JPG", map.get(FileType.JPG).get(1));
    
    Collections.sort(map.get(FileType.DIR));
    assertEquals("a_dir", map.get(FileType.DIR).get(0));
    assertEquals("dir1", map.get(FileType.DIR).get(1));
  }
  
}
