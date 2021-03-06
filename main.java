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

public class main extends JPanel implements KeyListener {
  static final double pi = Math.PI;
  static final int iterations_between_paint = 100;
  static final int iterations_between_smash = 10;
  static final int thickness = 20;
  static final int n = 20000; //Anzahl von Teilchen
  static final int virtual_seconds = 50;
  static final double Body_Length = 200.0;
  
  static Bunch b = new Bunch(n);
  static final double t = b.t; 
  static final int xg = b.xg; //x Rahmen
  static final int yg = b.yg; //y Rahmen    
  static final int std_radius = b.std_radius;
  static final int std_mass   = b.std_mass;
  static final int number_of_paintings = (int)(virtual_seconds/(t*iterations_between_paint));
   
  
  //Counter
  static int paint_counter = 0;
  static int paint_index = 0;
  static int it = 0;
  
  //Particles and Sections
  static int[][] xhist = new int[n][number_of_paintings];
  static int[][] yhist = new int[n][number_of_paintings];
  static int[][] X = new int[2][3];
  static int[][] Y = new int[X.length][X[0].length]; 
  static double[][] Oalpha = new double[X.length][X[0].length-1]; 
  
  static final int s = (int)(X[0].length/2);
  static int run = 1;
  static long time_cur = 0;
  static long time_start = 0;
  static int between = 10;
  
  
  public main() {}
  
  public void keyTyped(KeyEvent e) {}
  
