package main;

public enum FileType {
  
  BMP(0),
  JPG(1),
  OTHER(2),
  DIR(3);
  
  private int type;
  
  private FileType(int type) {
    this.type = type;
  }
  
}
