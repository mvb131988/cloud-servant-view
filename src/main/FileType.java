package main;

public enum FileType {
  
  FILE(0),
  DIR(1);
  
  private int type;
  
  private FileType(int type) {
    this.type = type;
  }
  
}
