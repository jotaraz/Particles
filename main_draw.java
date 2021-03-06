import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.util.Scanner;
import java.awt.event.KeyListener;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.lang.StringBuilder;
import java.io.FileReader;
import java.lang.Math;
import java.util.Vector;

public class main_draw extends JPanel implements KeyListener {
  static final double pi = Math.PI;
  static final String fileName = "file.txt";
  static final double gx = 0.0; //Gravitation x Komponente
  static final double gy = 0.0; //Gravitation y Komponente
  static final int iterations_between_paint = 100;
  static final int iterations_between_smash = 10; 
  static final int thickness = 20;    
  static final int std_radius = 2;
  static final int std_mass   = std_radius*std_radius*std_radius;
  static final double v_def = 100.0; //Default Geschwindigkeit
  static final double v_ran = 20.0; 
  static final int virtual_seconds = 10;
  static final double t = 0.0001; //1/100000.0; //dt
  static final double Body_Length = 200.0;
  
  static int n; //Anzahl von Teilchen
  static int number_of_paintings = (int)(virtual_seconds/(t*iterations_between_paint));
  static int xg = 1200; //x Rahmen
  static int yg = 600; //y Rahmen 
  
  //Counter
  static int paint_counter = 0;
  static int paint_index = 0;
  static int it = 0;
  
  //Particles and Sections
  static int[][] xhist;
  static int[][] yhist;
  static int[][] X;
  static int[][] Y;
  
  static int run = 1;
  static long time_cur = 0;
  static long time_start = 0;
  static int between = 10;
  
  
  public main_draw() {}
  
  public void keyTyped(KeyEvent e) {}

  public void keyPressed(KeyEvent e) {
    if(e.getKeyCode() == 37) { //left
      //System.out.println(37); //factor += Math.pow(10, factor2);
      //changed = true;
    }
    if(e.getKeyCode() == 39) {//right
      //System.out.println(39); //factor -= Math.pow(10, factor2);
      //changed = true;
    }
    if(e.getKeyCode() == 38) { //up
      //System.out.println(38); //factor2++;
      if(between > 0) between -= 1;
      //KD += 0.001;
      //changed = true;
    }
    if(e.getKeyCode() == 40) { //down
      //System.out.println(40); //factor2--;
      between += 1;
      //KD -= 0.001;
      //changed = true;
    }
    if(e.getKeyCode() == 8) { //delete
      //System.out.println(19); //factor2--
      run *= -1;
      //KD -= 0.001;
      //changed = true;
    }
  }

  public void keyReleased(KeyEvent e) {};
  
  public static double f(double x) {
    //return (int)(Math.sqrt(100*100-(x-100)*(x-100))); //circle
    return (10*Math.sqrt(x));
    //return (int)(30*Math.log(x));         
  }
  
  public static double squ(double x) {
    return x*x;  
  }
  
  public static double sin(double x) {
    return Math.sin(x); //x-(x*x*x)/6.0+(x*x*x*x*x)/120.0-(x*x*x*x*x*x*x)/5040.0;  
  }
  
  public static double cos(double x) {
    return Math.cos(x); //1.0-(x*x)/2.0+(x*x*x*x)/24.0-(x*x*x*x*x*x)/720.0;  
  }
  
  
  

  public static double angle(double dx, double dy) {
    if(dx == 0 && dy > 0) {
      return pi/2;
    } else if(dx == 0 && dy < 0) {
      return 1.5*pi;
    } // end of if-else
    double beta = Math.atan(dy/dx);
    //System.out.println(beta/pi*180);
    if(dx < 0 && dy >= 0) {
      beta = pi+beta;
    } else if(dx < 0 && dy < 0) {
      beta = 1.5*pi-beta;
    } else if(dx >= 0 && dy < 0) {
      beta = 2*pi+beta;
    }
    return beta;
  }

