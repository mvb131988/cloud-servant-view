package main;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

public class MainControllerTest {

  @Test
  public void test() throws IOException {
    String repo = "test-resources/main/file-visitor/repo";
    String repoMin = "test-resources/main/file-visitor/repo-min";
    
    FileManager fm = Mockito.mock(FileManager.class);
    MainController mc = new MainController();
    mc.setFm(fm);
    mc.setMinPathRoot(repoMin);
    mc.setBigPathRoot(repo);
    
    List<FileDescriptor> l0 = new ArrayList<>();
    l0.add(new FileDescriptor("1.bmp", FileType.BMP));
    l0.add(new FileDescriptor("2.jpg", FileType.JPG));
    l0.add(new FileDescriptor("3.txt", FileType.OTHER));
    l0.add(new FileDescriptor("back", FileType.DIR));
    l0.add(new FileDescriptor("dir1", FileType.DIR));
    when(fm.allPaths(Paths.get(repoMin), Paths.get(repo))).thenReturn(l0);
    
    ListResponse res = mc.list("");
    
    assertEquals(1, res.getBmps().size()); 
    assertEquals(1, res.getJpgs().size());
    assertEquals(1, res.getOthers().size());
    assertEquals(2, res.getDirs().size());
    
    assertEquals("1.bmp", res.getBmps().get(0));
    assertEquals("2.jpg", res.getJpgs().get(0));
    assertEquals("3.txt", res.getOthers().get(0));
    
    List<String> l = res.getDirs();
    Collections.sort(l);
    assertEquals("back", l.get(0));
    assertEquals("dir1", l.get(1));
  }
  
}