  public void keyPressed(KeyEvent e) {
    if(e.getKeyCode() == 37) { //left
      System.out.println(37); //factor += Math.pow(10, factor2);
      //changed = true;
    }
    if(e.getKeyCode() == 39) {//right
      System.out.println(39); //factor -= Math.pow(10, factor2);
      //changed = true;
    }
    if(e.getKeyCode() == 38) { //up
      System.out.println(38); //factor2++;
      if(between > 0) between -= 1;
      //KD += 0.001;
      //changed = true;
    }
    if(e.getKeyCode() == 40) { //down
      System.out.println(40); //factor2--;
      between += 1;
      //KD -= 0.001;
      //changed = true;
    }
    if(e.getKeyCode() == 8) { //delete
      System.out.println(19); //factor2--
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
  
  public static double val(double x) {
    if(x < 0) {
      x *= -1.0;  
    }
    return x;    
  }
  
  public static void build() {
    /*int num_po_row = s/2;
    for (int i = 0; i < num_po_row; i++) {
    X[0][i] = xg/5+(int)(Body_Length/num_po_row)*(num_po_row-i-1);       
    Y[0][i] = (int)(yg/2-f(num_po_row-i-1));
    
    X[1][i] = xg/2+(int)(Body_Length/num_po_row)*(num_po_row-i-1);       
    Y[1][i] = (int)(yg/2-f(num_po_row-i-1));
    }
    for (int i = num_po_row; i < 2*num_po_row; i++) {
    X[0][i] = xg/5+(int)(Body_Length/num_po_row)*(i-num_po_row+1);
    Y[0][i] = (int)(yg/2+f(i-num_po_row+1));
    
    X[1][i] = xg/2+(int)(Body_Length/num_po_row)*(i-num_po_row+1);
    Y[1][i] = (int)(yg/2+f(i-num_po_row+1));
    }
    
    for (int i = 2*num_po_row; i < 4*num_po_row; i++) {
    X[0][i] = X[0][4*num_po_row-1-i]+thickness;
    Y[0][i] = Y[0][4*num_po_row-1-i];
    
    X[1][i] = X[1][4*num_po_row-1-i]+thickness;
    Y[1][i] = Y[1][4*num_po_row-1-i];
    }*/
    
    X[0][0] = 100;
    Y[0][0] = 0;  
    X[0][1] = xg/2;
    Y[0][1] = yg/2-50;    
    X[0][2] = xg;
    Y[0][2] = yg/2-50;
    
    X[1][0] = 100;
    Y[1][0] = yg;    
    X[1][1] = xg/2;
    Y[1][1] = yg/2+50;  
    X[1][2] = xg;
    Y[1][2] = yg/2+50; 
    
    
    
    for (int i = 0; i < X.length; ++i) {
      for (int j = 0; j < X[0].length-1; ++j) {
        Oalpha[i][j] = angle((X[i][j+1]-X[i][j]), (Y[i][j+1]-Y[i][j]));
      }
    }
  }  
  
  public void paint(Graphics g) {
    super.paint(g); 
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    //g2d.setColor(Color.BLACK);
    //g2d.fillRect(0,0,xg,yg);
    if(it*t < virtual_seconds) {
      if(it%1 == 0) {
        g2d.setColor(Color.BLACK);
        int[] xc = {95, 505, 505, 95};
        int[] yc = {95, 95, 305, 305};
        g2d.drawPolygon(xc, yc, 4);
        g2d.drawString("Progess: "+(int)(100*it*t/virtual_seconds)+"%", 50, 50);
        g2d.drawString("running since: "+(time_cur-time_start)/(60*1000000000.0), 200, 50);
        g2d.drawString("Estimate : "+((time_cur-time_start)/(60*1000000000.0*it*t/virtual_seconds)), 400, 50);
        int[] xc2 = {100, 100+4*(int)(100*it*t/virtual_seconds), 100+4*(int)(100*it*t/virtual_seconds), 100};
        int[] yc2 = {100, 100, 300, 300};
        g2d.drawPolygon(xc2, yc2, 4);
      }
    } else {
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
  }    
  public void init() {    
  }
          
  public static void main(String[] args) throws InterruptedException {
    build();
    
    TestWriter TW = new TestWriter();
    
    int zeilen_vor_inhalt = 4;
    System.out.println("number of paintings         = "+number_of_paintings);
    System.out.println("number of calculated frames = "+number_of_paintings*iterations_between_paint);
    String[] content = new String[zeilen_vor_inhalt+X.length*X[0].length+n*number_of_paintings];
    content[0] = "0 "+n+" n";
    content[1] = "0 "+number_of_paintings;
    content[2] = "0 "+xg+" "+yg;
    content[3] = "0 "+X.length+" "+X[0].length;
    for (int j = 0; j < X.length; j++) {
      for(int i = 0; i < X[j].length; ++i) {
        content[zeilen_vor_inhalt+j*X[0].length+i] = (j+1)+" "+X[j][i]+" "+Y[j][i];
      }
    }        
    
    main points = new main();
    JFrame frame = new JFrame("CFD");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.addKeyListener(points);
    frame.add(points);
    frame.setSize(xg, yg); // x,y
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    
    time_start = System.nanoTime();
    
    while(it*t <= virtual_seconds) {
      b.update();
      for (int i=0; i<n; ++i) {
        if(it%iterations_between_smash == 0) {
          b.testsmash(i);       
        }
        for (int k = 0; k < Oalpha.length; k++) {
          for (int j = 0; j < Oalpha[k].length; ++j) {
            if(b.inSection(i, X[k][j], Y[k][j], X[k][j+1], Y[k][j+1])) {
              break;
            } 
          }
        }      
      }
      if(it%iterations_between_paint == 0) {
        if(paint_counter < number_of_paintings) {
          int start = X.length*X[0].length+zeilen_vor_inhalt+n*paint_counter;
          for (int i = start; i < start+n; i++) {
            content[i] = "0 "+(int)b.rx[i-start]+" "+(int)b.ry[i-start];
            
            xhist[i-start][paint_counter] = (int)b.rx[i-start];
            yhist[i-start][paint_counter] = (int)b.ry[i-start];
          }  
          paint_counter++;
          points.repaint();
        }
      }
      time_cur = System.nanoTime();
      it++;
    }
    
    long t2 = System.nanoTime()-time_start;
    double umrechnung = 60.0*1000000000;
    System.out.println("1.6 - n = "+n+" / v_s = "+virtual_seconds+" / iter_bw_paint = "+iterations_between_paint+" / dt = "+t+" / t[min] = "+((t2)/umrechnung));
    
    TW.write(content);
    Sound so = new Sound();
    so.bing();
    
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
                