  public static int length_file(String fn) {
    BufferedReader br;
    int j = 0;
    try {
      br = new BufferedReader(new FileReader(fn));
      try {
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        
        while (line != null) {
          sb.append(line);
          sb.append(System.lineSeparator());
          line = br.readLine();
          j++;
        }
      } finally {
        br.close();
      }
    } catch(Exception e) {
      
    }
    return j;  
  }
    
  public static int minint(int[] a) {
    int min = a[0];
    for (int i = 1; i < a.length; i++) {
      if(a[i] < min) {
        min = a[i];  
      }  
    }
    return min;  
  }
  
  public static double min(double[] a) {
    double min = a[0];
    for (int i = 1; i < a.length; i++) {
      if(a[i] < min) {
        min = a[i];  
      }  
    }
    return min;  
  }
  
  public double max(double[] a) {
    double max = a[0];
    for (int i = 1; i < a.length; i++) {
      if(a[i] > max) {
        max = a[i];  
      }  
    }
    return max;  
  } 
   
  public static double val(double x) {
    if(x < 0) {
      x *= -1.0;  
    }
    return x;    
  }
    
  public void paint(Graphics g) {
    super.paint(g); 
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setColor(Color.BLUE);
    g2d.drawString("iter "+paint_index, 500, 50);
    g2d.drawString("time "+between, 600, 50); 
    
    if(paint_index < number_of_paintings) {
      g2d.setColor(Color.RED);    
      for (int i = 0; i < X.length; i++) {
        for (int j = 0; j < X[0].length-1; j++) {
          g2d.drawLine((int)X[i][j], (int)Y[i][j], (int)X[i][j+1], (int)Y[i][j+1]);
        }
      }
      
      for(int i=0; i<n; ++i) {
        g2d.setColor(new Color(0, 0, 200));
        g2d.fillOval(xhist[i][paint_index]-std_radius, yhist[i][paint_index]-std_radius, 2*std_radius, 2*std_radius);
      }  
    }
    
  }    
  public void init() {    
  }
  
  public static void main(String[] args) throws InterruptedException {
    TestReader TR = new TestReader();
    
    String[] current;
    boolean still = true;
    String[] content = TR.read(fileName, length_file(fileName));
    
    current = content[0].split(" ");
    n = Integer.parseInt(current[1]);
    
    current = content[1].split(" ");
    number_of_paintings = Integer.parseInt(current[1]);
    
    xhist = new int[n][number_of_paintings];
    yhist = new int[n][number_of_paintings];
    
    current = content[2].split(" ");
    xg = Integer.parseInt(current[1]);
    yg = Integer.parseInt(current[2]);
    
    current = content[3].split(" ");
    
    int zeilen_vor_punkten = 4;
    
    X = new int[Integer.parseInt(current[1])][Integer.parseInt(current[2])];
    Y = new int[Integer.parseInt(current[1])][Integer.parseInt(current[2])];
    
    for (int i = 0; i < X.length; i++) {
      for (int j = 0; j < X[0].length; j++) {
        current = content[zeilen_vor_punkten+i*X[0].length+j].split(" "); 
        X[i][j] = Integer.parseInt(current[1]);                        
        Y[i][j] = Integer.parseInt(current[2]);
      }
    }
    
    int i = zeilen_vor_punkten+X.length*X[0].length;
    
    for(int counter = 0; counter < number_of_paintings; ++counter) {
      for(int j = 0; j < n; ++j) {
        current = content[i+counter*n+j].split(" ");
        xhist[j][counter] = Integer.parseInt(current[1]);
        yhist[j][counter] = Integer.parseInt(current[2]); 
      }
    }        
    
    main_draw points = new main_draw();
    JFrame frame = new JFrame("CFD");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.addKeyListener(points);
    frame.add(points);
    frame.setSize(xg, yg); // x,y
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    
    
    while(true) {
      for (paint_index = 0; paint_index < number_of_paintings; paint_index++) {
        if(run == 1) points.repaint();
        try {
          Thread.sleep(between);
        } catch(Exception e) {} 
      }
    }
  }
}    
    
  
