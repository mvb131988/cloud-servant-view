package main;

import java.util.List;

public class ListResponse {

  private List<String> bmps;
  
  private List<String> jpgs;

  private List<String> others;
  
  private List<String> dirs;
  
  public ListResponse(List<String> bmps, List<String> jpgs, List<String> others, List<String> dirs) {
    super();
    this.bmps = bmps;
    this.jpgs = jpgs;
    this.others = others;
    this.dirs = dirs;
  }

  public List<String> getBmps() {
    return bmps;
  }

  public void setBmps(List<String> bmps) {
    this.bmps = bmps;
  }

  public List<String> getJpgs() {
    return jpgs;
  }

  public void setJpgs(List<String> jpgs) {
    this.jpgs = jpgs;
  }

  public List<String> getOthers() {
    return others;
  }

  public void setOthers(List<String> others) {
    this.others = others;
  }

  public List<String> getDirs() {
    return dirs;
  }

  public void setDirs(List<String> dirs) {
    this.dirs = dirs;
  }
  
}
