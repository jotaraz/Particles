import java.io.PrintWriter;

public class TestWriter {
  static String fileName = "file.txt";

  public TestWriter() {
  }  
    
  public static void write(String[] content) {
    try {
      PrintWriter writer = new PrintWriter(fileName, "UTF-8");
      
      for (int i = 0; i < content.length; i++) {
        writer.println(content[i]);          
      }
      writer.close();  
    } catch(Exception e) {
      
    }
    
  }
  public static void main(String[] args) {
    String[] content = {"zufdz", "zflozo", "pglf"};
    write(content);    
  }
}
