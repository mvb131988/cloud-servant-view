package main;

public class FileDescriptor {

  private String name;
  
  private FileType type;

  public FileDescriptor(String name, FileType type) {
    super();
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public FileType getType() {
    return type;
  }

  public void setType(FileType type) {
    this.type = type;
  }
  
}
