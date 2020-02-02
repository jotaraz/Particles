import java.awt.Toolkit;

public class Sound {
  public Sound() {}
  
  public void bing() {
    final Runnable runnable =
    (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
    if (runnable != null) runnable.run();
  }  
  
  public static void main(String[] args) {
      
    
  }
  
}
  
