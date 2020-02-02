import java.io.BufferedReader;
import java.lang.StringBuilder;
import java.io.FileReader;

public class TestReader {
  public TestReader() {}

  public static String[] read(String fileName, int size) {
    String[] ret = new String[size];
    try {
      BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
      for(int i = 0; i < size; i++) {
        ret[i] = bufferedReader.readLine();
        ret[i].length();
      }
      bufferedReader.close();
    }
    catch(Exception e) {
      System.out.println("The file is shorter than the specified line number "+size+" or an IO error occured!\nPlease restart.");
      System.exit(1);
    }
    return ret;
  }
  public static void main(String[] args) {
    String everything = "";
    BufferedReader br;
    int j = 0;
    try {
      br = new BufferedReader(new FileReader("file.txt"));
      try {
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        
        while (line != null) {
          sb.append(line);
          sb.append(System.lineSeparator());
          line = br.readLine();
          j++;
        }
        everything = sb.toString();
      } finally {
        br.close();
      }
    } catch(Exception e) {
      
    }
    
    System.out.println(j+"\n"+everything);
    /*int size = 4;
    String[] f = read("file.txt", size);
    for (int i = 0; i < size; i++) {
      System.out.println(i+": "+f[i]);
    }*/    
    
  }
}
