package main;

import java.util.List;

public class ListResponse {

  private List<String> files;
  
  private List<String> dirs;

  public ListResponse(List<String> files, List<String> dirs) {
    super();
    this.files = files;
    this.dirs = dirs;
  }

  public List<String> getFiles() {
    return files;
  }

  public void setFiles(List<String> files) {
    this.files = files;
  }

  public List<String> getDirs() {
    return dirs;
  }

  public void setDirs(List<String> dirs) {
    this.dirs = dirs;
  }
  
}
